public class Vector2D {
	
	public static void main (String[] args) {
    private double x;
    private double y;
    
    public Vector2D (double x, double y){
		this.x = x;
		this.y = y;
	}
    
    public Vector2D (){
		x=0;
		y=0;
	}
	
	}
	
	public void add (Vector2D v){
		x += v.x ;
		y += v.y ;
	}
	
	public void substract (Vector2D v){
		x = x-v.x ;
		y = y-v.y ;
	}
	
	public static Vector2D substract(Vector2D v1,Vector2D v2){
		x = v1.x-v2.x ;
		y = v1.y-v2.y ;
		return new Vector2D (x,y);
	}
	public void multiply (Vector2D v){
		x = x*(v.x) ;
		y = y*(v.y) ;
	}
	
	public void divide (Vector2D v){
		if (v.x !=0 && v.y !=0){
			x = x/(v.x) ;
			y = y/(v.y) ;
		}
	}
	
	public double norm (){
		return Math.sqrt(x*x + y*y);
	}
	
	public void ceil (double a){
		if(v.norm()>a && a>0){
			x = ((x/v.norm())*a);
			y = ((y/v.norm())*a);
		}
	}
	public void floor (Vector2D v){
		if(v.norm()<a && a>0){
			x = ((x/v.norm())*a);
			y = ((y/v.norm())*a);
		}
	}
	
	
	
}

