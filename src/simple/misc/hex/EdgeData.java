package simple.misc.hex;

import java.util.Arrays;

public class EdgeData<T> {
    private T _data;
    private Tuple _cubeIndex;
    
    public T data() { return _data; }
    public Tuple   internalIndex() { return _cubeIndex; }
    // For calculating screen positions
    public float[] visualIndex()   { 
        float[] visIndex = {_cubeIndex.entry(0)/2f, _cubeIndex.entry(1)/2f, _cubeIndex.entry(2)/2f};
        return visIndex;
    }
    
    public EdgeData(Tuple cubeIndex) {
        this(null, cubeIndex);
    }
    public EdgeData(T data, Tuple cubeIndex) {
        _data = data;
        _cubeIndex = cubeIndex;
    }
    
    public static Tuple[] adjecentEdges(Tuple cubeIndex) {
        // Figure out which coordinate is even, use that as an index
        int edgeDir = ((Math.floorMod(cubeIndex.entry(0),2)^1) + (Math.floorMod(cubeIndex.entry(1),2)^1)*2 + (Math.floorMod(cubeIndex.entry(2),2)^1)*3) - 1;
        Tuple[] edges   = {new Tuple(cubeIndex), new Tuple(cubeIndex), new Tuple(cubeIndex), new Tuple(cubeIndex)};
        for (int i=0; i<4; i++) {
            edges[i] = edges[i].add(HexEdgeArray.DIRECTIONS[edgeDir][i]);
        }
        return edges;
    }
    public static Tuple[] adjecentCorners(Tuple cubeIndex) {
        // Figure out which coordinate is even, use that as an index
        int edgeDir = ((Math.floorMod(cubeIndex.entry(0),2)^1) + (Math.floorMod(cubeIndex.entry(1),2)^1)*2 + (Math.floorMod(cubeIndex.entry(2),2)^1)*3) - 1;
        Tuple[] corners = {new Tuple(cubeIndex), new Tuple(cubeIndex)};
        int[] adjust = {0, 0, 0};
        for (int i=0; i<2; i++) {
            adjust[(edgeDir+1)%3] = i*2 - 1;
            adjust[(edgeDir+2)%3] = i*2 - 1;
            corners[i] = corners[i].add(adjust).mult(0.5f);
        }
        return corners;
    }
    public static Tuple[] adjecentHexes(Tuple cubeIndex) {
        // Figure out which coordinate is even, use that as an index
        int edgeDir = ((Math.floorMod(cubeIndex.entry(0),2)^1) + (Math.floorMod(cubeIndex.entry(1),2)^1)*2 + (Math.floorMod(cubeIndex.entry(2),2)^1)*3) - 1;
        Tuple[] hexes = {new Tuple(cubeIndex), new Tuple(cubeIndex)};
        for (int i=0; i<2; i++) {
            hexes[i] = hexes[i].add(HexEdgeArray.DIRECTIONS[edgeDir][1+i*2]);
            hexes[i] = hexes[i].add(HexEdgeArray.DIRECTIONS[edgeDir][(2+i*2)%4]).mult(0.5f);
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
