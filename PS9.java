import java.util.Arrays;
import java.util.Scanner;

public class PS9 {

    // Helper: print table header
    private static void printHeader(int frames) {
        System.out.print("Ref |");
        for (int i = 1; i <= frames; i++) System.out.printf(" Frame%-2d |", i);
        System.out.println("  Status");
        System.out.println("----" + "-".repeat(frames * 10) + "--------");
    }

    // Helper: print current frame state and Hit/Fault
    private static void printState(int page, int[] mem, boolean hit) {
        System.out.printf("%3d |", page);
        for (int v : mem) System.out.printf("   %s   |", v == -1 ? "-" : Integer.toString(v));
        System.out.println(hit ? "   Hit" : "  Fault");
    }

    // FIFO implementation
    public static void fifo(int[] ref, int frames) {
        int[] mem = new int[frames];
        Arrays.fill(mem, -1);
        int pointer = 0;
        int hit = 0, fault = 0;

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

    // LRU implementation
    public static void lru(int[] ref, int frames) {
        int[] mem = new int[frames];
        Arrays.fill(mem, -1);
        int[] lastUsed = new int[frames];
        Arrays.fill(lastUsed, -1);

        int time = 0, hit = 0, fault = 0;

        System.out.println("\n=== LRU (frames = " + frames + ") ===");
        printHeader(frames);

        for (int page : ref) {
            boolean found = false;

            for (int i = 0; i < frames; i++) {
                if (mem[i] == page) {
                    found = true;
                    hit++;
                    lastUsed[i] = time;
                    break;
                }
            }

            if (!found) {
                fault++;

                int emptyIdx = -1;
                for (int i = 0; i < frames; i++) {
                    if (mem[i] == -1) { emptyIdx = i; break; }
                }

                if (emptyIdx != -1) {
                    mem[emptyIdx] = page;
                    lastUsed[emptyIdx] = time;
                } else {
                    int lruIndex = 0;
                    for (int i = 1; i < frames; i++)
                        if (lastUsed[i] < lastUsed[lruIndex])
                            lruIndex = i;

                    mem[lruIndex] = page;
                    lastUsed[lruIndex] = time;
                }
            }

            printState(page, mem, found);
            time++;
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
        lru(reference, frames);

        sc.close();
    }
}
