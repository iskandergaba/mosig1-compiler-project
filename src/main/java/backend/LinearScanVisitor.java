package backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.asml.*;
import common.asml.Float;
import common.visitor.*;

class LinearScanVisitor implements Visitor {
    Integer position = 0;
    String scope;
    Map<String, List<LiveInterval>> basicBlocksMap = new HashMap<String, List<LiveInterval>>();
    Map<String, LiveInterval> intervalsMap;
    List<LiveInterval> liveIntervals, active;

    final Integer activeMax = 2, registersNum = 16, fp = 11, regStart = 4, regEnd = 12;
    Boolean[] regIsFree = new Boolean[registersNum];
    Map<String, Integer> registers = new HashMap<String, Integer>();
    Map<String, Integer> locations = new HashMap<String, Integer>();
    Map<String, String> memory = new HashMap<String, String>();

    void updatePosition() {
        this.position++;
    }

    void startBlock(String fname) {
        this.intervalsMap = new HashMap<String, LiveInterval>();
        this.liveIntervals = new ArrayList<LiveInterval>();
        this.basicBlocksMap.put(fname, liveIntervals);
        this.scope = fname;
    }

    void endBlock() {
        this.intervalsMap = null;
        this.liveIntervals = null;
        this.scope = null;
    }

    void startInterval(String vname) {
        LiveInterval liveInterval = new LiveInterval(fullPath(vname), position);
        this.liveIntervals.add(liveInterval);
        this.intervalsMap.put(fullPath(vname), liveInterval);
    }

    void updateInterval(String vname) {
        if (locations.get(fullPath(vname)) == null)
            this.intervalsMap.get(fullPath(vname)).update(this.position);
    }

    void addParams(List<Id> params) {
        int i = 0;
        for (Id id: params) {
            if (i >= this.regStart) {
                this.locations.put(fullPath(id.id), -1);
            } else {
                this.locations.put(fullPath(id.id), (i + 10) * 4);
            }
            i++;
        }
    }

    String fullPath(String vname) {
        return this.scope + "." + vname;
    }

    void printIntervals() {
        System.out.println("# Live intervals");
        for (Map.Entry<String, List<LiveInterval>> entry : basicBlocksMap.entrySet()) {
            String label = entry.getKey();
            List<LiveInterval> block = entry.getValue();
            System.out.println("GROUP '" + label + "': ");
            for (LiveInterval liveInterval : block) {
                System.out.println("  " + liveInterval.name +
                " (" + liveInterval.startpoint + "," + liveInterval.endpoint + ") " +
                liveInterval.length);
            }
        }
        System.out.println("");
    }

    Integer getFreeRegister() {
        for (int i = this.regStart; i <= this.regEnd; i++) {
            if (i == fp)
                continue;
            if (this.regIsFree[i]) {
                this.regIsFree[i] = false;
                return i;
            }
        }
        return -1;
    }

    void freeRegisters() {
        for (int i = this.regStart; i <= this.regEnd; i++) {
            this.regIsFree[i] = true;
        }
    }

    void freeRegister(Integer i) {
        this.regIsFree[i] = true;
    }

    void linearScanRegisterAllocation() {
        System.out.println("# Live intervals traversal");

        for (Map.Entry<String, List<LiveInterval>> entry : basicBlocksMap.entrySet()) {
            this.active = new ArrayList<LiveInterval>();
            freeRegisters();
            Integer offset = 0;
            this.scope = entry.getKey();
            List<LiveInterval> liveIntervals = entry.getValue();
            for (LiveInterval i : liveIntervals) {
                expireOldInterval(i);
                if (this.active.size() == activeMax) {
                    // use location
                    offset -= 4;
                    spillInterval(i, offset);
                } else {
                    // use register
                    registers.put(i.name, getFreeRegister());
                    System.out.println("r" + registers.get(i.name) + " <- " + i.name);
                    this.active.add(i);
                    this.active.sort((i1, i2) -> i1.endpoint.compareTo(i2.endpoint));
                }
            }
        }
        for (Map.Entry<String, Integer> entry : this.registers.entrySet()) {
            this.memory.put(entry.getKey(), "r" + entry.getValue());
        }
        for (Map.Entry<String, Integer> entry : this.locations.entrySet()) {
            this.memory.put(entry.getKey(), "[r" + fp + ", " + entry.getValue() + "]");
        }

        System.out.println("\n# Register allocation memory map");
        for (Map.Entry<String, String> entry : this.memory.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        System.out.println("");
    }

    void expireOldInterval(LiveInterval i) {
        List<LiveInterval> expired = new ArrayList<LiveInterval>();
        for (LiveInterval j : this.active) {
            if (j.endpoint >= i.startpoint)
                break;
            // expire
            System.out.println("r" + registers.get(j.name) + " -> X");
            expired.add(j);
            // free register
            freeRegister(registers.get(j.name));
        }
        this.active.removeAll(expired);
    }

    void spillInterval(LiveInterval i, Integer offset) {
        if (this.active.size() == 0) {
            // location
            System.out.println("[r" + fp + ", " + offset + "]" + " <- " + i.name);
            locations.put(i.name, offset);
            return;
        }
        int last = this.active.size() - 1;
        LiveInterval spill = this.active.get(last);
        if (spill.endpoint > i.endpoint) {
            // register
            registers.put(i.name, registers.get(spill.name));
            System.out.println("r" + registers.get(spill.name) + " -> ");
            registers.remove(spill.name); // just for convenience
            System.out.println("r" + registers.get(i.name) + " <- " + i.name);
            // location
            locations.put(spill.name, offset);
            System.out.println("[r" + fp + ", " + locations.get(spill.name) + "]" + " <- " + spill.name);
            this.active.remove(last);
            this.active.add(i);
            this.active.sort((i1, i2) -> i1.endpoint.compareTo(i2.endpoint));
        } else {
            // location
            System.out.println("[r" + fp + ", " + offset + "]" + " <- " + i.name);
            locations.put(i.name, offset);
        }
    }



    @Override
    public void visit(Int e) {
        System.out.print(e.i);
    }

    @Override
    public void visit(Var e) {
        // currentBlock.addGen(e.id);
        updateInterval(e.id.id);

        System.out.print(e.id);
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
    public void visit(Neg e) {
        // currentBlock.addGen(e.id);
        updatePosition();
        updateInterval(e.id.id);

        System.out.print("(neg ");
        System.out.print(e.id);
        System.out.print(")");
    }

    @Override
    public void visit(FNeg e) {
        // currentBlock.addGen(e.id);
        updatePosition();
        updateInterval(e.id.id);

        System.out.print("(fneg ");
        System.out.print(e.id);
        System.out.print(")");
    }

    @Override
    public void visit(FAdd e) {
        // currentBlock.addGen(e.id1);
        // currentBlock.addGen(e.id2);
        updatePosition();
        updateInterval(e.id1.id);
        updateInterval(e.id2.id);

        System.out.print("(fadd ");
        System.out.print(e.id1);
        System.out.print(" ");
        System.out.print(e.id2);
        System.out.print(")");
    }

    @Override
    public void visit(FSub e) {
        // currentBlock.addGen(e.id1);
        // currentBlock.addGen(e.id2);
        updatePosition();
        updateInterval(e.id1.id);
        updateInterval(e.id2.id);

        System.out.print("(fsub ");
        System.out.print(e.id1);
        System.out.print(" ");
        System.out.print(e.id2);
        System.out.print(")");
    }

    @Override
    public void visit(FMul e) {
        // currentBlock.addGen(e.id1);
        // currentBlock.addGen(e.id2);
        updatePosition();
        updateInterval(e.id1.id);
        updateInterval(e.id2.id);

        System.out.print("(fmul ");
        System.out.print(e.id1);
        System.out.print(" ");
        System.out.print(e.id2);
        System.out.print(")");
    }

    @Override
    public void visit(FDiv e) {
        // currentBlock.addGen(e.id1);
        // currentBlock.addGen(e.id2);
        updatePosition();
        updateInterval(e.id1.id);
        updateInterval(e.id2.id);

        System.out.print("(fdiv ");
        System.out.print(e.id1);
        System.out.print(" ");
        System.out.print(e.id2);
        System.out.print(")");
    }

    @Override
    public void visit(New e) {
        System.out.print("new ");
        e.size.accept(this);
    }

    @Override
    public void visit(Add e) {
        // currentBlock.addGen(e.id);
        updatePosition();
        updateInterval(e.id.id);

        System.out.print("(add ");
        System.out.print(e.id);
        System.out.print(" ");
        e.e.accept(this);
        System.out.print(")");
    }

    @Override
    public void visit(Sub e) {
        // currentBlock.addGen(e.id);
        updatePosition();
        updateInterval(e.id.id);

        System.out.print("(sub ");
        System.out.print(e.id);
        System.out.print(" ");
        e.e.accept(this);
        System.out.print(")");
    }

    @Override
    public void visit(Get e) {
        // currentBlock.addGen(e.base);
        updatePosition();


        System.out.print("mem (");
        e.base.accept(this);
        System.out.print(" + ");
        e.offset.accept(this);
        System.out.print(")");
    }

    @Override
    public void visit(Put e) {
        // currentBlock.addGen(e.base);
        // currentBlock.addGen(e.dest);
        updatePosition();

        System.out.print("mem (");

        e.base.accept(this);

        updateInterval(e.dest.id);

        System.out.print(" + ");
        e.offset.accept(this);
        System.out.print(") <- ");
        System.out.print(e.dest);
    }

    @Override
    public void visit(If e) {
        // System.out.print(position + ": ");
        System.out.print("if ");
        e.cond.accept(this);
        System.out.print(" then\n");
        // updatePosition();
        e.e1.accept(this);
        System.out.print(" else\n");
        // updatePosition();
        e.e2.accept(this);
    }

    @Override
    public void visit(Eq e) {
        // currentBlock.addGen(e.id);
        updatePosition();
        updateInterval(e.id.id);

        System.out.print("(");
        System.out.print(e.id);
        System.out.print(" = ");
        e.e.accept(this);
        System.out.print(")");
    }

    @Override
    public void visit(LE e) {
        // currentBlock.addGen(e.id);
        updatePosition();
        updateInterval(e.id.id);

        System.out.print("(");
        System.out.print(e.id);
        System.out.print(" <= ");
        e.e.accept(this);
        System.out.print(")");
    }

    @Override
    public void visit(GE e) {
        // currentBlock.addGen(e.id);
        updatePosition();
        updateInterval(e.id.id);

        System.out.print("(");
        System.out.print(e.id);
        System.out.print(" >= ");
        e.e.accept(this);
        System.out.print(")");
    }

    @Override
    public void visit(FEq e) {
        // currentBlock.addGen(e.id1);
        // currentBlock.addGen(e.id2);
        updatePosition();
        updateInterval(e.id1.id);
        updateInterval(e.id2.id);

        System.out.print("(");
        System.out.print(e.id1);
        System.out.print(" =. ");
        System.out.print(e.id2);
        System.out.print(")");
    }

    @Override
    public void visit(FLE e) {
        // currentBlock.addGen(e.id1);
        // currentBlock.addGen(e.id2);
        updatePosition();
        updateInterval(e.id1.id);
        updateInterval(e.id2.id);

        System.out.print("(");
        System.out.print(e.id1);
        System.out.print(" <= ");
        System.out.print(e.id2);
        System.out.print(")");
    }

    @Override
    public void visit(Call e) {
        System.out.print("(");
        System.out.print(e.f);
        for (Id id: e.args) {
            System.out.print(" ");

            updatePosition();
            updateInterval(id.id);

            System.out.print(id);
        }
        System.out.print(")");
    }

    @Override
    public void visit(AppClosure e) {
        // currentBlock.addGen(e.id);

        System.out.print("appclo ");
        System.out.print(e.id);
        updateInterval(e.id.id);
        for (Id id: e.args) {
            System.out.print(" ");
            System.out.print(id);

            updatePosition();
            updateInterval(id.id);
        }
    }

    @Override
    public void visit(FunDefs e) {
        for (Exp exp: e.funs) {
            exp.accept(this);
            System.out.println("\n");
        }
    }

    @Override
    public void visit(Let e) {
        // currentBlock.addKill(e.id);

        System.out.print(position + ": ");
        System.out.print("let ");
        System.out.print(e.id);
        System.out.print(" = ");
        e.e1.accept(this);

        updatePosition();
        startInterval(e.id.id);

        System.out.print(" in\n");
        e.e2.accept(this);
        System.out.print("");


    }

    @Override
    public void visit(LetRec e) {
        startBlock(e.fd.fun.l.label);

        System.out.print(position + ": ");
        System.out.print("let ");
        System.out.print(e.fd.fun.l);
        updatePosition();
        addParams(e.fd.args);
        for (Id id: e.fd.args) {
            System.out.print(" ");
            System.out.print(id);

            // startInterval(id.id);
        }
        System.out.print(" =\n");
        // updatePosition();
        e.fd.e.accept(this);
        System.out.print("");
        endBlock();
    }

    @Override
    public void visit(Float e) {
        System.out.print(String.format("%.2f", e.f));
    }

    @Override
    public void visit(Self e) {

    }
}