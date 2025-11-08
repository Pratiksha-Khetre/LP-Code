import java.util.*;

public class PS3 {

    static class MntEntry {
        String name;
        int mdtIndex;   // 1-based index into MDT
        int paramCount;
        MntEntry(String n, int idx, int pc) { name = n; mdtIndex = idx; paramCount = pc; }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Macro Processor — Pass I (build MNT, MDT, and Intermediate Code)");
        System.out.print("Enter number of source lines: ");
        int totalLines = Integer.parseInt(sc.nextLine().trim());

        System.out.println("Enter source lines (one per line):");
        List<String> source = new ArrayList<>();
        for (int i = 0; i < totalLines; i++) {
            source.add(sc.nextLine());
        }

        // Data structures
        LinkedHashMap<String, MntEntry> mnt = new LinkedHashMap<>(); // preserve insertion order
        List<String> mdt = new ArrayList<>(); // 1-based index easier: we'll print indices starting at 1
        List<String> intermediate = new ArrayList<>();

        // Current MDT index (1-based)
        int currentMdtIndex = 1;

        // Process source lines
        for (int i = 0; i < source.size(); i++) {
            String line = source.get(i).trim();
            if (line.equalsIgnoreCase("MACRO")) {
                // Next line should be macro header
                i++;
                if (i >= source.size()) {
                    System.out.println("Error: MACRO without header.");
                    break;
                }
                String header = source.get(i).trim();
                // parse header: macroName followed by parameters (optional)
                // examples: INCR &A  OR INCR &A,&B OR INCR &A , &B
                String[] parts = header.split("\\s+", 2);
                String macroName = parts[0].trim();
                String paramsPart = (parts.length > 1) ? parts[1].trim() : "";
                // Normalize params: split by comma, trim and keep leading & if present
                String[] params;
                if (paramsPart.isEmpty()) params = new String[0];
                else params = Arrays.stream(paramsPart.split(","))
                                   .map(String::trim)
                                   .filter(s -> !s.isEmpty())
                                   .toArray(String[]::new);

                // Build parameter name -> positional index map (&A -> 1)
                Map<String, String> paramMap = new HashMap<>();
                for (int p = 0; p < params.length; p++) {
                    String pname = params[p];
                    // keep it in same form used in macro body: users usually write &A
                    paramMap.put(pname, "$" + (p + 1));
                }

                // Add MNT entry with current MDT index
                mnt.put(macroName, new MntEntry(macroName, currentMdtIndex, params.length));

                // Read macro body lines until MEND
                while (true) {
                    i++;
                    if (i >= source.size()) {
                        System.out.println("Error: macro " + macroName + " not terminated by MEND.");
                        break;
                    }
                    String bodyLine = source.get(i);
                    String trimmedBody = bodyLine.trim();
                    // store transformed body line in MDT unless it's MEND
                    if (trimmedBody.equalsIgnoreCase("MEND")) {
                        mdt.add("MEND"); // store MEND as part of MDT (conventional)
                        currentMdtIndex++; // increment index for MEND too
                        break;
                    } else {
                        // Replace all parameter occurrences (&A etc.) by positional $i
                        String replaced = bodyLine;
                        // To avoid partial matches, replace the exact parameter tokens.
                        // We will replace occurrences of param names as-is (like &A), and also the param name without & if user used that style.
                        for (Map.Entry<String, String> e : paramMap.entrySet()) {
                            String key = e.getKey();   // e.g. &A
                            String val = e.getValue(); // e.g. $1
                            // replace occurrences of key
                            replaced = replaced.replace(key, val);
                        }
                        // Add replaced line to MDT
                        mdt.add(replaced);
                        currentMdtIndex++;
                    }
                }
                // Done with macro, continue outer loop (macro def is NOT added to IC)
            } else {
                // Not a macro definition line: add to intermediate code
                intermediate.add(line);
            }
        }

        // Print MNT
        System.out.println("\n--- MNT (Macro Name Table) ---");
        System.out.printf("%-10s %-10s %-12s\n", "MacroName", "MDTIndex", "ParamCount");
        System.out.println("---------------------------------");
        for (MntEntry e : mnt.values()) {
            System.out.printf("%-10s %-10d %-12d\n", e.name, e.mdtIndex, e.paramCount);
        }

        // Print MDT (with indices starting at 1)
        System.out.println("\n--- MDT (Macro Definition Table) ---");
        System.out.printf("%-6s %s\n", "Index", "Definition");
        System.out.println("-------------------------------");
        for (int idx = 1; idx <= mdt.size(); idx++) {
            System.out.printf("%-6d %s\n", idx, mdt.get(idx - 1));
        }

        // Print Intermediate Code
        System.out.println("\n--- Intermediate Code (IC) ---");
        for (String icLine : intermediate) {
            System.out.println(icLine);
        }

        sc.close();
    }
}
