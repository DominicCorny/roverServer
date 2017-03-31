package com.company;

import java.io.IOException;

public class Main {
    private static boolean consoleControl = false;

    private static SendCommands senderThread;
    private static ReceiverThread receiverThread;

    public static void main(String[] args) throws IOException {
        System.out.println("SERVER: Start");

        senderThread = new SendCommands(3841, 1000);
        senderThread.start();

        if (consoleControl) {
            receiverThread = new ConsoleControlThread(senderThread);
        } else {
            receiverThread = new ReceiveCommandsThread(3842, 1000, senderThread);
        }
        receiverThread.start();
    }
}
