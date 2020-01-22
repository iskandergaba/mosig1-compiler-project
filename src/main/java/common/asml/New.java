package common.asml;

import common.visitor.*;

public class New extends Exp {
    final Exp size;

    public New(Exp size) {
        this.size = size;
    }

    public <E> E accept(ObjVisitor<E> v) {
        return v.visit(this);
    }

    public void accept(Visitor v) {
        v.visit(this);
    }
}