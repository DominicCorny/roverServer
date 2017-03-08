package com.company;

import java.io.IOException;

public class Main {
    private static boolean consoleControl = false;

    public static void main(String[] args) throws IOException {
        System.out.print("SERVER: Start");

        SendCommandsThread sendThread = new SendCommandsThread(3841, 1000);
        sendThread.start();

        if (consoleControl) {
            new ConsoleControlThread(sendThread::newData).start();
        } else {
            new ReceiveCommandsThread(3842, 1000, sendThread::newData).start();
        }
    }
}
