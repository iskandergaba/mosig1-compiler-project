package common.asml;

import common.visitor.*;

public class Self extends Exp {
    public Self() {

    }

    public <E> E accept(ObjVisitor<E> v) {
        return v.visit(this);
    }

    public void accept(Visitor v) {
        v.visit(this);
    }
}