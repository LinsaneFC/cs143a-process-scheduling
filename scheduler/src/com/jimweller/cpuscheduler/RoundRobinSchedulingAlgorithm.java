/** RoundRobinSchedulingAlgorithm.java
 * 
 * A scheduling algorithm that randomly picks the next job to go.
 *
 * @author: Kyle Benson
 * Winter 2013
 *
 */
package com.jimweller.cpuscheduler;

import java.util.*;

public class RoundRobinSchedulingAlgorithm extends BaseSchedulingAlgorithm {

    /** the time slice each process gets */
    private int quantum; 
    private ArrayList<Process> jobs;
    private long startTime; 

    class RRComparator implements Comparator<Process> {
		public int compare(Process p1, Process p2) {
			return Long.signum(p1.getPID() - p2.getPID());
		}
	}

	RRComparator comparator = new RRComparator();

    RoundRobinSchedulingAlgorithm() {
        // Fill in this method
        /*------------------------------------------------------------*/
        activeJob = null;
        jobs = new ArrayList<Process>();
        quantum = 10;
        startTime = 0;

        /*------------------------------------------------------------*/
    }

    /** Add the new job to the correct queue. */
    public void addJob(Process p) {
        // Remove the next lines to start your implementation        
        // Fill in this method
        /*------------------------------------------------------------*/

        jobs.add(p);
        Collections.sort(jobs, comparator);
        /*------------------------------------------------------------*/
    }

    /** Returns true if the job was present and was removed. */
    public boolean removeJob(Process p) {
        // Remove the next lines to start your implementation
        
        // Fill in this method
        /*------------------------------------------------------------*/
        if(p == activeJob)
            activeJob = null;


        return jobs.remove(p);
        /*------------------------------------------------------------*/
    }

    /** Transfer all the jobs in the queue of a SchedulingAlgorithm to another, such as
    when switching to another algorithm in the GUI */
    public void transferJobsTo(SchedulingAlgorithm otherAlg) {
        throw new UnsupportedOperationException();
    }

    /**
     * Get the value of quantum.
     * 
     * @return Value of quantum.
     */
    public int getQuantum() {
        return quantum;
    }

    /**
     * Set the value of quantum.
     * 
     * @param v
     *            Value to assign to quantum.
     */
    public void setQuantum(int v) {
        this.quantum = v;
    }

    /**
     * Returns the next process that should be run by the CPU, null if none
     * available.
     */
    public Process getNextJob(long currentTime) {
        // Remove the next lines to start your implementation
        
        // Fill in this method
        /*------------------------------------------------------------*/
        Process next = null;
        if(startTime + quantum >= currentTime){
            next = jobs.get(jobs.indexOf(activeJob) + 1);
            activeJob = next;
            startTime = currentTime;
            return activeJob;
        }
        
        return activeJob;


        /*------------------------------------------------------------*/
    }

    public String getName() {
        return "Round Robin";
    }
    
}