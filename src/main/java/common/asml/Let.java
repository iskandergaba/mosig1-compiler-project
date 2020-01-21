package common.asml;

import common.type.Type;
import common.visitor.*;

public class Let extends Exp {
    final Id id;
    final Type t;
    final Exp e1;
    final Exp e2;

    Let(Id id, Type t, Exp e1, Exp e2) {
        this.id = id;
        this.t = t;
        this.e1 = e1;
        this.e2 = e2;
    }

    <E> E accept(ObjVisitor<E> v) {
        return v.visit(this);
    }
    void accept(Visitor v) {
        v.visit(this);
    }
}