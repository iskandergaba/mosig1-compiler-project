package common.asml;

import java.util.*;

import common.visitor.*;

public class FunDefs extends Exp {
    public List<Exp> funs;

    FunDefs(List<Exp> funs) {
        this.funs = funs;
    }

    <E> E accept(ObjVisitor<E> v) {
        return v.visit(this);
    }

    public void accept(Visitor v) {
        v.visit(this);
    }
}
