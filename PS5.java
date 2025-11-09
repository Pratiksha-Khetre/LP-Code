import java.util.Arrays;
import java.util.Scanner;

public class PS5 {

    private static void printResults(String title, int[] pid, int[] arrival, int[] burst, int[] priority, int[] completion) {
        int n = pid.length;
        int[] tat = new int[n];
        int[] wt = new int[n];
        double totalTAT = 0, totalWT = 0;

        System.out.println("\n=== " + title + " ===");
        if (priority != null) {
            System.out.printf("%4s %8s %8s %10s %12s %12s %12s\n", "PID", "Arrival", "Burst", "Priority", "Completion", "TAT", "Waiting");
            System.out.println("--------------------------------------------------------------------------");
        } else {
            System.out.printf("%4s %8s %8s %12s %12s %12s\n", "PID", "Arrival", "Burst", "Completion", "TAT", "Waiting");
            System.out.println("----------------------------------------------------------------");
        }

        for (int i = 0; i < n; i++) {
            tat[i] = completion[i] - arrival[i];
            wt[i] = tat[i] - burst[i];
            totalTAT += tat[i];
            totalWT += wt[i];

            if (priority != null) {
                System.out.printf("%4d %8d %8d %10d %12d %12d %12d\n",
                                  pid[i], arrival[i], burst[i], priority[i], completion[i], tat[i], wt[i]);
            } else {
                System.out.printf("%4d %8d %8d %12d %12d %12d\n",
                                  pid[i], arrival[i], burst[i], completion[i], tat[i], wt[i]);
            }
        }

        System.out.println("----------------------------------------------------------------");
        System.out.printf("Average Turnaround Time: %.2f\n", totalTAT / n);
        System.out.printf("Average Waiting Time   : %.2f\n", totalWT / n);
    }

    // FCFS scheduling
    private static int[] fcfsSchedule(int[] pid, int[] arrival, int[] burst) {
        int n = pid.length;
        int[] completion = new int[n];

        // create index order sorted by arrival then by PID
        Integer[] order = new Integer[n];
        for (int i = 0; i < n; i++) order[i] = i;
        Arrays.sort(order, (a, b) -> {
            if (arrival[a] != arrival[b]) return Integer.compare(arrival[a], arrival[b]);
            return Integer.compare(pid[a], pid[b]);
        });

        int time = 0;
        for (int idx : order) {
            if (time < arrival[idx]) time = arrival[idx]; // CPU idle until process arrives
            time += burst[idx];
            completion[idx] = time;
        }

        return completion;
    }

    // Priority (non-preemptive) scheduling: lower priority number = higher priority
    private static int[] priorityNonPreemptive(int[] pid, int[] arrival, int[] burst, int[] priority) {
        int n = pid.length;
        int[] completion = new int[n];
        boolean[] done = new boolean[n];
        int completed = 0;
        int time = 0;

        while (completed < n) {
            // find all arrived & not done processes; pick one with smallest priority value
            int chosen = -1;
            int bestPriority = Integer.MAX_VALUE;
            int bestArrival = Integer.MAX_VALUE; // tie-breaker: earlier arrival
            for (int i = 0; i < n; i++) {
                if (!done[i] && arrival[i] <= time) {
                    if (priority[i] < bestPriority ||
                        (priority[i] == bestPriority && arrival[i] < bestArrival)) {
                        bestPriority = priority[i];
                        bestArrival = arrival[i];
                        chosen = i;
                    }
                }
            }

            if (chosen == -1) {
                // No process arrived yet, advance time to next arrival
                int nextArrival = Integer.MAX_VALUE;
                for (int i = 0; i < n; i++) if (!done[i]) nextArrival = Math.min(nextArrival, arrival[i]);
                time = Math.max(time, nextArrival);
                continue;
            }

            // run chosen process to completion (non-preemptive)
            time += burst[chosen];
            completion[chosen] = time;
            done[chosen] = true;
            completed++;
        }

        return completion;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Chit No. 5 — CPU Scheduling (FCFS and Priority Non-preemptive)");
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        int[] pid = new int[n];
        int[] arrival = new int[n];
        int[] burst = new int[n];
        int[] priority = new int[n];

        System.out.println("Enter process details. For each process enter: ArrivalTime BurstTime Priority");
        System.out.println("Note: Lower numeric priority value means higher priority (1 = highest).");
        for (int i = 0; i < n; i++) {
            pid[i] = i + 1;
            System.out.print("P" + pid[i] + " -> ");
            arrival[i] = sc.nextInt();
            burst[i] = sc.nextInt();
            priority[i] = sc.nextInt();
        }

        // FCFS
        int[] completionFCFS = fcfsSchedule(pid, arrival, burst);
        printResults("FCFS (First-Come First-Serve)", pid, arrival, burst, null, completionFCFS);

        // Priority (Non-preemptive)
        int[] completionPriority = priorityNonPreemptive(pid, arrival, burst, priority);
        printResults("Priority (Non-Preemptive) - Lower number = higher priority", pid, arrival, burst, priority, completionPriority);

        sc.close();
    }
}