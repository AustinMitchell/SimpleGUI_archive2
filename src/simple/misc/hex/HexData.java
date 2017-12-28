package simple.misc.hex;

public class HexData<T> {
    public static abstract class CoordinateConverter {        
        public abstract Tuple baseToCubeIndex(int even, int... index);
        public abstract Tuple cubeToBaseIndex(int even, int... index);
        
        public final Tuple baseToCubeIndex(int even, Tuple pair)   { return baseToCubeIndex(even, pair.entries()); }
        public final Tuple cubeToBaseIndex(int even, Tuple triple) { return cubeToBaseIndex(even, triple.entries()); }
    }
    
    public static final CoordinateConverter FLAT_TOP_COORD = new CoordinateConverter() {        
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
        public E generate(int hx, int hy, int hz);
        public E generate(Tuple cube);
    }
    
    private T _data;
    private Tuple _index;

    public Tuple cubeIndex()  { return _index; }
    public T     data()       { return _data; }
    
    public void  setData(T data) { _data = data; }

    public HexData(T data, int hx, int hy, int hz) {
        _data = data;
        _index = new Tuple(hx, hy, hz);
    }
    public HexData(T data, Tuple pos) {
        _data = data;
        _index = new Tuple(pos);
    }

    public HexData(int hx, int hy, int hz, Generator<T> dgen) {
        _index = new Tuple(hx, hy, hz);
        _data = (dgen==null) ? null : dgen.generate(_index);
    }
    public HexData(Tuple pos, Generator<T> dgen) {
        _index = new Tuple(pos);
        _data = (dgen==null) ? null : dgen.generate(_index);
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
            corners[i] = corners[i].add(HexCornerArray.DIRECTIONS[i&1][i>>1]);
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
        return adjecentEdges(_index);
    }
    public Tuple[] adjecentCorners() {
        return adjecentCorners(_index);
    }
    public Tuple[] adjecentHexes() {
        return adjecentHexes(_index);
    }
}

