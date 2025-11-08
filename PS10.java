import java.util.Arrays;
import java.util.Scanner;

public class PS10 {

    // Print header helper
    private static void printHeader(int frames) {
        System.out.print("Ref |");
        for (int i = 1; i <= frames; i++) System.out.printf(" Frame%-2d |", i);
        System.out.println("  Status");
        System.out.println("----" + "-".repeat(frames * 10) + "--------");
    }

    // Print current memory state and status
    private static void printState(int page, int[] mem, boolean hit) {
        System.out.printf("%3d |", page);
        for (int v : mem) System.out.printf("   %s   |", v == -1 ? "-" : Integer.toString(v));
        System.out.println(hit ? "   Hit" : "  Fault");
    }

    // FIFO algorithm
    public static void fifo(int[] ref, int frames) {
        int[] mem = new int[frames];
        Arrays.fill(mem, -1);
        int pointer = 0, hit = 0, fault = 0;

        System.out.println("\n=== FIFO (frames = " + frames + ") ===");
        printHeader(frames);

        for (int page : ref) {
            boolean found = false;
            for (int i = 0; i < frames; i++) {
                if (mem[i] == page) { found = true; hit++; break; }
            }

            if (!found) {
                mem[pointer] = page;
                pointer = (pointer + 1) % frames;
                fault++;
            }

            printState(page, mem, found);
        }

        System.out.println("---- Results ----");
        System.out.println("Total Hits  : " + hit);
        System.out.println("Total Faults: " + fault);
    }

    // Optimal algorithm
    public static void optimal(int[] ref, int frames) {
        int[] mem = new int[frames];
        Arrays.fill(mem, -1);
        int hit = 0, fault = 0;

        System.out.println("\n=== Optimal (frames = " + frames + ") ===");
        printHeader(frames);

        for (int i = 0; i < ref.length; i++) {
            int page = ref[i];
            boolean found = false;

            // Check for hit
            for (int j = 0; j < frames; j++) {
                if (mem[j] == page) { found = true; hit++; break; }
            }

            if (!found) {
                fault++;

                // If free frame exists
                int emptyIdx = -1;
                for (int j = 0; j < frames; j++) {
                    if (mem[j] == -1) { emptyIdx = j; break; }
                }

                if (emptyIdx != -1) {
                    mem[emptyIdx] = page;
                } else {
                    int replaceIdx = -1, farthest = -1;

                    for (int j = 0; j < frames; j++) {
                        int nextUse = Integer.MAX_VALUE;
                        for (int k = i + 1; k < ref.length; k++) {
                            if (mem[j] == ref[k]) { nextUse = k; break; }
                        }
                        if (nextUse > farthest) {
                            farthest = nextUse;
                            replaceIdx = j;
                        }
                    }
                    mem[replaceIdx] = page;
                }
            }

            printState(page, mem, found);
        }

        System.out.println("---- Results ----");
        System.out.println("Total Hits  : " + hit);
        System.out.println("Total Faults: " + fault);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of frames: ");
        int frames = sc.nextInt();

        System.out.print("Enter length of reference string: ");
        int n = sc.nextInt();

        int[] reference = new int[n];
        System.out.println("Enter reference string elements:");
        for (int i = 0; i < n; i++) {
            reference[i] = sc.nextInt();
        }

        fifo(reference, frames);
        optimal(reference, frames);

        sc.close();
    }
}
