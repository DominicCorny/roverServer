package com.company;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ReceiveCommandsThread extends ReceiverThread {
    private final int port;
    private final int timeout;

    ReceiveCommandsThread(int port, int timeout, Listener listener) {
        super(listener);
        this.port = port;
        this.timeout = timeout;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port, 1)) {
            while (!isInterrupted()) {
                System.out.println(TAG + "Try to connect to app");
                Socket socket = serverSocket.accept();
                System.out.println(TAG + "Succesfully connected to app");
                socket.setTcpNoDelay(true);//socket.setPerformancePreferences(0,1,0);
                socket.setSoTimeout(timeout);
                receiveLoop(socket);
            }
        } catch (IOException e) {
            System.out.print("WTF? Should not have happened.");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private void receiveLoop(Socket socket) {
        byte[] buffer = new byte[2];
        try {
            while (!isInterrupted()) {
                //respond
                socket.getOutputStream().write(1);

                //receive
                int readCount = socket.getInputStream().read(buffer);
                if (readCount < 2) break;//not expected

                //parse
                byte speed = buffer[0];
                byte steering = buffer[1];
                System.out.println(TAG + "Received new data: speed = " + speed + " steering = " + steering);
                update(speed, steering);
            }
        } catch (Exception e) {
            System.out.println("\n\n" + TAG + "Connection to app lost because of " + e.getMessage());
            update((byte) 0, (byte) 0);
            util.close(socket);
        }
    }
}
