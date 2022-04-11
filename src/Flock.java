import java.awt.*;
import java.util.ArrayList;

public class Flock {

	private ArrayList<Boid> boids;
	private double cohesionCoeff = 0.005;
	private double separationCoeff = 1;
	private double alignementCoeff = 1;
	private double segregationCoeff = 1;
	private int viewRange = 100;
	private double speedMax = 10;
	private int fuiteCoeff = 1;
	private int chasseCoeff = 1;
	private String name;
	private FlockColor color;
	public int type;

	public boolean displayViewRange = false;
	public boolean displayTrails = false;

	public Flock(String n, int q, int t, FlockColor c){
		type = t;
		name = n;
		color = c;
		boids = new ArrayList<Boid>();
		addBoids(q);
	}
	
	// Ajout de boids au flock
	public void addBoids(int n){
		for(int i = 0; i<n ; i++){
			Boid b = new Boid(this);
			boids.add(b);
		}
	}

	public void addBoidAt(int x, int y) {
		boids.add(new Boid(this, x, y));
	}
	
	public void removeBoid(Boid b){
		
		boids.remove(b);
	
	}
	
	// Dessin de flock
	public void drawBoids(Graphics2D g){
		for(Boid b : boids){
			b.draw(g);
		}
	}

	public int getBoidsNumber() {
		return boids.size();
	}


	// Getters et setters :

	public ArrayList<Boid> getBoids() {return boids;}

	public double getCohesionCoeff(){return cohesionCoeff;}
	
	public double getSeparationCoeff(){return separationCoeff;}
	
	public double getAlignementCoeff(){return alignementCoeff;}
	
	public double getSegregationCoeff(){return segregationCoeff;}
	
	public void setCohesionCoeff(double c){cohesionCoeff = c;}
	
	public void setSeparationCoeff(double c){separationCoeff = c;}
	
	public void setAlignementCoeff(double c){alignementCoeff = c;}
	
	public void setSegregationCoeff(double c){segregationCoeff = c;}

	public void setFuiteCoeff(int c){fuiteCoeff = c;}

	public int getFuiteCoeff(){return fuiteCoeff;}

	public void setChasseCoeff(int c) {chasseCoeff = c;}

	public int getChasseCoeff(){return chasseCoeff;}

	public int getViewRange(){return viewRange;}

	public void setViewRange(int i){viewRange = i;}

	public double getSpeedMax(){return speedMax;}

	public void setSpeedMax(double x){speedMax = x;}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public FlockColor getColor() {
		return color;
	}

	public void setColor(FlockColor color) {
		this.color = color;
	}

	public void updateBoidNumber(int nbBoids) {
		if (nbBoids < boids.size()) {
			while (boids.size() > nbBoids) {
				boids.remove(boids.size()-1);
			}
		} else {
			addBoids(nbBoids - boids.size());
		}
	}

	@Override
	public String toString() {
		return "Flock " + name + " : " + getBoidsNumber() + " boids";
	}
}
