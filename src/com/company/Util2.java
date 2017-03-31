package com.company;

import java.io.Closeable;
import java.io.IOException;

class Util2 {

    static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ignore) {
            }
        }
    }

    static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignore) {
        }
    }

    static void putInt(int value, byte[] array, int offset) {
        array[offset] = (byte) (value >>> 24);
        array[offset + 1] = (byte) (value >>> 16);
        array[offset + 2] = (byte) (value >>> 8);
        array[offset + 3] = (byte) (value & 255);
    }

    static int readInt(byte[] value, int offset) {
        return value[offset] << 24 |
                (value[1 + offset] & 255) << 16 |
                (value[2 + offset] & 255) << 8 |
                value[3 + offset] & 255;
    }
}
