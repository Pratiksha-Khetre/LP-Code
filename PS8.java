import java.util.Scanner;

public class PS8 {
    public PS8() {
    }

    static void printTable(int[] procSizes, int[] origBlocks, int[] allocation, String title) {
        System.out.println("\n===== " + title + " Allocation =====");
        System.out.println("Process\tProcess Size\tBlock No\tBlock Size\tUnused Space");
        System.out.println("---------------------------------------------------------------");

        for (int i = 0; i < procSizes.length; i++) {
            if (allocation[i] != -1) {
                int b = allocation[i];
                System.out.println("P" + (i + 1) + "\t\t" + procSizes[i] + "\t\t" + (b + 1) + "\t\t" + origBlocks[b] + "\t\t" + (origBlocks[b] - procSizes[i]));
            } else {
                System.out.println("P" + (i + 1) + "\t\t" + procSizes[i] + "\t\tNot Allocated\t  -\t\t  -");
            }
        }
    }

    static void firstFit(int[] origBlocks, int[] procSizes) {
        int[] allocation = new int[procSizes.length];
        int[] blocks = origBlocks.clone();

        for (int i = 0; i < procSizes.length; i++) {
            allocation[i] = -1;
            for (int b = 0; b < blocks.length; b++) {
                if (blocks[b] >= procSizes[i]) {
                    allocation[i] = b;
                    blocks[b] -= procSizes[i];
                    break;
                }
            }
        }

        printTable(procSizes, origBlocks, allocation, "First Fit");
    }

    static void nextFit(int[] origBlocks, int[] procSizes) {
        int[] allocation = new int[procSizes.length];
        int[] blocks = origBlocks.clone();
        int last = 0;

        for (int i = 0; i < procSizes.length; i++) {
            allocation[i] = -1;
            int n = blocks.length;
            for (int k = 0; k < n; k++) {
                int idx = (last + k) % n;
                if (blocks[idx] >= procSizes[i]) {
                    allocation[i] = idx;
                    blocks[idx] -= procSizes[i];
                    last = idx; // next search starts from here
                    break;
                }
            }
        }

        printTable(procSizes, origBlocks, allocation, "Next Fit");
    }

    static void worstFit(int[] origBlocks, int[] procSizes) {
        int[] allocation = new int[procSizes.length];
        int[] blocks = origBlocks.clone();

        for (int i = 0; i < procSizes.length; i++) {
            allocation[i] = -1;
            int worstIdx = -1;
            int worstSize = -1;
            for (int b = 0; b < blocks.length; b++) {
                if (blocks[b] >= procSizes[i] && blocks[b] > worstSize) {
                    worstSize = blocks[b];
                    worstIdx = b;
                }
            }
            if (worstIdx != -1) {
                allocation[i] = worstIdx;
                blocks[worstIdx] -= procSizes[i];
            }
        }

        printTable(procSizes, origBlocks, allocation, "Worst Fit");
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of memory blocks: ");
        if (!sc.hasNextInt()) {
            System.out.println("Invalid input. Exiting.");
            sc.close();
            return;
        }
        int nb = sc.nextInt();
        if (nb <= 0) {
            System.out.println("Need at least one block. Exiting.");
            sc.close();
            return;
        }
        int[] blocks = new int[nb];
        System.out.println("Enter block sizes:");
        for (int i = 0; i < nb; i++) {
            blocks[i] = sc.nextInt();
        }

        System.out.print("Enter number of processes: ");
        if (!sc.hasNextInt()) {
            System.out.println("Invalid input. Exiting.");
            sc.close();
            return;
        }
        int np = sc.nextInt();
        if (np <= 0) {
            System.out.println("Need at least one process. Exiting.");
            sc.close();
            return;
        }
        int[] procs = new int[np];
        System.out.println("Enter process sizes:");
        for (int i = 0; i < np; i++) {
            procs[i] = sc.nextInt();
        }

        firstFit(blocks, procs);
        nextFit(blocks, procs);
        worstFit(blocks, procs);

        sc.close();
    }
}


