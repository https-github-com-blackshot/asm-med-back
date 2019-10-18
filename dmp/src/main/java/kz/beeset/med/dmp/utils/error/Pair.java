package kz.beeset.med.dmp.utils.error;

public class Pair {
    private String first;
    private Object second;

    public String getFirst() {
        return first;
    }

    public Object getSecond() {
        return second;
    }

    public Pair(String first, Object second) {
        this.first = first;
        this.second = second;
    }
}
