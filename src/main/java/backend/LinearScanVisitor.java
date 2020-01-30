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
    boolean debug;

    public LinearScanVisitor(boolean debug) {
        this.debug = debug;
    }

    final Integer activeMax = 2, registersNum = 16, fp = 11, regStart = 4, regEnd = 12;
    Boolean[] regIsFree = new Boolean[registersNum];
    Map<String, Integer> registers = new HashMap<String, Integer>();
    Map<String, Integer> locations = new HashMap<String, Integer>();
    Map<String, String> memory = new HashMap<String, String>();

    void printlnDebug(String msg) {
        if (debug) {
            System.out.println(msg);
        }
    }

    void printDebug(Object msg) {
        if (debug) {
            System.out.print(msg.toString());
        }
    }

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
            this.locations.put(fullPath(id.id), (i + 10) * 4);
            i++;
        }
    }

    String fullPath(String vname) {
        return this.scope + "." + vname;
    }

    void printIntervals() {
        if (debug) {
            printlnDebug("# Live intervals");
            for (Map.Entry<String, List<LiveInterval>> entry : basicBlocksMap.entrySet()) {
                String label = entry.getKey();
                List<LiveInterval> block = entry.getValue();
                printlnDebug("GROUP '" + label + "': ");
                for (LiveInterval liveInterval : block) {
                    printlnDebug("  " + liveInterval.name +
                    " (" + liveInterval.startpoint + "," + liveInterval.endpoint + ") " +
                    liveInterval.length);
                }
            }
            printlnDebug("");
        }
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
        printlnDebug("# Live intervals traversal");

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
                    printlnDebug("r" + registers.get(i.name) + " <- " + i.name);
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

        printlnDebug("\n# Register allocation memory map");
        for (Map.Entry<String, String> entry : this.memory.entrySet()) {
            printlnDebug(entry.getKey() + ": " + entry.getValue());
        }
        printlnDebug("");
    }

    void expireOldInterval(LiveInterval i) {
        List<LiveInterval> expired = new ArrayList<LiveInterval>();
        for (LiveInterval j : this.active) {
            if (j.endpoint >= i.startpoint)
                break;
            // expire
            printlnDebug("r" + registers.get(j.name) + " -> X");
            expired.add(j);
            // free register
            freeRegister(registers.get(j.name));
        }
        this.active.removeAll(expired);
    }

    void spillInterval(LiveInterval i, Integer offset) {
        if (this.active.size() == 0) {
            // location
            printlnDebug("[r" + fp + ", " + offset + "]" + " <- " + i.name);
            locations.put(i.name, offset);
            return;
        }
        int last = this.active.size() - 1;
        LiveInterval spill = this.active.get(last);
        if (spill.endpoint > i.endpoint) {
            // register
            registers.put(i.name, registers.get(spill.name));
            printlnDebug("r" + registers.get(spill.name) + " -> ");
            registers.remove(spill.name); // just for convenience
            printlnDebug("r" + registers.get(i.name) + " <- " + i.name);
            // location
            locations.put(spill.name, offset);
            printlnDebug("[r" + fp + ", " + locations.get(spill.name) + "]" + " <- " + spill.name);
            this.active.remove(last);
            this.active.add(i);
            this.active.sort((i1, i2) -> i1.endpoint.compareTo(i2.endpoint));
        } else {
            // location
            printlnDebug("[r" + fp + ", " + offset + "]" + " <- " + i.name);
            locations.put(i.name, offset);
        }
    }



    @Override
    public void visit(Int e) {
        printDebug(e.i);
    }

    @Override
    public void visit(Var e) {
        // currentBlock.addGen(e.id);
        updateInterval(e.id.id);

        printDebug(e.id);
    }

    @Override
    public void visit(Nop e) {
        printDebug("nop");
    }

    @Override
    public void visit(Fun e) {
        printDebug(e.l);
    }

    @Override
    public void visit(Neg e) {
        // currentBlock.addGen(e.id);
        updatePosition();
        updateInterval(e.id.id);

        printDebug("(neg ");
        printDebug(e.id);
        printDebug(")");
    }

    @Override
    public void visit(FNeg e) {
        // currentBlock.addGen(e.id);
        updatePosition();
        updateInterval(e.id.id);

        printDebug("(fneg ");
        printDebug(e.id);
        printDebug(")");
    }

    @Override
    public void visit(FAdd e) {
        // currentBlock.addGen(e.id1);
        // currentBlock.addGen(e.id2);
        updatePosition();
        updateInterval(e.id1.id);
        updateInterval(e.id2.id);

        printDebug("(fadd ");
        printDebug(e.id1);
        printDebug(" ");
        printDebug(e.id2);
        printDebug(")");
    }

    @Override
    public void visit(FSub e) {
        // currentBlock.addGen(e.id1);
        // currentBlock.addGen(e.id2);
        updatePosition();
        updateInterval(e.id1.id);
        updateInterval(e.id2.id);

        printDebug("(fsub ");
        printDebug(e.id1);
        printDebug(" ");
        printDebug(e.id2);
        printDebug(")");
    }

    @Override
    public void visit(FMul e) {
        // currentBlock.addGen(e.id1);
        // currentBlock.addGen(e.id2);
        updatePosition();
        updateInterval(e.id1.id);
        updateInterval(e.id2.id);

        printDebug("(fmul ");
        printDebug(e.id1);
        printDebug(" ");
        printDebug(e.id2);
        printDebug(")");
    }

    @Override
    public void visit(FDiv e) {
        // currentBlock.addGen(e.id1);
        // currentBlock.addGen(e.id2);
        updatePosition();
        updateInterval(e.id1.id);
        updateInterval(e.id2.id);

        printDebug("(fdiv ");
        printDebug(e.id1);
        printDebug(" ");
        printDebug(e.id2);
        printDebug(")");
    }

    @Override
    public void visit(New e) {
        printDebug("new ");
        e.size.accept(this);
    }

    @Override
    public void visit(Add e) {
        // currentBlock.addGen(e.id);
        updatePosition();
        updateInterval(e.id.id);

        printDebug("(add ");
        printDebug(e.id);
        printDebug(" ");
        e.e.accept(this);
        printDebug(")");
    }

    @Override
    public void visit(Sub e) {
        // currentBlock.addGen(e.id);
        updatePosition();
        updateInterval(e.id.id);

        printDebug("(sub ");
        printDebug(e.id);
        printDebug(" ");
        e.e.accept(this);
        printDebug(")");
    }

    @Override
    public void visit(Get e) {
        // currentBlock.addGen(e.base);
        updatePosition();

        System.out.print("mem (");
        e.base.accept(this);
        System.out.print(" + ");
        e.offset.accept(this);
        printDebug(")");
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
        printDebug(") <- ");
        printDebug(e.dest);
    }

    @Override
    public void visit(If e) {
        // printDebug(position + ": ");
        printDebug("if ");
        e.cond.accept(this);
        printDebug(" then\n");
        // updatePosition();
        e.e1.accept(this);
        printDebug(" else\n");
        // updatePosition();
        e.e2.accept(this);
    }

    @Override
    public void visit(Eq e) {
        // currentBlock.addGen(e.id);
        updatePosition();
        updateInterval(e.id.id);

        printDebug("(");
        printDebug(e.id);
        printDebug(" = ");
        e.e.accept(this);
        printDebug(")");
    }

    @Override
    public void visit(LE e) {
        // currentBlock.addGen(e.id);
        updatePosition();
        updateInterval(e.id.id);

        printDebug("(");
        printDebug(e.id);
        printDebug(" <= ");
        e.e.accept(this);
        printDebug(")");
    }

    @Override
    public void visit(GE e) {
        // currentBlock.addGen(e.id);
        updatePosition();
        updateInterval(e.id.id);

        printDebug("(");
        printDebug(e.id);
        printDebug(" >= ");
        e.e.accept(this);
        printDebug(")");
    }

    @Override
    public void visit(FEq e) {
        // currentBlock.addGen(e.id1);
        // currentBlock.addGen(e.id2);
        updatePosition();
        updateInterval(e.id1.id);
        updateInterval(e.id2.id);

        printDebug("(");
        printDebug(e.id1);
        printDebug(" =. ");
        printDebug(e.id2);
        printDebug(")");
    }

    @Override
    public void visit(FLE e) {
        // currentBlock.addGen(e.id1);
        // currentBlock.addGen(e.id2);
        updatePosition();
        updateInterval(e.id1.id);
        updateInterval(e.id2.id);

        printDebug("(");
        printDebug(e.id1);
        printDebug(" <= ");
        printDebug(e.id2);
        printDebug(")");
    }

    @Override
    public void visit(Call e) {
        printDebug("(");
        printDebug(e.f);
        for (Id id: e.args) {
            printDebug(" ");

            updatePosition();
            updateInterval(id.id);

            printDebug(id);
        }
        printDebug(")");
    }

    @Override
    public void visit(AppClosure e) {
        // currentBlock.addGen(e.id);

        printDebug("appclo ");
        printDebug(e.id);
        updateInterval(e.id.id);
        for (Id id: e.args) {
            printDebug(" ");
            printDebug(id);

            updatePosition();
            updateInterval(id.id);
        }
    }

    @Override
    public void visit(FunDefs e) {
        for (Exp exp: e.funs) {
            exp.accept(this);
            printlnDebug("\n");
        }
    }

    @Override
    public void visit(Let e) {
        // currentBlock.addKill(e.id);

        printDebug(position + ": ");
        printDebug("let ");
        printDebug(e.id);
        printDebug(" = ");
        e.e1.accept(this);

        updatePosition();
        startInterval(e.id.id);

        printDebug(" in\n");
        e.e2.accept(this);
        printDebug("");


    }

    @Override
    public void visit(LetRec e) {
        startBlock(e.fd.fun.l.label);

        printDebug(position + ": ");
        printDebug("let ");
        printDebug(e.fd.fun.l);
        updatePosition();
        addParams(e.fd.args);
        for (Id id: e.fd.args) {
            printDebug(" ");
            printDebug(id);

            // startInterval(id.id);
        }
        printDebug(" =\n");
        // updatePosition();
        e.fd.e.accept(this);
        printDebug("");
        endBlock();
    }

    @Override
    public void visit(Float e) {
        printDebug(String.format("%.2f", e.f));
    }

    @Override
    public void visit(Self e) {

    }
}