package common.asml;

import common.visitor.*;

public class Add extends Exp {
    final Id id;
    final Exp e;

    public Add(Id id, Exp e) {
        this.id = id;
        this.e = e;
    }

    public <E> E accept(ObjVisitor<E> v) {
        return v.visit(this);
    }
    public void accept(Visitor v) {
        v.visit(this);
    }
}