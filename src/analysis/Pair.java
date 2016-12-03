package analysis;

import java.io.Serializable;

public class Pair implements Serializable {

    private static final long serialVersionUID = 1L;
    public int key;
    public double value;
    
    public Pair(int key, double value) {
        this.key = key;
        this.value = value;
    }
    
    public String toString() {
        return "[" + key + ", " + value + "]";
    }
}
