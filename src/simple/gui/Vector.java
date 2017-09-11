package simple.gui;

public class Vector {	
	/** The components of the vector**/
	public double x, y;
	
	/** Returns x as an integer */
	public int x() { return (int)x; }
	/** Returns y as an integer */
	public int y() { return (int)y; }
	
	/** Creates a new vector [x, y] */
    public Vector(double...v) {
        this.x = v[0];
        this.y = v[1];
    }
    /** Creates a new vector [x, y] */
    public Vector(float...v) {
        this.x = v[0];
        this.y = v[1];
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
	
	/** Bounds the vector inside a box with (a.x, a.y) as the bottom left corner and (b.x, b.y) as the upper right corner. When called, 
	 * 		this method will adjust the x and y components of this vector to keep them within the bounding box.**/
	public Vector bound(Vector a, Vector b) {
		x = Math.max(a.x, Math.min(b.x, x));
		y = Math.max(a.y, Math.min(b.y, y));
		return this; 
	}
	
	/** Returns a vector with the same component values **/
	public Vector copy() { return new Vector(x, y); }
	
	/** Returns the magnitude(length) of this vector **/
	public double mag() { return Math.pow(Math.pow(x, 2) + Math.pow(y, 2), 0.5); }
	/** Returns the distance between this vector and another vector. **/
	public double dist(Vector v) { return sub(v).mag(); }
	
	/** Returns the dotproduct of this and another vector **/
	public double dotProduct(Vector v) { return x*v.x + y*v.y; }
	/** Returns the angle between this and another vector **/
	public double angle(Vector v) { return Math.acos((this.dotProduct(v) / (this.mag()*v.mag()))); }
	/** Returns the angle of this vector (essentially compares it to the vector[1, 0]) **/
	public double angle() { return this.angle(new Vector(1, 0)); }
	
	/** Normalizes this vector (adjust the x and y components so the magnitude is 1) **/
	public Vector normalize() { double m = mag(); return new Vector(x/m, y/m); }
	/** Normalizes this vector and multiplies it by a scalar (adjust the x and y components so the magnitude is c) **/
	public Vector normalize(double c) { return normalize().mult(c); }
	/** Rotates this vector by the given angle **/
	public Vector rotate(double angle) {
	    double newx = x*Math.cos(angle) - y*Math.sin(angle);
	    double newy = y*Math.cos(angle) + x*Math.sin(angle);
	    return new Vector(newx, newy); 
	}
	/** Adjusts the x and y components of this vector to match a vector of the given angle with the same magnitude **/
	public Vector setAngle(double angle) { 
		double m = mag();
		double newx = Math.cos(angle)*m;
	    double newy = Math.sin(angle)*m;
	    return new Vector(newx, newy); 
	}

	/** Multiplies the components of this vector by -1 **/
	public Vector neg() { return new Vector(x*-1, y*-1); }
	/** Inverts the components with regards to the line y = x **/
	public Vector inv() { return new Vector(y, x); }
	
	/** Adds the components of another vector to components of this vector **/
	public Vector add (Vector v) { return new Vector(x+v.x, y+v.y); }
	/** Subtracts the components of another vector from components of this vector **/
	public Vector sub (Vector v) { return new Vector(x-v.x, y-v.y); }
	/** Multiplies the components of this vector by components of another vector **/
	public Vector mult(Vector v) { return new Vector(x*v.x, y*v.y); }
	/** Divides the components of this vector by the components of another vector. WARNING: Does not check for 0 **/
	public Vector div (Vector v) { return new Vector(x/v.x, y/v.y); }
	
	/** Adds the given scalar to each component of this vector **/
	public Vector add (double c) { return new Vector(x+c, y+c); }
	/** Subtracts the given scalar from each component of this vector **/
	public Vector sub (double c) { return new Vector(x-c, y-c); }
	/** Multiplies each component of this vector by the given scalar **/
	public Vector mult(double c) { return new Vector(x*c, y*c); }
	/** Divides each component of this vector by the given scalar **/
	public Vector div (double c) { return new Vector(x/c, y/c); }
	
	/** Adds each given scalar to each component of this vector respectively **/
	public Vector add (double cx, double cy) { return new Vector(x+cx, y+cy); }
	/** Subtracts each given scalar from each component of this vector respectively **/
	public Vector sub (double cx, double cy) { return new Vector(x-cx, y-cy); }
	/** Multiplies each component of this vector by each given scalar respectively **/
	public Vector mult(double cx, double cy) { return new Vector(x*cx, y*cy); }
	/** Divides each component of this vector by each given scalar respectively **/
	public Vector div (double cx, double cy) { return new Vector(x/cx, y/cy); }
	
	/** Returns a string representation of this vector **/
	public String toString() {
		return "Vector: X=" + x + "; Y=" + y;
	}
}
