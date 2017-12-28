package simple.misc.hex;

import java.util.*;

public class HexArray<T> implements Iterable<HexData<T>> {     
                
    /** Coordinate differences between different hexes */
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
    protected Map<Tuple, HexData<T>> _cubeMap;
    protected Tuple.Generator _tupleGenerator;
    protected HexData.Generator<T> _dataGenerator;
    
    public HexData<T> index(int hx, int hy, int hz) { return index(new Tuple(hx, hy, hz)); }
    public HexData<T> index(Tuple triple) { return _cubeMap.get(triple); }
    public Tuple.Generator tupleGenerator() { return _tupleGenerator; }
    public HexData.Generator<T> dataGenerator() { return _dataGenerator; }
    
    public void setIndex(T data, int hx, int hy, int hz) { index(new Tuple(hx, hy, hz)).setData(data); }
    public void setIndex(T data, Tuple triple) { _cubeMap.get(triple).setData(data); }
    public void setTupleGenerator(Tuple.Generator newGenerator) { _tupleGenerator = newGenerator; }
    public void setDataGenerator(HexData.Generator<T> newGenerator) { _dataGenerator = newGenerator; }
    
    /* Adds a new item into the set*/
    public void put(HexData<T> hexData) { 
        _cubeMap.put(hexData.cubeIndex(), hexData);
    }
    public void remove(HexData<T> hexData) {
        _cubeMap.remove(hexData.cubeIndex());
    }
    
    public void put(Tuple cubeCoord, T data) { 
        put(new HexData<T>(data, cubeCoord));
    }
    public void remove(Tuple cubeCoord) {
        remove(new HexData<T>(null, cubeCoord));
    }
    
    public void putGenerated(Tuple cubeCoord) { 
        put(new HexData<T>(cubeCoord, _dataGenerator));
    }
    
    /****************
     * CONSTRUCTORS *
     ****************/
    public HexArray(Tuple.Generator tupleGenerator, HexData.Generator<T> dataGenerator) {
        _cubeMap = new HashMap<Tuple, HexData<T>>();
        
        _tupleGenerator = tupleGenerator;
        _dataGenerator = dataGenerator;
        
        if (_tupleGenerator != null) {
            for (Tuple t: _tupleGenerator) {
                HexData<T> hexData = new HexData<T>(t, _dataGenerator);
                _cubeMap.put(hexData.cubeIndex(), hexData);
            }
        }
    }
    public HexArray() {
        this(null, null);
    }
    public HexArray(Tuple.Generator tupleGenerator) {
        this(tupleGenerator, null);
    }
    public HexArray(HexData.Generator<T> dataGenerator) {
        this(null, dataGenerator);
    }
    
    /** Use the stored generator to generate tuples used to iterate through the map */
    @Override
    public Iterator<HexData<T>> iterator() {
        if (_tupleGenerator == null) {
            return _cubeMap.values().iterator();
        }
        
        // If the generator returns 2-tuples, use the basemap. Else use the cubemap
        final Map<Tuple, HexData<T>> workingMap = _cubeMap;
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
