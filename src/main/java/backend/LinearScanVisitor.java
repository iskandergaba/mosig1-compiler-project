package backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.asml.*;
import common.asml.Float;
import common.visitor.*;

/**
 * Visits the ASML AST creates an allocation registers map using a linear scanning algorithm.
 * If activeMax is set to 0, this is the same as a spill-all approach.
 */
class LinearScanVisitor implements Visitor {

    // Current operatin position
    Integer position = 0;

    // Name of a current scope
    String scope;

    // Map of live intervals for each scope
    Map<String, List<LiveInterval>> basicBlocksMap = new HashMap<String, List<LiveInterval>>();

    // Map of all live intervals
    Map<String, LiveInterval> intervalsMap;

    // List of live and active intervals
    List<LiveInterval> liveIntervals, active;

    // Debug mode flag
    boolean debug;

    public LinearScanVisitor(boolean debug) {
        this.debug = debug;
    }

    // Maximum number of simultaneously allocated registers, total number of registers,
    // FP register index, start and end of local variable registers
    final Integer activeMax = 2, registersNum = 16, fp = 11, regStart = 4, regEnd = 12;
    Boolean[] regIsFree = new Boolean[registersNum];

    // Maps of registers and stack location
    Map<String, Integer> registers = new HashMap<String, Integer>();
    Map<String, Integer> locations = new HashMap<String, Integer>();

    // Final memory reresentation
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

    // Increment current position
    void updatePosition() {
        this.position++;
    }

    /**
     * Initialize a new block, updating the scope
     * @param fname The name of function
     */
    void startBlock(String fname) {
        this.intervalsMap = new HashMap<String, LiveInterval>();
        this.liveIntervals = new ArrayList<LiveInterval>();
        this.basicBlocksMap.put(fname, liveIntervals);
        this.scope = fname;
    }

    /**
     * Closes the current block
     */
    void endBlock() {
        this.intervalsMap = null;
        this.liveIntervals = null;
        this.scope = null;
    }

    /**
     * Start a new live interval for the selected variable
     * @param vname The name of variable
     */
    void startInterval(String vname) {
        LiveInterval liveInterval = new LiveInterval(fullPath(vname), position);
        this.liveIntervals.add(liveInterval);
        this.intervalsMap.put(fullPath(vname), liveInterval);
    }

    /**
     * Updates the range of the selected variable live interval
     * @param fname The name of function
     */
    void updateInterval(String vname) {
        if (locations.get(fullPath(vname)) == null)
            this.intervalsMap.get(fullPath(vname)).update(this.position);
    }

    /**
     * Generate FP offsets for function parameters
     * @param params The list of function parameters
     */
    void addParams(List<Id> params) {
        int i = 0;
        for (Id id: params) {
            this.locations.put(fullPath(id.id), (i + 10) * 4);
            i++;
        }
    }

    /**
     * Get the full path of the varible in the current scope
     * @param vname The name of variable
     * @return Full variable path
     */
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

    /**
     * Get a free register
     * @return Index of a free register
     */
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

    /**
     * Perform a linear scanning
     */
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

    /**
     * Free expired registers
     */
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

    /**
     * Perform a register spilling.
     * A register to be spilled is the one is longest remaining TTL
     */
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
        updatePosition();
        updateInterval(e.id.id);

        printDebug("(neg ");
        printDebug(e.id);
        printDebug(")");
    }

    @Override
    public void visit(FNeg e) {
        updatePosition();
        updateInterval(e.id.id);

        printDebug("(fneg ");
        printDebug(e.id);
        printDebug(")");
    }

    @Override
    public void visit(FAdd e) {
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
        updatePosition();

        printDebug("mem (");
        e.base.accept(this);
        printDebug(" + ");
        e.offset.accept(this);
        printDebug(")");
    }

    @Override
    public void visit(Put e) {
        updatePosition();

        printlnDebug("mem (");

        e.base.accept(this);

        updateInterval(e.dest.id);

        printlnDebug(" + ");
        e.offset.accept(this);
        printDebug(") <- ");
        printDebug(e.dest);
    }

    @Override
    public void visit(If e) {
        printDebug("if ");
        e.cond.accept(this);
        printDebug(" then\n");
        e.e1.accept(this);
        printDebug(" else\n");
        e.e2.accept(this);
    }

    @Override
    public void visit(Eq e) {
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
        }
        printDebug(" =\n");
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