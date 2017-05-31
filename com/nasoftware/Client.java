package com.company;

import sun.reflect.generics.scope.Scope;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by zeyongshan on 5/30/17.
 */
public class Client {
    private Socket client;

    public Client(String hostName, int port)
    {
        try {
            client = new Socket(hostName, port);
            DataOutputStream out = new DataOutputStream(client.getOutputStream());
            out.writeUTF("BIANhao5213");        // The password that used to match the server.
            Reader reader = new Reader(client);
            reader.start();
            Sender sender = new Sender(out);
            sender.start();
        }catch (IOException e)
        {
            System.out.println("connection faild");
        }
    }
}


class Sender extends Thread
{
    private DataOutputStream out;

    public Sender(DataOutputStream out)
    {
        this.out = out;
    }

    public void run()
    {
        while (true)
        {
            try {
                String instruction = BufferPool.instructionQueue.dequeueReadyInstruction();
                if (instruction != null) {
                    out.writeUTF(instruction);
                }
            } catch (IOException e)
            {
                e.printStackTrace();
                break;
            }
        }
    }
}


class Reader extends Thread
{
    private Socket client;

    public Reader(Socket client)
    {
        this.client = client;
    }

    public void run()
    {
        while (client.isConnected())
        {
            try {
                DataInputStream in = new DataInputStream(client.getInputStream());
                String temp = in.readUTF();
                if(temp.charAt(0) <= '5' && temp.charAt(0) >= '0')
                    storeReturnCode(temp);
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }
    }

    private void storeReturnCode(String code)
    {
            try {
                String[] parts = code.split("-");
                if(parts.length != 2)
                    throw(new IOException());
                if(parts[0] == "5")
                    BufferPool.messageQueue.enqueueNewMessage(parts[1]);
                else
                    BufferPool.instructionPool.storeIn(parts[0].charAt(0) - '1', parts[1]);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}



