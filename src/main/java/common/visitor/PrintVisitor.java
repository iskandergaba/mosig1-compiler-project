package common.visitor;

import java.io.PrintStream;

import common.asml.*;
import common.asml.Float;

public class PrintVisitor implements Visitor {
    PrintStream stream;

    public PrintVisitor() {
        this.stream = System.out;
    }

    public PrintVisitor(PrintStream s) {
        this.stream = s;
    }

    @Override
    public void visit(Int e) {
        stream.print(e.i);
    }

    @Override
    public void visit(Float e) {
        stream.print("let " + e.l.label + " = " + String.format("%.2f", e.f));
    }

    @Override
    public void visit(Neg e) {
        stream.print("(neg ");
        stream.print(e.id);
        stream.print(")");
    }

    @Override
    public void visit(Add e) {
        stream.print("(add ");
        stream.print(e.id);
        stream.print(" ");
        e.e.accept(this);
        stream.print(")");
    }

    @Override
    public void visit(Sub e) {
        stream.print("(sub ");
        stream.print(e.id);
        stream.print(" ");
        e.e.accept(this);
        stream.print(")");
    }

    @Override
    public void visit(FNeg e) {
        stream.print("(fneg ");
        stream.print(e.id);
        stream.print(")");
    }

    @Override
    public void visit(FAdd e) {
        stream.print("(fadd ");
        stream.print(e.id1);
        stream.print(" ");
        stream.print(e.id2);
        stream.print(")");
    }

    @Override
    public void visit(FSub e) {
        stream.print("(fsub ");
        stream.print(e.id1);
        stream.print(" ");
        stream.print(e.id2);
        stream.print(")");
    }

    @Override
    public void visit(FMul e) {
        stream.print("(fmul ");
        stream.print(e.id1);
        stream.print(" ");
        stream.print(e.id2);
        stream.print(")");
    }

    @Override
    public void visit(FDiv e) {
        stream.print("(fdiv ");
        stream.print(e.id1);
        stream.print(" ");
        stream.print(e.id2);
        stream.print(")");
    }

    @Override
    public void visit(Eq e) {
        stream.print("(");
        stream.print(e.id);
        stream.print(" = ");
        e.e.accept(this);
        stream.print(")");
    }

    @Override
    public void visit(LE e) {
        stream.print("(");
        stream.print(e.id);
        stream.print(" <= ");
        e.e.accept(this);
        stream.print(")");
    }

    @Override
    public void visit(GE e) {
        stream.print("(");
        stream.print(e.id);
        stream.print(" >= ");
        e.e.accept(this);
        stream.print(")");
    }

    @Override
    public void visit(FEq e) {
        stream.print("(");
        stream.print(e.id1);
        stream.print(" =. ");
        stream.print(e.id2);
        stream.print(")");
    }

    @Override
    public void visit(FLE e) {
        stream.print("(");
        stream.print(e.id1);
        stream.print(" <= ");
        stream.print(e.id2);
        stream.print(")");
    }

    @Override
    public void visit(If e) {
        stream.print("if ");
        e.cond.accept(this);
        stream.print(" then\n");
        e.e1.accept(this);
        stream.print(" else\n");
        e.e2.accept(this);
    }

    @Override
    public void visit(Let e) {
        stream.print("let ");
        stream.print(e.id);
        stream.print(" = ");
        e.e1.accept(this);
        stream.print(" in\n");
        e.e2.accept(this);
        stream.print("");
    }

    @Override
    public void visit(Var e) {
        stream.print(e.id);
    }

    @Override
    public void visit(LetRec e) {
        stream.print("let ");
        stream.print(e.fd.fun.l);
        for (Id id : e.fd.args) {
            stream.print(" ");
            stream.print(id);
        }
        stream.print(" =\n");
        e.fd.e.accept(this);
    }

    @Override
    public void visit(Call e) {
        stream.print("(");
        stream.print(e.f);
        for (Id id : e.args) {
            stream.print(" ");
            stream.print(id);
        }
        stream.print(")");
    }

    @Override
    public void visit(New e) {
        stream.print("new ");
        e.size.accept(this);
    }

    @Override
    public void visit(Get e) {
        stream.print("mem (");
        e.base.accept(this);
        stream.print(" + ");
        e.offset.accept(this);
        stream.print(")");
    }

    @Override
    public void visit(Put e) {
        stream.print("mem (");
        e.base.accept(this);
        stream.print(" + ");
        e.offset.accept(this);
        stream.print(") <- ");
        stream.print(e.dest);
    }

    @Override
    public void visit(Nop e) {
        stream.print("nop");
    }

    @Override
    public void visit(Fun e) {
        stream.print(e.l);
    }

    @Override
    public void visit(AppClosure e) {
        stream.print("appclo ");
        stream.print(e.id);
        for (Id id : e.args) {
            stream.print(" ");
            stream.print(id);
        }
    }

    @Override
    public void visit(FunDefs e) {
        for (Exp exp : e.funs) {
            exp.accept(this);
            stream.println("\n");
        }
    }

    @Override
    public void visit(Self e) {
        stream.print("%self");
    }
 }