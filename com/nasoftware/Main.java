package com.nasoftware;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
	// write your code here
    //example code for using NAChatClient:
        NAManager.init();
        Holder holder = (String result) ->
        {
            NAManager.messagesManager.addMessageListener((String a) -> System.out.println(a));
        };
        NAManager.accountManager.logIn("shan27", "BIANhao5213", holder);
    }
}
