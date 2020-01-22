package common.asml;

public class Label {
    String label;
    public Label(String label) {
        this.label = label;
    }
    @Override
    public String toString() {
        return label;
    }
    static int x = -1;
    public static Label gen() {
        x++;
        return new Label("?f" + x);
    }
}