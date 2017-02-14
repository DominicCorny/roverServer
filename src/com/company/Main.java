package com.company;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Main {
    private static byte speed, steering;

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        ServerSocket serverSocket = new ServerSocket(4572);

        Socket socket = serverSocket.accept();
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();

        Thread t = new SendThread(outputStream, inputStream);
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
    }

    static class SendThread extends Thread {
        OutputStream outputStream;
        InputStream inputStream;

        public SendThread(OutputStream outputStream, InputStream inputStream) {
            this.outputStream = outputStream;
            this.inputStream = inputStream;
        }

        @Override
        public void run() {
            byte [] buffer = new byte[50];

            while (true) {
                try {
                    outputStream.write(new byte[]{speed, steering});
                    inputStream.read(buffer);
                    sleep(200);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
