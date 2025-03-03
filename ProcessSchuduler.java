import java.util.*;
public class ProcessSchuduler {

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		 int pID = 0;
		 int arriveT;
		 int burstT;
	     int pNum;
	     System.out.println("Welcome to CSC227 platform\n"
	     		+ "Enter the number of your processes: ");
	     pNum = input.nextInt();
	     
	     Process [] p = new Process[pNum];
	     
	    for(int i = 1 ; i <= pNum; i++) {
	    	pID = i;
	    	System.out.printf("Enter the arrival time of P%d: \n",i);
	    	arriveT = input.nextInt();
	    	System.out.printf("Enter the burst time of P%d: \n",i);
	    	burstT = input.nextInt();
	    	p[i-1] = new Process(pID,arriveT,burstT);
	    	
	    	
	    	
	    }
	    SchuduleProcesses(p);
	 //FCFS
	    //SJRF	 

	}
public static int processingTime(Process [] p){
		int CS = 1;   //refer to context switch time
		
		int processing = 0;
		Process[] p2 = sortShortest(p);
		for(int i = 0 ; i<p2.length ; i++) {
			processing+=p2[i].burstTime + CS;
		}
		
		return processing;
	}

		public static Process[] sortShortest(Process[] p) {
	    Process[] p2 = Arrays.copyOf(p, p.length); // Copy original array to avoid modifying it
	    Arrays.sort(p2, Comparator.comparingInt(process -> process.burstTime)); // Sort by burst time
	    return p2;
	}

	public static Process[] sortArrival(Process[] p) {
	    Process[] p2 = Arrays.copyOf(p, p.length); // Copy original array to avoid modifying it
	    Arrays.sort(p2, Comparator.comparingInt(process -> process.arrivalTime)); // Sort by burst time
	    return p2;
	}
	
	public static void displayInfo(Process []p) {
		
		
	}

	public static void SchuduleProcesses(Process[] p) {
	    p = sortArrival(p); // Sort processes by arrival time

	    PriorityQueue<Process> remainQueue = new PriorityQueue<>(Comparator.comparingInt(proc -> proc.remainingTime));

	    int time = 0;
	    int completed = 0;
	    int totalExecutionTime = 0;
	    int totalWaitingTime = 0;
	    int totalTurnaroundTime = 0;
	    int contextSwitches = 0;
	    int i = 0;
	    Process currentProcess = null;
	    Process lastProcess = null; // Track the last executed process

	    System.out.println("\nTime  Process/CS");

	    while (completed < p.length) {
	        // Add processes that have arrived at this time
	        while (i < p.length && p[i].arrivalTime <= time) {
	            remainQueue.add(p[i]);
	            i++;
	        }

	        // Reinsert the running process if it was preempted
	        if (currentProcess != null && currentProcess.remainingTime > 0) {
	            remainQueue.add(currentProcess);
	        }

	        if (!remainQueue.isEmpty()) {
	            Process nextProcess = remainQueue.poll();

	            // 🔹 Context Switch if switching to a different process
	            if (lastProcess != null && nextProcess != lastProcess) {
	                System.out.printf("%d-%d  CS\n", time, time + 1);
	                time++; // 1ms context switch delay
	                contextSwitches++;
	            }
	            lastProcess = nextProcess; // Update lastProcess to the new one

	            int startTime = time;

	            // 🔹 Execute process 1ms at a time to allow preemptions
	            while (nextProcess.remainingTime > 0) {
	                nextProcess.remainingTime--; // Execute for 1ms
	                time++;
	                totalExecutionTime++;

	                // Add newly arriving processes
	                while (i < p.length && p[i].arrivalTime == time) {
	                    remainQueue.add(p[i]);
	                    i++;
	                }

	                // 🔹 Preempt immediately if a shorter job arrives
	                if (!remainQueue.isEmpty() && nextProcess.remainingTime > remainQueue.peek().remainingTime) {
	                    currentProcess = nextProcess;
	                    break; // Stop execution and allow preemption
	                }
	            }

	            System.out.printf("%d-%d P%d\n", startTime, time, nextProcess.processID);

	            if (nextProcess.remainingTime == 0) {
	                nextProcess.setCompletionTime(time);
	                nextProcess.calculateTurnaroundTime();
	                nextProcess.calculateWaitingTime();

	                totalWaitingTime += nextProcess.waitingTime;
	                totalTurnaroundTime += nextProcess.turnaroundTime;
	                completed++;
	                currentProcess = null; // Process finished
	            } else {
	                currentProcess = nextProcess; // Process was preempted
	            }
	        } else {
	            time++; // CPU idle time
	        }
	    }

	    double cpuUtilization = ((double) totalExecutionTime / time) * 100;

	    // 🔹 Print Performance Metrics
	    System.out.println("\nPerformance Metrics");
	    System.out.printf("Average Turnaround Time: %.2f\n", (double) totalTurnaroundTime / p.length);
	    System.out.printf("Average Waiting Time: %.2f\n", (double) totalWaitingTime / p.length);
	    System.out.printf("CPU Utilization: %.2f\n", cpuUtilization);
	}

}

