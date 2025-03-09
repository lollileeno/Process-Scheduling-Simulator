
import java.util.*;
public class MemorySimulator {

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		int [] blocks;
		int n;
		while(true) {
			System.out.print("Enter the total number of memory blocks: ");
			n = input.nextInt();
			if(n>0) {
			blocks = new int [n];
			break;
		}
			else {
				System.out.print("invalid input, please enter a positive number: ");
				 n = input.nextInt();
			}				
	}
		
		 System.out.print("Enter the size of each block:");
	        for (int i = 0; i < n; i++) {
	            blocks[i] = input.nextInt();
	        }	
	        
	        System.out.println("Enter allocation strategy (1 for first-fit, 2 for best-fit, 3 for worst-fit): ");
	         int strategy = input.nextInt();
	       

	        MemoryManager manager = new MemoryManager(blocks);
	        
	        System.out.println("Memory blocks are created…");
	        
	        
	        int choice;
	        do {
	            System.out.println("\n1) Allocate memory");
	            System.out.println("2) Deallocate memory");
	            System.out.println("3) Print memory report");
	            System.out.println("4) Exit");
	            System.out.print("Enter your choice: ");
	            choice = input.nextInt();

	            switch (choice) {
	                case 1:
	                    System.out.print("Enter process ID and size: ");
	                    String processID = input.next();
	                    int size = input.nextInt();
	                    manager.allocateMemory(processID, size, strategy);
	                    break;
	                case 2:
	                    System.out.print("Enter process ID to deallocate: ");
	                    processID = input.next();
	                    manager.deallocateMemory(processID);
	                    break;
	                case 3:
	                    manager.printMemoryStatus();
	                    break;
	                case 4:
	                    System.out.println("Exiting...");
	                    break;
	                default:
	                    System.out.println("Invalid choice.");
	            }
	        } while (choice != 4);

	        input.close();

}
}
