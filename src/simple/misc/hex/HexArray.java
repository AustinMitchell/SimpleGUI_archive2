package simple.misc.hex;

import java.util.*;

public class HexArray<T> implements Iterable<HexData<T>> { 
    public static abstract class CoordinateConverter {
        public abstract float[][] getAxisVectors();
        
        public abstract Tuple baseToCubeIndex(int even, int... index);
        public abstract Tuple cubeToBaseIndex(int even, int... index);
        
        public final Tuple baseToCubeIndex(int even, Tuple pair)   { return baseToCubeIndex(even, pair.get()); }
        public final Tuple cubeToBaseIndex(int even, Tuple triple) { return cubeToBaseIndex(even, triple.get()); }
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
    
    protected Map<Tuple, HexData<T>> _baseMap, _cubeMap;
    protected int _even, _w, _h;
    protected CoordinateConverter _coordConv;
    
    public HexData<T> getBase(int x, int y)           { return getBase(new Tuple(x, y));       }
    public HexData<T> getCube(int hx, int hy, int hz) { return getBase(new Tuple(hx, hy, hz)); }
    public HexData<T> getBase(Tuple pair)   { return _baseMap.get(pair);   }
    public HexData<T> getCube(Tuple triple) { return _cubeMap.get(triple); }
    public CoordinateConverter getCoordConv() { return _coordConv; }
    
    public void setBase(T data, int x, int y)           { getBase(new Tuple(x, y)).setData(data);  }
    public void setCube(T data, int hx, int hy, int hz) { getBase(new Tuple(hx, hy, hz)).setData(data); }
    public void setBase(T data, Tuple pair)   { _baseMap.get(pair).setData(data);   }
    public void setCube(T data, Tuple triple) { _cubeMap.get(triple).setData(data); }
    
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
    
    public HexArray(int width, int height, int even) {
        this(width, height, even, POINT_TOP_COORD); 
    }
    public HexArray(int width, int height, int even, CoordinateConverter coordConv) {
        _even = even&1;
        _coordConv = coordConv;
        
        _baseMap = new HashMap<Tuple, HexData<T>>();
        _cubeMap = new HashMap<Tuple, HexData<T>>();
        
        _w = width;
        _h = height;
        
        for (int i=0; i<_w; i++) {
            for (int j=0; j<_h; j++) {
                HexData<T> hexData = new HexData<T>(null, i, j, _even, _coordConv);
                _baseMap.put(hexData.getBaseIndex(), hexData);
                _cubeMap.put(hexData.getCubeIndex(), hexData);
            }
        }  
    }
    
    protected HexArray(int even) {
        this(0, 0, even);
    }
    
    @Override
    public Iterator<HexData<T>> iterator() {
        return new Iterator<HexData<T>>(){
            Tuple current = null;
            int i=0;
            int j=0;
        
            @Override
            public boolean hasNext() {
                current = new Tuple(i, j);
                return i<_w && j<_h;
            }
            @Override
            public HexData<T> next() {
                j += 1;
                if (j==_h) {
                    i += 1;
                    j  = 0;
                }
                return _baseMap.get(current);
            }
        };
    }
}
