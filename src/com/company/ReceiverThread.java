package com.company;


class ReceiverThread extends Thread {
    static final String TAG = "RECEIVER: ";

    private final SendCommands sender;

    ReceiverThread(SendCommands sender) {
        this.sender = sender;
    }

    void update(byte speed, byte steering) {
        sender.newData(speed, steering);
    }

    int getPingToRover() {
        return sender.getPing();
    }
}
