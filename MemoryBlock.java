
public class MemoryBlock {
	int size;
	int startAddress;
	int endAddress;
	boolean isAllocated;
	String processID;
	int internalFragmentation;
	
	public MemoryBlock(int size , int startAddress) {
		this.size = size;
		this.startAddress = startAddress;
		endAddress = startAddress + size - 1;
		isAllocated = false;
		processID = null;
		internalFragmentation =0;
	}


}
