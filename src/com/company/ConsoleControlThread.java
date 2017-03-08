package com.company;

import java.util.Scanner;

import static java.lang.System.in;


class ConsoleControlThread extends ReceiverThread {
    ConsoleControlThread(Listener listener) {
        super(listener);
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(in);
        byte speed, steering;
        while (true) {
            try {
                System.out.println(TAG + "Gib die Geschwindigkeit ein(0-100)");
                speed = scanner.nextByte();
                System.out.println(TAG + "Gib die Lenkung ein (0-100)");
                steering = scanner.nextByte();

                update(speed, steering);
            } catch (Exception e) {
                System.out.println(TAG + e.getMessage());
            }
        }
    }
}
