package analysis;

public class Pair {
    public int key;
    public double value;
    
    public Pair(int key, double value) {
        this.key = key;
        this.value = value;
    }
    
    public boolean isEqual(Pair other) {
        return this.key == other.key && this.value == other.value;
    }
    
    public String toString() {
        return "[" + key + ", " + value + "], ";
    }
}
