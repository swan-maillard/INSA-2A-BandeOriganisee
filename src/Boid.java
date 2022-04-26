import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;

public class Boid {

    private final int MAX_TRAILS = 50;
    private final double BOID_SIZE = 10;
    private final double DEAD_ANGLE = 45;
    private final double REPULSE_RANGE = 20;

    private Vector2D position;
    private Vector2D velocity;
    private Vector2D forces;
    private ArrayList<Vector2D> trails;
    private ArrayList<Boid> strangerNeighbours;
    private ArrayList<Boid> flockNeighbours;

    private Flock flock;


    public Boid(Flock flock) {
        this(flock, 0, 0);
        setRandomLocation();
    }

    public Boid(Flock flock, double x, double y) {
        this.flock = flock;
        position = new Vector2D(x, y);
        setRandomVelocity();
        forces = new Vector2D();
        trails = new ArrayList<>(MAX_TRAILS);
    }

    public void setRandomLocation() {
        position = new Vector2D(Math.random() * AppView.SIMULATION_PANEL_WIDTH, Math.random() * AppView.HEIGHT);
    }

    public void setRandomVelocity() {
        do {
            velocity = new Vector2D(Math.random() * 2 * flock.getSpeedLimit() - flock.getSpeedLimit(), Math.random() * 2 * flock.getSpeedLimit() - flock.getSpeedLimit());
        } while (velocity.norm() < App.BOIDS_MIN_SPEED);
    }

    private void findNeighbours() {
        flockNeighbours = new ArrayList<>();
        strangerNeighbours = new ArrayList<>();


        for (Flock fl : App.flocks) {
            for (Boid boid : fl.getBoids()) {

                Vector2D vectorBetweenBoids = Vector2D.subtract(boid.position, this.position);
                double angleBoid = Math.atan2(velocity.y, velocity.x);
                double angleBetweenBoids = Math.abs(Math.atan2(vectorBetweenBoids.y, vectorBetweenBoids.x));
                double deadAngleRadian = Math.PI * DEAD_ANGLE / 180;

                boolean isBoidInDeadAngle = (angleBetweenBoids - angleBoid >= Math.PI - deadAngleRadian / 2 && angleBetweenBoids - angleBoid <= Math.PI + deadAngleRadian / 2);

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

    private void computeForces() {
        forces.multiply(0);

        findNeighbours();

        computeCohesionForce();
        computeSeparationForce();
        computeAlignementForce();
        computeIntoleranceForce();

        if (flock.getType() == App.TYPE_PREDATOR) {
            computeHuntingForce();
        } else {
            computeFleeingForce();
        }

        computeObstacleAvoidanceForce();
        computeWallAvoidanceForce();

        applyForces();
    }

    private void computeCohesionForce() {
        Vector2D cohesionForce = new Vector2D();
        Vector2D flockCenter = new Vector2D();
        for (Boid boid : flockNeighbours) {
            flockCenter.add(boid.position);
        }

        if (!flockNeighbours.isEmpty()) {
            flockCenter.divide(flockNeighbours.size());
            cohesionForce = Vector2D.subtract(flockCenter, position).subtract(velocity).multiply(flock.getCohesionCoeff());
        }
        forces.add(cohesionForce);
    }

    private void computeIntoleranceForce() {
        Vector2D intoleranceForce = new Vector2D();

        for (Boid boid : strangerNeighbours) {
            Vector2D force = new Vector2D(boid.velocity);
            intoleranceForce.add(force.multiply(-1));
        }
        if (intoleranceForce.norm() > 0) {
            intoleranceForce.divide(strangerNeighbours.size());
        }
        intoleranceForce.multiply(flock.getIntoleranceCoeff());
        forces.add(intoleranceForce);
    }

    private void computeSeparationForce() {
        Vector2D separationForce = new Vector2D();

        ArrayList<Boid> neighbours = new ArrayList<>();
        neighbours.addAll(flockNeighbours);
        neighbours.addAll(strangerNeighbours);
        for (Boid boid : neighbours) {
            if (Vector2D.subtract(this.position, boid.position).norm() <= REPULSE_RANGE) {
                separationForce = Vector2D.subtract(this.position, boid.position).subtract(velocity).multiply(flock.getSeparationCoeff());
            }
        }
        forces.add(separationForce);
    }

    private void computeAlignementForce() {
        Vector2D alignmentForce = new Vector2D();
        for (Boid boid : flockNeighbours) {
            alignmentForce.add(boid.velocity);
        }
        if (alignmentForce.norm() > 0) {
            alignmentForce.divide(flockNeighbours.size());
        }
        alignmentForce.multiply(flock.getAlignementCoeff());
        forces.add(alignmentForce);
    }

    private void computeHuntingForce() {
        Vector2D huntingForce = new Vector2D();

        Vector2D preysFlockCenter = new Vector2D();
        int nbPreys = 0;
        for (Boid boid : strangerNeighbours) {
            if (boid.flock.getType() == App.TYPE_PREY) {
                preysFlockCenter.add(boid.position);
                nbPreys++;
            }
        }
        if (nbPreys > 0) {
            preysFlockCenter.divide(nbPreys);
            huntingForce.add(Vector2D.subtract(preysFlockCenter, position));
        }
        forces.add(huntingForce);
    }

    private void computeFleeingForce() {
        Vector2D fleeingForce = new Vector2D();

        Boid closestPredator = null;
        for (Boid boid : strangerNeighbours) {
            if (boid.flock.getType() == App.TYPE_PREDATOR) {
                double boidDistance = Vector2D.subtract(boid.position, this.position).norm();
                if (closestPredator == null ||  boidDistance < Vector2D.subtract(closestPredator.position, this.position).norm()) {
                    closestPredator = boid;
                }
            }
        }

        if (closestPredator != null) {
            fleeingForce.add(Vector2D.subtract(this.position, closestPredator.position));
        }
        forces.add(fleeingForce);
    }

    private void computeObstacleAvoidanceForce() {
        for (Obstacle obstacle : App.obstacles) {
            if (willCollideObstacle(obstacle)) {
                Vector2D BO = Vector2D.subtract(obstacle.position, this.position); // Vecteur entre le boid et le centre de l'obstacle
                double projection = Math.sqrt(Math.pow(BO.norm(), 2) - Math.pow(obstacle.avoidanceRadius, 2)); // Projection de BO sur une direction qui ne traverse pas l'obstacle

                double m;
                if (BO.norm() >= obstacle.avoidanceRadius) {
                    m = (Math.pow(projection, 2) - Math.pow(obstacle.avoidanceRadius, 2) - Math.pow(BO.norm(), 2)) / (-2 * BO.norm());
                } else {
                    m = BO.norm();
                }

                double q = Math.sqrt(Math.pow(obstacle.avoidanceRadius, 2) - Math.pow(m, 2));

                Vector2D U = BO.normalized(); // Vecteur unitaire entre le boid et le centre de l'obstacle
                Vector2D V = velocity.normalized(); // Vecteur uniteur de la direction du boid
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

    private boolean willCollideObstacle(Obstacle obstacle) {
        Vector2D BO = Vector2D.subtract(obstacle.position, this.position); // Vecteur entre le boid et le centre de l'obstacle
        double projection = Vector2D.scalarProduct(BO, velocity.normalized()); // Projection de BO sur la direction du boid
        double distanceBoidObstacle = Math.sqrt(Math.pow(BO.norm(), 2) - Math.pow(projection, 2)); // Plus petite distance de l'obstacle que le boid pourra atteindre

        return (BO.norm() <= REPULSE_RANGE + obstacle.avoidanceRadius && distanceBoidObstacle < obstacle.avoidanceRadius && projection >= 0);
    }

    private void computeWallAvoidanceForce() {
        double forceIntensity = Math.max(1, velocity.norm()/App.BOIDS_MIN_SPEED);
        if (position.x - REPULSE_RANGE <= 150) {
            forces.add(new Vector2D(forceIntensity, 0));
        } else if (position.x + REPULSE_RANGE >= AppView.SIMULATION_PANEL_WIDTH - 150) {
            forces.add(new Vector2D(-forceIntensity, 0));
        }

        if (position.y - REPULSE_RANGE <= 150) {
            forces.add(new Vector2D(0, forceIntensity));
        } else if (position.y + REPULSE_RANGE >= AppView.HEIGHT - 150) {
            forces.add(new Vector2D(0, -forceIntensity));
        }
    }

    private void applyForces() {
        if (trails.size() > MAX_TRAILS) {
            trails.remove(0);
        }
        trails.add(new Vector2D(position));

        velocity.add(forces);
        if (velocity.norm() > flock.getSpeedLimit()) {
            velocity.scaleNorm(flock.getSpeedLimit());
        } else if (velocity.norm() < App.BOIDS_MIN_SPEED) {
            velocity.scaleNorm(App.BOIDS_MIN_SPEED);
        }

        preventLeavingWindow();

        position.add(velocity);
    }

    private void preventLeavingWindow() {
        if (position.x <= 0) {
            velocity = new Vector2D(flock.getSpeedLimit(), 0);
        } else if (position.x >= AppView.SIMULATION_PANEL_WIDTH) {
            velocity = new Vector2D(-flock.getSpeedLimit(), 0);
        }

        if (position.y <= 0) {
            velocity = new Vector2D(0, flock.getSpeedLimit());
        } else if (position.y >= AppView.HEIGHT) {
            velocity = new Vector2D(0, -flock.getSpeedLimit());
        }
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
        boidShape.lineTo(-BOID_SIZE * Math.cos(boidAngle), BOID_SIZE * Math.sin(boidAngle));
        boidShape.lineTo(BOID_SIZE, 0);
        boidShape.lineTo(-BOID_SIZE * Math.cos(boidAngle), -BOID_SIZE * Math.sin(boidAngle));
        boidShape.closePath();

        AffineTransform initState = g.getTransform();

        g.translate(position.x, position.y);

        double directionAngle = Math.atan2(velocity.y, velocity.x);
        g.rotate(directionAngle);

        g.setColor(flock.getPrimaryColor());
        g.fill(boidShape);
        g.setColor(flock.getSecondaryColor());
        g.draw(boidShape);

        g.setTransform(initState);
    }

    private void drawViewRange(Graphics2D g) {
        double viewRange = flock.getViewRange();

        Arc2D viewRangeShape = new Arc2D.Double(-viewRange, -viewRange, 2 * viewRange, 2 * viewRange, 180 + DEAD_ANGLE / 2, 360 - DEAD_ANGLE, Arc2D.PIE);

        AffineTransform initState = g.getTransform();

        g.translate(position.x, position.y);

        double directionAngle = Math.atan2(velocity.y, velocity.x);
        g.rotate(directionAngle);
        g.setColor(Color.RED);
        g.draw(viewRangeShape);

        g.setTransform(initState);
    }

    public void drawTrails(Graphics2D g) {
        for (int i = 0; i < trails.size(); i++) {
            Vector2D currentTrail = trails.get(i);
            Vector2D nextTrack = (i < trails.size() - 1) ? trails.get(i + 1) : position;
            int alpha = i * 255 / trails.size();
            Color colorTrail = flock.getSecondaryColor();
            g.setColor(new Color(colorTrail.getRed(), colorTrail.getGreen(), colorTrail.getBlue(), alpha));
            g.draw(new Line2D.Double(currentTrail.x, currentTrail.y, nextTrack.x, nextTrack.y));
        }
    }

    public void clearTrails() {
        trails.clear();
    }

    public String toString() {
        return "Boid en " + position;
    }
}
