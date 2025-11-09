import java.util.*;
import java.io.*;

public class PS2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Symbol Table Input
        System.out.print("Enter number of symbols: ");
        int n = sc.nextInt();
        String[] symbol = new String[n];
        int[] address = new int[n];

        System.out.println("Enter Symbol and Address:");
        for (int i = 0; i < n; i++) {
            symbol[i] = sc.next();
            address[i] = sc.nextInt();
        }

        // Intermediate Code Input
        System.out.print("Enter number of Intermediate Code lines: ");
        int m = sc.nextInt();
        sc.nextLine(); // clear buffer

        String[] IC = new String[m];
        System.out.println("Enter Intermediate Code lines:");
        for (int i = 0; i < m; i++) {
            IC[i] = sc.nextLine();
        }

        System.out.println("\nGenerated Object Code:");
        System.out.println("----------------------");

        for (String line : IC) {
            // ensure separation between adjacent )(
            line = line.replace(")(", ") (");
            // remove parentheses
            line = line.replace("(", "").replace(")", "");
            String parts[] = line.trim().split("\\s+");
            if (parts.length == 0 || parts[0].isEmpty()) continue;

            String first = parts[0]; // e.g., IS,04 or AD,01
            String[] op = first.split(",");

            String type = op[0]; // IS / AD / DL
            int opcode = 0;
            try {
                opcode = Integer.parseInt(op[1]);
            } catch (Exception e) {
                // if no numeric opcode, skip line
                continue;
            }

            if (type.equals("AD")) {
                // No machine code output for assembler directives
                continue;
            }

            // Print opcode
            System.out.print(opcode + " ");

            // Register / R field
            int reg = 0;
            if (parts.length > 1) {
                String p1 = parts[1].trim();
                // p1 can be just a number like "1" (register) or like "S,1" sometimes
                if (isInteger(p1)) {
                    reg = Integer.parseInt(p1);
                } else if (p1.contains(",")) {
                    String[] op2 = p1.split(",");
                    // if format like "1,..." treat second as reg
                    if (op2.length > 1 && isInteger(op2[1])) {
                        reg = Integer.parseInt(op2[1]);
                    } else if (isInteger(op2[0])) {
                        reg = Integer.parseInt(op2[0]);
                    }
                }
            }
            System.out.print(reg + " ");

            // Operand / Address field
            int addrVal = 0;
            if (parts.length > 2) {
                String operand = parts[2].trim(); // e.g., S,1 or C,100
                String[] op3 = operand.split(",");
                if (op3[0].equals("S") && op3.length > 1 && isInteger(op3[1])) {
                    int symIndex = Integer.parseInt(op3[1]) - 1;
                    if (symIndex >= 0 && symIndex < address.length) {
                        addrVal = address[symIndex];
                    } else {
                        addrVal = 0;
                    }
                } else if (op3[0].equals("C") && op3.length > 1 && isInteger(op3[1])) {
                    addrVal = Integer.parseInt(op3[1]);
                } else if (isInteger(op3[0])) {
                    // sometimes operand might just be a number
                    addrVal = Integer.parseInt(op3[0]);
                }
            }
            System.out.print(addrVal);

            System.out.println();
        }

        sc.close();
    }

    private static boolean isInteger(String s) {
        if (s == null || s.isEmpty()) return false;
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
