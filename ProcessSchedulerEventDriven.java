import java.util.*;

class Event { //this class stores the state of the upcoming process
    enum EventType {
        ARRIVAL, COMPLETION
    }

    int time;
    EventType type;
    Process process;

    public Event(int time, EventType type, Process process) {
        this.time = time;
        this.type = type;
        this.process = process;
    }
}

class Process { //this is the process class that stores the process information
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

    public void setCompletionTime(int time) {
        this.completionTime = time;
        this.turnaroundTime = completionTime - arrivalTime;
        this.waitingTime = turnaroundTime - burstTime;
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
            System.out.println("P" + process.processID + ": Arrival time = " + process.arrivalTime + ", Burst time = "
                    + process.burstTime + " ms");
        }
        System.out.println("Scheduling Algorithm: Shortest Remaining Time First \nContext switch: " + CS + " ms");
    }

 	 public static void scheduleProcesses(Process[] processes) { // this method schedules processes using shortest
         // remaining time

PriorityQueue<Process> remainQueue = new PriorityQueue<>(Comparator
.comparingInt((Process proc) -> proc.remainingTime).thenComparingInt(proc -> proc.arrivalTime)); // this sorts the priority queue based on
                                                          // remaining time and if the remaining time
                                                          // is equal then FIFO

PriorityQueue<Event> eventQueue = new PriorityQueue<>(Comparator.comparingInt((Event e)-> e.time).thenComparingInt( e -> e.process.remainingTime));  // time in which
                                              // the event occurred

int time = 0, totalExecutionTime = 0;
int totalWaitingTime = 0, totalTurnaroundTime = 0;
int startTime = 0;
Process lastProcess = null;

for (Process pro : processes) { // adds arriving proccesses to event queue
eventQueue.add(new Event(pro.arrivalTime, Event.EventType.ARRIVAL, pro));
}

Event event = eventQueue.peek();

while (event.type == Event.EventType.ARRIVAL && event.time > time) {// set start time to the first process
                     // arrival time
time++;
startTime++;
}

System.out.println("\nTime\tProcess/CS");

while (!eventQueue.isEmpty()) {
event = eventQueue.poll();
Event event2 = eventQueue.peek();
if (event.type == Event.EventType.ARRIVAL) { 
	if(event2.type == Event.EventType.ARRIVAL && event2.time == event.time){
		eventQueue.poll();
		remainQueue.add(event.process);
		remainQueue.add(event2.process);// adds event to remain queue
	}
remainQueue.add(event.process);
}

while (!remainQueue.isEmpty()) {
Process curProcess = remainQueue.peek();

if (lastProcess != null && curProcess != lastProcess) { // when the code changes the process, a context
                 // switch occurs
System.out.printf("%d-%d\tCS\n", time, time + CS);
time += CS;

}

if (lastProcess != null && curProcess != lastProcess) // change start time when process changes
startTime = time;

lastProcess = curProcess;

while (curProcess.remainingTime > 0) { // if current process has remaining time, we execute it until an
// event occurs, and then we compare the upcoming event with the
// current process then we start executing the heighest priority
// process
curProcess.remainingTime--;
time++;
totalExecutionTime++;

Event nextEvent = eventQueue.peek();
if (nextEvent != null && nextEvent.type == Event.EventType.ARRIVAL && nextEvent.time <= time) {
eventQueue.poll();
remainQueue.add(nextEvent.process);
if (remainQueue.peek().processID != curProcess.processID && curProcess.remainingTime != 0) 
System.out.printf("%d-%d\tP%d\n", startTime, time, curProcess.processID);
break;
}
}

if (curProcess.remainingTime == 0) { // when the process is completed, we change its state (event) and
// remove it from remainqueue
curProcess.setCompletionTime(time);

totalWaitingTime += curProcess.waitingTime;
totalTurnaroundTime += curProcess.turnaroundTime;
remainQueue.poll();
eventQueue.add(new Event(curProcess.completionTime, Event.EventType.COMPLETION, curProcess));
System.out.printf("%d-%d\tP%d\n", startTime, time, curProcess.processID);
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
}
