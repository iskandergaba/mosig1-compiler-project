package frontend;

import java.util.*;

import common.type.Type;

abstract class Exp {

    Boolean retClosureFlag = false;
    Boolean isClosureFlag = false;

    abstract void accept(Visitor v);

    abstract <E> E accept(ObjVisitor<E> v) throws Exception;
}

class Unit extends Exp {
    <E> E accept(ObjVisitor<E> v) throws Exception {
        return v.visit(this);
    }

    void accept(Visitor v) {
        v.visit(this);
    }
}

class Bool extends Exp {
    final boolean b;

    Bool(boolean b) {
        this.b = b;
    }

    <E> E accept(ObjVisitor<E> v) throws Exception {
        return v.visit(this);
    }

    void accept(Visitor v) {
        v.visit(this);
    }
}

class Int extends Exp {
    final int i;

    Int(int i) {
        this.i = i;
    }

    <E> E accept(ObjVisitor<E> v) throws Exception {
        return v.visit(this);
    }

    void accept(Visitor v) {
        v.visit(this);
    }
}

class Float extends Exp {
    float f;

    Float(float f) {
        this.f = f;
    }

    <E> E accept(ObjVisitor<E> v) throws Exception {
        return v.visit(this);
    }

    void accept(Visitor v) {
        v.visit(this);
    }
}

class Not extends Exp {
    final Exp e;

    Not(Exp e) {
        this.e = e;
    }

    <E> E accept(ObjVisitor<E> v) throws Exception {
        return v.visit(this);
    }

    void accept(Visitor v) {
        v.visit(this);
    }
}

class Neg extends Exp {
    final Exp e;

    Neg(Exp e) {
        this.e = e;
    }

    <E> E accept(ObjVisitor<E> v) throws Exception {
        return v.visit(this);
    }

    void accept(Visitor v) {
        v.visit(this);
    }
}

class Add extends Exp {
    final Exp e1;
    final Exp e2;

    Add(Exp e1, Exp e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    <E> E accept(ObjVisitor<E> v) throws Exception {
        return v.visit(this);
    }

    void accept(Visitor v) {
        v.visit(this);
    }
}

class Sub extends Exp {
    final Exp e1;
    final Exp e2;

    Sub(Exp e1, Exp e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    <E> E accept(ObjVisitor<E> v) throws Exception {
        return v.visit(this);
    }

    void accept(Visitor v) {
        v.visit(this);
    }
}

class FNeg extends Exp {
    final Exp e;

    FNeg(Exp e) {
        this.e = e;
    }

    <E> E accept(ObjVisitor<E> v) throws Exception {
        return v.visit(this);
    }

    void accept(Visitor v) {
        v.visit(this);
    }
}

class FAdd extends Exp {
    final Exp e1;
    final Exp e2;

    FAdd(Exp e1, Exp e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    <E> E accept(ObjVisitor<E> v) throws Exception {
        return v.visit(this);
    }

    void accept(Visitor v) {
        v.visit(this);
    }
}

class FSub extends Exp {
    final Exp e1;
    final Exp e2;

    FSub(Exp e1, Exp e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    <E> E accept(ObjVisitor<E> v) throws Exception {
        return v.visit(this);
    }

    void accept(Visitor v) {
        v.visit(this);
    }
}

class FMul extends Exp {
    final Exp e1;
    final Exp e2;

    FMul(Exp e1, Exp e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    <E> E accept(ObjVisitor<E> v) throws Exception {
        return v.visit(this);
    }

    void accept(Visitor v) {
        v.visit(this);
    }
}

class FDiv extends Exp {
    final Exp e1;
    final Exp e2;

    FDiv(Exp e1, Exp e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    <E> E accept(ObjVisitor<E> v) throws Exception {
        return v.visit(this);
    }

    void accept(Visitor v) {
        v.visit(this);
    }
}

class Eq extends Exp {
    final Exp e1;
    final Exp e2;
    Type t;

    Eq(Exp e1, Exp e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    <E> E accept(ObjVisitor<E> v) throws Exception {
        return v.visit(this);
    }

    void accept(Visitor v) {
        v.visit(this);
    }
}

class LE extends Exp {
    final Exp e1;
    final Exp e2;
    Type t;

    LE(Exp e1, Exp e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    <E> E accept(ObjVisitor<E> v) throws Exception {
        return v.visit(this);
    }

    void accept(Visitor v) {
        v.visit(this);
    }
}

class If extends Exp {
    final Exp e1;
    final Exp e2;
    final Exp e3;

    If(Exp e1, Exp e2, Exp e3) {
        this.e1 = e1;
        this.e2 = e2;
        this.e3 = e3;
    }

    <E> E accept(ObjVisitor<E> v) throws Exception {
        return v.visit(this);
    }

    void accept(Visitor v) {
        v.visit(this);
    }
}

class Let extends Exp {
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

    <E> E accept(ObjVisitor<E> v) throws Exception {
        return v.visit(this);
    }

    void accept(Visitor v) {
        v.visit(this);
    }
}

class Var extends Exp {
    private static int count = 0;
    final Id id;

    Var(Id id) {
        this.id = id;
    }

    static Var gen() {
        return new Var(new Id("$" + count++));
    }

    <E> E accept(ObjVisitor<E> v) throws Exception {
        return v.visit(this);
    }

    void accept(Visitor v) {
        v.visit(this);
    }
}

class LetRec extends Exp {
    final FunDef fd;
    final Exp e;

    LetRec(FunDef fd, Exp e) {
        this.fd = fd;
        this.e = e;
    }

    <E> E accept(ObjVisitor<E> v) throws Exception {
        return v.visit(this);
    }

    void accept(Visitor v) {
        v.visit(this);
    }
}

class App extends Exp {
    final Exp e;
    final List<Exp> es;

    boolean closureFlag = false;

    App(Exp e, List<Exp> es) {
        this.e = e;
        this.es = es;
    }

    <E> E accept(ObjVisitor<E> v) throws Exception {
        return v.visit(this);
    }

    void accept(Visitor v) {
        v.visit(this);
    }
}

class Tuple extends Exp {
    final List<Exp> es;

    Tuple(List<Exp> es) {
        this.es = es;
    }

    <E> E accept(ObjVisitor<E> v) throws Exception {
        return v.visit(this);
    }

    void accept(Visitor v) {
        v.visit(this);
    }
}

class LetTuple extends Exp {
    final List<Id> ids;
    final List<Type> ts;
    final Exp e1;
    final Exp e2;

    LetTuple(List<Id> ids, List<Type> ts, Exp e1, Exp e2) {
        this.ids = ids;
        this.ts = ts;
        this.e1 = e1;
        this.e2 = e2;
    }

    <E> E accept(ObjVisitor<E> v) throws Exception {
        return v.visit(this);
    }

    void accept(Visitor v) {
        v.visit(this);
    }
}

class Array extends Exp {
    final Exp e1;
    final Exp e2;

    Array(Exp e1, Exp e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    <E> E accept(ObjVisitor<E> v) throws Exception {
        return v.visit(this);
    }

    void accept(Visitor v) {
        v.visit(this);
    }
}

class Get extends Exp {
    final Exp e1;
    final Exp e2;

    Get(Exp e1, Exp e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    <E> E accept(ObjVisitor<E> v) throws Exception {
        return v.visit(this);
    }

    void accept(Visitor v) {
        v.visit(this);
    }
}

class Put extends Exp {
    final Exp e1;
    final Exp e2;
    final Exp e3;

    Put(Exp e1, Exp e2, Exp e3) {
        this.e1 = e1;
        this.e2 = e2;
        this.e3 = e3;
    }

    <E> E accept(ObjVisitor<E> v) throws Exception {
        return v.visit(this);
    }

    void accept(Visitor v) {
        v.visit(this);
    }
}

class FunDef {
    final Id id;
    final Type type;
    final List<Id> args;
    final Exp e;

    List<Id> free = null;

    FunDef(Id id, Type t, List<Id> args, Exp e) {
        this.id = id;
        this.type = t;
        this.args = args;
        this.e = e;
    }

}
