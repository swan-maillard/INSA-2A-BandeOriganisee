public class Vector2D {

    private double x;
    private double y;

    public Vector2D(double x, double y){
		this.x = x;
		this.y = y;
	}

	public Vector2D(Vector2D v) {
		this.x = v.x;
		this.y = v.y;
	}
    
    public Vector2D() {
		x = 0;
		y = 0;
	}

	public Vector2D add(Vector2D v){
		x += v.x;
		y += v.y;

		return this;
	}

	public Vector2D substract(Vector2D v){
		x -= v.x;
		y -= v.y;

		return this;
	}

	public Vector2D multiply(double coeff){
		x *= coeff;
		y *= coeff;

		return this;
	}

	public Vector2D divide (double coeff){
		if (coeff != 0) {
			x /= coeff;
			y /= coeff;
		}

		return this;
	}

	public double norm() {
		return Math.sqrt(x * x + y * y);
	}

	public Vector2D normalized() {
		if (norm() > 0) {
			return new Vector2D(x/norm(), y/norm());
		} else {
			return this;
		}
	}

	public Vector2D limitNorm(double limit) {
		limit = Math.abs(limit);

		x = ((x / norm()) * limit);
		y = ((y / norm()) * limit);

		return this;
	}

	public static Vector2D add(Vector2D v1, Vector2D v2) {
		double x = v1.x + v2.x;
		double y = v1.y + v2.y;

		return new Vector2D(x, y);
	}

	public static Vector2D substract(Vector2D v1, Vector2D v2){
		double x = v1.x - v2.x;
		double y = v1.y - v2.y;

		return new Vector2D(x, y);
	}

	public static double scalarProduct(Vector2D v1, Vector2D v2) {
		return (v1.x * v2.x) + (v1.y * v2.y);
	}

	@Override
	public String toString() {
		return "{" + x + ";" + y + "}";
	}
}

