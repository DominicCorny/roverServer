package com.company;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class SendCommandsThread extends Thread {
    private static final String TAG = "SENDER: ";

    private final int port;
    private final int timeout;
    private byte runningNumber = 0;
    private byte speed = 0, steering = 50;

    SendCommandsThread(int port, int timeout) {
        this.port = port;
        this.timeout = timeout;
    }


    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port, 1)) {
            while (true) {
                System.out.println(TAG + "Try to connect to rover");
                Socket socket = serverSocket.accept();
                System.out.println(TAG + "Succesfully connected to rover");
                socket.setTcpNoDelay(true);
                socket.setSoTimeout(timeout);
                sendLoop(socket);
            }
        } catch (IOException e) {
            System.out.print("WTF? Should not have happened.");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private void sendLoop(Socket socket) {
        byte[] sendData = new byte[2];

        try {
            while (true) {
                sendData[0] = speed;
                sendData[1] = steering;

                socket.getOutputStream().write(sendData);
                System.out.println(TAG + "Sending data to Rover: speed = " + speed + " steering = " + steering + "  " + getNumber());
                socket.getInputStream().read();

                if (!interrupted()) util.sleep(1000);
            }
        } catch (Exception e) {
            System.out.println("\n\n" + TAG + "Connection to Rover lost because of " + e.getMessage());
            util.close(socket);
        }
    }

    void newData(byte speed, byte steering) {
        this.speed = speed;
        this.steering = steering;
        this.interrupt();
    }
    private byte getNumber()
    {
        if(runningNumber > 8){
            return runningNumber = 0;
        }else {
            return ++runningNumber;
        }
    }
}
