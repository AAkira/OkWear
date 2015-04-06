package jp.android.a.akira.library.okwear.util;

import java.nio.ByteBuffer;

public class ParseByteArray {

    public static byte[] fromString(final String value) {
        return value.getBytes();
    }

    public static byte[] fromChar(final char value) {
        final int arraySize = Character.SIZE / Byte.SIZE;
        final ByteBuffer buffer = ByteBuffer.allocate(arraySize);
        return buffer.putChar(value).array();
    }

    public static byte[] fromShort(final short value) {
        final int arraySize = Short.SIZE / Byte.SIZE;
        final ByteBuffer buffer = ByteBuffer.allocate(arraySize);
        return buffer.putShort(value).array();
    }

    public static byte[] fromInt(final int value) {
        final int arraySize = Integer.SIZE / Byte.SIZE;
        final ByteBuffer buffer = ByteBuffer.allocate(arraySize);
        return buffer.putInt(value).array();
    }

    public static byte[] fromLong(final long value) {
        final int arraySize = Long.SIZE / Byte.SIZE;
        final ByteBuffer buffer = ByteBuffer.allocate(arraySize);
        return buffer.putLong(value).array();
    }

    public static byte[] fromFloat(final float value) {
        final int arraySize = Float.SIZE / Byte.SIZE;
        final ByteBuffer buffer = ByteBuffer.allocate(arraySize);
        return buffer.putFloat(value).array();
    }

    public static byte[] fromDouble(final double value) {
        int arraySize = Double.SIZE / Byte.SIZE;
        ByteBuffer buffer = ByteBuffer.allocate(arraySize);
        return buffer.putDouble(value).array();
    }
}
