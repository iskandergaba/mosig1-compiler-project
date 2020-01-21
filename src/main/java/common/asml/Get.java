package common.asml;

import common.visitor.*;

public class Get extends Exp {
    final Id base;
    final Exp offset;

    Get(Id base, Exp offset) {
        this.base = base;
        this.offset = offset;
    }

    <E> E accept(ObjVisitor<E> v) {
        return v.visit(this);
    }

    void accept(Visitor v) {
        v.visit(this);
    }
}