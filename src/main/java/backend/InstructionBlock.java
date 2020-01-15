package backend;

import java.util.*;

public class InstructionBlock {
    private List<Instruction> instructions;
    private List<Integer> usedRegisters;
    public String lastFunctionLabel;

    public InstructionBlock() {
        this.instructions = new ArrayList<Instruction>();
        this.usedRegisters = new ArrayList<Integer>();
        this.lastFunctionLabel = "";
    }

    public InstructionBlock(Instruction instr) {
        this();
        this.instructions.add(instr);
    }

    public Instruction lastInstruction() {
        return this.instructions.get(this.instructions.size() - 1);
    }

    public InstructionBlock endWithNewline() {
        lastInstruction().setFinal();
        return this;
    }

    public InstructionBlock add(Instruction... instrs) {
        for (Instruction i: instrs) {
            this.instructions.add(i);
        }
        return this;
    }

    public InstructionBlock chain(InstructionBlock block) {
        this.instructions.addAll(block.instructions);
        this.lastFunctionLabel = block.lastFunctionLabel;
        return this;
    }

    public InstructionBlock comment(String comment) {
        lastInstruction().comment(comment);
        return this;
    }

    public InstructionBlock label(String label) {
        lastInstruction().setLabel(label);
        return this;
    }

    public InstructionBlock useRegister(int register) {
        this.usedRegisters.add(register);
        return this;
    }

    public InstructionBlock setReturn(String register) {
        lastInstruction().replaceArgument(0, register);
        return this;
    }

    public InstructionBlock setFunctionLabel(String label) {
        this.lastFunctionLabel = label;
        return this;
    }

    public List<Integer> getUsedRegisters() {
        return this.usedRegisters;
    }

    @Override
    public String toString() {
        return instructions.stream()      // Convert into a string
            .map(i -> i.toString())       // Transform the Instruction collection into a String one
            .reduce("", (a, b) -> a + b); // Concatenate all the strings into one
    }
}