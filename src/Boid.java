import java.awt.*;
import java.util.ArrayList;

public class Boid {

    private final int MIN_WIDTH = 5;
    private final int MAX_TRAILS = 50;
    private final int SIZE = 10;
    private final int REPULSE_RANGE = 20;

    private Vector2D position;
    private Vector2D vitesse;
    private Vector2D forces;
    private ArrayList<Vector2D> trails;
    private ArrayList<Boid> neighbours;
    private ArrayList<Boid> flockNeighbours;
    private Flock flock;

    public Boid(Flock flock) {
        this(flock, Math.random() * GUI.SIMULATION_PANEL_WIDTH, Math.random() * GUI.HEIGHT);
    }

    public Boid(Flock flock, double x, double y) {
        position = new Vector2D(x, y);
        do {
            vitesse = new Vector2D(Math.random() * flock.getSpeedMax(), Math.random() * flock.getSpeedMax());
        } while (vitesse.norm() < MIN_WIDTH);

        forces = new Vector2D();

        trails = new ArrayList<>(MAX_TRAILS);

        this.flock = flock;
    }

    //recherche et ajoute les voisins du boids qui vont avoir une influence sur son déplacement
    private void findNeighbours() {

        //récupère l'ensemble des boids de sa flock et l'enlève de celle-ci
        flockNeighbours = new ArrayList<>();
        ArrayList<Boid> ensembleBoid = new ArrayList<>();
        for (Boid b : flock.getBoids()){
            ensembleBoid.add(b);
        }
        ensembleBoid.remove(this);

        //trouve les voisins dans la même flock
        for(Boid boid : ensembleBoid ){
            System.out.println(getDistanceTo(boid));
            if(getDistanceTo(boid)<flock.getViewRange()){
                flockNeighbours.add(boid);
            }
        }

        neighbours = new ArrayList<>();
        //récupère l'ensemble des flock et enlève celle qui contient le Boid pour ne pas avoir de doublon

        ArrayList<Flock> ensembleFlock =new ArrayList<>();
        for (Flock f : GUI.flocks) {
            ensembleFlock.add(f);
        }
        ensembleFlock.remove(flock);

        //trouve les voisins dans les autres flocks
        for(Flock f : ensembleFlock){
            for (Boid b : f.getBoids()){
                if(getDistanceTo(b)<flock.getViewRange()){
                    neighbours.add(b);
                }
            }
        }
    }
    //calcule les forces qui s'exercent sur ce boid
    private void computeForces() {
        findNeighbours();
            for(Boid b : neighbours){
                System.out.println(b + " je suis un voisin d'une autre folck");
            }
            for (Boid boid : flockNeighbours) {
                System.out.println(boid +" je suis un voisin de la même folck");
            }

        neighbours.clear();
        flockNeighbours.clear();
        System.out.println("======================");
        System.out.println();
        System.out.println();
        System.out.println();
    }

    private void applyForces() {

    }

    public void drawBoid(Graphics g) {
        computeForces();
        g.setColor(flock.getColor().getColor());
        g.fillOval((int)position.x - 5, (int)position.y - 5, 10, 10);
    }

    private double getDistanceTo(Boid boid) {
        return Vector2D.substract(this.position, boid.position).norm();
    }
    public String toString(){
        String s = "ma position est"+position.x;

        return s;
    }
}
