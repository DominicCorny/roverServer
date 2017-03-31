package com.company;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;


class SendCommands {
    private static final String TAG = "SENDER: ";

    private DatagramSocket socket;
    private SenderThread senderThread;
    private ReceiverThread receiverThread;
    private InetSocketAddress roverAddress;
    private int ping = -1;

    SendCommands(int port, int timeout) {
        try {
            socket = new DatagramSocket(port);
            socket.setSoTimeout(timeout);
        } catch (IOException e) {
            System.out.print("WTF? Should not have happened.");
            e.printStackTrace();
            Util.close(socket);
            System.exit(-1);
        }
        receiverThread = new ReceiverThread();
        senderThread = new SenderThread();
    }

    void start() {
        receiverThread.start();
    }

    private class ReceiverThread extends Thread {
        @Override
        public void run() {
            DatagramPacket packet = new DatagramPacket(new byte[6], 6);

            while (true) {
                if (ping == -1) Util.println(TAG + "Try to connect to rover");
                try {
                    socket.receive(packet);
                    if (ping == -1) {
                        roverAddress = (InetSocketAddress) packet.getSocketAddress();//this is the address we will send the data to
                        if (!senderThread.isAlive()) senderThread.start();//start the senderThread if the senderThread is not running
                    }
                    ping = ((int) System.currentTimeMillis()) - Util.readInt(packet.getData(), 2);
                } catch (Exception e) {
                    if (ping >= 0)
                        Util.println("\n" + TAG + "Connection to rover lost because of " + e.getMessage());
                    ping = -1;
                }
            }
        }
    }

    private class SenderThread extends Thread {
        private byte[] data = new byte[6];

        @Override
        public void run() {
            data[0] = 50;//Default value for robot with reverse Funktion
            data[1] = 50;//please dont remove
            DatagramPacket packet = new DatagramPacket(data, data.length);

            while (true) {
                try {
                    packet.setSocketAddress(roverAddress);
                    Util.println(TAG + "Sending data to Rover: speed = " + packet.getData()[0] + " steering = " + packet.getData()[1]);
                    Util.putInt((int) System.currentTimeMillis(), packet.getData(), 2);
                    socket.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (!interrupted()) Util.sleep(50);
            }
        }

        private void newData(byte speed, byte steering) {
            data[0] = speed;
            data[1] = steering;
            this.interrupt();
        }
    }

    void newData(byte speed, byte steering) {
        if (senderThread != null) senderThread.newData(speed, steering);
    }

    int getPing() {
        return ping;
    }
}
