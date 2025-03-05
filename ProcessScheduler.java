import java.util.*;

public class ProcessScheduler {
	static int CS = 1;// 1ms context switch
	
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		int pID;
		int arriveT;
		int burstT;
		int pNum;

		System.out.println("Welcome to CSC227 platform");

		
		while (true) {
			System.out.println("Enter the number of your processes: ");
			if (input.hasNextInt()) {
				pNum = input.nextInt();
				if (pNum > 0) {
					break; // Exit loop if valid input
				}
			} else {
				input.next(); // Consume invalid input
			}
			System.out.println("Invalid input. Please enter a positive integer.");
		}

		// Read processes
		Process[] p = new Process[pNum];
		for (int i = 1; i <= pNum; i++) {
			pID = i;
			System.out.printf("Enter the arrival time of P%d: \n", i);
			arriveT = input.nextInt();
			System.out.printf("Enter the burst time of P%d: \n", i);
			burstT = input.nextInt();
			p[i - 1] = new Process(pID, arriveT, burstT);
		}

		input.close(); // Close scanner to prevent resource leaks

		displayInfo(p);
		ScheduleProcesses(p);
	}
	
	public static void displayInfo(Process[] p) {
		System.out.print("Number of proccesses= " + (p.length));
		System.out.print(" (");
		for (int j = 0; j < p.length; j++) {
			System.out.print("P" + p[j].processID);
			if (j < p.length - 1) {
				System.out.print(", ");
			}
		}
		System.out.println(")");
		System.out.println("Arrival times and burst times as follows: ");
		for (int i = 0; i < p.length; i++) {
			Process process = p[i];
			System.out.println("P" + (i + 1) + ": Arrival time = " + process.arrivalTime + ", " + "Burst time = "
					+ process.burstTime + " ms");
		}
		System.out.println("Scheduling Algorithm: Shortest remaining time first \nContext switch: " + CS + " ms");

	}

public static void ScheduleProcesses(Process[] p) {
	    // Sort processes by arrival time to handle them in order
	    Arrays.sort(p, Comparator.comparingInt(proc -> proc.arrivalTime));

	    PriorityQueue<Process> remainQueue = new PriorityQueue<>(
	        Comparator.<Process>comparingInt(proc -> proc.remainingTime)
	                .thenComparingInt(proc -> proc.arrivalTime));

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

				// Context Switch if switching to a different process
				if (lastProcess != null && nextProcess != lastProcess) {
					System.out.printf("%d-%d   CS\n", time, time + 1);
					time = time + CS;
					contextSwitches++;
				}
				lastProcess = nextProcess; // Update lastProcess to the new one

				int startTime = time;

				// Execute process 1ms at a time to allow preemptions
				while (nextProcess.remainingTime > 0) {
					nextProcess.remainingTime--; // Execute for 1ms
					time++;
					totalExecutionTime++;

					// Add newly arriving processes
					while (i < p.length && p[i].arrivalTime == time) {
						remainQueue.add(p[i]);
						i++;
					}

					// Preempt immediately if a shorter job arrives
					if (!remainQueue.isEmpty() && nextProcess.remainingTime > remainQueue.peek().remainingTime) {
						currentProcess = nextProcess;
						break; // Stop execution and allow preemption
					}
				}

				System.out.printf("%d-%d   P%d\n", startTime, time, nextProcess.processID);

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

		// Print Performance Metrics
		System.out.println("\nPerformance Metrics");
		System.out.printf("Average Turnaround Time: %.2f\n", (double) totalTurnaroundTime / p.length);
		System.out.printf("Average Waiting Time: %.2f\n", (double) totalWaitingTime / p.length);
		System.out.printf("CPU Utilization: %.2f\n", cpuUtilization);
	}

}
