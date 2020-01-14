package frontend;

import java.io.*;

public class Main {
  static public void main(String argv[]) {
    try {
      Parser p = new Parser(new Lexer(new FileReader(argv[0])));
      Exp expression = (Exp) p.parse().value;
      assert (expression != null);

      System.out.println("------ AST generation ------");
      expression.accept(new PrintVisitor());
      System.out.println();
      System.out.println("------ AST generation DONE ------");
      
      System.out.println("------ Type checking ------");
      expression.accept(new TypeVisitor());
      System.out.println("------ Type checking DONE ------");
      
      System.out.println("------ K-Normalization ------");
      expression = expression.accept(new KNVisitor());
      expression.accept(new PrintVisitor());
      System.out.println();
      System.out.println("------ K-Normalization DONE------");
      
      System.out.println("------ Alpha-conversion ------");
      expression.accept(new ACVisitor());
      expression.accept(new PrintVisitor());
      System.out.println();
      System.out.println("------ Alpha-conversion DONE ------");
    } catch (TypingException e) {
      System.out.print("(TYPING ERROR) ");
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
