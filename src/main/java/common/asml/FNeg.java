package common.asml;

import common.visitor.*;

public class FNeg extends Exp {
    public final Id id;

    public FNeg(Id id) {
        this.id = id;
    }

    public <E> E accept(ObjVisitor<E> v) {
        return v.visit(this);
    }

    public void accept(Visitor v) {
        v.visit(this);
    }
}