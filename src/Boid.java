import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;

public class Boid {

    private final int MIN_SPEED = 5;
    private final int MAX_TRAILS = 50;
    private final double SIZE = 10;
    private final double DEAD_ANGLE = 45;
    private final double REPULSE_RANGE = 20;

    private Vector2D position;
    private Vector2D vitesse;
    private Vector2D forces;
    private ArrayList<Vector2D> trails;
    private ArrayList<Boid> strangerNeighbours;
    private ArrayList<Boid> flockNeighbours;
    private Flock flock;


    public Boid(Flock flock) {
        this(flock, Math.random() * GUI.SIMULATION_PANEL_WIDTH, Math.random() * GUI.HEIGHT);
    }

    public Boid(Flock flock, double x, double y) {
        position = new Vector2D(x, y);
        do {
            vitesse = new Vector2D(Math.random() * 2*flock.getSpeedMax() - flock.getSpeedMax(), Math.random() * 2*flock.getSpeedMax() - flock.getSpeedMax());
        } while (vitesse.norm() < MIN_SPEED);

        forces = new Vector2D();

        trails = new ArrayList<>(MAX_TRAILS);

        this.flock = flock;
    }

    //recherche et ajoute les voisins du boids qui vont avoir une influence sur son déplacement
    private void findNeighbours() {
        flockNeighbours = new ArrayList<>();
        strangerNeighbours = new ArrayList<>();


        for (Flock fl : GUI.flocks) {
            for (Boid boid : fl.getBoids()) {

                Vector2D vectorBetweenBoids = Vector2D.substract(boid.position, this.position);
                double angleBoid = Math.atan2(vitesse.y, vitesse.x);
                double angleBetweenBoids = Math.abs(Math.atan2(vectorBetweenBoids.y, vectorBetweenBoids.x));
                double deadAngleRadian = Math.PI * DEAD_ANGLE / 180;

                boolean isBoidInDeadAngle = (angleBetweenBoids-angleBoid >= Math.PI-deadAngleRadian/2 && angleBetweenBoids-angleBoid <= Math.PI+deadAngleRadian/2);

                if (boid != this && vectorBetweenBoids.norm() <= flock.getViewRange() && !isBoidInDeadAngle) {
                    if (fl == this.flock) {
                        flockNeighbours.add(boid);
                    } else {
                        strangerNeighbours.add(boid);
                    }
                }
            }
        }
    }

    //calcule les forces qui s'exercent sur ce boid
    private void computeForces() {
        forces.multiply(0);

        findNeighbours();
        computeCohesionForce();
        computeSeparationForces();
        computeAlignementForces();
        computeObstacleAvoidanceForce();
        computeWallAvoidanceForce();

        if(flock.type ==0) { //0 = proie
            computeSegregationForces();
            computeFuiteForces();
        }else{
            computeChasseForces();
        }
        applyForces();
    }

    private void computeCohesionForce() {
        Vector2D flockCenter = new Vector2D();
        for (Boid boid : flockNeighbours) {
            flockCenter.add(boid.position);
        }

        if (!flockNeighbours.isEmpty()) {
            flockCenter.divide(flockNeighbours.size());
            Vector2D cohesionForce = Vector2D.substract(flockCenter, position).substract(vitesse).multiply(flock.getCohesionCoeff());
            forces.add(cohesionForce);
        }
    }

    private void computeSegregationForces(){ //proportionnel à 1/distance
        for (Boid boid : strangerNeighbours) {
            Vector2D vecteur = Vector2D.substract(this.position,boid.position);
            forces.add(vecteur.multiply(flock.getSegregationCoeff()));
        }
    }
    private void computeSeparationForces(){
        ArrayList<Boid> neighbours = new ArrayList<>();
        neighbours.addAll(flockNeighbours);
        neighbours.addAll(strangerNeighbours);
        for(Boid boid : neighbours){
            if (Vector2D.substract(this.position, boid.position).norm() <= REPULSE_RANGE) {
                Vector2D vecteur = Vector2D.substract(this.position,boid.position).substract(vitesse).multiply(flock.getSeparationCoeff());
                forces.add(vecteur);
            }
        }
    }

    private void computeAlignementForces(){
        Vector2D vitesseFlock = new Vector2D();
        for(Boid boid : flockNeighbours){
           vitesseFlock =vitesseFlock.add(boid.vitesse) ;
        }
        vitesseFlock.divide(flockNeighbours.size());
        this.forces = this.forces.add(vitesseFlock.multiply(flock.getAlignementCoeff()));

    }

    private void computeObstacleAvoidanceForce() {
        for (Obstacle obstacle : GUI.obstacles) {
            if (willCollideObstacle(obstacle)) {
                Vector2D BO = Vector2D.substract(obstacle.position, this.position); // Vecteur entre le boid et le centre de l'obstacle
                double projection = Math.sqrt(Math.pow(BO.norm(), 2) - Math.pow(obstacle.avoidanceRadius, 2)); // Projection de BO sur une direction qui ne traverse pas l'obstacle

                double m;
                if (BO.norm() >= obstacle.avoidanceRadius) {
                    m = (Math.pow(projection, 2) - Math.pow(obstacle.avoidanceRadius, 2) - Math.pow(BO.norm(), 2)) / (-2* BO.norm());
                } else {
                    m = BO.norm();
                }

                double q = Math.sqrt(Math.pow(obstacle.avoidanceRadius, 2) - Math.pow(m, 2));

                Vector2D U = BO.normalized(); // Vecteur unitaire entre le boid et le centre de l'obstacle
                Vector2D V = vitesse.normalized(); // Vecteur uniteur de la direction du boid
                Vector2D W = (Math.atan2(U.y, U.x) - Math.atan2(V.y, V.x) < 0) ? new Vector2D(-U.y, U.x) : new Vector2D(U.y, -U.x); // Vecteur unitaire perpendiculaire à U
                Vector2D force = Vector2D.add(U.multiply(BO.norm() - m), W.multiply(q));

                if (BO.norm() <= obstacle.avoidanceRadius) {
                    forces = force;
                } else {
                    forces.add(force.multiply(0.5));
                }


            }
        }
    }

    private void computeFuiteForces(){
        ArrayList<Boid> neighbours = new ArrayList<>();
        neighbours.addAll(flockNeighbours);
        neighbours.addAll(strangerNeighbours);
        for (Boid boid : neighbours){
            if (boid.flock.type ==1){
                Vector2D distance = Vector2D.substract(boid.position,this.position);
                forces.add(distance.multiply(flock.getFuiteCoeff()));
            }
        }

    }

    private void computeChasseForces(){
        Vector2D barycentre =new Vector2D();
        for (Boid boid : flockNeighbours){
            if(boid.flock.type==0){
                barycentre.add(boid.position);
            }
        }
        if(barycentre.norm()!=0 ) {
            barycentre.divide(flockNeighbours.size());
            forces.add(barycentre.substract(this.position).multiply(flock.getChasseCoeff()));
        }

    }

    private boolean willCollideObstacle(Obstacle obstacle) {
        Vector2D BO = Vector2D.substract(obstacle.position, this.position); // Vecteur entre le boid et le centre de l'obstacle
        double projection = Vector2D.scalarProduct(BO, vitesse.normalized()); // Projection de BO sur la direction du boid
        double distanceBoidObstacle = Math.sqrt(Math.pow(BO.norm(), 2) - Math.pow(projection, 2)); // Plus petite distance de l'obstacle que le boid pourra atteindre

        return (BO.norm() <= REPULSE_RANGE + obstacle.avoidanceRadius && distanceBoidObstacle < obstacle.avoidanceRadius && projection >= 0);
    }

    private void computeWallAvoidanceForce() {
        double forceIntensity = 3;
        if (position.x - REPULSE_RANGE <= 200) {
            forces.add(new Vector2D(forceIntensity, 0));
        } else if (position.x + REPULSE_RANGE >= GUI.SIMULATION_PANEL_WIDTH - 200) {
            forces.add(new Vector2D(-forceIntensity, 0));
        }

        if (position.y - REPULSE_RANGE <= 200) {
            forces.add(new Vector2D(0, forceIntensity));
        } else if (position.y + REPULSE_RANGE >= GUI.HEIGHT - 200) {
            forces.add(new Vector2D(0, -forceIntensity));
        }
    }

    private void applyForces() {
        if (trails.size() > MAX_TRAILS) {
            trails.remove(0);
        }
        trails.add(new Vector2D(position));

        vitesse.add(forces);
        if (vitesse.norm() > flock.getSpeedMax()) {
            vitesse.limitNorm(flock.getSpeedMax());
        } else if (vitesse.norm() < MIN_SPEED) {
            vitesse.limitNorm(MIN_SPEED);
        }

        position.add(vitesse);
    }

    public void draw(Graphics2D g) {
        computeForces();

        drawBoid(g);
        if (flock.displayViewRange) drawViewRange(g);
        if (flock.displayTrails) drawTrails(g);
    }

    private void drawBoid(Graphics2D g) {
        double boidAngle = Math.PI / 4;

        Path2D boidShape = new Path2D.Double();

        boidShape.moveTo(0, 0);
        boidShape.lineTo(-SIZE*Math.cos(boidAngle), SIZE*Math.sin(boidAngle));
        boidShape.lineTo(SIZE, 0);
        boidShape.lineTo(-SIZE*Math.cos(boidAngle), -SIZE*Math.sin(boidAngle));
        boidShape.closePath();

        AffineTransform initState = g.getTransform();

        g.translate(position.x, position.y);

        double directionAngle = Math.atan2(vitesse.y, vitesse.x);
        g.rotate(directionAngle);

        g.setColor(flock.getColor().getColor());
        g.fill(boidShape);

        g.setTransform(initState);
    }

    private void drawViewRange(Graphics2D g) {
        double viewRange = flock.getViewRange();

        Arc2D viewRangeShape = new Arc2D.Double(-viewRange, -viewRange, 2*viewRange, 2*viewRange, 180+DEAD_ANGLE/2, 360-DEAD_ANGLE, Arc2D.PIE);

        AffineTransform initState = g.getTransform();

        g.translate(position.x, position.y);

        double directionAngle = Math.atan2(vitesse.y, vitesse.x);
        g.rotate(directionAngle);
        g.setColor(Color.RED);
        g.draw(viewRangeShape);

        g.setTransform(initState);
    }

    public void drawTrails(Graphics2D g) {
        for (int i = 0; i < trails.size(); i++) {
            Vector2D currentTrail = trails.get(i);
            Vector2D nextTrack = (i < trails.size() - 1) ? trails.get(i+1) : position;
            int alpha =i*255/trails.size();
            Color colorTrail = flock.getColor().getColor().darker();
            g.setColor(new Color(colorTrail.getRed(), colorTrail.getGreen(), colorTrail.getBlue(), alpha));
            g.draw(new Line2D.Double(currentTrail.x, currentTrail.y, nextTrack.x, nextTrack.y));
        }
    }

    public void clearTracks() {
        trails.clear();
    }

    public String toString(){
        return "Boid en " + position;
    }
}
