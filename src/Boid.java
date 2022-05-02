import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;

/**
 * Un boid représente un individu faisant partie d'une espèce
 * Il suit des règles lui appliquant des forces en fonction des boids environnants
 */
public class Boid {

    // Taille du boid
    private final double BOID_SIZE = 10;
    // Angle mort de son champ de vision (en degré)
    private final double DEAD_ANGLE = 45;
    // Rayon de répulsion
    private final double REPULSE_RANGE = 20;
    // Nombre de traces maximales (le boid laisse des traces derrière lui)
    private final int MAX_TRAILS = 50;


    // Vecteurs position, vitesse et forces
    private Vector2D position;
    private Vector2D velocity;
    private Vector2D forces;

    // Traces laissées par le boid
    private ArrayList<Vector2D> trails;

    // Boids voisins d'une même espèce
    private ArrayList<Boid> strangerNeighbours;
    // Boids voisins d'une espèce différente
    private ArrayList<Boid> flockNeighbours;

    // Espèce à laquelle le boid fait partie
    private Flock flock;


    /**
     * Constructeur
     *
     * @param flock Espèce du boid
     */
    public Boid(Flock flock) {
        this(flock, 0, 0);
        setRandomPosition();
    }

    /**
     * Constructeur
     *
     * @param flock Espèce du boid
     * @param x     Position x du boid
     * @param y     Position y du boid
     */
    public Boid(Flock flock, double x, double y) {
        this.flock = flock;
        position = new Vector2D(x, y);
        setRandomVelocity();
        forces = new Vector2D();
        trails = new ArrayList<>(MAX_TRAILS);
    }

    /**
     * Définit une position aléatoire au boid
     */
    public void setRandomPosition() {
        position = new Vector2D(Math.random() * AppView.SIMULATION_PANEL_WIDTH, Math.random() * AppView.HEIGHT);
    }

    /**
     * Définit une vitesse aléatoire au boid
     */
    public void setRandomVelocity() {
        do {
            velocity = new Vector2D(Math.random() * 2 * flock.getSpeedLimit() - flock.getSpeedLimit(), Math.random() * 2 * flock.getSpeedLimit() - flock.getSpeedLimit());
        } while (velocity.norm() < App.BOIDS_MIN_SPEED);
    }

    /**
     * Trouve les voisins du boid
     * Un boid est considéré comme voisin s'il se trouve dans son champ de vision
     */
    private void findNeighbours() {
        flockNeighbours = new ArrayList<>();
        strangerNeighbours = new ArrayList<>();

        for (Flock fl : App.flocks) {
            for (Boid boid : fl.getBoids()) {

                Vector2D vectorBetweenBoids = Vector2D.subtract(boid.position, this.position);
                double angleBoid = Math.atan2(velocity.y, velocity.x);
                double angleBetweenBoids = Math.abs(Math.atan2(vectorBetweenBoids.y, vectorBetweenBoids.x));
                double deadAngleRadian = Math.PI * DEAD_ANGLE / 180;

                // Calcule si le boid se trouve dans son angle mort
                boolean isBoidInDeadAngle = (angleBetweenBoids - angleBoid >= Math.PI - deadAngleRadian / 2 && angleBetweenBoids - angleBoid <= Math.PI + deadAngleRadian / 2);

                // Si le boid est dans le rayon du champ de vision et pas dans l'angle mort, c'est un voisin
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

    /**
     * Calcule les forces à appliquer à chaque instant
     */
    private void computeForces() {
        forces.multiply(0);

        findNeighbours();

        computeCohesionForce();
        computeSeparationForce();
        computeAlignementForce();
        computeIntoleranceForce();

        // Si le boid est un prédateur on calcule une force de chasse
        // Sinon so le boid est une proie on calcule une force de fuite
        if (flock.getType() == App.TYPE_PREDATOR) {
            computeHuntingForce();
        } else {
            computeFleeingForce();
        }

        computeObstacleAvoidanceForce();
        computeWallAvoidanceForce();

        // On applique les forces
        applyForces();
    }

    /**
     * Calcule la force de cohésion
     * Cette force est dirigée vers le centre local des boids voisins
     */
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

    /**
     * Calcule la force d'intolérance
     * Cette force s'oppose aux boids voisins d'une espèce différente
     */
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

    /**
     * Calcule la force de séparation
     * Cette force s'oppose à un boid s'il est dans le champ de répulsion
     */
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

    /**
     * Calcule la force d'alignement
     * Cette force s'aligne avec les vitesses moyennes des boids voisins
     */
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

    /**
     * Calcule la force de chasse
     * Cette force est dirigée vers le centre local des voisins de type proie
     */
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

    /**
     * Calcule la force de fuite
     * Cette force est dirigée à l'opposé du boid prédateur voisin le plus proche
     */
    private void computeFleeingForce() {
        Vector2D fleeingForce = new Vector2D();

        Boid closestPredator = null;
        for (Boid boid : strangerNeighbours) {
            if (boid.flock.getType() == App.TYPE_PREDATOR) {
                double boidDistance = Vector2D.subtract(boid.position, this.position).norm();
                if (closestPredator == null || boidDistance < Vector2D.subtract(closestPredator.position, this.position).norm()) {
                    closestPredator = boid;
                }
            }
        }

        if (closestPredator != null) {
            fleeingForce.add(Vector2D.subtract(this.position, closestPredator.position));
        }
        forces.add(fleeingForce);
    }

    /**
     * Calcule la force d'évitement des obstacles
     */
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

    /**
     * Renvoie si le boid risque de heurter un obstacle s'il ne dévie pas sa trajectoire
     *
     * @param obstacle
     * @return si le boid risque de heurter un obstacle
     */
    private boolean willCollideObstacle(Obstacle obstacle) {
        Vector2D BO = Vector2D.subtract(obstacle.position, this.position); // Vecteur entre le boid et le centre de l'obstacle
        double projection = Vector2D.scalarProduct(BO, velocity.normalized()); // Projection de BO sur la direction du boid
        double distanceBoidObstacle = Math.sqrt(Math.pow(BO.norm(), 2) - Math.pow(projection, 2)); // Plus petite distance de l'obstacle que le boid pourra atteindre

        return (BO.norm() <= REPULSE_RANGE + obstacle.avoidanceRadius && distanceBoidObstacle < obstacle.avoidanceRadius && projection >= 0);
    }

    /**
     * Calcule la force d'évitement des bords de l'écran
     */
    private void computeWallAvoidanceForce() {
        double forceIntensity = Math.max(1, velocity.norm() / App.BOIDS_MIN_SPEED);
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

    /**
     * Applique les forces au boid
     * Modifie la vitesse du boid
     * Modifie la position du boid
     */
    private void applyForces() {
        if (trails.size() > MAX_TRAILS) {
            trails.remove(0);
        }
        trails.add(new Vector2D(position));

        velocity.add(forces);

        // On limite la vitesse pour ne pas qu'elle soit contenue entre un intervalle
        if (velocity.norm() > flock.getSpeedLimit()) {
            velocity.scaleNorm(flock.getSpeedLimit());
        } else if (velocity.norm() < App.BOIDS_MIN_SPEED) {
            velocity.scaleNorm(App.BOIDS_MIN_SPEED);
        }

        // Empêche le boid de sortir de la fenêtre
        preventLeavingWindow();

        position.add(velocity);
    }

    /**
     * Empêche les boids de sortir de la fenêtre
     */
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

    /**
     * Rafraichit le boid (Recalcule sa position et le redessine)
     *
     * @param g Graphics2D
     */
    public void refresh(Graphics2D g) {
        computeForces();

        if (flock.displayTrails) drawTrails(g);
        drawBoid(g);
        if (flock.displayViewRange) drawViewRange(g);
    }

    /**
     * Dessine le boid
     *
     * @param g Graphics2D
     */
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

    /**
     * Dessine le champ de vision
     *
     * @param g Graphics2D
     */
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

    /**
     * Dessine les traces
     *
     * @param g Graphics2D
     */
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

    /**
     * Supprime les traces
     */
    public void clearTrails() {
        trails.clear();
    }

    public String toString() {
        return "Boid en " + position;
    }
}
