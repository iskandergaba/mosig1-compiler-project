package backend;

import java.io.*;

public class Backend {
  static public void execute(common.asml.Exp expression, FileWriter writer, boolean debug) {
    try {
      /*
      Parser p = new Parser(new Lexer(new FileReader(argv[0])));
      Exp expression = (Exp) p.parse().value;
      assert (expression != null);
      */

      LinearScanVisitor v = new LinearScanVisitor(debug);
      expression.accept(v);
      v.printIntervals();
      v.linearScanRegisterAllocation();

      CodeGenerationVisitor cgv = new CodeGenerationVisitor(v.registers, v.locations);

      InstructionBlock text = expression.accept(cgv);
      Program prog = new Program(text, cgv.data);
      prog.generateHeapAllocationCode();

      if (writer != null) {
        writer.write(prog.toString());
        writer.close();
      } else {
        System.out.println(prog);
      }


    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
