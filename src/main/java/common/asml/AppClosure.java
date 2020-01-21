package common.asml;

import java.util.List;

import common.visitor.*;

public class AppClosure extends Exp {
    final Id id;
    final List<Id> args;

    AppClosure(Id id, List<Id> args) {
        this.id = id;
        this.args = args;
    }

    <E> E accept(ObjVisitor<E> v) {
        return v.visit(this);
    }
    void accept(Visitor v) {
        v.visit(this);
    }
}