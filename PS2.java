import java.util.*;

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
            line = line.replace("(", "").replace(")", "");
            String parts[] = line.split(" ");

            String first = parts[0]; // e.g., IS,04 or AD,01
            String[] op = first.split(",");

            String type = op[0]; // IS / AD / DL
            int opcode = Integer.parseInt(op[1]); // opcode value

            if (type.equals("AD")) {
                continue; // No machine code output for directives
            }

            System.out.print(opcode + " ");

            if (parts.length > 1) {
                if (parts[1].contains(",")) {
                    String[] op2 = parts[1].split(",");
                    int reg = Integer.parseInt(op2[1]);
                    System.out.print(reg + " ");
                } else {
                    System.out.print("0 ");
                }
            } else {
                System.out.print("0 ");
            }

            if (parts.length > 2) {
                String operand = parts[2];
                String[] op3 = operand.split(",");
                int symIndex = Integer.parseInt(op3[1]) - 1;
                System.out.print(address[symIndex]);
            } else {
                System.out.print("0");
            }

            System.out.println();
        }
        sc.close();
    }
}
