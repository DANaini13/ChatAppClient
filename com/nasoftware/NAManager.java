package com.company;

/**
 * Created by zeyongshan on 5/30/17.
 */

interface Holder
{
    void completeHolder(String result);
}

class AccountManager
{
    private String currentAccount = null;
    private boolean logInStatus = false;

    private void signUpHolder(String account, String password, String name, Holder holder)
    {
        BufferPool.instructionQueue.enquenuReadyInstruction("1-" + account + "-" + password + "-" + name);
        ReciptHandler handler = new ReciptHandler(holder, 1);
        handler.start();
    }

    private void logInHolder(String account, String password, Holder holder)
    {
        BufferPool.instructionQueue.enquenuReadyInstruction("2-" + account + "-" + password);
        ReciptHandler handler = new ReciptHandler(holder, 2);
        handler.start();
    }

    public String getCurrentAccount()
    {
        return currentAccount;
    }

    public boolean getLogInStatus()
    {
        return logInStatus;
    }

    public void signUp(String account, String password, String name, Holder holder)
    {
        Holder holder1 = (String result) ->
        {
            if(result.equals("1"))
            {
                currentAccount = account;
                this.logInStatus = true;
                holder.completeHolder("sign up successful! -true");
            }
            else
                holder.completeHolder("account exist! -false");
        };
        signUpHolder(account,password,name, holder1);
    }

    public void logIn(String account , String password, Holder holder)
    {
        Holder holder1 = (String result) ->
        {
            if(result.equals("1"))
            {
                currentAccount = account;
                this.logInStatus = true;
                holder.completeHolder("log in successful! -true");
            }
            else
                holder.completeHolder("account or password doesn't correct! -false");
        };
        logInHolder(account, password, holder1);
    }
}

class FriendsManager
{
    private void addFriendHandler(String friendAccount, Holder holder)
    {
        BufferPool.instructionQueue.enquenuReadyInstruction("3-" + NAManager.accountManager.getCurrentAccount() + "-" + friendAccount);
        ReciptHandler handler = new ReciptHandler(holder, 3);
        handler.start();
    }

    public void addFriend(String friendAccount, Holder holder)
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
                holder.completeHolder("add friend successful! -true");
            }
            else
                holder.completeHolder("add friend failed! -false");
        };
        addFriendHandler(friendAccount, holder1);
    }
}

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

    public void addNewMessageListener(Holder holder)
    {
        NewMessageHandler handler = new NewMessageHandler(holder);
        handler.start();
    }
}

class ReciptHandler extends Thread
{
    private Holder holder;
    private int func;

    public ReciptHandler(Holder holder, int function)
    {
        this.holder = holder;
        this.func = function;
    }
    public void run()
    {
        while(!BufferPool.instructionPool.isUpdated(func-1));
        holder.completeHolder(BufferPool.instructionPool.takeout(func-1));
    }
}

class NewMessageHandler extends Thread
{
    private Holder holder;

    public NewMessageHandler(Holder holder)
    {
        this.holder = holder;
    }

    public void run()
    {
        while (true)
        {
            String newMessage = BufferPool.messageQueue.dequeueMessage();
            if(newMessage != null)
                holder.completeHolder(newMessage);
        }
    }
}

public class NAManager {
    static public AccountManager accountManager =  new AccountManager();
    static public FriendsManager friendsManager =  new FriendsManager();
    static public MessagesManager messagesManager = new MessagesManager();
    static public void init()
    {
        Client client = new Client("45.56.93.181", 22000);
    }
}
