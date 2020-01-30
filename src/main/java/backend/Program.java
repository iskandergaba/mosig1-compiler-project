package backend;

/**
 * Represents an ARM assembly program, with different sections
 */
public class Program {
    // The text block, essentially the program's code
    private String data;
    private InstructionBlock text;

    // Factory used for creating instructions
    private InstructionFactory factory;

    // The size of the heap, in bytes
    private static final int HEAP_SIZE = 65536;

    public Program(InstructionBlock text, String data) {
        this.data = data;
        this.text = text;
        this.factory = new InstructionFactory();
    }

    // Shorthand for producing an exit syscall
    private InstructionBlock exit() {
        return new InstructionBlock(factory.instr("MOV", "r0", "#0"))
            .comment("Exit syscall")
            .add(factory.instr("MOV", "r7", "#1"))
            .add(factory.instr("SVC", "#0"));
    }

    /**
     * Produces the instructions necessary for initializing
     * the heap in our program, by calling mmap2().
     */
    public void generateHeapAllocationCode() {
        factory.setLabel("_start");

        text
            .add(factory.instr("MOV", "r0", "#0")).comment("Heap allocation")
            .add(factory.instr("LDR", "r1", "=#" + HEAP_SIZE))
            .add(factory.instr("MOV", "r2", "#0x3"))  // PROT_READ | PROT_WRITE
            .add(factory.instr("MOV", "r3", "#0x22")) // MAP_PRIVATE | MAP_ANONYMOUS
            .add(factory.instr("MOV", "r4", "#-1"))
            .add(factory.instr("MOV", "r5", "#0"))
            .add(factory.instr("MOV", "r7", "#0xc0")).comment("mmap2() syscall number")
            .add(factory.instr("SVC", "#0")).comment("Execute syscall")
            .add(factory.instr("LDR", "r4", "heap_start"))
            .add(factory.instr("STR", "r0", "[r4]")).comment("Store the heap start at the 'heap_start' symbol")
            .add(factory.instr("BL", text.lastFunctionLabel)).comment("Branch to main function")
            .chain(exit());
    }

    @Override
    public String toString() {
        return
              ".data\n"
            + "heap_start_addr: .word 0\n"
            + "heap_offset_addr: .word 0\n"
            + data
            + "\n"
            + ".text\n"
            + ".global _start\n"
            + ".arch armv7-a\n"
            + ".arm\n"
            + ".fpu vfpv4\n\n"
            + text
            + "heap_start: .word heap_start_addr\n"
            + "heap_offset: .word heap_offset_addr\n";
    }
}