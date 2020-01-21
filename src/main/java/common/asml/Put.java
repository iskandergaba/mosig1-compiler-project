package common.asml;

import common.visitor.*;

public class Put extends Exp {
    final Id base;
    final Exp offset;
    final Id dest;

    Put(Id base, Exp offset, Id dest) {
        this.base = base;
        this.offset = offset;
        this.dest = dest;
    }

    <E> E accept(ObjVisitor<E> v) {
        return v.visit(this);
    }

    void accept(Visitor v) {
        v.visit(this);
    }
}