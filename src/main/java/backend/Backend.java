package backend;

import java.io.*;

import common.asml.*;

public class Backend {
  static public void execute(common.asml.Exp expression, FileWriter writer) {
    try {
      /*
      Parser p = new Parser(new Lexer(new FileReader(argv[0])));
      Exp expression = (Exp) p.parse().value;
      assert (expression != null);
      */

      LinearScanVisitor v = new LinearScanVisitor();
      expression.accept(v);
      v.printIntervals();
      v.linearScanRegisterAllocation();

      CodeGenerationVisitor cgv = new CodeGenerationVisitor(v.registers, v.locations);

      InstructionBlock text = expression.accept(cgv);
      Program prog = new Program(text);
      prog.generateHeapAllocationCode();

      System.out.println(writer);
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
