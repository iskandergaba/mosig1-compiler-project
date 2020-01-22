package common.asml;

import common.visitor.*;

public class If extends Exp {
    public final Exp cond;
    public final Exp e1;
    public final Exp e2;

    public If(Exp cond, Exp e1, Exp e2) {
        this.cond = cond;
        this.e1 = e1;
        this.e2 = e2;
    }

    public <E> E accept(ObjVisitor<E> v) {
        return v.visit(this);
    }
    public void accept(Visitor v) {
        v.visit(this);
    }
}