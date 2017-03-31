package com.company;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ReceiveCommandsThread extends ReceiverThread {

    private DatagramSocket socket;

    ReceiveCommandsThread(int port, int timeout, SendCommands sender) {
        super(sender);

        try {
            socket = new DatagramSocket(port);
            socket.setSoTimeout(timeout);
        } catch (IOException e) {
            System.out.print("WTF? Should not have happened.");
            e.printStackTrace();
            Util2.close(socket);
            System.exit(-1);
        }
    }

    @Override
    public void run() {
        byte[] data = new byte[10];
        DatagramPacket packet = new DatagramPacket(data, 0, data.length);
        boolean connected = false;

        while (true) {
            if (!connected) System.out.println(TAG + "Try to connect to app");
            try {
                socket.receive(packet);
                connected = true;
                System.out.println(TAG + "Received new data: speed = " + data[0] + " steering = " + data[1]);
                update(data[0], data[1]);

                Util2.putInt(getPingToRover(), data, 6);
                packet.setLength(data.length);
                socket.send(packet);
            } catch (Exception e) {
                if (connected) {
                    connected = false;
                    System.out.println("\n" + TAG + "Connection to app lost because of " + e.getMessage());
                    update((byte) 50, (byte) 50);
                }
            }
        }
    }
}
