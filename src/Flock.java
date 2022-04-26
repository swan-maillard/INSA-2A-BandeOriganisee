import java.awt.*;
import java.util.ArrayList;

public class Flock {
    private ArrayList<Boid> boids;

    private double cohesionCoeff = 0.002;
    private double separationCoeff = .05;
    private double alignementCoeff = .05;
    private double intoleranceCoeff = .05;

    private int viewRange = 100;
    private double speedLimit = 10;

    private String name;
    private Colors colors;

    private int type;

    public boolean displayViewRange = false;
    public boolean displayTrails = false;

    public Flock(String name, int number, Colors colors, int type) {
        this.name = name;
        this.colors = colors;
        this.type = type;
        this.boids = new ArrayList<>(number);
        addBoids(number);
    }

    public void addBoids(int number) {
        for (int i = 0; i < number; i++) {
            boids.add(new Boid(this));
        }
    }

    public void addBoidAt(int x, int y) {
        boids.add(new Boid(this, x, y));
    }

    public void drawBoids(Graphics2D g) {
        for (Boid boid : boids) {
            boid.draw(g);
        }
    }

    public int getBoidsNumber() {
        return boids.size();
    }

    public ArrayList<Boid> getBoids() {
        return boids;
    }

    public double getCohesionCoeff() {
        return cohesionCoeff;
    }

    public double getSeparationCoeff() {
        return separationCoeff;
    }

    public double getAlignementCoeff() {
        return alignementCoeff;
    }

    public double getIntoleranceCoeff() {
        return intoleranceCoeff;
    }

    public int getViewRange() {
        return viewRange;
    }

    public double getSpeedLimit() {
        return speedLimit;
    }

    public String getName() {
        return name;
    }

    public Colors getColors() {
        return colors;
    }

    public Color getPrimaryColor() {
        return colors.getPrimaryColor();
    }

    public Color getSecondaryColor() {
        return colors.getSecondaryColor();
    }

    public int getType() {
        return type;
    }

    public void setCohesionCoeff(double coeff) {
        cohesionCoeff = Math.min(0.01, Math.max(0, coeff));
    }

    public void setSeparationCoeff(double coeff) {
        separationCoeff = Math.min(0.1, Math.max(0, coeff));
    }

    public void setAlignementCoeff(double coeff) {
        alignementCoeff = Math.min(0.1, Math.max(0, coeff));
    }

    public void setIntoleranceCoeff(double coeff) {
        intoleranceCoeff = Math.min(0.1, Math.max(0, coeff));
    }

    public void setViewRange(int viewRange) {
        this.viewRange = Math.min(200, Math.max(0, viewRange));
    }

    public void setSpeedLimit(double speedLimit) {
        this.speedLimit = Math.min(App.BOIDS_MAX_SPEED, Math.max(App.BOIDS_MIN_SPEED, speedLimit));
    }

    public void setColors(Colors colors) {
        this.colors = colors;
    }

    public void setType(int type) {
        System.out.println(type);
        if (type == App.TYPE_PREY || type == App.TYPE_PREDATOR) {
            this.type = type;
        }
    }

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
        return "Flock " + name + " : " + getBoidsNumber() + " boids";
    }
}
