package simple.misc.hex;

public class HexData<T> {
    public static abstract class CoordinateConverter {
        public abstract float[][] getAxisVectors();
        
        public abstract Tuple baseToCubeIndex(int even, int... index);
        public abstract Tuple cubeToBaseIndex(int even, int... index);
        
        public final Tuple baseToCubeIndex(int even, Tuple pair)   { return baseToCubeIndex(even, pair.entries()); }
        public final Tuple cubeToBaseIndex(int even, Tuple triple) { return cubeToBaseIndex(even, triple.entries()); }
    }
    public static final CoordinateConverter FLAT_TOP_COORD = new CoordinateConverter() {
        public final float[][] AXIS_VECTOR = {
                {(float)Math.cos( Math.PI*2.0/6), (float)Math.sin( Math.PI*2.0/6)},
                {(float)Math.cos(-Math.PI*2.0/6), (float)Math.sin(-Math.PI*2.0/6)},
                {-1, 0}
            };
    
        public float[][] getAxisVectors() { return AXIS_VECTOR; }
        
        /** Based on a formula for turning offset-square grid coordinates into cube flat-top-hexagon coordinates
         *  e = evenness of grid, 0 or 1
         *  x = square x index
         *  y = square y index
         *  
         *  hx =  x + (y+e)/2
         *  hy =  x - (y+(e xor 1))/2
         *  hz = -x
         * */
        public Tuple baseToCubeIndex(int even, int... index) {
            even &= 1;
            return new Tuple(index[1] + (index[0]+even)/2,
                            -index[1] + (index[0]+(even^1))/2,
                            -index[0]);
        }
        /** Inverse of function above */
        public Tuple cubeToBaseIndex(int even, int... index) {
            return new Tuple(-index[2], index[0] - (even-index[2])/2);
        }};
        
    public static final CoordinateConverter POINT_TOP_COORD = new CoordinateConverter() {
        public final float[][] AXIS_VECTOR = {
                {(float)Math.cos(Math.PI*1.0/6), (float)Math.sin(Math.PI*1.0/6)},
                {(float)Math.cos(Math.PI*5.0/6), (float)Math.sin(Math.PI*5.0/6)},
                {0, -1}
            };
        
        public float[][] getAxisVectors() { return AXIS_VECTOR; }
    
        /** Based on a formula for turning offset-square grid coordinates into cube pointy-top-hexagon coordinates
         *  e = evenness of grid, 0 or 1
         *  x = square x index
         *  y = square y index
         *  
         *  hx =  x + (y+e)/2
         *  hy = -x + (y+(e xor 1))/2
         *  hz = -y
         * */
        public Tuple baseToCubeIndex(int even, int... index) {
            even &= 1;
            return new Tuple(index[0] + (index[1]+even)/2,
                            -index[0] + (index[1]+(even^1))/2, 
                            -index[1]);
        }
        /** Inverse of function above */
        public Tuple cubeToBaseIndex(int even, int... index) {
            even &= 1;
            return new Tuple(index[0] - (even-index[2])/2, -index[2]);
        }};
        
    public abstract interface Generator<E> {
        public E generate(int x, int y, int hx, int hy, int hz);
        public E generate(Tuple base, Tuple cube);
    }
    
    private T _data;
    private Tuple _baseIndex, _cubeIndex;

    public Tuple baseIndex()  { return _baseIndex; }
    public Tuple cubeIndex()  { return _cubeIndex; }
    public T     data()       { return _data; }
    
    public void  setData(T data) { _data = data; }

    public HexData(T data, int x, int y, int even, CoordinateConverter coordConv) {
        _data = data;
        _baseIndex = new Tuple(x, y);
        _cubeIndex = coordConv.baseToCubeIndex(even, _baseIndex);
    }
    public HexData(T data, int hx, int hy, int hz, int even, CoordinateConverter coordConv) {
        _data = data;
        _cubeIndex = new Tuple(hx, hy, hz);
        _baseIndex = coordConv.cubeToBaseIndex(even, _cubeIndex);
    }
    public HexData(T data, Tuple pos, int even, CoordinateConverter coordConv) {
        _data = data;
        if (pos.length() == 2) {
            _baseIndex = new Tuple(pos);
            _cubeIndex = coordConv.baseToCubeIndex(even, _baseIndex);
        } else {
            _cubeIndex = new Tuple(pos);
            _baseIndex = coordConv.cubeToBaseIndex(even, _cubeIndex);
        }
    }
    
    public HexData(int x, int y, int even, CoordinateConverter coordConv, Generator<T> dgen) {
        _baseIndex = new Tuple(x, y);
        _cubeIndex = coordConv.baseToCubeIndex(even, _baseIndex);
        _data = (dgen==null) ? null : dgen.generate(_baseIndex, _cubeIndex);
    }
    public HexData(int hx, int hy, int hz, int even, CoordinateConverter coordConv, Generator<T> dgen) {
        _cubeIndex = new Tuple(hx, hy, hz);
        _baseIndex = coordConv.cubeToBaseIndex(even, _cubeIndex);
        _data = (dgen==null) ? null : dgen.generate(_baseIndex, _cubeIndex);
    }
    public HexData(Tuple pos, int even, CoordinateConverter coordConv, Generator<T> dgen) {
        if (pos.length() == 2) {
            _baseIndex = new Tuple(pos);
            _cubeIndex = coordConv.baseToCubeIndex(even, _baseIndex);
        } else {
            _cubeIndex = new Tuple(pos);
            _baseIndex = coordConv.cubeToBaseIndex(even, _cubeIndex);
        }
        _data = (dgen==null) ? null : dgen.generate(_baseIndex, _cubeIndex);
    }
    
    public static Tuple[] adjecentEdges(Tuple cubeIndex) {
        Tuple[] edges = {new Tuple(cubeIndex).mult(2), new Tuple(cubeIndex).mult(2), new Tuple(cubeIndex).mult(2), 
                         new Tuple(cubeIndex).mult(2), new Tuple(cubeIndex).mult(2), new Tuple(cubeIndex).mult(2)};
        for (int i=0; i<6; i++) {
            edges[i] = edges[i].add(HexArray.DIRECTIONS[i]);
        }
        return edges;
    }
    public static Tuple[] adjecentCorners(Tuple cubeIndex) {
        Tuple[] corners = {new Tuple(cubeIndex), new Tuple(cubeIndex), new Tuple(cubeIndex), new Tuple(cubeIndex), new Tuple(cubeIndex), new Tuple(cubeIndex)};
        for (int i=0; i<6; i++) {
            corners[i] = corners[i].add(HexCornerArray.DIRECTIONS[i]);
        }
        return corners;
    }
    public static Tuple[] adjecentHexes(Tuple cubeIndex) {
        Tuple[] hexes = {new Tuple(cubeIndex), new Tuple(cubeIndex), new Tuple(cubeIndex), new Tuple(cubeIndex), new Tuple(cubeIndex), new Tuple(cubeIndex)};
        for (int i=0; i<6; i++) {
            hexes[i] = hexes[i].add(HexArray.DIRECTIONS[i]);
        }
        return hexes;
    }
    public Tuple[] adjecentEdges() {
        return adjecentEdges(_cubeIndex);
    }
    public Tuple[] adjecentCorners() {
        return adjecentCorners(_cubeIndex);
    }
    public Tuple[] adjecentHexes() {
        return adjecentHexes(_cubeIndex);
    }
}

