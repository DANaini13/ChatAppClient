package com.nasoftware;

/**
 * Created by zeyongshan on 5/31/17.
 */
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
