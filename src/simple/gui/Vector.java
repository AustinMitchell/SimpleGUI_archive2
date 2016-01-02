package simple.gui;

/** The Vector class simply holds two double values, x and y, and has a variety of ways to manipulate them. 
 * <P>Notice the difference between the intance and the static functions: Static functions return a new Vector object with the results of an
 * operation on a vector, while instance functions directly act on the vector itself and the vector it returns is itself. **/
public class Vector {
	/** Returns the negative of the given vector **/
	public static Vector neg(Vector v) { return new Vector(-v.x, -v.y); }
	/** Returns the inverse of the given vector **/
	public static Vector inv(Vector v) { return new Vector(v.y, v.x); }
			
	/** Returns the normalization of the given vector **/
	public static Vector normalize(Vector v) { double m = v.mag(); return new Vector(v.x/m, v.y/m); }
	/** Returns the normalization of the given vector multiplied by a scalar **/
	public static Vector normalize(Vector v, double c) { double m = v.mag(); return new Vector(v.x*c/m, v.y*c/m); }
	/** Returns a new vector of the given vector rotated by the given angle **/
	public static Vector rotate(Vector v, double angle) {
	    return new Vector(v.x*Math.cos(angle) - v.y*Math.sin(angle), v.y*Math.cos(angle) + v.x*Math.sin(angle));
	}
	/** Returns a new vector with the same magnitude as the given vector, set at the given angle **/
	public static Vector setAngle(Vector v, double angle) { 
		double m = v.mag();
		return new Vector(Math.cos(angle)*m, Math.sin(angle)*m);
	}

	/** Returns addition of two vectors **/
	public static Vector add (Vector a, Vector b) { return new Vector(a.x+b.x, a.y+b.y); }
	/** Returns the subtraction of two vectors, in the order given **/
	public static Vector sub (Vector a, Vector b) { return new Vector(a.x-b.x, a.y-b.y); }
	/** Returns the multiplication of two vectors, by multiplying each component **/
	public static Vector mult(Vector a, Vector b) { return new Vector(a.x*b.x, a.y*b.y); }
	/** Returns the division of two vectors, by dividing each component. WARNING: Does not check for 0 **/
	public static Vector div (Vector a, Vector b) { return new Vector(a.x/b.x, a.y/b.y); }
	
	/** Returns the addition of a vector with a scalar added to each component **/
	public static Vector add (Vector v, double c) { return new Vector(v.x+c, v.y+c); }
	/** Returns the subtraction of a vector with a scalar subtracted from each component **/
	public static Vector sub (Vector v, double c) { return new Vector(v.x-c, v.y-c); }
	/** Returns the multiplication of a vector with a scalar multiplying each component **/
	public static Vector mult(Vector v, double c) { return new Vector(v.x*c, v.y*c); }
	/** Returns the division of a vector with a scalar dividing each component **/
	public static Vector div (Vector v, double c) { return new Vector(v.x/c, v.y/c); }
	
	/** Returns the addition of a vector with two scalars added to each component respectively **/
	public static Vector add (Vector v, double cx, double cy) { return new Vector(v.x+cx, v.y+cy); }
	/** Returns the subtraction of a vector with two scalars subtracted from each component respectively **/
	public static Vector sub (Vector v, double cx, double cy) { return new Vector(v.x-cx, v.y-cy); }
	/** Returns the multiplication of a vector with two scalars multiplying each component respectively **/
	public static Vector mult(Vector v, double cx, double cy) { return new Vector(v.x*cx, v.y*cy); }
	/** Returns the division of a vector with two scalars dividing each component respectively **/
	public static Vector div (Vector v, double cx, double cy) { return new Vector(v.x/cx, v.y/cy); }
	
	/** The components of the vector**/
	public double x, y;
	
	/** Returns x as an integer */
	public int x() { return (int)x; }
	/** Returns y as an integer */
	public int y() { return (int)y; }
	
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
	public double dist(Vector v) { return Vector.sub(this, v).mag(); }
	
	/** Returns the dotproduct of this and another vector **/
	public double dotProduct(Vector v) { return x*v.x + y*v.y; }
	/** Returns the angle between this and another vector **/
	public double angle(Vector v) { return Math.acos((this.dotProduct(v) / (this.mag()*v.mag()))); }
	/** Returns the angle of this vector (essentially compares it to the vector[1, 0]) **/
	public double angle() { return this.angle(new Vector(1, 0)); }
	
	/** Normalizes this vector (adjust the x and y components so the magnitude is 1) **/
	public Vector normalize() { double m = mag(); x/=m; y/=m; return this; }
	/** Normalizes this vector and multiplies it by a scalar (adjust the x and y components so the magnitude is c) **/
	public Vector normalize(double c) { double m = mag(); x*=(c/m); y*=(c/m); return this; }
	/** Rotates this vector by the given angle **/
	public Vector rotate(double angle) {
	    double newx = x*Math.cos(angle) - y*Math.sin(angle);
	    y = y*Math.cos(angle) + x*Math.sin(angle);
	    x = newx;
	    return this; 
	}
	/** Adjusts the x and y components of this vector to match a vector of the given angle with the same magnitude **/
	public Vector setAngle(double angle) { 
		double m = mag();
		x = Math.cos(angle)*m;
	    y = Math.sin(angle)*m;
	    return this; 
	}

	/** Multiplies the components of this vector by -1 **/
	public Vector neg() { x*=-1; y*=-1; return this; }
	/** Inverts the components with regards to the line y = x **/
	public Vector inv() { double temp = x; x = y; y = temp; return this; }
	
	/** Adds the components of another vector to components of this vector **/
	public Vector add (Vector v) { x+=v.x; y+=v.y; return this; }
	/** Subtracts the components of another vector from components of this vector **/
	public Vector sub (Vector v) { x-=v.x; y-=v.y; return this; }
	/** Multiplies the components of this vector by components of another vector **/
	public Vector mult(Vector v) { x*=v.x; y*=v.y; return this; }
	/** Divides the components of this vector by the components of another vector. WARNING: Does not check for 0 **/
	public Vector div (Vector v) { x/=v.x; y/=v.y; return this; }
	
	/** Adds the given scalar to each component of this vector **/
	public Vector add (double c) { x+=c; y+=c; return this; }
	/** Subtracts the given scalar from each component of this vector **/
	public Vector sub (double c) { x-=c; y-=c; return this; }
	/** Multiplies each component of this vector by the given scalar **/
	public Vector mult(double c) { x*=c; y*=c; return this; }
	/** Divides each component of this vector by the given scalar **/
	public Vector div (double c) { x/=c; y/=c; return this; }
	
	/** Adds each given scalar to each component of this vector respectively **/
	public Vector add (double cx, double cy) { x+=cx; y+=cy; return this; }
	/** Subtracts each given scalar from each component of this vector respectively **/
	public Vector sub (double cx, double cy) { x-=cx; y-=cy; return this; }
	/** Multiplies each component of this vector by each given scalar respectively **/
	public Vector mult(double cx, double cy) { x*=cx; y*=cy; return this; }
	/** Divides each component of this vector by each given scalar respectively **/
	public Vector div (double cx, double cy) { x/=cx; y/=cy; return this; }
	
	/** Returns a string representation of this vector **/
	public String toString() {
		return "Vector: X=" + x + "; Y=" + y;
	}
}
