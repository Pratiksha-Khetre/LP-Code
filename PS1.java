import java.util.*;

public class PS1 {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // TABLES
        Map<String, Integer> MNT = new LinkedHashMap<>();            // macro name -> MDT index
        List<String> MDT = new ArrayList<>();                        // macro body
        Map<String, List<String>> ALA = new LinkedHashMap<>();       // macro -> formal params

        Map<String, Integer> SYMTAB = new LinkedHashMap<>();
        Map<String, Integer> LITTAB = new LinkedHashMap<>();
        List<Integer> POOLTAB = new ArrayList<>();
        List<String> Intermediate = new ArrayList<>();

        List<String> literals = new ArrayList<>();

        System.out.println("Enter number of lines:");
        int n = sc.nextInt();
        sc.nextLine();

        System.out.println("Enter Assembly code line by line:");
        List<String> input = new ArrayList<>();
        for (int i = 0; i < n; i++)
            input.add(sc.nextLine().trim());

        int lc = 0;
        boolean inMacro = false;
        String currentMacro = null;

        POOLTAB.add(0); // first literal pool starts

        // ------------------ PASS I ------------------
        for (int i = 0; i < n; i++) {

            String line = input.get(i);
            if (line.isEmpty()) continue;

            String parts[] = line.split("\\s+");

            // ----------- MACRO DEFINITION START ----------
            if (parts[0].equals("MACRO")) {
                inMacro = true;
                continue;
            }

            // Inside MACRO definition
            if (inMacro) {
                if (parts[0].equals("MEND")) {
                    MDT.add("MEND");
                    inMacro = false;
                    currentMacro = null;
                    continue;
                }

                // First line after MACRO is macro header
                if (currentMacro == null) {
                    currentMacro = parts[0];          // macro name
                    MNT.put(currentMacro, MDT.size()); // MDT start index for macro

                    // extract formal parameters
                    List<String> params = new ArrayList<>();
                    for (int k = 1; k < parts.length; k++)
                        if (parts[k].startsWith("&"))
                            params.add(parts[k]);

                    ALA.put(currentMacro, params);
                    MDT.add(line); // store macro header
                } else {
                    MDT.add(line); // macro body
                }
                continue;
            }
            // ----------- MACRO DEFINITION END ----------

            // ----------- MACRO CALL ----------
            if (MNT.containsKey(parts[0])) {
                String macro = parts[0];
                List<String> formals = ALA.get(macro);

                // actual parameters
                Map<String, String> map = new HashMap<>();
                for (int k = 0; k < formals.size(); k++)
                    map.put(formals.get(k), parts[k + 1]);

                // expand macro from MDT
                int start = MNT.get(macro);

                for (int j = start + 1; j < MDT.size(); j++) {
                    if (MDT.get(j).equals("MEND"))
                        break;

                    String mline = MDT.get(j);
                    for (String f : formals)
                        mline = mline.replace(f, map.get(f));

                    Intermediate.add(lc + ": " + mline);

                    // detect literal
                    if (mline.contains("=")) {
                        String lit = mline.substring(mline.indexOf("=")).trim();
                        if (!literals.contains(lit)) {
                            literals.add(lit);
                            LITTAB.put(lit, -1);
                        }
                    }

                    lc++;
                }

                continue;
            }
            // ----------- END MACRO CALL ----------

            // START
            if (parts[0].equals("START")) {
                Intermediate.add("START");
                continue;
            }

            // END → place literals
            if (parts[0].equals("END")) {
                Intermediate.add("END");

                for (String lit : literals) {
                    if (LITTAB.get(lit) == -1) {
                        LITTAB.put(lit, lc);
                        Intermediate.add(lc + ": (Literal) " + lit);
                        lc++;
                    }
                }
                break;
            }

            // NORMAL INSTRUCTION
            Intermediate.add(lc + ": " + line);
            lc++;
        }

        // ---------------- OUTPUT ----------------

        System.out.println("\n===== MACRO NAME TABLE (MNT) =====");
        for (var e : MNT.entrySet())
            System.out.println(e.getKey() + " -> MDT Index " + e.getValue());

        System.out.println("\n===== MACRO DEFINITION TABLE (MDT) =====");
        int idx = 0;
        for (String s : MDT)
            System.out.println((idx++) + ": " + s);

        System.out.println("\n===== ARGUMENT LIST ARRAY (ALA) =====");
        for (var e : ALA.entrySet())
            System.out.println(e.getKey() + " -> " + e.getValue());

        System.out.println("\n===== INTERMEDIATE CODE =====");
        for (String s : Intermediate)
            System.out.println(s);

        System.out.println("\n===== LITERAL TABLE =====");
        for (var e : LITTAB.entrySet())
            System.out.println(e.getKey() + " -> " + e.getValue());

        System.out.println("\n===== SYMBOL TABLE =====");
        for (var e : SYMTAB.entrySet())
            System.out.println(e.getKey() + " -> " + e.getValue());

        System.out.println("\n===== POOL TABLE =====");
        for (int p : POOLTAB)
            System.out.println(p);

        sc.close();
    }
}

/*  
START 200
READ A
READ B
MOVER AREG, ='5'
MOVER AREG, A
ADD AREG, B
SUB AREG, ='6'
MOVEM AREG, C
PRINT C
LTORG
MOVER AREG, ='15'
MOVER AREG, A
ADD AREG, B
SUB AREG, ='16'
DIV AREG, ='26'
MOVEM AREG, C
A DS 1
B DS 1
C DS 1
STOP
END
*/