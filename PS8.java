import java.util.Arrays;
import java.util.Scanner;

public class PS8 {

    // Parse a line like "100 500 200" or "100,500,200" into int[]
    private static int[] parseLineToIntArray(String line) {
        line = line.trim();
        if (line.isEmpty()) return new int[0];
        String[] parts = line.split("[,\\s]+"); // split on commas or whitespace
        int[] arr = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            arr[i] = Integer.parseInt(parts[i].trim());
        }
        return arr;
    }

    // Helper to print results in a table
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
        int[] blocks = Arrays.copyOf(origBlocks, origBlocks.length);
        int[] allocatedToBlock = new int[blocks.length];
        int[] leftover = new int[blocks.length];
        Arrays.fill(allocatedToBlock, -1);

        for (int p = 0; p < processes.length; p++) {
            int size = processes[p];
            for (int b = 0; b < blocks.length; b++) {
                if (blocks[b] >= size) {
                    blocks[b] -= size;
                    allocatedToBlock[b] = p + 1; // store process number (1-based)
                    break;
                }
            }
        }

        for (int i = 0; i < blocks.length; i++) leftover[i] = blocks[i];
        printTable("First Fit", origBlocks, allocatedToBlock, leftover);
    }

    // Next Fit
    private static void nextFit(int[] origBlocks, int[] processes) {
        int[] blocks = Arrays.copyOf(origBlocks, origBlocks.length);
        int[] allocatedToBlock = new int[blocks.length];
        int[] leftover = new int[blocks.length];
        Arrays.fill(allocatedToBlock, -1);

        int startIndex = 0; // pointer where we last allocated
        for (int p = 0; p < processes.length; p++) {
            int size = processes[p];
            boolean allocated = false;
            int b = startIndex;
            int tried = 0;
            while (tried < blocks.length) {
                if (blocks[b] >= size) {
                    blocks[b] -= size;
                    allocatedToBlock[b] = p + 1;
                    startIndex = b; // next search starts from here
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

    // Worst Fit
    private static void worstFit(int[] origBlocks, int[] processes) {
        int[] blocks = Arrays.copyOf(origBlocks, origBlocks.length);
        int[] allocatedToBlock = new int[blocks.length];
        int[] leftover = new int[blocks.length];
        Arrays.fill(allocatedToBlock, -1);

        for (int p = 0; p < processes.length; p++) {
            int size = processes[p];
            int worstIndex = -1;
            int worstSize = -1;
            for (int b = 0; b < blocks.length; b++) {
                if (blocks[b] >= size && blocks[b] > worstSize) {
                    worstSize = blocks[b];
                    worstIndex = b;
                }
            }
            if (worstIndex != -1) {
                blocks[worstIndex] -= size;
                allocatedToBlock[worstIndex] = p + 1;
            }
        }

        for (int i = 0; i < blocks.length; i++) leftover[i] = blocks[i];
        printTable("Worst Fit", origBlocks, allocatedToBlock, leftover);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter memory block sizes (space or comma separated). Example: 100 500 200 300 600");
        System.out.print("Blocks: ");
        String blocksLine = sc.nextLine();
        int[] memoryBlocks = parseLineToIntArray(blocksLine);

        System.out.println("Enter process sizes (space or comma separated). Example: 212 417 112 426");
        System.out.print("Processes: ");
        String processesLine = sc.nextLine();
        int[] processSizes = parseLineToIntArray(processesLine);

        if (memoryBlocks.length == 0 || processSizes.length == 0) {
            System.out.println("Error: Need at least one block and one process.");
            sc.close();
            return;
        }

        System.out.println("\nMemory blocks: " + Arrays.toString(memoryBlocks));
        System.out.println("Process sizes: " + Arrays.toString(processSizes));

        firstFit(memoryBlocks, processSizes);
        nextFit(memoryBlocks, processSizes);
        worstFit(memoryBlocks, processSizes);

        sc.close();
    }
}
