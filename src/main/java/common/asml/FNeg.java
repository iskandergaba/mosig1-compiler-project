package common.asml;

import common.visitor.*;

public class FNeg extends Exp {
    final Id id;

    FNeg(Id id) {
        this.id = id;
    }

    <E> E accept(ObjVisitor<E> v) {
        return v.visit(this);
    }

    void accept(Visitor v) {
        v.visit(this);
    }
}