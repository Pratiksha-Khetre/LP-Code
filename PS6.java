import java.util.*;

public class PS6 {

    // Print table of results
    private static void printResults(String title, int[] pid, int[] arrival, int[] burst, int[] completion) {
        int n = pid.length;
        int[] tat = new int[n];
        int[] wt = new int[n];
        double totTAT = 0, totWT = 0;

        System.out.println("\n=== " + title + " ===");
        System.out.printf("%4s %8s %8s %12s %12s %12s\n", "PID", "Arrival", "Burst", "Completion", "TAT", "Waiting");
        System.out.println("---------------------------------------------------------------");

        for (int i = 0; i < n; i++) {
            tat[i] = completion[i] - arrival[i];
            wt[i] = tat[i] - burst[i];
            totTAT += tat[i];
            totWT += wt[i];
            System.out.printf("%4d %8d %8d %12d %12d %12d\n", pid[i], arrival[i], burst[i], completion[i], tat[i], wt[i]);
        }

        System.out.println("---------------------------------------------------------------");
        System.out.printf("Average Turnaround Time: %.2f\n", totTAT / n);
        System.out.printf("Average Waiting Time   : %.2f\n", totWT / n);
    }

    // SJF Preemptive (Shortest Remaining Time First)
    private static void sjfPreemptive(int[] pid, int[] arrival, int[] burst, int[] completionOut) {
        int n = pid.length;
        int[] remaining = Arrays.copyOf(burst, n);
        int completed = 0, time = 0;
        int[] completion = new int[n];
        boolean[] isCompleted = new boolean[n];

        while (completed < n) {
            // find process with minimum remaining time among arrived and not completed
            int idx = -1;
            int minRem = Integer.MAX_VALUE;
            for (int i = 0; i < n; i++) {
                if (arrival[i] <= time && !isCompleted[i] && remaining[i] < minRem && remaining[i] > 0) {
                    minRem = remaining[i];
                    idx = i;
                }
            }

            if (idx == -1) { // no process has arrived yet or all arrived finished
                time++;
            } else {
                // execute 1 unit
                remaining[idx]--;
                time++;

                if (remaining[idx] == 0) {
                    isCompleted[idx] = true;
                    completed++;
                    completion[idx] = time; // current time is completion time
                }
            }
        }

        // copy to output
        System.arraycopy(completion, 0, completionOut, 0, n);
    }

    // Round Robin (preemptive) with arrival times
    private static void roundRobin(int[] pid, int[] arrival, int[] burst, int timeQuantum, int[] completionOut) {
        int n = pid.length;
        int[] remaining = Arrays.copyOf(burst, n);
        int[] completion = new int[n];
        boolean[] added = new boolean[n]; // whether process has been added to queue
        Queue<Integer> q = new LinkedList<>();
        int time = 0;
        int completed = 0;

        // Add processes that arrive at time 0
        for (int i = 0; i < n; i++) {
            if (arrival[i] <= time && !added[i]) {
                q.add(i);
                added[i] = true;
            }
        }

        while (completed < n) {
            if (q.isEmpty()) {
                // no process ready; advance time to next arrival
                int nextArrival = Integer.MAX_VALUE;
                for (int i = 0; i < n; i++) if (!added[i]) nextArrival = Math.min(nextArrival, arrival[i]);
                time = nextArrival;
                for (int i = 0; i < n; i++) {
                    if (arrival[i] <= time && !added[i]) {
                        q.add(i);
                        added[i] = true;
                    }
                }
                continue;
            }

            int idx = q.poll();
            int exec = Math.min(timeQuantum, remaining[idx]);

            // run for exec time units; during execution, new arrivals are enqueued as they appear
            for (int t = 0; t < exec; t++) {
                time++;
                remaining[idx]--;

                // check arrivals at this time and enqueue them (if not already added)
                for (int j = 0; j < n; j++) {
                    if (!added[j] && arrival[j] <= time) {
                        q.add(j);
                        added[j] = true;
                    }
                }

                if (remaining[idx] == 0) break;
            }

            if (remaining[idx] == 0) {
                completion[idx] = time;
                completed++;
            } else {
                // still has remaining burst; push back to queue
                q.add(idx);
            }
        }

        System.arraycopy(completion, 0, completionOut, 0, n);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("CPU Scheduling Simulation: SJF (Preemptive) & Round Robin (Preemptive)");
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        int[] pid = new int[n];
        int[] arrival = new int[n];
        int[] burst = new int[n];

        System.out.println("Enter process details:");
        System.out.println("(For each process enter: ArrivalTime BurstTime). Example: 0 5");
        for (int i = 0; i < n; i++) {
            pid[i] = i + 1;
            System.out.print("P" + pid[i] + " -> ");
            arrival[i] = sc.nextInt();
            burst[i] = sc.nextInt();
        }

        // SJF Preemptive
        int[] completionSJF = new int[n];
        sjfPreemptive(pid, arrival, burst, completionSJF);
        printResults("SJF (Preemptive) Results", pid, arrival, burst, completionSJF);

        // Round Robin
        System.out.print("\nEnter Time Quantum for Round Robin: ");
        int tq = sc.nextInt();
        int[] completionRR = new int[n];
        roundRobin(pid, arrival, burst, tq, completionRR);
        printResults("Round Robin (Time Quantum = " + tq + ") Results", pid, arrival, burst, completionRR);

        sc.close();
    }
}
