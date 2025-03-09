
import java.util.*;
public class MemoryManager {
	
	private ArrayList<MemoryBlock> memoryBlocks;
	
	 public MemoryManager(int[] blockSizes) {
	        int start = 0;
	        for (int size : blockSizes) {
	            memoryBlocks.add(new MemoryBlock(size, start));
	            start += size;
	        }
	    }
	 
	   public void allocateMemory(String processID, int size, int strategy) {
	        // Implement First-Fit, Best-Fit, Worst-Fit logic based on 'strategy' parameter
	    }

	    public void deallocateMemory(String processID) {
	        // Find the process and mark the block as free
	    }

	    public void printMemoryStatus() {
	        // Display block details
	    }
	
	

}
