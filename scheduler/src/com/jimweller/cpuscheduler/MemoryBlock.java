package com.jimweller.cpuscheduler;

public class MemoryBlock{

    private long size;
    private boolean free;
    private Process p;

    public MemoryBlock(Process ap){
        size = 0;
        free = true;
        p = ap;
    }

    public MemoryBlock(long aSize, Process ap){
        size = aSize;
        free = true;
        p = ap;
    }

    public MemoryBlock(long aSize, boolean aFree, Process ap){
        size = aSize;
        free = aFree;
        p = ap;
    }

    public Process getProcess(){
        return p;
    }

    public void setProcess(Process ap){
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



}