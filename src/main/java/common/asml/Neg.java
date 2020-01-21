package common.asml;

import common.visitor.*;

public class Neg extends Exp {
    final Id id;

    Neg(Id id) {
        this.id = id;
    }

    <E> E accept(ObjVisitor<E> v) {
        return v.visit(this);
    }
    void accept(Visitor v) {
        v.visit(this);
    }
}