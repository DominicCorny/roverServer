package com.company;

import java.io.Closeable;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

class Util {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm:ss:SSS");
    private static Date date = new Date();

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

    static void println(String s) {
        date.setTime(System.currentTimeMillis());
        System.out.println(dateFormat.format(date) + '\t' + s);
    }
}
