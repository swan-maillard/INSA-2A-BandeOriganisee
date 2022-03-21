import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Flock {

	private ArrayList<Boid> boids;
	private double cohesionCoeff;
	private double separationCoeff;
	private double alignementCoeff;
	private double segregationCoeff;
	private int viewRange;
	private double speedMax;
	private String name;
	private FlockColor color;
	
	public Flock(String n, int q, FlockColor c){
		cohesionCoeff = 0;
		separationCoeff = 0;
		alignementCoeff = 0 ;
		segregationCoeff = 0;
		viewRange = 0;
		speedMax = 10;
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
	
	public void removeBoid(Boid b){
		
		boids.remove(b);
	
	}
	
	// Dessin de flock
	public void drawBoids(Graphics g){
		for(Boid b : boids){
			b.drawBoid(g);
		}
	}

	public int getBoidsNumber() {
		return boids.size();
	}
	
	// Getters et setters :
	
	public double getCohesionCoeff(){return cohesionCoeff;}
	
	public double getSeparationCoeff(){return separationCoeff;}
	
	public double getAlignementCoeff(){return alignementCoeff;}
	
	public double getSegregationCoeff(){return segregationCoeff;}
	
	public void setCohesionCoeff(double c){cohesionCoeff = c;}
	
	public void setSeparationCoeff(double c){separationCoeff = c;}
	
	public void setAlignementCoeff(double c){alignementCoeff = c;}
	
	public void setSegregationCoeff(double c){segregationCoeff = c;}

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
}
