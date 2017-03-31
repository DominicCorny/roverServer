package com.company;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import static com.company.Util.println;

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
            Util.close(socket);
            System.exit(-1);
        }
    }

    @Override
    public void run() {
        byte[] data = new byte[10];
        DatagramPacket packet = new DatagramPacket(data, 0, data.length);
        boolean connected = false;

        while (true) {
            if (!connected) println(TAG + "Try to connect to app");
            try {
                socket.receive(packet);
                connected = true;

                println(TAG + "Received new data: speed = " + data[0] + " steering = " + data[1]);
                update(data[0], data[1]);

                Util.putInt(getPingToRover(), data, 6);
                packet.setLength(data.length);
                socket.send(packet);
            } catch (Exception e) {
                if (connected) {
                    connected = false;
                    println("\n" + TAG + "Connection to app lost because of " + e.getMessage());
                    update((byte) 50, (byte) 50);
                }
                Util.sleep(25);
            }
        }
    }
}
