package common.asml;

import common.visitor.*;

public class Get extends Exp {
    public final Id base;
    public final Exp offset;

    public Get(Id base, Exp offset) {
        this.base = base;
        this.offset = offset;
    }

    public <E> E accept(ObjVisitor<E> v) {
        return v.visit(this);
    }

    public void accept(Visitor v) {
        v.visit(this);
    }
}