package com.jimweller.cpuscheduler;

public class MemoryBlock{

    long size;
    boolean free;

    public MemoryBlock(){
        size = 0;
        free = true;
    }

    public MemoryBlock(long aSize){
        size = aSize;
        free = true;
    }

    public MemoryBlock(long aSize, boolean aFree){
        size = aSize;
        free = aFree;
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