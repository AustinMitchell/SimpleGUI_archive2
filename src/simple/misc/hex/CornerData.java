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
        // For figuring out which corners are adjacent, we need an offset for the directions in HexCornerArray
        // Adding all coordinates gives 1 or -1. Map to 0 or 1 respectively.
        int dirOffset = ((cubeIndex.entry(0) + cubeIndex.entry(1) + cubeIndex.entry(2))*-1 + 1) / 2;
        Tuple[] edges = {new Tuple(cubeIndex).mult(2), new Tuple(cubeIndex).mult(2), new Tuple(cubeIndex).mult(2)};
        for (int i=0; i<3; i++) {
            edges[i] = edges[i].add(HexCornerArray.DIRECTIONS[i*2 + dirOffset]);
        }
        return edges;
    }
    public static Tuple[] adjecentCorners(Tuple cubeIndex) {
        // For figuring out which corners are adjacent, we need an offset for the directions in HexCornerArray
        // Adding all coordinates gives 1 or -1. Map to 0 or 1 respectively.
        int dirOffset = ((cubeIndex.entry(0) + cubeIndex.entry(1) + cubeIndex.entry(2))*-1 + 1) / 2;
        Tuple[] corners = {new Tuple(cubeIndex), new Tuple(cubeIndex), new Tuple(cubeIndex)};
        for (int i=0; i<3; i++) {
            corners[i] = corners[i].add(HexCornerArray.DIRECTIONS[i*2 + dirOffset]);
        }
        return corners;
    }
    public static Tuple[] adjecentHexes(Tuple cubeIndex) {
        // For figuring out which corners are adjacent, we need an offset for the directions in HexCornerArray
        // Adding all coordinates gives 1 or -1. Map to 0 or 1 respectively.
        int dirOffset = ((cubeIndex.entry(0) + cubeIndex.entry(1) + cubeIndex.entry(2))*-1 + 1) / 2;
        Tuple[] hexes = {new Tuple(cubeIndex), new Tuple(cubeIndex), new Tuple(cubeIndex)};
        for (int i=0; i<3; i++) {
            hexes[i] = hexes[i].add(HexCornerArray.DIRECTIONS[i*2 + dirOffset^1]);
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
