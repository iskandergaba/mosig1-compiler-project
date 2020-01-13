package backend;

class RegAllocVisitor extends PrintVisitor {
    public Memory memory = new Memory();

    @Override
    public void visit(Var e) {
        System.out.print(memory.localVariable(e.id));
    }

    @Override
    public void visit(Let e) {
        System.out.print("let ");
        System.out.print(memory.localVariable(e.id));
        System.out.print(" = ");
        e.e1.accept(this);
        System.out.print(" in\n");
        e.e2.accept(this);
        System.out.print("");
    }

    @Override
    public void visit(Add e) {
        System.out.print("(add ");
        System.out.print(memory.localVariable(e.id));
        System.out.print(" ");
        e.e.accept(this);
        System.out.print(")");
    }

    @Override
    public void visit(Put e) {
        System.out.print("mem (");
        System.out.print(memory.localVariable(e.base));
        System.out.print(" + ");
        e.offset.accept(this);
        System.out.print(") <- ");
        System.out.print(memory.localVariable(e.dest));
    }

    @Override
    public void visit(LetRec e) {
        System.out.print("let ");
        System.out.print(e.fd.fun.l);
        for (Id id: e.fd.args) {
            System.out.print(" ");
            System.out.print(memory.localVariable(id));
        }
        System.out.print(" =\n");
        e.e.accept(this);
        System.out.print("");
    }
}