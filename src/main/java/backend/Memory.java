package backend;

import java.lang.Object;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class Memory {
    // Boolean[] regIsFree = Collections.nCopies(16, true).toArray(new Boolean[16]);
    static final int fp = 11;
    Boolean[] regIsFree = new Boolean[16];
    int offset;

    Map<String, String> finalMap = new HashMap<String, String>();

    Map<String, Integer> idRegMap = new HashMap<String, Integer>();
    Map<String, Integer> idOffMap = new HashMap<String, Integer>();

    String scope;

    void UpdateScope(Label label) {
        scope = label.label;
    }

    void ExitScope() {
        scope = null;
    }

    void memInit() {
        Arrays.fill(regIsFree, true);
        offset = 0;
        idRegMap.clear();
        idOffMap.clear();
    }

    String allocate(Id id) {
        String path = scope + "." + id.toString();
        return allocateBasic(path);
    }

    String allocateBasic(String path) {
        String out = varLookup(path);
        if (out != null) return out;

        for (int i=4;i<=12;i++) {
            if (i == fp) continue;
            if (regIsFree[i]) {
                regIsFree[i] = false;
                idRegMap.put(path, i);
                out = new String("r" + i);
                finalMap.put(path, out);
                return out;
            }
        }
        offset -= 4;
        idOffMap.put(path, offset);
        out = new String("[r" + fp + ", " + offset + "]");
        finalMap.put(path, out);
        return out;
    }

    String varLookup(String path) {
        if (idRegMap.containsKey(path)) return new String("r" + idRegMap.get(path));
        if (idOffMap.containsKey(path)) return new String("[fp, " + idOffMap.get(path) + "]");
        return null;
    }

    void free(Id id) {
        String path = scope + "." + id.toString();
        if (idRegMap.containsKey(path)) {
            int i = idRegMap.get(path);
            idRegMap.remove(path);
            regIsFree[i] = true;
        }
    }

    void printMem() {
        for (Map.Entry<String, String> entry : finalMap.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue().toString());
        }
    }
}