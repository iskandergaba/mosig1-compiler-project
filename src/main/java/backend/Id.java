package backend;

class Id {
    String id;
    Id(String id) {
        this.id = id;
    }
    @Override
    public String toString() {
        return id;
    }
    static int x = -1;
    static Id gen() {
        x++;
        return new Id("?v" + x);
    }

}

class Label {
    String label;
    Label(String label) {
        this.label = label;
    }
    @Override
    public String toString() {
        return label;
    }
    static int x = -1;
    static Label gen() {
        x++;
        return new Label("?f" + x);
    }
}