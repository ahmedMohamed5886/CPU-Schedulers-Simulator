import java.util.*;
public class CPU_Schedulers_Simulator
{
    class RRScheduler {
        public static List<String> run(List<Process> processes, int quantum, int contextSwitch) {
            // Sort processes by arrival time
            processes.sort(Comparator.comparingInt(p -> p.arrivalTime));
            
            List<String> executeOrder = new ArrayList<>();
            Queue<Process> readyQueue = new LinkedList<>();
            
            int currentTime = 0;
            int completeProcess = 0;
            int numProcess = processes.size();

            // To Check if processes arrived
            boolean[] arrived = new boolean[numProcess];

            while(completeProcess < numProcess) {

                // Add all processes are arrived now
                for(int i = 0; i < numProcess; i++) {
                    Process p = processes.get(i);
                    if(!arrived[i] && p.arrivalTime <= currentTime) {
                        readyQueue.add(p);
                        arrived[i] = true;
                    }
                }

                // If no process is ready, jump to next arrival
                if(readyQueue.isEmpty()) {
                    currentTime++;
                    continue;
                }
                
                // Pick First process from ready queue
                Process currentProcessName = readyQueue.poll();

                executeOrder.add(currentProcessName.name);

                // Execute for quantum or remaining time
                int execTime = Math.min(quantum, currentProcessName.remainingTime);

                // Simualate execution
                for(int j = 0; j < execTime; j++) {
                    currentTime++;

                    // Check for newly arrived processes during execution
                    for(int i = 0; i < numProcess; i++) {
                        Process p = processes.get(i);
                        if(!arrived[i] && p.arrivalTime <= currentTime) {
                            readyQueue.add(p);
                            arrived[i] = true;
                        }
                    }
                }

                // Deduct executed time from remaining time
                currentProcessName.remainingTime -= execTime;

                // If process is completed
                if(currentProcessName.remainingTime == 0) {
                    completeProcess++;
                    currentProcessName.turnaroundTime = currentTime - currentProcessName.arrivalTime;
                    currentProcessName.waitingTime = currentProcessName.turnaroundTime - currentProcessName.burstTime;
                } else {
                    // Re-add to the end of the queue if not completed
                    readyQueue.add(currentProcessName);
                }

                // Add context switch time
                currentTime += contextSwitch;
            }
            return executeOrder;
        }
    }
    public static void main(String[] args)
    {

    }
}
public static void agSchedule(List<Process> list) {
        Queue<Process> queue = new LinkedList<>(list);
        int time = 0;

        System.out.println("\nAG Scheduling Execution Order:");

        while (!queue.isEmpty()) {
            Process p = queue.poll();

            if (p.remaining <= 0) continue;

            System.out.print("(" + p.name + " Q=" + p.quantum + ") ");

            int quarter = (int) Math.ceil(p.quantum * 0.25);
            int run = Math.min(quarter, p.remaining);

            time += run;
            p.remaining -= run;

            if (p.remaining == 0) {
                p.turnaround = time - p.arrival;
                p.waiting = p.turnaround - p.burst;
            } else {
                p.quantum += 2;
                queue.add(p);
            }
        }

        System.out.println();
}
