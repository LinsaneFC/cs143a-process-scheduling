/** FCFSSchedulingAlgorithm.java
 * 
 * A first-come first-served scheduling algorithm.
 * The current implementation will work without memory management features
 *
 */
package com.jimweller.cpuscheduler;

import java.util.*;

class MemoryBlock{

    private long size;
    private boolean free;
    private long p;

    public MemoryBlock(long ap){
        size = 0;
        free = true;
        p = ap;
    }

    public MemoryBlock(long aSize, long ap){
        size = aSize;
        free = true;
        p = ap;
    }

    public MemoryBlock(long aSize, boolean aFree, long ap){
        size = aSize;
        free = aFree;
        p = ap;
    }

    public long getProcess(){
        return p;
    }

    public void setProcess(long ap){
        p = ap;
    }

    public long getSize(){
        return size;
    }

    public boolean getFree(){
        return free;
    }

    public void setSize(long aSize){
        size = aSize;
    }

    public void setFree(boolean aFree){
        free = aFree;
	}
	
	public String toString(){
		return "Size: " + size + " Free: " + free + " Process " + p;
	}

	@Override
	public boolean equals(Object otherMemBlock){
		if(otherMemBlock == this)
			return true;
		
		if(!(otherMemBlock instanceof MemoryBlock))
			return false;
		
		MemoryBlock other = (MemoryBlock) otherMemBlock;

		return (this.free == other.free) && (Long.compare(p, other.p) == 0) && (Long.compare(size, other.size) == 0);
	}

}


public class FCFSSchedulingAlgorithm extends BaseSchedulingAlgorithm {

	private ArrayList<Process> jobs;
	// Add data structures to support memory management
	/*------------------------------------------------------------*/
	private ArrayList<MemoryBlock> mem;
	private String memFit;

	/*------------------------------------------------------------*/

	class FCFSComparator implements Comparator<Process> {
		public int compare(Process p1, Process p2) {
			if (p1.getArrivalTime() != p2.getArrivalTime()) {
				return Long.signum(p1.getArrivalTime() - p2.getArrivalTime());
			}
			return Long.signum(p1.getPID() - p2.getPID());
		}
	}

	FCFSComparator comparator = new FCFSComparator();

	FCFSSchedulingAlgorithm() {
		activeJob = null;
		jobs = new ArrayList<Process>();

		// Initialize memory
		/*------------------------------------------------------------*/
		mem = new ArrayList<MemoryBlock>();
		mem.add(new MemoryBlock(380, true, -1));
		memFit = "";
		/*------------------------------------------------------------*/
	}


	/** Add the new job to the correct queue. */
	public void addJob(Process p) {
		
	// Check if any memory is available 
	/*------------------------------------------------------------*/
		if(!memFit.equals("")){
			boolean found = false;
			int i = -1;
			if(memFit.toUpperCase().equals("FIRST")){
				for(MemoryBlock m : mem){
					if(m.getSize() >= p.getMemSize() && m.getFree()){
						found = true;
						i = mem.indexOf(m);
						break;
					}
				}	
			}else{
				long difference = Long.MAX_VALUE;
				for(MemoryBlock m : mem){
					if(m.getSize() >= p.getMemSize() && m.getFree()){
						found = true;
						if( (m.getSize() - p.getMemSize()) < difference ){
							difference = m.getSize() - p.getMemSize();
							i = mem.indexOf(m);
							if(difference == 0){
								break;
							}
						}
					}
				}	
			}
		
	/*------------------------------------------------------------*/

		// If enough memory is not available then don't add it to queue
			if(!found){
				p.setIgnore(true);
				return;
			}

			if(found){
				if(mem.get(i).getSize() > p.getMemSize()){
					mem.add(i + 1, new MemoryBlock((mem.get(i).getSize() - p.getMemSize()), true, -1));
				}
				mem.get(i).setSize(p.getMemSize());
				mem.get(i).setProcess(p.getPID());
				mem.get(i).setFree(false);
			}
		}
		jobs.add(p);
		Collections.sort(jobs, comparator);
	}

	/** Returns true if the job was present and was removed. */
	public boolean removeJob(Process p) {
		if (p == activeJob)
			activeJob = null;

		// In case memory was allocated, free it
		/*------------------------------------------------------------*/
		if(!memFit.equals("")){
			int i = -1;
			boolean found = false;
			for(MemoryBlock m : mem){
				if(!m.getFree() && m.getProcess() == p.getPID()){
					found = true;
					i = mem.indexOf(m);
					break;
				}
			}

			if(found){
				if(i == 0){
					if(mem.size() > 1){
						if(mem.get(i+1).getFree()){
							mem.get(i).setSize(mem.get(i).getSize() + mem.get(i+1).getSize());
							mem.get(i).setProcess(-1);
							mem.remove(i+1);
						}
					}
					mem.get(i).setProcess(-1);
					mem.get(i).setFree(true);
				}else if(i == mem.size() - 1){
					if(mem.get(i-1).getFree()){
						mem.get(i-1).setSize(mem.get(i).getSize() + mem.get(i-1).getSize());
						mem.get(i-1).setProcess(-1);
						mem.remove(i);
					}else{
						mem.get(i).setProcess(-1);
						mem.get(i).setFree(true);
					}
				}else{
					if(mem.get(i-1).getFree() && mem.get(i+1).getFree()){
						mem.get(i-1).setSize(mem.get(i).getSize() + mem.get(i-1).getSize() + mem.get(i+1).getSize());
						mem.get(i-1).setProcess(-1);
						mem.remove(i+1);
						mem.remove(i);
					}else if(mem.get(i-1).getFree()){
						mem.get(i-1).setSize(mem.get(i).getSize() + mem.get(i-1).getSize());
						mem.get(i-1).setProcess(-1);
						mem.remove(i);
					}else if(mem.get(i+1).getFree()){
						mem.get(i).setSize(mem.get(i).getSize() + mem.get(i+1).getSize());
						mem.get(i).setProcess(-1);
						mem.get(i).setFree(true);
						mem.remove(i+1);
					}else{
						mem.get(i).setProcess(-1);
						mem.get(i).setFree(true);
					}
				}
			}
		}

		/*------------------------------------------------------------*/

		return jobs.remove(p);
	}

	/**
	 * Transfer all the jobs in the queue of a SchedulingAlgorithm to another, such
	 * as when switching to another algorithm in the GUI
	 */
	public void transferJobsTo(SchedulingAlgorithm otherAlg) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns the next process that should be run by the CPU, null if none
	 * available.
	 */
	public Process getNextJob(long currentTime) {
		Process earliest = null;

		if (!isJobFinished())
			return activeJob;
		if (jobs.size() > 0)
			earliest = jobs.get(0);
		activeJob = earliest;
		return activeJob;
	}

	public String getName() {
		return "First-Come First-Served";
	}

	public void setMemoryManagment(String v) {
		// Modify class to suppor memory management
		this.memFit = v.toUpperCase();
	}
}