package backend;

import java_cup.runtime.*;
import java.io.*;
import java.util.*;
import frontend.*;

public class Main {
  static public void main(String argv[]) {    
    try {
      Parser p = new Parser(new Lexer(new FileReader(argv[0])));
      Exp expression = (Exp) p.parse().value;
      assert (expression != null);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

