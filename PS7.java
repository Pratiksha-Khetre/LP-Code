import java.util.Arrays;
import java.util.Scanner;

public class PS7 {

    // Helper to print results in a table (per-block)
    private static void printTable(String title, int[] originalBlocks, int[] allocatedProcessIndex, int[] leftover) {
        System.out.println("\n=== " + title + " ===");
        System.out.printf("%-8s %-12s %-12s %-12s\n", "Block#", "BlockSize", "Process", "Unused");
        System.out.println("-------------------------------------------------");
        for (int i = 0; i < originalBlocks.length; i++) {
            String proc = (allocatedProcessIndex[i] == -1) ? "-" : "P" + allocatedProcessIndex[i];
            System.out.printf("%-8d %-12d %-12s %-12d\n", (i + 1), originalBlocks[i], proc, leftover[i]);
        }
    }

    // First Fit
    private static void firstFit(int[] origBlocks, int[] processes) {
        int[] blocks = Arrays.copyOf(origBlocks, origBlocks.length); // available free space
        int[] allocatedToBlock = new int[blocks.length]; // store last process index (1-based) that used the block
        int[] leftover = new int[blocks.length];
        Arrays.fill(allocatedToBlock, -1);

        for (int p = 0; p < processes.length; p++) {
            int size = processes[p];
            for (int b = 0; b < blocks.length; b++) {
                if (blocks[b] >= size) {
                    blocks[b] -= size;
                    allocatedToBlock[b] = p + 1; // process number (1-based)
                    break;
                }
            }
        }

        for (int i = 0; i < blocks.length; i++) leftover[i] = blocks[i];
        printTable("First Fit", origBlocks, allocatedToBlock, leftover);
    }

    // Best Fit
    private static void bestFit(int[] origBlocks, int[] processes) {
        int[] blocks = Arrays.copyOf(origBlocks, origBlocks.length); // available free space
        int[] allocatedToBlock = new int[blocks.length];
        int[] leftover = new int[blocks.length];
        Arrays.fill(allocatedToBlock, -1);

        for (int p = 0; p < processes.length; p++) {
            int size = processes[p];
            int bestIndex = -1;
            int bestRemain = Integer.MAX_VALUE;
            for (int b = 0; b < blocks.length; b++) {
                if (blocks[b] >= size) {
                    int remain = blocks[b] - size;
                    if (remain < bestRemain) {
                        bestRemain = remain;
                        bestIndex = b;
                    }
                }
            }
            if (bestIndex != -1) {
                blocks[bestIndex] -= size;
                allocatedToBlock[bestIndex] = p + 1;
            }
        }

        for (int i = 0; i < blocks.length; i++) leftover[i] = blocks[i];
        printTable("Best Fit", origBlocks, allocatedToBlock, leftover);
    }

    // Next Fit
    private static void nextFit(int[] origBlocks, int[] processes) {
        int[] blocks = Arrays.copyOf(origBlocks, origBlocks.length);
        int[] allocatedToBlock = new int[blocks.length];
        int[] leftover = new int[blocks.length];
        Arrays.fill(allocatedToBlock, -1);

        int startIndex = 0; // pointer where we last allocated (search will start here)
        for (int p = 0; p < processes.length; p++) {
            int size = processes[p];
            boolean allocated = false;
            int b = startIndex;
            int tried = 0;
            while (tried < blocks.length) {
                if (blocks[b] >= size) {
                    blocks[b] -= size;
                    allocatedToBlock[b] = p + 1;
                    startIndex = b; // next search starts from this block
                    allocated = true;
                    break;
                }
                b = (b + 1) % blocks.length;
                tried++;
            }
            // if not allocated, process remains unallocated
        }

        for (int i = 0; i < blocks.length; i++) leftover[i] = blocks[i];
        printTable("Next Fit", origBlocks, allocatedToBlock, leftover);
    }

    // Main: read counts first and fill arrays with for-loops
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Input blocks
        System.out.print("Enter number of memory blocks: ");
        if (!sc.hasNextInt()) {
            System.out.println("Invalid input. Exiting.");
            sc.close();
            return;
        }
        int n = sc.nextInt();
        if (n <= 0) {
            System.out.println("Need at least one block. Exiting.");
            sc.close();
            return;
        }
        int[] memoryBlocks = new int[n];

        System.out.println("Enter sizes of " + n + " memory blocks (one by one):");
        for (int i = 0; i < n; i++) {
            System.out.print("Block " + (i + 1) + ": ");
            if (!sc.hasNextInt()) {
                System.out.println("Invalid input. Exiting.");
                sc.close();
                return;
            }
            memoryBlocks[i] = sc.nextInt();
            if (memoryBlocks[i] < 0) {
                System.out.println("Block size must be non-negative. Exiting.");
                sc.close();
                return;
            }
        }

        // Input processes
        System.out.print("\nEnter number of processes: ");
        if (!sc.hasNextInt()) {
            System.out.println("Invalid input. Exiting.");
            sc.close();
            return;
        }
        int m = sc.nextInt();
        if (m <= 0) {
            System.out.println("Need at least one process. Exiting.");
            sc.close();
            return;
        }
        int[] processSizes = new int[m];

        System.out.println("Enter sizes of " + m + " processes (one by one):");
        for (int i = 0; i < m; i++) {
            System.out.print("Process " + (i + 1) + ": ");
            if (!sc.hasNextInt()) {
                System.out.println("Invalid input. Exiting.");
                sc.close();
                return;
            }
            processSizes[i] = sc.nextInt();
            if (processSizes[i] < 0) {
                System.out.println("Process size must be non-negative. Exiting.");
                sc.close();
                return;
            }
        }

        System.out.println("\nMemory blocks: " + Arrays.toString(memoryBlocks));
        System.out.println("Process sizes: " + Arrays.toString(processSizes));

        // Call allocation algorithms
        bestFit(memoryBlocks, processSizes);
        firstFit(memoryBlocks, processSizes);
        nextFit(memoryBlocks, processSizes);

        sc.close();
    }
}
