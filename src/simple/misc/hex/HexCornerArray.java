package simple.misc.hex;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HexCornerArray<T> implements Iterable<CornerData<T>> {
    /** Coordinate differences between different corners (although only 3 are valid per corner) */
    public static final int[][][] DIRECTIONS = {
            // Directions in terms of triangles making up a flat-top hexagon
            {
                { 1,  0,  0}, // right-up
                { 0,  1,  0}, // left-up
                { 0,  0,  1} // down
            },
            {
                { 0,  0, -1}, // up
                {-1,  0,  0}, // left-down
                { 0, -1,  0}  // right-down
            }
        };
    
    public static int[][] validDirectionsToCorner(Tuple cornerCoord) {
        // For figuring out which corners are adjacent, we need an offset for the directions in HexCornerArray
        // Adding all coordinates gives 1 or -1. Map to 0 or 1 respectively.
        return DIRECTIONS[((cornerCoord.entry(0) + cornerCoord.entry(1) + cornerCoord.entry(2))*-1 + 1) / 2];
    }
    public static int[][] validDirectionsToHex(Tuple cornerCoord) {
        // For figuring out which corners are adjacent, we need an offset for the directions in HexCornerArray
        // Adding all coordinates gives 1 or -1. Map to 0 or 1 respectively.
        return DIRECTIONS[((cornerCoord.entry(0) + cornerCoord.entry(1) + cornerCoord.entry(2)) + 1) / 2];
    }
    
    
    // Coordinates only made of cube coordinates
    protected Map<Tuple, CornerData<T>> _cubeMap;

    public CornerData<T> atIndex(int x, int y) { return atIndex(new Tuple(x, y)); }
    public CornerData<T> atIndex(Tuple cube) { return _cubeMap.get(cube); }
    
    public void setAtIndex(T data, int x, int y) { setAtIndex(data, new Tuple(x, y)); }
    public void setAtIndex(T data, Tuple cube) { _cubeMap.put(cube, new CornerData<T>(data, cube)); }
    
    public void addAtIndex(int x, int y) { addAtIndex(new Tuple(x, y)); }
    public void addAtIndex(Tuple cube) { 
        if (!_cubeMap.containsKey(cube)) _cubeMap.put(cube, new CornerData<T>(cube)); 
    }
    
    public HexCornerArray() {
        _cubeMap = new HashMap<Tuple, CornerData<T>>();
    }
    public <E> HexCornerArray(HexArray<E> baseArray) {
        _cubeMap = new HashMap<Tuple, CornerData<T>>();
        
        for (HexData<E> hex: baseArray) {
            for (Tuple cube: hex.adjecentCorners()) {
                addAtIndex(cube);
            }
        }
    }
    
    @Override
    public Iterator<CornerData<T>> iterator() {
        // TODO Auto-generated method stub
        return _cubeMap.values().iterator();
    }
}

