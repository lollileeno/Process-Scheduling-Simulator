import java.util.*;
public class Runner {

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		 int pID = 0;
		 int arriveT = 0;
		 int burstT;
	     int pNum;
	     System.out.println("Welcome to CSC227 platform\n"
	     		+ "Enter the number of your processes: ");
	     pNum = input.nextInt();
	     
	     Process [] p = new Process[pNum];
	     
	    for(int i = 1 ; i <= pNum; i++) {
	    	pID = i;
	    	System.out.printf("Enter the burst time of P%d: \n",i);
	    	burstT = input.nextInt();
	    	p[i-1] = new Process(pID,arriveT,burstT);
	    	arriveT++;
	    	
	    }
		 

	}
	public static int processingTime(Process [] p){
		int CS = 1;   //refer to context switch time
		int i = 0;
		int processing = 0;
		while(i<p.length-1) {
			if(p[i].burstTime<=p[i+1].burstTime)
			processing+=(p[i].burstTime + p[i+1].burstTime + CS);
			else
				processing +=(p[i].burstTime - p[i+1].burstTime + CS);
			
			i++;
		}
		
		return processing;
	}

		public static Process[] sortShortest(Process[] p) {
	    Process[] p2 = Arrays.copyOf(p, p.length); // Copy original array to avoid modifying it
	    Arrays.sort(p2, Comparator.comparingInt(process -> process.burstTime)); // Sort by burst time
	    return p2;
	}

}

