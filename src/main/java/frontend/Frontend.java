package frontend;

import java.io.*;
import java.util.Optional;

public class Frontend {
  public static String writer = null;

  static public Optional<common.asml.Exp> execute(String argv[]) throws Exception {
    try {
      FileReader file = null;
      boolean parseOnly = false;
      boolean typeCheckOnly = false;
      boolean asmlOnly = false;

      // Command Line Arguments
      if (argv.length == 0) {
        throw new Exception();
      } else if (argv.length == 1) {

        // Case for direct compilation
        if (argv[0].endsWith(".ml")) {
          file = new FileReader(argv[0]);
        }

        // Check if 1st arg is a flag
        if (argv[0].startsWith("-")) {
          char c = argv[0].charAt(1);
          switch (c) {
          case 'h':
            System.out.println(
                "Options :\n-o : output file\n-h : display help\n-v : display version\n-t : type check only\n-p : parse only\n-asml : output ASML");
            throw new Exception();
          case 'v':
            System.out.println("mincamlc 1.0");
            throw new Exception();
          case 'o':
          case 't':
          case 'p':
          case 'a':
            System.out.println("Error : Missing filename after '" + argv[0] + "'");
            throw new Exception();
          default:
            System.out.println("Error : Invalid flag: " + argv[0]);
            throw new Exception();
          }
        }

        // Check if invalid extension
        if (!argv[0].endsWith(".ml")) {
          System.out.println("Error: Only .ml file extensions supported");
          throw new Exception();
        }

      } else if (argv.length == 2) {

        // Check if 1st arg is a flag
        if (argv[0].startsWith("-")) {
          char c = argv[0].charAt(1);
          switch (c) {
          case 't':
            typeCheckOnly = true;
            break;
          case 'p':
            parseOnly = true;
            break;
          case 'a':
            // Output ASML
            asmlOnly = true;
            break;
          case 'o':
            System.out.println("Error : No input files");
            throw new Exception();
          default:
            System.out.println("Error : Invalid flag: " + argv[0]);
            throw new Exception();
          }
          file = new FileReader(argv[1]);
        }
      } else if (argv.length == 3) {

        // Check if 2nd arg is a flag
        if (argv[1].startsWith("-")) {
          char c = argv[1].charAt(1);
          switch (c) {
          case 'o':
            // Output to a specific file
            Frontend.writer = argv[2];
            break;
          case 'h':
          case 'v':
          case 't':
          case 'p':
          case 'a':
            System.out.println("Error : Too many arguments ");
            throw new Exception();
          default:
            System.out.println("Error : Invalid flag: " + argv[0]);
            throw new Exception();
          }
        }
        // Check if 1st arg is a flag
        else if (argv[0].startsWith("-")) {
          char c = argv[0].charAt(1);
          switch (c) {
          case 'o':
            // Output to a specific file
            Frontend.writer = argv[1];
            break;
          case 'h':
          case 'v':
          case 't':
          case 'p':
          case 'a':
            System.out.println("Error : Too many arguments ");
            throw new Exception();
          default:
            System.out.println("Error : Invalid flag: " + argv[0]);
            throw new Exception();
          }
          file = new FileReader(argv[2]);
        } else if (argv[2].startsWith("-")) {
          System.out.println("Error : Too many arguments");
          throw new Exception();
        }
      } else if (argv.length > 3) {
        System.out.println("Error : Too many arguments");
        throw new Exception();
      }

      Parser p = new Parser(new Lexer(file));
      Exp expression = (Exp) p.parse().value;
      assert (expression != null);

      if (parseOnly) {
        return Optional.empty();
      }

      System.out.println("------ AST Generation ------");
      expression.accept(new PrintVisitor());
      System.out.println();
      System.out.println("------ AST Generation DONE ------");

      System.out.println("------ Scope checking ------");
      expression.accept(new ScopeVisitor());
      System.out.println("------ Scope checking DONE ------");

      System.out.println("------ Type checking ------");
      expression.accept(new TypeVisitor());
      System.out.println("------ Type checking DONE ------");

      if (typeCheckOnly) {
        return Optional.empty();
      }

      System.out.println("------ K-Normalization ------");
      expression = expression.accept(new KNormalizer());
      expression.accept(new PrintVisitor());
      System.out.println();
      System.out.println("------ K-Normalization DONE------");

      System.out.println("------ Alpha-Conversion ------");
      expression.accept(new AlphaConverter());
      expression.accept(new PrintVisitor());
      System.out.println();
      System.out.println("------ Alpha-Conversion DONE ------");

      System.out.println("------ Nested Let-Reduction ------");
      expression = expression.accept(new LetReducer());
      expression.accept(new PrintVisitor());
      System.out.println();
      System.out.println("------ Nested Let-Reduction DONE ------");

      System.out.println("------ Beta-Reduction #1 ------");
      expression = expression.accept(new BetaReducer());
      expression.accept(new PrintVisitor());
      System.out.println();
      System.out.println("------ Beta-Reduction #1 DONE ------");

      /*System.out.println("------ Inline Expansion ------");
      expression = expression.accept(new InlineExpander(100));
      expression.accept(new AlphaConverter());
      expression.accept(new PrintVisitor());
      System.out.println();
      System.out.println("------ Inline Expansion DONE ------");

      System.out.println("------ Beta-Reduction #2 ------");
      expression = expression.accept(new BetaReducer());
      expression.accept(new PrintVisitor());
      System.out.println();
      System.out.println("------ Beta-Reduction #2 DONE ------");*/

      System.out.println("------ Constant folding ------");
      expression = expression.accept(new ConstantFolder());
      expression.accept(new PrintVisitor());
      System.out.println("------ Constant folding DONE ------");

      System.out.println("------ Free Variable Computation ------");
      FreeVarVisitor v1 = new FreeVarVisitor();
      expression.accept(v1);
      System.out.println("------ Free Variable Computation DONE ------");

      System.out.println("------ Closure Conversion ------");
      ClosureConverter v2 = new ClosureConverter(v1);
      expression = expression.accept(v2);
      expression = v2.join(expression);
      expression.accept(new PrintVisitor());
      System.out.println();
      System.out.println("------ Closure Conversion DONE ------");

      System.out.println("------ Elimination of Unnecessary Definitions ------");
      expression = expression.accept(new UnnecessaryDefRemover());
      expression.accept(new PrintVisitor());
      System.out.println("------ Elimination of Unnecessary Definitions DONE ------");

      System.out.println("------ ASML Generation ------");
      AsmlGenerator v = new AsmlGenerator();
      common.asml.Exp result = expression.accept(v);
      result = v.join(result);
      result.accept(new common.visitor.PrintVisitor());
      System.out.println();
      System.out.println("------ ASML Generation DONE ------");

      if (asmlOnly) {
        result.accept(new common.visitor.PrintVisitor(new PrintStream(Frontend.writer)));
        return Optional.empty();
      }
      return Optional.of(result);

    } catch (TypingException e) {
      System.out.print("(TYPING ERROR) ");
      e.printStackTrace();
    } catch (EnvironmentException e) {
      System.out.print("(SCOPE ERROR) ");
      e.printStackTrace();
    } catch (AsmlTranslationException e) {
      System.out.print("(ASML GENERATION ERROR) ");
      e.printStackTrace();
    } catch (FileNotFoundException e) {
      System.out.println("Error: file not found: " + argv[0]);
    } catch (AssertionError e) {
      System.out.print("");
    } catch (Exception e) {
      System.out.println("Usage: ./mincamlc <options> <source files>");
      e.printStackTrace();
      System.out.println("Compilation terminated");
    }
    return null;
  }
}
