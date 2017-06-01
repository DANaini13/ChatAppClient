package com.nasoftware;
import java.util.ArrayList;
/**
 * Created by zeyongshan on 5/31/17.
 */
class MessagesManager
{
    private void sendMessageHolder(String friendAccount, String message, Holder holder)
    {
        BufferPool.instructionQueue.enquenuReadyInstruction("4-" + NAManager.accountManager.getCurrentAccount() + "-" + friendAccount + "-" + message);
        ReciptHandler handler = new ReciptHandler(holder, 4);
        handler.start();
    }

    public void sendMessageTo(String friendAccount, String message, Holder holder)
    {
        if(!NAManager.accountManager.getLogInStatus())
        {
            holder.completeHolder("Should log in first!");
            return;
        }
        Holder holder1 = (String result) ->
        {
            if(result.equals("1"))
            {
                holder.completeHolder("send message successful! -true");
            }
            else
                holder.completeHolder("send message failed! -false");
        };
        sendMessageHolder(friendAccount, message, holder1);
    }

    public void getUnreadMessage(Receiver receiver)
    {
        BufferPool.instructionQueue.enquenuReadyInstruction("5-" + NAManager.accountManager.getCurrentAccount());
        NewMessageHandler newMessageHandler = new NewMessageHandler(receiver);
        newMessageHandler.start();
    }

    public void addMessageListener(Holder newMessageAction)
    {
        MessageListener messageListener = new MessageListener(newMessageAction);
        messageListener.start();
    }
}

class MessageListener extends Thread
{
    private Holder handler;

    MessageListener(Holder handler)
    {
        this.handler = handler;
    }

    public void run()
    {
        while (true)
        {
            try {
                this.sleep(500);
                BufferPool.instructionQueue.enquenuReadyInstruction("5-" + NAManager.accountManager.getCurrentAccount());
                String temp = BufferPool.messageQueue.dequeueMessage();
                if(temp!= null && !temp.equals("0"))
                    handler.completeHolder(temp);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}

class NewMessageHandler extends Thread
{
    private Receiver holder;

    public NewMessageHandler(Receiver holder)
    {
        this.holder = holder;
    }

    public void run()
    {
        ArrayList<String> result = new ArrayList<String>();
        String temp = BufferPool.messageQueue.dequeueMessage();
        while (temp == null)
            temp = BufferPool.messageQueue.dequeueMessage();
        if(temp.equals("0")) return;
        while(temp != null) {
            result.add(temp);
            temp = BufferPool.messageQueue.dequeueMessage();
        }
        holder.messageReceiver(result);
    }
}

