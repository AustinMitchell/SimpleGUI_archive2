package simple.misc.hex;

import java.util.Arrays;

public class Tuple {
    private int[] _entry;
    public Tuple(int... entry) {
        _entry = entry;
    }
    public int   get(int i) { return _entry[i]; }
    public int[] get()      { return _entry;    }
    
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Tuple)) {
           return false;
        }
        Tuple otherTuple = (Tuple)other;
        if (this._entry.length != otherTuple._entry.length) {
            return false;
        }
        for (int i=0; i<this._entry.length; i++) {
            if (this._entry[i] != otherTuple._entry[i]) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public int hashCode(){
        return Arrays.hashCode(_entry);
    }
    
    @Override
    public String toString() {
        if (_entry.length == 0) return "";
        
        String str = "(";
        for (int e: _entry) {
            str += e+", ";
        }
        return str.substring(0, str.length()-2) + ")";
        
    }
}
