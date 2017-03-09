package com.company;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class SendCommandsThread extends Thread {
    private static final String TAG = "SENDER: ";

    private final int port;
    private final int timeout;
    private int runningNumber = 0;
    private byte [] sendData;
    private Sender sender;

    SendCommandsThread(int port, int timeout) {
        this.port = port;
        this.timeout = timeout;
        sendData = new byte[2];
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port, 1)) {
            while (true) {
                System.out.println(TAG + "Try to connect to rover");
                Socket socket = serverSocket.accept();
                socket.setTcpNoDelay(true);
                socket.setSoTimeout(timeout);
                //test connection
                socket.getOutputStream().write(1);
                if (socket.getInputStream().read() != 1) throw new IOException("read error");
                System.out.println(TAG + "Succesfully connected to rover");

                sender = new Sender(socket);
                sender.start();
                while (true) {
                    if (socket.getInputStream().read() != 1) throw new IOException("read error");
                }
            }
        } catch (IOException e) {
            System.out.print("WTF? Should not have happened.");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private class Sender extends Thread {
        private Socket socket;

        Sender(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    socket.getOutputStream().write(sendData);
                    System.out.println(TAG + "Sending data to Rover: speed = " + sendData[0] + " steering = " + sendData[1] + '\t' + runningNumber++ % 10);
                    if (!interrupted()) util.sleep(250);
                }
            } catch (Exception e) {
                util.close(socket);
            }
        }
    }

    void newData(byte speed, byte steering) {
        sendData[0]= speed;
        sendData[1] = steering;
        if(sender != null) {
            sender.interrupt();
        }
    }
}
