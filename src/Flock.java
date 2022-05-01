import java.awt.*;
import java.util.ArrayList;

/**
 * La flock (nuée) représente une espèce de boids
 */
public class Flock {
    // Liste des boids de cette espèce
    private ArrayList<Boid> boids;

    // Coefficients à appliquer aux différentes forces
    private double cohesionCoeff = 0.002;
    private double separationCoeff = .05;
    private double alignementCoeff = .05;
    private double intoleranceCoeff = .05;

    // Champ de vision des boids de cette espèce
    private int viewRange = 100;
    // Vitesse limite des boids de cette espèce
    private double speedLimit = 10;

    // Nom de l'espèce
    private String name;
    // Couleurs de l'espèce
    private Colors colors;
    // Type de l'espèce (Proie ou Prédateur)
    private int type;

    // Affichage ou non des champs de visions et des traces
    public boolean displayViewRange = false;
    public boolean displayTrails = false;

    /**
     * Constructeur
     *
     * @param name   Nom de l'espèce
     * @param number Nombre de boids
     * @param colors Couleurs de l'espèce
     * @param type   Type de l'espèce (Proie ou Prédateur)
     */
    public Flock(String name, int number, Colors colors, int type) {
        this.name = name;
        this.colors = colors;
        this.type = type;
        this.boids = new ArrayList<>(number);
        addBoids(number);
    }

    /**
     * Ajoute des boids à la nuée
     *
     * @param number nombre de boids
     */
    public void addBoids(int number) {
        for (int i = 0; i < number; i++) {
            boids.add(new Boid(this));
        }
    }

    /**
     * Ajoute un boid à la nuée à une certaine position
     *
     * @param x position x
     * @param y position y
     */
    public void addBoidAt(int x, int y) {
        boids.add(new Boid(this, x, y));
    }

    /**
     * Rafraichit les boids (Recalcule leur position et les redessine)
     *
     * @param g Graphics2D
     */
    public void refreshBoids(Graphics2D g) {
        for (Boid boid : boids) {
            boid.refresh(g);
        }
    }

    /**
     * Renvoie le nombre de boids dans la nuée
     *
     * @return nombre de boids
     */
    public int getBoidsNumber() {
        return boids.size();
    }

    /**
     * Renvoie la liste de boids
     *
     * @return boids
     */
    public ArrayList<Boid> getBoids() {
        return boids;
    }

    /**
     * Renvoie le coefficient de cohésion
     *
     * @return cohesionCoeff
     */
    public double getCohesionCoeff() {
        return cohesionCoeff;
    }

    /**
     * Renvoie le coefficient de séparation
     *
     * @return separationCoeff
     */
    public double getSeparationCoeff() {
        return separationCoeff;
    }

    /**
     * Renvoit le coefficient d'alignement
     *
     * @return alignmentCoeff
     */
    public double getAlignementCoeff() {
        return alignementCoeff;
    }

    /**
     * Renvoit le coefficient d'intolérance
     *
     * @return intoleranceCoeff
     */
    public double getIntoleranceCoeff() {
        return intoleranceCoeff;
    }

    /**
     * Renvoit le rayon du champ de vision
     *
     * @return viewRange
     */
    public int getViewRange() {
        return viewRange;
    }

    /**
     * Renvoit la limite de vitesse
     *
     * @return speedLimit
     */
    public double getSpeedLimit() {
        return speedLimit;
    }

    /**
     * Renvoit le nom de l'espèce
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Renvoit les couleurs
     *
     * @return color
     */
    public Colors getColors() {
        return colors;
    }

    /**
     * Renvoit la couleur primaire
     *
     * @return couleur primaire
     */
    public Color getPrimaryColor() {
        return colors.getPrimaryColor();
    }

    /**
     * Renvoit la couleur secondaire
     *
     * @return couleur secondaire
     */
    public Color getSecondaryColor() {
        return colors.getSecondaryColor();
    }

    /**
     * Renvoit le type de l'espèce (Proie ou Prédateur)
     *
     * @return type
     */
    public int getType() {
        return type;
    }

    /**
     * Modifie le coefficient de cohésion
     *
     * @param coeff coefficient de cohésion
     */
    public void setCohesionCoeff(double coeff) {
        cohesionCoeff = Math.min(0.01, Math.max(0, coeff));
    }

    /**
     * Modifie le coefficient de séparation
     *
     * @param coeff coefficient de séparation
     */
    public void setSeparationCoeff(double coeff) {
        separationCoeff = Math.min(0.1, Math.max(0, coeff));
    }

    /**
     * Modifie le coefficient de alignement
     *
     * @param coeff coefficient de alignement
     */
    public void setAlignementCoeff(double coeff) {
        alignementCoeff = Math.min(0.1, Math.max(0, coeff));
    }

    /**
     * Modifie le coefficient d'intolérance
     *
     * @param coeff coefficient d'intolérance
     */
    public void setIntoleranceCoeff(double coeff) {
        intoleranceCoeff = Math.min(0.1, Math.max(0, coeff));
    }

    /**
     * Modifie le rayon du champ de vision
     *
     * @param viewRange rayon du champ de vision
     */
    public void setViewRange(int viewRange) {
        this.viewRange = Math.min(200, Math.max(0, viewRange));
    }

    /**
     * Modifie la limite de vitesse
     *
     * @param speedLimit limite de vitesse
     */
    public void setSpeedLimit(double speedLimit) {
        this.speedLimit = Math.min(App.BOIDS_MAX_SPEED, Math.max(App.BOIDS_MIN_SPEED, speedLimit));
    }

    /**
     * Modifie les couleurs
     *
     * @param colors couleurs
     */
    public void setColors(Colors colors) {
        this.colors = colors;
    }

    /**
     * Modifie le type de l'espèce (Proie ou Prédateur)
     *
     * @param type type de l'espèce
     */
    public void setType(int type) {
        System.out.println(type);
        if (type == App.TYPE_PREY || type == App.TYPE_PREDATOR) {
            this.type = type;
        }
    }

    /**
     * Modifie le nombre de boids de l'espèce
     *
     * @param number nombre de boids
     */
    public void updateBoidNumber(int number) {
        if (number < boids.size()) {
            while (boids.size() > number) {
                boids.remove(boids.size() - 1);
            }
        } else {
            addBoids(number - boids.size());
        }
    }

    @Override
    public String toString() {
        return "Espèce " + name + " : " + getBoidsNumber() + " boids";
    }
}
