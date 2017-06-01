package com.nasoftware;

/**
 * Created by zeyongshan on 5/31/17.
 */
class FriendsManager
{
    private void addFriendHandler(String friendAccount, Holder holder)
    {
        BufferPool.instructionQueue.enquenuReadyInstruction("3-" + NAManager.accountManager.getCurrentAccount() + "-" + friendAccount);
        ReciptHandler handler = new ReciptHandler(holder, 3);
        handler.start();
    }

    public void addFriend(String shan27, String friendAccount, Holder holder)
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
