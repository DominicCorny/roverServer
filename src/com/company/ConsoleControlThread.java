package com.company;

import java.util.Scanner;

import static com.company.Util.println;
import static java.lang.System.in;


class ConsoleControlThread extends ReceiverThread {
    ConsoleControlThread(SendCommands sender) {
        super(sender);
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(in);
        byte speed, steering;
        while (true) {
            try {
                int ping = getPingToRover();
                if (ping >= 0) {
                    println(TAG + "Ping: " + ping);
                }
                println(TAG + "Gib die Geschwindigkeit ein (0-100)");
                speed = scanner.nextByte();
                println(TAG + "Gib die Lenkung ein (0-100)");
                steering = scanner.nextByte();

                update(speed, steering);
            } catch (Exception e) {
                println(TAG + e.getMessage());
            }
        }
    }
}
