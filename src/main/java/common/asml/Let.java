package common.asml;

import common.type.Type;
import common.visitor.*;

public class Let extends Exp {
    public final Id id;
    final Type t;
    public final Exp e1;
    public final Exp e2;

    public Let(Id id, Type t, Exp e1, Exp e2) {
        this.id = id;
        this.t = t;
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