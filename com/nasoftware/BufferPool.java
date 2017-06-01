package com.nasoftware;


import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by zeyongshan on 5/30/17.
 */


class InstructionPool
{
    private String[] data = new String[5];
    private boolean[] updated = new boolean[5];

    public InstructionPool()
    {
        for(int i=0; i<5; ++i) {
            data[i] = "";
            updated[i] = false;
        }
    }

    public void storeIn(int i, String content)
    {
        BufferPool.lock();
        data[i] = content;
        updated[i] = true;
        BufferPool.unlock();
    }

    public String takeout(int i)
    {
        BufferPool.lock();
        String temp = data[i];
        updated[i] = false;
        BufferPool.unlock();
        return temp;
    }

    public boolean isUpdated(int i)
    {
       BufferPool.lock();
       boolean temp = updated[i];
       BufferPool.unlock();
       return temp;
    }

    public String toString()
    {
        String temp = "";
        int i = 0;
        for(String x: data)
        {
            temp += i + ": " + x + "\n";
            ++i;
        }
        return temp;
    }
}


class MessageQueue
{
    private LinkedList<String> messagesList= new LinkedList<String >();

    public void enqueueNewMessage(String message)
    {
        BufferPool.lock();
        messagesList.addFirst(message);
        BufferPool.unlock();
    }

    public String dequeueMessage()
    {
        BufferPool.lock();
        String temp = null;
        if(messagesList.peekLast() != null)
            temp = messagesList.removeLast();
        BufferPool.unlock();
        return temp;
    }
}


class InstructionQueue
{
    private LinkedList<String> instructionQueue= new LinkedList<String >();

    public void enquenuReadyInstruction(String instruction)
    {
        BufferPool.lock();
        instructionQueue.addFirst(instruction);
        BufferPool.unlock();
    }

    public String dequeueReadyInstruction()
    {
        BufferPool.lock();
        String temp = null;
        if(instructionQueue.peekLast() != null)
            temp = instructionQueue.removeLast();
        BufferPool.unlock();
        return temp;
    }
}


public class BufferPool {
    static private Lock Rlock = new ReentrantLock();
    static public InstructionPool instructionPool = new InstructionPool();
    static public MessageQueue messageQueue = new MessageQueue();
    static public InstructionQueue instructionQueue = new InstructionQueue();

    static public void lock()
    {
       Rlock.lock();
    }

    static public void unlock()
    {
        Rlock.unlock();
    }




}



