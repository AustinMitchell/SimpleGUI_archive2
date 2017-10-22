package simple.misc.hex;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HexEdgeArray<T> implements Iterable<EdgeData<T>> {
    /** Coordinate differences between different edges. Alignment determines 4 direction possibilities */
    public static final int[][][] DIRECTIONS = {
            { // x-aligned edge
                { 1,  0, -1},
                { 1, -1,  0},
                {-1,  0,  1},
                {-1,  1,  0}
            },
            { // y-aligned edge
                { 0,  1, -1},
                {-1,  1,  0},
                { 0, -1,  1},
                { 1, -1,  0}
            },
            { // z-aligned edge
                {-1,  0,  1},
                { 0, -1,  1},
                { 1,  0, -1},
                { 0,  1, -1}
            }
        };
    
    // Coordinates only made of cube coordinates
    protected Map<Tuple, EdgeData<T>> _cubeMap;

    public EdgeData<T> atIndex(int x, int y) { return atIndex(new Tuple(x, y)); }
    public EdgeData<T> atIndex(Tuple cube) { return _cubeMap.get(cube); }
    
    public void setAtIndex(T data, int x, int y) { setAtIndex(data, new Tuple(x, y)); }
    public void setAtIndex(T data, Tuple cube) { _cubeMap.put(cube, new EdgeData<T>(data, cube)); }
    
    public void addAtIndex(int x, int y) { addAtIndex(new Tuple(x, y)); }
    public void addAtIndex(Tuple cube) { 
        if (!_cubeMap.containsKey(cube)) _cubeMap.put(cube, new EdgeData<T>(cube)); 
    }
    
    public HexEdgeArray() {
        _cubeMap = new HashMap<Tuple, EdgeData<T>>();
    }
    public <E> HexEdgeArray(HexArray<E> baseArray) {
        _cubeMap = new HashMap<Tuple, EdgeData<T>>();
        
        for (HexData<E> hex: baseArray) {
            for (Tuple cube: hex.adjecentCorners()) {
                addAtIndex(cube);
            }
        }
    }
    
    @Override
    public Iterator<EdgeData<T>> iterator() {
        // TODO Auto-generated method stub
        return _cubeMap.values().iterator();
    }
}
