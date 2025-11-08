import java.util.*;

public class PS4 {

    static class MntEntry {
        String name;
        int mdtIndex;   // 1-based index into MDT list
        int paramCount;
        MntEntry(String n, int idx, int pc) { name = n; mdtIndex = idx; paramCount = pc; }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Macro Processor Pass II (positional params with $1, $2, ...)");
        System.out.println();

        // Read MNT
        System.out.print("Enter number of MNT entries: ");
        int mntCount = Integer.parseInt(sc.nextLine().trim());
        Map<String, MntEntry> mnt = new LinkedHashMap<>();
        System.out.println("Enter MNT entries (one per line) in format: MacroName MDTStartIndex ParamCount");
        System.out.println("Example: INCR 1 2");
        for (int i = 0; i < mntCount; i++) {
            String line = sc.nextLine().trim();
            if (line.isEmpty()) { i--; continue; }
            String[] parts = line.split("\\s+");
            if (parts.length < 3) {
                System.out.println("Invalid entry — try again.");
                i--; continue;
            }
            String name = parts[0];
            int start = Integer.parseInt(parts[1]);
            int pcount = Integer.parseInt(parts[2]);
            mnt.put(name, new MntEntry(name, start, pcount));
        }

        // Read MDT
        System.out.print("Enter number of MDT lines: ");
        int mdtCount = Integer.parseInt(sc.nextLine().trim());
        List<String> mdt = new ArrayList<>(mdtCount + 1);
        // 1-based indexing for convenience
        mdt.add(null);
        System.out.println("Enter MDT lines in order (use MEND to mark end of a macro definition).");
        for (int i = 1; i <= mdtCount; i++) {
            String l = sc.nextLine();
            mdt.add(l);
        }

        // Read Intermediate Code (IC)
        System.out.print("Enter number of Intermediate Code (IC) lines: ");
        int icCount = Integer.parseInt(sc.nextLine().trim());
        List<String> ic = new ArrayList<>();
        System.out.println("Enter IC lines (one per line). Macro calls should be like: MACRONAME arg1,arg2,...");
        for (int i = 0; i < icCount; i++) {
            ic.add(sc.nextLine());
        }

        // Process IC and expand macros
        System.out.println("\n=== Expanded Output (Pass II result) ===");
        for (String line : ic) {
            if (line == null || line.trim().isEmpty()) {
                System.out.println(line);
                continue;
            }
            String trimmed = line.trim();
            String[] tokens = trimmed.split("\\s+", 2);
            String first = tokens[0];
            if (mnt.containsKey(first)) {
                // Macro call
                MntEntry entry = mnt.get(first);
                String argsPart = tokens.length > 1 ? tokens[1].trim() : "";
                String[] actuals;
                if (argsPart.isEmpty()) actuals = new String[0];
                else actuals = Arrays.stream(argsPart.split(","))
                                     .map(String::trim).toArray(String[]::new);

                if (actuals.length < entry.paramCount) {
                    System.out.printf("// Warning: macro %s expects %d args but got %d. Missing args will be empty.\n",
                                       entry.name, entry.paramCount, actuals.length);
                }

                int idx = entry.mdtIndex;
                while (idx < mdt.size()) {
                    String mline = mdt.get(idx);
                    if (mline == null) { idx++; continue; }
                    if (mline.trim().equalsIgnoreCase("MEND")) break;

                    String expanded = mline;
                    // Replace $1, $2, ... with actuals (or empty string)
                    for (int p = 1; p <= entry.paramCount; p++) {
                        String placeholder = "$" + p;
                        String val = (p - 1 < actuals.length) ? actuals[p - 1] : "";
                        expanded = expanded.replace(placeholder, val);
                    }
                    // Remove any leftover $n placeholders (if any)
                    expanded = expanded.replaceAll("\\$\\d+", "");

                    System.out.println(expanded);
                    idx++;
                }
            } else {
                // Non-macro line: print as-is
                System.out.println(line);
            }
        }

        sc.close();
    }
}
