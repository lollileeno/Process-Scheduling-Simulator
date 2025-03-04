

public class Process {
	int processID;
	public int arrivalTime;
	int burstTime;
	int completionTime;
	int waitingTime;
	int turnaroundTime;
	public int remainingTime; //it is the remaining time of burst time
	public Process(int processID, int arrivalTime, int burstTime) {
		this.processID = processID;
		this.arrivalTime = arrivalTime;
		this.burstTime = burstTime;
		remainingTime = burstTime;
		
	}
	
	  public void setCompletionTime(int end) {
	    	completionTime = end;
	    	
	    }

	
    public void calculateTurnaroundTime() {
    	turnaroundTime = completionTime - arrivalTime;
    	
    }
    
    
    public void calculateWaitingTime() {
    	waitingTime = turnaroundTime  - burstTime;
    	
    }
    
  
}
