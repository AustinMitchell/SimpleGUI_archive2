package simple.misc.hex;

import java.util.*;

public class Tuple {
    public static abstract class Generator implements Iterable<Tuple> {
        /** Creates an iterator object */
        public abstract Iterator<Tuple> create();
        /** Returns the value of the size for each tuple in the iteration */
        public abstract int tupleDimension();
        
        @Override
        public Iterator<Tuple> iterator() { return create(); }
    }
    /** Creates a generator that generates coordinates like a 2D array.<P>
     * 
     *      i: first dimension
     *      j: second dimension<P>
     *      
     *      Each iteration results in a 2-tuple
     *      
     *      @param width    maximum value for i
     *      @param height   maximum value for j
     * */
    public static Generator createArrayGenerator(final int width, final int height) {
        return new Generator() {
            final List<Tuple> tupleList = new ArrayList<Tuple>(){{
                    for (int i=0; i<width; i++) {
                        for (int j=0; j<height; j++) {
                            add(new Tuple(i, j));
                        }
                    }}};
            @Override
            public Iterator<Tuple> create() {
                return tupleList.iterator();
            }

            @Override
            public int tupleDimension() {
                return 2;
            }};
    }
    /** Creates a generator that starts at 0,0,0 in a hex grid and generates coordinates
     * in a radius around the origin, i.e. max(|x|,|y|,|z|) &lt;= radius for all points generated.<P>
     * 
     *      x: first cube coordinate dimension
     *      y: second cube coordinate dimension
     *      z: third cube coordinate dimension
     *      rad: current radius being worked on
     *      dir: specifies which direction to go on each repetition.
     *      rep: repetition counter where the (x,y,z) are modified<P>
     *      
     *      Each iteration results in a 3-tuple
     *      
     *      @param radius       maximum value for rad
     *      @param centerx      coordinate offset for x value
     *      @param centery      coordinate offset for y value
     *      @param centerz      coordinate offset for z value
     * */
    public static Generator createRadialHexGenerator(final int radius, final int centerx, final int centery, final int centerz) {
        return new Generator() {
            final List<Tuple> tupleList = new ArrayList<Tuple>() {{
                    add(new Tuple(centerx, centery, centerz));
                    for (int rad=1; rad<=radius; rad++) {
                        int x = centerx - rad;
                        int y = centery;
                        int z = centerz + rad;
                        for (int dir=0; dir<6; dir++) {
                            for (int rep=0; rep<rad; rep++) {
                                x += HexArray.DIRECTIONS[dir][0];
                                y += HexArray.DIRECTIONS[dir][1];
                                z += HexArray.DIRECTIONS[dir][2];
                                add(new Tuple(x, y, z));
                            }
                        }
                    }}};
            
            @Override
            public Iterator<Tuple> create() {
                return tupleList.iterator();
            }

            @Override
            public int tupleDimension() {
                return 3;
            }};
    }
    public static Generator createRadialHexGenerator(final int radius) {
        return createRadialHexGenerator(radius, 0, 0, 0);
    }
    
    /** Creates a generator that starts at 0,0,0 in a hex grid and generates coordinates
     * in layers of triangles around the origin, i.e. (|x|+|y|+|z|)/2 &lt;= 2*layers for all points 
     * generated.<P>
     * 
     *      x: first cube coordinate dimension
     *      y: second cube coordinate dimension
     *      z: third cube coordinate dimension
     *      layer: current layer being worked on
     *      dir:   specifies which direction to go on each repetition.
     *      rep:   repetition counter where the (x,y,z) are modified<P>
     *      
     *      Each iteration results in a 3-tuple
     *      
     *      @param layers       layers in the triangle
     *      @param invert       Evenness designates triangles orientation
     *      @param centerx      coordinate offset for x value
     *      @param centery      coordinate offset for y value
     *      @param centerz      coordinate offset for z value
     * */
    public static Generator createTriangleHexGenerator(final int layers, final int invert, final int centerx, final int centery, final int centerz) {
        final int mult = (invert&1)*2 - 1;
        return new Generator() {
            final List<Tuple> tupleList = new ArrayList<Tuple>() {{
                    add(new Tuple(centerx, centery, centerz));
                    for (int layer=1; layer<=layers; layer++) {
                        int x = centerx - 2*layer*mult;
                        int y = centery + layer*mult;
                        int z = centerz + layer*mult;
                        for (int dir=0; dir<6; dir+=2) {
                            for (int rep=0; rep<layer*3; rep++) {
                                x += HexArray.DIRECTIONS[dir][0]*mult;
                                y += HexArray.DIRECTIONS[dir][1]*mult;
                                z += HexArray.DIRECTIONS[dir][2]*mult;
                                add(new Tuple(x, y, z));
                            }
                        }
                    }}};
            
            @Override
            public Iterator<Tuple> create() {
                return tupleList.iterator();
            }

            @Override
            public int tupleDimension() {
                return 3;
            }};
    }
    
    private int[] _entry;
    
    public Tuple(int... entry) {
        _entry = entry;
    }
    public Tuple(Tuple other) {
        _entry = Arrays.copyOf(other.get(), other.length());
    }
    
    
    public int   get(int i) { return _entry[i]; }
    public int[] get()      { return _entry; }
    public int   length()   { return _entry.length; }
    
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Tuple)) {
           return false;
        }
        Tuple otherTuple = (Tuple)other;
        if (this._entry.length != otherTuple._entry.length) {
            return false;
        }
        for (int i=0; i<this._entry.length; i++) {
            if (this._entry[i] != otherTuple._entry[i]) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public int hashCode(){
        return Arrays.hashCode(_entry);
    }
    
    @Override
    public String toString() {
        if (_entry.length == 0) return "";
        
        String str = "(";
        for (int e: _entry) {
            str += e+", ";
        }
        return str.substring(0, str.length()-2) + ")";
        
    }
}
