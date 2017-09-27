package simple.misc.hex;

import java.util.Iterator;

public class HexSet<T> extends HexArray<T> {
    public void put(HexData<T> hexData) { 
        _baseMap.put(hexData.getBaseIndex(), hexData);
        _cubeMap.put(hexData.getCubeIndex(), hexData);
    }
    
    public void remove(HexData<T> hexData) {
        _baseMap.remove(hexData.getBaseIndex());
        _cubeMap.remove(hexData.getCubeIndex());
    }
    
    public HexSet(int even) {
        super(even);
    }
    
    @Override
    public Iterator<HexData<T>> iterator() {
        return (Iterator<HexData<T>>)_baseMap.values().iterator();
    }
}
