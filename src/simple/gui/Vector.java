package simple.gui;

public class Vector {
	public double x, y;
	
	/** Returns x as an integer */
	public int getX() { return (int)x; }
	/** Returns y as an integer */
	public int getY() { return (int)y; }
	
	/** Creates a new vector [x, y] */
	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}
	/** Creates a new vector as [m*cos(t), m*sin(t)] 
	 * @param m			magnitude of vector
	 * @param t			angle of vector
	 * @param isRad 	denotes whether to use t as degrees or radians */
	public Vector(double m, double t, boolean isRad) {
		if (isRad) {
			this.x = m*Math.cos(t);
			this.y = m*Math.sin(t);
		} else {
			this.x = m*Math.cos(Math.toRadians(t));
			this.y = m*Math.sin(Math.toRadians(t));
		}
	}
	
	public Vector copy() { return new Vector(x, y); }
	public Vector neg() { return new Vector(-x, -y); }
	
	public double mag() { return Math.pow(Math.pow(x, 2) + Math.pow(y, 2), 0.5); }
	public double dist(Vector v) { return sub(v).mag(); }
	
	public double dotProduct(Vector v) { return x*v.x + y*v.y; }
	public double angle(Vector v) { return Math.acos((this.dotProduct(v) / (this.mag()*v.mag()))); }
	public double angle() { return this.angle(new Vector(1, 0)); }
	
	public Vector normalize() { double m = mag(); return new Vector(x/m, y/m); }
	public Vector rotate(double angle) {
	    return new Vector(x*Math.cos(angle) - y*Math.sin(angle), y*Math.cos(angle) + x*Math.sin(angle));
	}
	public Vector setAngle(double angle) { 
		double m = mag();
		return new Vector(Math.cos(angle)*m, Math.sin(angle)*m);
	}

	public Vector add(Vector v) { return new Vector(x+v.x, y+v.y); }
	public Vector sub(Vector v) { return new Vector(x-v.x, y-v.y); }
	public Vector mult(Vector v) { return new Vector(x*v.x, y*v.y); }
	public Vector div(Vector v) { return new Vector(x/v.x, y/v.y); }
	
	public Vector add(double c) { return new Vector(x+c, y+c); }
	public Vector sub(double c) { return new Vector(x-c, y-c); }
	public Vector mult(double c) { return new Vector(x*c, y*c); }
	public Vector div(double c) { return new Vector(x/c, y/c); }
	
	public Vector add(double cx, double cy) { return new Vector(x+cx, y+cy); }
	public Vector sub(double cx, double cy) { return new Vector(x-cx, y-cy); }
	public Vector mult(double cx, double cy) { return new Vector(x*cx, y*cy); }
	public Vector div(double cx, double cy) { return new Vector(x/cx, y/cy); }
	
	public String toString() {
		return "Vector: X=" + x + "; Y=" + y;
	}
}
