package com.company;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    private static byte speed, steering = 50;

    public static void main(String[] args) throws IOException {
        System.out.print("Start");
        SendDataThread sendDataThread = new SendDataThread();
        sendDataThread.start();

        ReceiveDataThread receiveDataThread = new ReceiveDataThread(sendDataThread);
        receiveDataThread.start();
    }

    /*
            Scanner scanner = new Scanner(in);

        ServerSocket serverSocket = new ServerSocket(4572);

        Socket socket = serverSocket.accept();
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();

        Thread t = new ReceiveDataThread(outputStream, inputStream);
        t.start();

        byte[] buffer = new byte[8];
        boolean running = true;
        inputStream.read(buffer);
        if (new String(buffer).equals("PI Hello")) {
            System.out.println("Received PI Hello");
            while (running) {
                try {
                    System.out.println("Gib die Geschwindigkeit ein");
                    speed = Byte.valueOf(scanner.nextLine());
                    System.out.println("Gib die Lenkung ein");
                    steering = Byte.valueOf(scanner.nextLine());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("FAILURE");
        }
     */

    static class ReceiveDataThread extends Thread {
        Thread otherDependenThread;

        ReceiveDataThread(Thread otherDependenThread) {
            this.otherDependenThread = otherDependenThread;
        }

        //receive from app
        @Override
        public void run() {
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(4586);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(-1);
            }

            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    System.out.print("Server OK");
                    InputStream in = socket.getInputStream();
                    OutputStream out = socket.getOutputStream();

                    byte[] inputData = new byte[2];
                    byte[] serverOk = "SERVER OK".getBytes();


                    while (true) {
                        in.read(inputData);
                        speed = inputData[0];
                        steering = inputData[1];

                        System.out.println("speed" + speed);
                        System.out.println("steering" + steering);
                        otherDependenThread.interrupt();

                        out.write(serverOk);
                    }
                } catch (IOException e) {
                    System.out.println("Reconnecting to App");
                }
            }
        }
    }

    //send to Pi
    static class SendDataThread extends Thread {
        @Override
        public void run() {
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(4587);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(-1);
            }
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    InputStream in = socket.getInputStream();
                    OutputStream out = socket.getOutputStream();

                    byte[] sendData = new byte[2];
                    byte[] buffer = new byte[20];

                    while (true) {

                        sendData[0] = speed;
                        sendData[1] = steering;
                        out.write(sendData);
                        System.out.println("Sende an Pi Speed:" + sendData[0] + "steering" + sendData[1]);
                        in.read(buffer);

                        if (!interrupted()) {
                            sleep(1000);
                        }
                    }
                } catch (IOException e) {
                    //e.printStackTrace();
                    System.out.println("Reconnecting to Pi");
                }
            }
        }

        void sleep(int time) {
            try {
                Thread.sleep(time);
            } catch (InterruptedException ignore) {
            }
        }
    }
}
