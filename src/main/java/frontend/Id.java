package frontend;

class Id {
    String id, old;
    Boolean retClosureFlag = false;
    Boolean isClosureFlag = false;

    Id(String id) {
        this.id = id;
        old = null;
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
