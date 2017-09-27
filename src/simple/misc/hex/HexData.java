package simple.misc.hex;

import simple.misc.hex.HexArray.CoordinateConverter;

public class HexData<T> {
    private T _data;
    private Tuple _baseIndex, _cubeIndex;

    public Tuple getBaseIndex()  { return _baseIndex; }
    public Tuple getCubeIndex()  { return _cubeIndex; }
    public T     getData()       { return _data; }
    
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
}

