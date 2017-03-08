package com.company;


class ReceiverThread extends Thread {
    static final String TAG = "RECEIVER: ";
    private final Listener listener;

    ReceiverThread(Listener listener) {
        this.listener = listener;
    }

    void update(byte speed, byte steering) {
        listener.update(speed, steering);
    }

    interface Listener {
        void update(byte speed, byte steering);
    }
}
