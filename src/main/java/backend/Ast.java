package backend;

import java.util.*;

class Int extends Exp {
    final int i;

    Int(int i) {
        this.i = i;
    }

    <E> E accept(ObjVisitor<E> v) {
        return v.visit(this);
    }
    void accept(Visitor v) {
        v.visit(this);
    }
}

class Var extends Exp {
    final Id id;

    Var(Id id) {
        this.id = id;
    }

    <E> E accept(ObjVisitor<E> v) {
        return v.visit(this);
    }
    void accept(Visitor v) {
        v.visit(this);
    }
}

class Nop extends Exp {
    Nop() {

    }
    <E> E accept(ObjVisitor<E> v) {
        return v.visit(this);
    }

    void accept(Visitor v) {
        v.visit(this);
    }
}

class Fun extends Exp {
    final Label l;

    Fun(Label l) {
        this.l = l;
    }

    <E> E accept(ObjVisitor<E> v) {
        return v.visit(this);
    }

    void accept(Visitor v) {
        v.visit(this);
    }
}

class Neg extends Exp {
    final Id id;

    Neg(Id id) {
        this.id = id;
    }

    <E> E accept(ObjVisitor<E> v) {
        return v.visit(this);
    }
    void accept(Visitor v) {
        v.visit(this);
    }
}

class FNeg extends Exp {
    final Id id;

    FNeg(Id id) {
        this.id = id;
    }

    <E> E accept(ObjVisitor<E> v) {
        return v.visit(this);
    }

    void accept(Visitor v) {
        v.visit(this);
    }
}

class FAdd extends Exp {
    final Id id1;
    final Id id2;

    FAdd(Id id1, Id id2) {
        this.id1 = id1;
        this.id2 = id2;
    }

    <E> E accept(ObjVisitor<E> v) {
        return v.visit(this);
    }
    void accept(Visitor v) {
        v.visit(this);
    }
}

class FSub extends Exp {
    final Id id1;
    final Id id2;

    FSub(Id id1, Id id2) {
        this.id1 = id1;
        this.id2 = id2;
    }

    <E> E accept(ObjVisitor<E> v) {
        return v.visit(this);
    }
    void accept(Visitor v) {
        v.visit(this);
    }
}

class FMul extends Exp {
    final Id id1;
    final Id id2;

    FMul(Id id1, Id id2) {
        this.id1 = id1;
        this.id2 = id2;
    }

    <E> E accept(ObjVisitor<E> v) {
        return v.visit(this);
    }
    void accept(Visitor v) {
        v.visit(this);
    }
}

class FDiv extends Exp {
    final Id id1;
    final Id id2;

    FDiv(Id id1, Id id2) {
        this.id1 = id1;
        this.id2 = id2;
    }

    <E> E accept(ObjVisitor<E> v) {
        return v.visit(this);
    }
    void accept(Visitor v) {
        v.visit(this);
    }
}

class New extends Exp {
    final Exp size;

    New(Exp size) {
        this.size = size;
    }

    <E> E accept(ObjVisitor<E> v) {
        return v.visit(this);
    }

    void accept(Visitor v) {
        v.visit(this);
    }
}

class Add extends Exp {
    final Id id;
    final Exp e;

    Add(Id id, Exp e) {
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

class Sub extends Exp {
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

class Get extends Exp {
    final Id base;
    final Exp offset;

    Get(Id base, Exp offset) {
        this.base = base;
        this.offset = offset;
    }

    <E> E accept(ObjVisitor<E> v) {
        return v.visit(this);
    }

    void accept(Visitor v) {
        v.visit(this);
    }
}

class Put extends Exp {
    final Id base;
    final Exp offset;
    final Id dest;

    Put(Id base, Exp offset, Id dest) {
        this.base = base;
        this.offset = offset;
        this.dest = dest;
    }

    <E> E accept(ObjVisitor<E> v) {
        return v.visit(this);
    }

    void accept(Visitor v) {
        v.visit(this);
    }
}

class If extends Exp {
    final Exp cond;
    final Exp e1;
    final Exp e2;

    If(Exp cond, Exp e1, Exp e2) {
        this.cond = cond;
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

class Eq extends Exp {
    final Id id;
    final Exp e;

    Eq(Id id, Exp e) {
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

class LE extends Exp {
    final Id id;
    final Exp e;

    LE(Id id, Exp e) {
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

class GE extends Exp {
    final Id id;
    final Exp e;

    GE(Id id, Exp e) {
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

class FEq extends Exp {
    final Id id1;
    final Id id2;

    FEq(Id id1, Id id2) {
        this.id1 = id1;
        this.id2 = id2;
    }

    <E> E accept(ObjVisitor<E> v) {
        return v.visit(this);
    }
    void accept(Visitor v) {
        v.visit(this);
    }
}

class FLE extends Exp {
    final Id id1;
    final Id id2;

    FLE(Id id1, Id id2) {
        this.id1 = id1;
        this.id2 = id2;
    }

    <E> E accept(ObjVisitor<E> v) {
        return v.visit(this);
    }
    void accept(Visitor v) {
        v.visit(this);
    }
}

class Call extends Exp {
    final Label f;
    final List<Id> args;

    Call(Label f, List<Id> args) {
        this.f = f;
        this.args = args;
    }

    <E> E accept(ObjVisitor<E> v) {
        return v.visit(this);
    }
    void accept(Visitor v) {
        v.visit(this);
    }
}

class AppClosure extends Exp {
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

    <E> E accept(ObjVisitor<E> v) {
        return v.visit(this);
    }
    void accept(Visitor v) {
        v.visit(this);
    }
}

class FunDef {
    final Fun fun;
    final Type type;
    final List<Id> args;
    final Exp e;

    FunDef(Fun fun, Type t, List<Id> args, Exp e) {
        this.fun = fun;
        this.type = t;
        this.args = args;
        this.e = e;
    }

}

class LetRec extends Exp {
    final FunDef fd;
    final Exp e;

    LetRec(FunDef fd, Exp e) {
        this.fd = fd;
        this.e = e;
    }

    <E> E accept(ObjVisitor<E> v) {
        return v.visit(this);
    }
    void accept(Visitor v) {
        v.visit(this);
    }
}

class Float extends Exp {
    float f;
    Label l;

    Float(float f, Label l) {
        this.f = f;
        this.l = l;
    }

    <E> E accept(ObjVisitor<E> v) {
        return v.visit(this);
    }
    void accept(Visitor v) {
        v.visit(this);
    }
}

class FunDefs extends Exp {
    List<Exp> funs;

    FunDefs(List<Exp> funs) {
        this.funs = funs;
    }

    <E> E accept(ObjVisitor<E> v) {
        return v.visit(this);
    }

    void accept(Visitor v) {
        v.visit(this);
    }
}
