# Event-Driven SRTF Process Scheduler (Java)

This project implements an event-driven Shortest Remaining Time First (SRTF) CPU scheduling algorithm using Java. The scheduler simulates process execution based on arrival events, supports context switching, and calculates key performance metrics.

#Features
	•	Event-driven simulation (ARRIVAL, COMPLETION)
	•	Shortest Remaining Time First (preemptive)
	•	Context switching support (1 ms)
	•	Priority queues for efficient scheduling
	•	Gantt-chart–style execution output
	•	Performance metrics calculation

# Technologies
	•	Java
	•	PriorityQueue
	•	Comparator
	•	Console-based input/output

# How It Works
	•	Processes are added as arrival events.
	•	The scheduler always selects the process with the shortest remaining time.
	•	Preemption occurs when a shorter job arrives.
	•	Context switching time is applied when switching processes.

# Output Metrics
	•	Average Turnaround Time
	•	Average Waiting Time
	•	CPU Utilization

# How to Run

1.	Compile the program:
   javac ProcessSchedulerEventDriven.java

2.	Run the program:
   java ProcessSchedulerEventDriven
  	
3. 	Enter process arrival and burst times when prompted.
    
  Scheduling Algorithm
	•	Algorithm: Shortest Remaining Time First (SRTF)
	•	Context Switch Time: 1 ms

# Notes

This project is designed for educational purposes as part of an Operating Systems or CPU Scheduling course and demonstrates correct event-driven preemptive scheduling behavior.
