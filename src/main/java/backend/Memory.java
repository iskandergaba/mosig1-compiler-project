package backend;

import java.lang.Object;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class Memory {
    Boolean[] regIsFree = Collections.nCopies(16, true).toArray(new Boolean[16]);

    Map<String, Integer> varMap = new HashMap<String, Integer>();

    String localVariable(Id id) {
        if (!varMap.containsKey(id.id)) {
            for (int i=4;i<=12;i++) {
                if (regIsFree[i]) {
                    regIsFree[i] = false;
                    varMap.put(id.id, i);
                    return new String("r" + i);
                }
            }
            throw new java.lang.RuntimeException(
                "Failed to allocate register for variable " + id.id);
        }
        return new String("r" + varMap.get(id.id));
    }
}