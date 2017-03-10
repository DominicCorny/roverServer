package com.company;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReceiveCommandsThread extends ReceiverThread {
    private final int port;
    private final int timeout;
    private SimpleDateFormat time;

    ReceiveCommandsThread(int port, int timeout, Listener listener) {
        super(listener);
        this.port = port;
        this.timeout = timeout;
        time = new SimpleDateFormat("HH:mm:ss");
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port, 1)) {
            while (true) {
                System.out.println(TAG + "Try to connect to app");
                Socket socket = serverSocket.accept();
                socket.setTcpNoDelay(true);//socket.setPerformancePreferences(0,1,0);
                socket.setSoTimeout(timeout);
                //test connection
                socket.getOutputStream().write(1);
                if (socket.getInputStream().read() != 1) throw new IOException("read error");
                System.out.println(TAG + "Succesfully connected to app");

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
            while (true) {
                //respond
                socket.getOutputStream().write(1);
                //receive
                int readCount = socket.getInputStream().read(buffer);
                if (readCount < 2) throw new IOException("Read error");
                //notify send thread
                System.out.println(TAG + "Received new data: speed = " + buffer[0] + " steering = " + buffer[1] + '\t' + time.format(new Date()));
                update(buffer[0], buffer[1]);
            }
        } catch (Exception e) {
            System.out.println("\n\n" + TAG + "Connection to app lost because of " + e.getMessage());
            update((byte) 50, (byte) 50);//Domi: default value changed because of "TrackedRover" Prototype that has a Reverse Gear.
            util.close(socket);
            util.sleep(100);
        }
    }
}
