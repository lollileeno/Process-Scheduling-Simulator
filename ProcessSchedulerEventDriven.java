import java.util.*;

class Event {
    enum EventType { ARRIVAL, COMPLETION }
    int time;
    EventType type;
    Process process;

    public Event(int time, EventType type, Process process) {
        this.time = time;
        this.type = type;
        this.process = process;
    }
}

class Process {
    int processID;
    public int arrivalTime;
    int burstTime;
    int completionTime;
    int waitingTime;
    int turnaroundTime;
    public int remainingTime;

    public Process(int processID, int arrivalTime, int burstTime) {
        this.processID = processID;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
    }

    public void setCompletionTime(int end) {
        completionTime = end;
    }

    public void calculateTurnaroundTime() {
        turnaroundTime = completionTime - arrivalTime;
    }

    public void calculateWaitingTime() {
        waitingTime = turnaroundTime - burstTime;
    }
}

public class ProcessSchedulerEventDriven {
    static final int CS = 1; // Context Switch Time (1 ms)

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int pNum;

        System.out.println("Welcome to CSC227 platform");

        while (true) {
            System.out.println("Enter the number of your processes: ");
            if (input.hasNextInt()) {
                pNum = input.nextInt();
                if (pNum > 0) {
                    break;
                }
            } else {
                input.next();
            }
            System.out.println("Invalid input. Please enter a positive integer.");
        }

        Process[] processes = new Process[pNum];
        for (int i = 1; i <= pNum; i++) {
            System.out.printf("Enter the arrival time of P%d: \n", i);
            int arriveT = input.nextInt();
            System.out.printf("Enter the burst time of P%d: \n", i);
            int burstT = input.nextInt();
            processes[i - 1] = new Process(i, arriveT, burstT);
        }

        input.close(); // Close scanner to prevent resource leaks

        displayInfo(processes);
        scheduleProcesses(processes);
    }

    public static void displayInfo(Process[] processes) {
        System.out.print("Number of processes = " + processes.length + " (");
        for (int j = 0; j < processes.length; j++) {
            System.out.print("P" + processes[j].processID);
            if (j < processes.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println(")");
        System.out.println("Arrival times and burst times as follows: ");
        for (Process process : processes) {
            System.out.println("P" + process.processID + ": Arrival time = " + process.arrivalTime +
                    ", Burst time = " + process.burstTime + " ms");
        }
        System.out.println("Scheduling Algorithm: Shortest Remaining Time First \nContext switch: " + CS + " ms");
    }

    public static void scheduleProcesses(Process[] processes) {
        Arrays.sort(processes, Comparator.comparingInt(proc -> proc.arrivalTime));

        PriorityQueue<Process> remainQueue = new PriorityQueue<>(
                Comparator.comparingInt((Process proc) -> proc.remainingTime)
                        .thenComparingInt(proc -> proc.arrivalTime));

        PriorityQueue<Event> eventQueue = new PriorityQueue<>(Comparator.comparingInt(e -> e.time));

        int time = 0, completed = 0, totalExecutionTime = 0;
        int totalWaitingTime = 0, totalTurnaroundTime = 0;
        int startTime = 0;
        Process lastProcess = null;

        for (Process pro : processes) {
            eventQueue.add(new Event(pro.arrivalTime, Event.EventType.ARRIVAL, pro));
        }
        
        Event event = eventQueue.peek();
        while (event.type == Event.EventType.ARRIVAL && event.time > time) {
            time++;
            startTime++;
            }

        System.out.println("\nTime\tProcess/CS");

        while (!eventQueue.isEmpty()) {
            event = eventQueue.poll();
            if (event.type == Event.EventType.ARRIVAL) {
                remainQueue.add(event.process);
            }

            while (!remainQueue.isEmpty()) {
                Process nextProcess = remainQueue.peek();

                if (lastProcess != null && nextProcess != lastProcess) {
                    System.out.printf("%d-%d\tCS\n", time, time + CS);
                    time += CS;
                }


                if (lastProcess != null && nextProcess != lastProcess)
                    startTime = time;
                  
                lastProcess = nextProcess;
                

                while (nextProcess.remainingTime > 0) {
                    nextProcess.remainingTime--;
                    time++;
                    totalExecutionTime++;

                    Event nextEvent = eventQueue.peek();
                    if (nextEvent != null && nextEvent.type == Event.EventType.ARRIVAL && nextEvent.time <= time) {
                        eventQueue.poll();
                        remainQueue.add(nextEvent.process);
                        if(remainQueue.peek().processID != nextProcess.processID && nextProcess.remainingTime != 0)
                        System.out.printf("%d-%d\tP%d\n", startTime, time, nextProcess.processID);
                                                break;
                    }
                }

                if (nextProcess.remainingTime == 0) {
                    nextProcess.setCompletionTime(time);
                    nextProcess.calculateTurnaroundTime();
                    nextProcess.calculateWaitingTime();
                    totalWaitingTime += nextProcess.waitingTime;
                    totalTurnaroundTime += nextProcess.turnaroundTime;
                    completed++;
                    remainQueue.poll();
                    eventQueue.add(new Event(nextProcess.completionTime, Event.EventType.COMPLETION, nextProcess));
                    System.out.printf("%d-%d\tP%d\n", startTime, time, nextProcess.processID);
                } 
            }
        }

        double cpuUtilization = ((double) totalExecutionTime / time) * 100;

        System.out.println("\nPerformance Metrics");
        System.out.printf("Average Turnaround Time: %.2f\n", (double) totalTurnaroundTime / processes.length);
        System.out.printf("Average Waiting Time: %.2f\n", (double) totalWaitingTime / processes.length);
        System.out.printf("CPU Utilization: %.2f%%\n", cpuUtilization);
    }
}
