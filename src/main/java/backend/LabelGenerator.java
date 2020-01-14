package backend;

public class LabelGenerator {
    private int counter;

    public LabelGenerator() {
        this.counter = 0;
    }

    public String getLabel() {
        return "label" + (++counter);
    } 
}