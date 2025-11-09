import java.util.*;

public class Pass1 {

    static class Symbol {
        String name;
        int address;
        Symbol(String n, int a) { name = n; address = a; }
    }

    static class Literal {
        String literal;
        int address;
        Literal(String l, int a) { literal = l; address = a; }
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        List<String> code = new ArrayList<>();
        System.out.println("Enter Assembly Program (type END to finish input):");

        while (true) {
            String line = sc.nextLine().trim();
            code.add(line);
            if (line.equals("END")) break;
        }

        List<Symbol> SYMTAB = new ArrayList<>();
        List<Literal> LITTAB = new ArrayList<>();
        List<Integer> POOLTAB = new ArrayList<>();
        List<String> INTERMEDIATE = new ArrayList<>();

        int LC = 0;
        POOLTAB.add(0);   // first literal pool starts at index 0

        for (String line : code) {

            String[] parts = line.split("[ ,]+");
            String op = parts[0];

            // START
            if (op.equals("START")) {
                LC = Integer.parseInt(parts[1]);
                INTERMEDIATE.add("(AD,01) (C," + LC + ")");
            }

            // LTORG and END => process literals
            else if (op.equals("LTORG") || op.equals("END")) {
                INTERMEDIATE.add("(AD,02)");

                // allocate literals from current pool
                for (int i = POOLTAB.get(POOLTAB.size() - 1); i < LITTAB.size(); i++) {
                    LITTAB.get(i).address = LC;
                    LC++;
                }

                POOLTAB.add(LITTAB.size());
            }

            // DS/DC declaration
            else if (parts.length == 3 && (parts[1].equals("DS") || parts[1].equals("DC"))) {
                String symbol = parts[0];
                int size = Integer.parseInt(parts[2]);

                SYMTAB.add(new Symbol(symbol, LC));
                INTERMEDIATE.add("(DL," + (parts[1].equals("DS") ? "02" : "01") + ") (C," + size + ")");

                LC += size;
            }

            // Literal operand
            else if (line.contains("='")) {
                String literal = line.substring(line.indexOf("='"), line.indexOf("'") + 2);

                LITTAB.add(new Literal(literal, -1));
                INTERMEDIATE.add("(IS,XX) (L," + (LITTAB.size() - 1) + ")");

                LC++;
            }

            // Instruction with symbol operand
            else if (parts.length == 2 && Character.isLetter(parts[1].charAt(0))) {

                SYMTAB.add(new Symbol(parts[1], LC));
                INTERMEDIATE.add("(IS,XX) (S," + (SYMTAB.size() - 1) + ")");
                LC++;
            }

            // Simple instruction
            else {
                INTERMEDIATE.add("(IS,XX)");
                LC++;
            }
        }

        System.out.println("\n=== SYMBOL TABLE ===");
        System.out.println("Index\tSymbol\tAddress");
        for (int i = 0; i < SYMTAB.size(); i++) {
            Symbol s = SYMTAB.get(i);
            System.out.println(i + "\t" + s.name + "\t" + s.address);
        }

        System.out.println("\n=== LITERAL TABLE ===");
        System.out.println("Index\tLiteral\tAddress");
        for (int i = 0; i < LITTAB.size(); i++) {
            Literal l = LITTAB.get(i);
            System.out.println(i + "\t" + l.literal + "\t" + l.address);
        }

        System.out.println("\n=== POOL TABLE ===");
        for (int p : POOLTAB) {
            System.out.println(p);
        }

        System.out.println("\n=== INTERMEDIATE CODE ===");
        for (String ic : INTERMEDIATE) {
            System.out.println(ic);
        }

        sc.close();
    }
}
