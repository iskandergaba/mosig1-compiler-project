package common.visitor;

import common.asml.*;
import common.asml.Float;

public class PrintVisitor implements Visitor {
    @Override
    public void visit(Int e) {
        System.out.print(e.i);
    }

    @Override
    public void visit(Float e) {
        System.out.print("let " + e.l.label + " = " + String.format("%.2f", e.f));
    }

    @Override
    public void visit(Neg e) {
        System.out.print("(neg ");
        System.out.print(e.id);
        System.out.print(")");
    }

    @Override
    public void visit(Add e) {
        System.out.print("(add ");
        System.out.print(e.id);
        System.out.print(" ");
        e.e.accept(this);
        System.out.print(")");
    }

    @Override
    public void visit(Sub e) {
        System.out.print("(sub ");
        System.out.print(e.id);
        System.out.print(" ");
        e.e.accept(this);
        System.out.print(")");
    }

    @Override
    public void visit(FNeg e) {
        System.out.print("(fneg ");
        System.out.print(e.id);
        System.out.print(")");
    }

    @Override
    public void visit(FAdd e) {
        System.out.print("(fadd ");
        System.out.print(e.id1);
        System.out.print(" ");
        System.out.print(e.id2);
        System.out.print(")");
    }

    @Override
    public void visit(FSub e) {
        System.out.print("(fsub ");
        System.out.print(e.id1);
        System.out.print(" ");
        System.out.print(e.id2);
        System.out.print(")");
    }

    @Override
    public void visit(FMul e) {
        System.out.print("(fmul ");
        System.out.print(e.id1);
        System.out.print(" ");
        System.out.print(e.id2);
        System.out.print(")");
    }

    @Override
    public void visit(FDiv e) {
        System.out.print("(fdiv ");
        System.out.print(e.id1);
        System.out.print(" ");
        System.out.print(e.id2);
        System.out.print(")");
    }

    @Override
    public void visit(Eq e) {
        System.out.print("(");
        System.out.print(e.id);
        System.out.print(" = ");
        e.e.accept(this);
        System.out.print(")");
    }

    @Override
    public void visit(LE e) {
        System.out.print("(");
        System.out.print(e.id);
        System.out.print(" <= ");
        e.e.accept(this);
        System.out.print(")");
    }

    @Override
    public void visit(GE e) {
        System.out.print("(");
        System.out.print(e.id);
        System.out.print(" >= ");
        e.e.accept(this);
        System.out.print(")");
    }

    @Override
    public void visit(FEq e) {
        System.out.print("(");
        System.out.print(e.id1);
        System.out.print(" =. ");
        System.out.print(e.id2);
        System.out.print(")");
    }

    @Override
    public void visit(FLE e) {
        System.out.print("(");
        System.out.print(e.id1);
        System.out.print(" <= ");
        System.out.print(e.id2);
        System.out.print(")");
    }

    @Override
    public void visit(If e) {
        System.out.print("if ");
        e.cond.accept(this);
        System.out.print(" then\n");
        e.e1.accept(this);
        System.out.print(" else\n");
        e.e2.accept(this);
    }

    @Override
    public void visit(Let e) {
        System.out.print("let ");
        System.out.print(e.id);
        System.out.print(" = ");
        e.e1.accept(this);
        System.out.print(" in\n");
        e.e2.accept(this);
        System.out.print("");
    }

    @Override
    public void visit(Var e) {
        System.out.print(e.id);
    }

    @Override
    public void visit(LetRec e) {
        System.out.print("let ");
        System.out.print(e.fd.fun.l);
        for (Id id : e.fd.args) {
            System.out.print(" ");
            System.out.print(id);
        }
        System.out.print(" =\n");
        e.fd.e.accept(this);
    }

    @Override
    public void visit(Call e) {
        System.out.print("(");
        System.out.print(e.f);
        for (Id id : e.args) {
            System.out.print(" ");
            System.out.print(id);
        }
        System.out.print(")");
    }

    @Override
    public void visit(New e) {
        System.out.print("new ");
        e.size.accept(this);
    }

    @Override
    public void visit(Get e) {
        System.out.print("mem (");
        System.out.print(e.base);
        System.out.print(" + ");
        e.offset.accept(this);
        System.out.print(")");
    }

    @Override
    public void visit(Put e) {
        System.out.print("mem (");
        System.out.print(e.base);
        System.out.print(" + ");
        e.offset.accept(this);
        System.out.print(") <- ");
        System.out.print(e.dest);
    }

    @Override
    public void visit(Nop e) {
        System.out.print("nop");
    }

    @Override
    public void visit(Fun e) {
        System.out.print(e.l);
    }

    @Override
    public void visit(AppClosure e) {
        System.out.print("appclo ");
        System.out.print(e.id);
        for (Id id : e.args) {
            System.out.print(" ");
            System.out.print(id);
        }
    }

    @Override
    public void visit(FunDefs e) {
        for (Exp exp : e.funs) {
            exp.accept(this);
            System.out.println("\n");
        }
    }
}