package simple.misc.hex;

public class CornerData<T> {
    private T _data;
    private Tuple _cubeIndex;
    
    public T data() { return _data; }
    public Tuple index() { return _cubeIndex; }
    
    public CornerData(Tuple cubeIndex) {
        this(null, cubeIndex);
    }
    public CornerData(T data, Tuple cubeIndex) {
        _data = data;
        _cubeIndex = cubeIndex;
    }
    
    public static Tuple[] adjecentEdges(Tuple cubeIndex) {
        int[][] directions = HexCornerArray.validDirectionsToCorner(cubeIndex);
        Tuple[] edges = {new Tuple(cubeIndex).mult(2), new Tuple(cubeIndex).mult(2), new Tuple(cubeIndex).mult(2)};
        for (int i=0; i<3; i++) {
            edges[i] = edges[i].add(directions[i]);
        }
        return edges;
    }
    public static Tuple[] adjecentCorners(Tuple cubeIndex) {
        int[][] directions = HexCornerArray.validDirectionsToCorner(cubeIndex);
        Tuple[] corners = {new Tuple(cubeIndex), new Tuple(cubeIndex), new Tuple(cubeIndex)};
        for (int i=0; i<3; i++) {
            corners[i] = corners[i].add(directions[i]);
        }
        return corners;
    }
    public static Tuple[] adjecentHexes(Tuple cubeIndex) {
        int[][] directions = HexCornerArray.validDirectionsToHex(cubeIndex);
        Tuple[] hexes = {new Tuple(cubeIndex), new Tuple(cubeIndex), new Tuple(cubeIndex)};
        for (int i=0; i<3; i++) {
            hexes[i] = hexes[i].add(directions[i]);
        }
        return hexes;
    }
    public Tuple[] adjecentEdges() {
        return adjecentEdges(_cubeIndex);
    }
    public Tuple[] adjecentCorners() {
        return adjecentCorners(_cubeIndex);
    }
    // Same as corners but opposite directions. We flip _dirOffset
    public Tuple[] adjecentHexes() {
        return adjecentHexes(_cubeIndex);
    }
}
