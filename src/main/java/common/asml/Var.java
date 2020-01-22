package common.asml;

import common.visitor.*;

public class Var extends Exp {
    final Id id;

    public Var(Id id) {
        this.id = id;
    }

    public <E> E accept(ObjVisitor<E> v) {
        return v.visit(this);
    }
    public void accept(Visitor v) {
        v.visit(this);
    }
}