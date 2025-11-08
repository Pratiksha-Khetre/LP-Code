import java.util.Scanner;

public class PS7 {

    // Function to print result table
    static void printTable(int[] process, int[] blockSize, int[] allocation, String title) {
        System.out.println("\n===== " + title + " Allocation =====");
        System.out.println("Process\tProcess Size\tBlock No\tBlock Size\tUnused Space");
        System.out.println("---------------------------------------------------------------");

        for (int i = 0; i < process.length; i++) {
            if (allocation[i] != -1) {
                int b = allocation[i];
                System.out.println("P" + (i + 1) + "\t\t" + process[i] + "\t\t" + (b + 1) +
                        "\t\t" + blockSize[b] + "\t\t" + (blockSize[b] - process[i]));
            } else {
                System.out.println("P" + (i + 1) + "\t\t" + process[i] + "\t\tNot Allocated\t  -\t\t  -");
            }
        }
    }

    // First Fit
    static void firstFit(int[] block, int[] process) {
        int[] allocation = new int[process.length];
        int[] b = block.clone();

        for (int i = 0; i < process.length; i++) {
            allocation[i] = -1;
            for (int j = 0; j < b.length; j++) {
                if (b[j] >= process[i]) {
                    allocation[i] = j;
                    b[j] -= process[i];
                    break;
                }
            }
        }

        printTable(process, block, allocation, "First Fit");
    }

    // Next Fit
    static void nextFit(int[] block, int[] process) {
        int[] allocation = new int[process.length];
        int[] b = block.clone();
        int lastPos = 0;

        for (int i = 0; i < process.length; i++) {
            allocation[i] = -1;
            for (int j = 0; j < b.length; j++) {
                int index = (lastPos + j) % b.length;
                if (b[index] >= process[i]) {
                    allocation[i] = index;
                    b[index] -= process[i];
                    lastPos = index;
                    break;
                }
            }
        }

        printTable(process, block, allocation, "Next Fit");
    }

    // Best Fit
    static void bestFit(int[] block, int[] process) {
        int[] allocation = new int[process.length];
        int[] b = block.clone();

        for (int i = 0; i < process.length; i++) {
            allocation[i] = -1;
            int best = -1;

            for (int j = 0; j < b.length; j++) {
                if (b[j] >= process[i]) {
                    if (best == -1 || b[j] < b[best]) {
                        best = j;
                    }
                }
            }

            if (best != -1) {
                allocation[i] = best;
                b[best] -= process[i];
            }
        }

        printTable(process, block, allocation, "Best Fit");
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of memory blocks: ");
        int nb = sc.nextInt();
        int[] block = new int[nb];

        System.out.println("Enter block sizes:");
        for (int i = 0; i < nb; i++) block[i] = sc.nextInt();

        System.out.print("Enter number of processes: ");
        int np = sc.nextInt();
        int[] process = new int[np];

        System.out.println("Enter process sizes:");
        for (int i = 0; i < np; i++) process[i] = sc.nextInt();

        // Perform all strategies
        firstFit(block, process);
        nextFit(block, process);
        bestFit(block, process);

        sc.close();
    }
}
