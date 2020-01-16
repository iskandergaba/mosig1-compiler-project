package backend;

import java.util.*;

/**
 * A block of instructions that can be chained to others
 */
public class InstructionBlock {
    // Instructions in the block
    private List<Instruction> instructions;

    // Registers used by the instruction (used for freeing registers)
    private List<Integer> usedRegisters;

    // Label of the last function defined in the program
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

    /**
     * Returns the last instruction of the program
     * @return the last instruction of the program
     */
    public Instruction lastInstruction() {
        return this.instructions.get(this.instructions.size() - 1);
    }

    /**
     * Sets the last instruction to be ended with an additional newline
     * character (used to separate functions in the produced code)
     * @return The current instruction block
     */
    public InstructionBlock endWithNewline() {
        lastInstruction().setFinal();
        return this;
    }

    /**
     * Adds instructions to the block
     * @param instrs A list of instructions to be added to the block
     * @return The updated instruction block
     */
    public InstructionBlock add(Instruction... instrs) {
        for (Instruction i: instrs) {
            this.instructions.add(i);
        }
        return this;
    }

    /**
     * Chains this block to another, adding the other
     * block's instruction to this one and returning the
     * new block 
     * @param block The other block to be chained
     * @return The update block
     */
    public InstructionBlock chain(InstructionBlock block) {
        this.instructions.addAll(block.instructions);
        this.lastFunctionLabel = block.lastFunctionLabel;
        return this;
    }

    /**
     * Adds a comment to the last instruction of the block
     * @param comment The comment (without the '@' symbol)
     * @return The updated block
     */
    public InstructionBlock comment(String comment) {
        lastInstruction().comment(comment);
        return this;
    }

    /**
     * Adds a comment to the first instruction of the block
     * @param comment The comment (without the '@' symbol)
     * @return The updated block
     */
    public InstructionBlock commentFirst(String comment) {
        instructions.get(0).comment(comment);
        return this;
    }

    /**
     * Adds a label to the last instruction, which will be displayed
     * before the instruction. This means the label will point to the
     * last instruction of this block
     * @param label The label to put on the last instruction of the block
     * @return The updated block
     */
    public InstructionBlock label(String label) {
        lastInstruction().setLabel(label);
        return this;
    }

    /**
     * Adds a register to the list of used registers
     * @param register A register between 4 and 12 included
     * @return The updated block
     */
    public InstructionBlock useRegister(int register) {
        this.usedRegisters.add(register);
        return this;
    }

    /**
     * Replaces the first argument of the last instruction,
     * essentially used for specifying the register in which
     * to move the result of the sub-expression, which cannot be
     * known at the time of the parsing of the sub-expression.
     * @param register The register in which the result will be put
     * @return The updated block
     */
    public InstructionBlock setReturn(String register) {
        lastInstruction().replaceArgument(0, register);
        return this;
    }

    /**
     * Sets the label of the last defined function of the block
     * @param label The label of the last function block
     * @return The updated block
     */
    public InstructionBlock setFunctionLabel(String label) {
        this.lastFunctionLabel = label;
        return this;
    }

    /**
     * Returns the list of used registers of this block
     * @return the list of used registers of this block
     */
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