package com.company;

import javax.print.attribute.standard.MediaSize;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
	// write your code here
        NAManager.init();
        Holder holder = (String result) ->
        {
            NAManager.messagesManager.sendMessageTo("shan20", "Hello!", (String h) -> System.out.println(h));
        };
        NAManager.accountManager.logIn("shan27", "BIANhao5213", holder);

    }
}
