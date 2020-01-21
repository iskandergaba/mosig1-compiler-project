package common.asml;

import common.visitor.*;

public class Sub extends Exp {
    final Id id;
    final Exp e;

    Sub(Id id, Exp e) {
        this.id = id;
        this.e = e;
    }

    <E> E accept(ObjVisitor<E> v) {
        return v.visit(this);
    }
    void accept(Visitor v) {
        v.visit(this);
    }
}