import backend.*;
import frontend.*;

import java.io.FileWriter;
import java.util.Optional;

public class Main {
    public static void main(String[] argv) {
        try {
            Optional<common.asml.Exp> frontendResult = Frontend.execute(argv);
            if (frontendResult.isPresent()) {
                FileWriter fw = null;
                if (Frontend.writer != null) {
                    fw = new FileWriter(Frontend.writer);
                }
                Backend.execute(frontendResult.get(), fw);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}