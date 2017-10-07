package simple.misc.hex;

import java.util.*;

public class HexArray<T> implements Iterable<HexData<T>> {     
                
     public static final int[][] DIRECTIONS = {
             // comments are in terms of pointy-top coordinates
             { 1, -1,  0}, // right
             { 1,  0, -1}, // right-up
             { 0,  1, -1}, // left-up
             {-1,  1,  0}, // left
             {-1,  0,  1}, // left-down
             { 0, -1,  1}  // right-down
         };
    
            
    /**************************
     * NON-STATIC DEFINITIONS *
     **************************/
    protected Map<Tuple, HexData<T>> _baseMap, _cubeMap;
    protected int _even, _w, _h;
    protected HexData.CoordinateConverter _coordConv;
    protected Tuple.Generator _tupleGenerator;
    
    public HexData<T> getBase(int x, int y)           { return getBase(new Tuple(x, y));       }
    public HexData<T> getCube(int hx, int hy, int hz) { return getBase(new Tuple(hx, hy, hz)); }
    public HexData<T> getBase(Tuple pair)   { return _baseMap.get(pair);   }
    public HexData<T> getCube(Tuple triple) { return _cubeMap.get(triple); }
    public HexData.CoordinateConverter getCoordConv() { return _coordConv; }
    public Tuple.Generator getTupleGenerator() { return _tupleGenerator; }
    
    public void setBase(T data, int x, int y)           { getBase(new Tuple(x, y)).setData(data);  }
    public void setCube(T data, int hx, int hy, int hz) { getBase(new Tuple(hx, hy, hz)).setData(data); }
    public void setBase(T data, Tuple pair)   { _baseMap.get(pair).setData(data);   }
    public void setCube(T data, Tuple triple) { _cubeMap.get(triple).setData(data); }
    public void setTupleGenerator(Tuple.Generator newGenerator) { _tupleGenerator = newGenerator; }
    
    /* Adds a new item into the set*/
    public void put(HexData<T> hexData) { 
        _baseMap.put(hexData.getBaseIndex(), hexData);
        _cubeMap.put(hexData.getCubeIndex(), hexData);
    }
    public void remove(HexData<T> hexData) {
        _baseMap.remove(hexData.getBaseIndex());
        _cubeMap.remove(hexData.getCubeIndex());
    }
    
    public void putBaseCoord(Tuple baseCoord, T data) { 
        put(new HexData<T>(data, baseCoord.get(0), baseCoord.get(1), _even, _coordConv));
    }
    public void removeBaseCoord(Tuple baseCoord) {
        remove(new HexData<T>(null, baseCoord.get(0), baseCoord.get(1), _even, _coordConv));
    }
    
    public void putCubeCoord(Tuple cubeCoord, T data) { 
        put(new HexData<T>(data, cubeCoord.get(0), cubeCoord.get(1), _even, _coordConv));
    }
    public void removeCubeCoord(Tuple cubeCoord) {
        remove(new HexData<T>(null, cubeCoord.get(0), cubeCoord.get(1), _even, _coordConv));
    }
    
    /****************
     * CONSTRUCTORS *
     ****************/
    public HexArray(int even, HexData.CoordinateConverter coordConv, Tuple.Generator tupleGenerator) {
        _even = even&1;
        _coordConv = coordConv;
        
        _baseMap = new HashMap<Tuple, HexData<T>>();
        _cubeMap = new HashMap<Tuple, HexData<T>>();
        
        _tupleGenerator = tupleGenerator;
        if (_tupleGenerator != null) {
            for (Tuple t: _tupleGenerator) {
                HexData<T> hexData = new HexData<T>(null, t, _even, _coordConv);
                _baseMap.put(hexData.getBaseIndex(), hexData);
                _cubeMap.put(hexData.getCubeIndex(), hexData);
            }
        }
    }
    public HexArray(int width, int height, int even, HexData.CoordinateConverter coordConv) {
        this(even, coordConv, Tuple.createArrayGenerator(width, height));
    }
    
    public HexArray(int radius, int even, HexData.CoordinateConverter coordConv) {
        this(radius, 0, 0, 0, even, coordConv);
    }
    public HexArray(int radius, int centerx, int centery, int centerz, int even, HexData.CoordinateConverter coordConv) {
        this(even, coordConv, Tuple.createRadialHexGenerator(radius, centerx, centery, centerz));
    }
    
    /** Use the stored generator to generate tuples used to iterate through the map */
    @Override
    public Iterator<HexData<T>> iterator() {
        if (_tupleGenerator == null) {
            return _baseMap.values().iterator();
        }
        
        // If the generator returns 2-tuples, use the basemap. Else use the cubemap
        final Map<Tuple, HexData<T>> workingMap = (_tupleGenerator.tupleDimension() == 2) ? _baseMap : _cubeMap;
        final Iterator<Tuple> tupleIterator = _tupleGenerator.create();
        
        return new Iterator<HexData<T>>() {
            
            @Override
            public boolean hasNext() {
                return tupleIterator.hasNext();
            }

            @Override
            public HexData<T> next() {
                return workingMap.get(tupleIterator.next());
            }};
    }
}
