package common.asml;

import common.visitor.*;

public class FDiv extends Exp {
    public final Id id1;
    public final Id id2;

    public FDiv(Id id1, Id id2) {
        this.id1 = id1;
        this.id2 = id2;
    }

    public <E> E accept(ObjVisitor<E> v) {
        return v.visit(this);
    }
    public void accept(Visitor v) {
        v.visit(this);
    }
}