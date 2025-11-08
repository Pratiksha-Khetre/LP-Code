import java.util.*;

public class PS1 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Opcode Table
        Map<String, String> opcodeTable = new HashMap<>();
        opcodeTable.put("START", "AD,01");
        opcodeTable.put("END", "AD,02");
        opcodeTable.put("MOVER", "IS,01");
        opcodeTable.put("MOVEM", "IS,02");
        opcodeTable.put("ADD", "IS,03");
        opcodeTable.put("SUB", "IS,04");
        opcodeTable.put("MULT", "IS,05");
        opcodeTable.put("DIV", "IS,06");
        opcodeTable.put("DC", "DL,01");
        opcodeTable.put("DS", "DL,02");

        // Register Table
        Map<String, Integer> registerTable = new HashMap<>();
        registerTable.put("AREG", 1);
        registerTable.put("BREG", 2);
        registerTable.put("CREG", 3);
        registerTable.put("DREG", 4);

        // Read Assembly Code
        System.out.println("Enter assembly code (empty line to finish):");
        List<String> asmCode = new ArrayList<>();
        while (true) {
            String line = sc.nextLine().trim();
            if (line.isEmpty()) break;
            asmCode.add(line);
        }

        // Pass I: Symbol Table, Literal Table, Intermediate Code
        Map<String, Integer> symbolTable = new LinkedHashMap<>();
        Map<String, Integer> literalTable = new LinkedHashMap<>();
        List<String> literalPool = new ArrayList<>();
        List<String> intermediateCode = new ArrayList<>();

        int locCounter = 0;

        for (String line : asmCode) {
            if (line.isEmpty()) continue;

            String[] parts = line.split("\\s+");
            if (parts.length == 0) continue;

            // Handle START
            if (parts[0].equals("START")) {
                if (parts.length > 1) locCounter = Integer.parseInt(parts[1]);
                intermediateCode.add(locCounter + " (AD,01) START " + (parts.length > 1 ? parts[1] : ""));
                continue;
            }

            // Handle END
            if (parts[0].equals("END")) {
                for (String lit : literalPool) {
                    literalTable.put(lit, locCounter++);
                }
                intermediateCode.add(locCounter + " (AD,02) END");
                break;
            }

            String label = null, opcode = null, operand = null;

            if (!opcodeTable.containsKey(parts[0])) { // Label exists
                label = parts[0];
                symbolTable.put(label, locCounter);
                if (parts.length > 1) opcode = parts[1];
                if (parts.length > 2) operand = parts[2];
            } else { // No label
                opcode = parts[0];
                if (parts.length > 1) operand = parts[1];
            }

            // Handle literals
            if (operand != null && operand.startsWith("='")) {
                literalPool.add(operand);
            }

            // Generate Intermediate Code
            String ic = locCounter + " (" + (opcode != null && opcodeTable.containsKey(opcode) ? opcodeTable.get(opcode) : "") + ") " + (opcode != null ? opcode : "");
            if (operand != null) {
                if (operand.contains(",")) {
                    String[] opParts = operand.split(",");
                    String reg = registerTable.getOrDefault(opParts[0].trim(), 0) + "";
                    String sym = opParts[1].trim();
                    ic += " " + reg + " " + sym;
                } else {
                    ic += " " + operand;
                }
            }
            intermediateCode.add(ic);
            locCounter++;
        }

        // Display Symbol Table
        System.out.println("\nSymbol Table:");
        System.out.println("Symbol\tAddress");
        for (Map.Entry<String, Integer> e : symbolTable.entrySet()) {
            System.out.println(e.getKey() + "\t" + e.getValue());
        }

        // Display Literal Table
        System.out.println("\nLiteral Table:");
        System.out.println("Literal\tAddress");
        for (Map.Entry<String, Integer> e : literalTable.entrySet()) {
            System.out.println(e.getKey() + "\t" + e.getValue());
        }

        // Display Intermediate Code
        System.out.println("\nIntermediate Code:");
        for (String ic : intermediateCode) {
            System.out.println(ic);
        }

        sc.close();
    }
}
