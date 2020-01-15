package backend;

public class Program {
    private InstructionBlock text;
    private InstructionFactory factory;

    private static final int HEAP_SIZE = 4096;

    public Program(InstructionBlock text) {
        this.text = text;
        this.factory = new InstructionFactory();
    }

    private InstructionBlock exit() {
        return new InstructionBlock(factory.instr("MOV", "r0", "#0"))
            .comment("Exit syscall")
            .add(factory.instr("MOV", "r7", "#1"))
            .add(factory.instr("SVC", "#0"));
    }

    public void generateHeapAllocationCode() {
        factory.setLabel("_start");

        text
            .add(factory.instr("MOV", "r0", "#0")).comment("Heap allocation")
            .add(factory.instr("MOV", "r1", "#" + HEAP_SIZE))
            .add(factory.instr("MOV", "r2", "#0x2"))
            .add(factory.instr("MOV", "r3", "#0x22"))
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
            + "heap_offset_addr: .word 0\n\n"
            + ".text\n"
            + ".global _start\n\n"
            + text
            + "heap_start: .word heap_start_addr\n"
            + "heap_offset: .word heap_offset_addr\n";
    }
}