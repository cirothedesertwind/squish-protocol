package com.codingcrucible.squishprotocol;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.BitSet;

/**
 * Squish is a class which allows users to build Squish messages in ByteBuffers
 * by providing a series of static methods that serialize the specified
 * information into chunks specified by Squish which allows for a brief grammar
 * to quickly transfer information.
 *
 * Squish also provides a series of static methods that allow for the
 * deserialization of a Squish message.
 *
 * To write a message, allocate a ByteBuffer and write each value in sequence
 * using the put methods and pass in the ByteBuffer which backs the message you
 * would like to write to. Use the put method that closest matches the values
 * that will be transmitted to minimize unused space.
 *
 * To read a message, wrap a byte[] that contains the message into a ByteBuffer
 * and read each Squish object using the get methods. If the object being read
 * is of a different type then the get method, an error is thrown and the
 * ByteBuffer is rewound back to the point prior to the get call that was just
 * attempted, if possible.
 *
 * This code does not monitor the capacity of the ByteBuffer provided. It is up
 * to the user to maintain or designate the space required.
 *
 * Note: There are limitations of this code that result from the differences in
 * Java not having unsigned numbers.
 *
 * @author Luke Petrolekas
 * @version 0.1
 */
public final class Squish {

    private static final DateTimeFormatter LOCAL_TIME = 
            DateTimeFormatter.ofPattern("HHmmss");
    private static final DateTimeFormatter LOCAL_DATETIME =
          DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    
    private static final byte NONE = (byte) 0xA0;
    private static final byte SOME = (byte) 0xA1;

    private static final byte TRUE = (byte) 0xFF;
    private static final byte FALSE = (byte) 0x00;

    //1-127
    //Use SIZE0
    //256
    private static final byte SIZE1 = (byte) 0xB1;
    //65536
    private static final byte SIZE2 = (byte) 0xB2;
    //2^31-1
    private static final byte SIZE4 = (byte) 0xB4;

    public static final void putNone(ByteBuffer b) {
        b.put(NONE);
    }

    public static final void putSome(ByteBuffer b) {
        b.put(SOME);
    }

    public static final boolean hasSome(ByteBuffer b) {
        b.mark();

        byte v = b.get();

        if (v == SOME)
            return true;
        else if (v == NONE)
            return false;
        else {
            b.reset();
            throw new IllegalStateException("The data type stored is of type " + v);
        }
    }

    public static final void put(ByteBuffer b, boolean bool) {
        if (bool)
            b.put(TRUE);
        else
            b.put(FALSE);
    }

    public static final boolean getBoolean(ByteBuffer b) {
        b.mark();

        byte v = b.get();

        if (v == TRUE)
            return true;
        else if (v == FALSE)
            return false;
        else {
            b.reset();
            throw new IllegalStateException("The data type stored is of type " + v);
        }
    }

    public static final void putUByte(ByteBuffer b, int i) {
        if (i < 0 || i > 255)
            throw new IllegalArgumentException("This is used for numbers from 0 to 2^8-1");

        b.put((byte) i);
    }

    public static final void putUShort(ByteBuffer b, int i) {
        if (i < 0 || i > 65535)
            throw new IllegalArgumentException("This is used for numbers from 0 to 2^16-1");

        b.putShort((short) i);
    }

    public static final void putUInt(ByteBuffer b, long i) {
        if (i < 0 || i > (1L << 31))
            throw new IllegalArgumentException("This is used for numbers from 0 to 2^31-1");

        b.putInt((int) i);
    }

    public static final void putULong(ByteBuffer b, long i) {
        if (i < 0)
            throw new IllegalArgumentException("This is used for numbers from 0 to 2^63-1. "
                    + "Use BigInteger version for numbers >= 2^63 to 2^64-1.");

        b.putLong(i);
    }

    /**
     * It is advisable to only use this for numbers that are >= 2^63
     *
     * @param b
     * @param i
     */
    public static final void putULong(ByteBuffer b, BigInteger i) {
        if (i.compareTo(BigInteger.ZERO) >= 0)
            throw new IllegalArgumentException("This is used for numbers from 0 to 2^64-1 (Preferably >= 2^63).");

        b.putLong(i.longValueExact());
    }

    public static final void put(ByteBuffer b, byte byt) {
        b.put(byt);
    }

    public static final void put(ByteBuffer b, short i) {
        b.putShort(i);
    }

    public static final void put(ByteBuffer b, int i) {
        b.putInt(i);
    }

    public static final void put(ByteBuffer b, long i) {
        b.putLong(i);
    }

    public static final byte getByte(ByteBuffer b) {
        return b.get();
    }

    public static final short getShort(ByteBuffer b) {
        return b.getShort();
    }

    public static final int getInt(ByteBuffer b) {
        return b.getInt();
    }

    public static final long getLong(ByteBuffer b) {
        return b.getLong();
    }

    public static final int getUByte(ByteBuffer b) {
        return (0xFF & b.get());
    }

    public static final int getUShort(ByteBuffer b) {
        return (0xFFFF & b.getShort());
    }

    public static final long getUInt(ByteBuffer b) {
        return (0xFFFFFFL & b.getInt());
    }

    /**
     * It is advisable to only use this for numbers that are >= 2^63
     *
     * @param b
     * @return
     */
    public static final BigInteger getLargeULong(ByteBuffer b) {
        byte[] a = new byte[Long.BYTES];
        b.get(a);
        return new BigInteger(a);
    }

    public static final void putVarInt(ByteBuffer b, int size) {
        if (size < 0)
            throw new IllegalArgumentException("Size must be greater than 0.");
        else if (size <= 127)
            b.put((byte) size);
        else if (size <= 256) {
            b.put(SIZE1);
            b.put((byte) size);
        } else if (size <= 65536) {
            b.put(SIZE2);
            b.putShort((short) size);
        } else {
            b.put(SIZE4);
            b.putInt(size);
        }
    }

    public static final int getVarInt(ByteBuffer b) {
        int size = -1;

        b.mark();
        byte format = b.get();

        if (((format >> 7) & 1) == 0)
            return 0x7F & format;
        else if (format == SIZE1)
            return 0xFF & b.get();
        else if (format == SIZE2)
            return 0xFFFF & b.getShort();
        else if (format == SIZE4)
            size = b.getInt();
        else {
            b.reset();
            throw new IllegalStateException("The size data is unreadable. "
                    + "The data type stored is of type " + format);
        }

        return size;
    }

    public static final void putString(ByteBuffer b, String s) {
        byte[] stringAsBytes = s.getBytes(StandardCharsets.UTF_8);

        putVarInt(b, stringAsBytes.length);
        b.put(stringAsBytes);
    }

    public static final String getString(ByteBuffer b) {
        int lengthInBytes = getVarInt(b);

        byte[] dst = new byte[lengthInBytes];
        b.get(dst);

        return new String(dst, StandardCharsets.UTF_8);
    }

    public static final void put(ByteBuffer b, byte[] a) {
        putVarInt(b, a.length);
        b.put(a);
    }

    public static final void put(ByteBuffer b, short[] a) {
        putVarInt(b, a.length);
        b.asShortBuffer().put(a);
    }

    public static final void put(ByteBuffer b, int[] a) {
        putVarInt(b, a.length);
        b.asIntBuffer().put(a);
    }

    public static final void put(ByteBuffer b, long[] a) {
        putVarInt(b, a.length);
        b.asLongBuffer().put(a);
    }

    public static final void put(ByteBuffer b, float[] a) {
        putVarInt(b, a.length);
        b.asFloatBuffer().put(a);
    }

    public static final void put(ByteBuffer b, double[] a) {
        putVarInt(b, a.length);
        b.asDoubleBuffer().put(a);
    }

    public static final void putUByteArray(ByteBuffer b, int[] a) {
        putVarInt(b, a.length);
        for (int i = 0; i < a.length; i++)
            putUByte(b, a[i]);
    }

    public static final void putUShortArray(ByteBuffer b, int[] a) {
        putVarInt(b, a.length);
        for (int i = 0; i < a.length; i++)
            putUShort(b, a[i]);
    }

    public static final void putUIntArray(ByteBuffer b, long[] a) {
        putVarInt(b, a.length);
        for (int i = 0; i < a.length; i++)
            putUInt(b, a[i]);
    }

    public static final void putULongArray(ByteBuffer b, long[] a) {
        putVarInt(b, a.length);
        for (int i = 0; i < a.length; i++)
            putULong(b, a[i]);
    }

    public static final void putULongArray(ByteBuffer b, BigInteger[] a) {
        putVarInt(b, a.length);
        for (BigInteger i : a)
            putULong(b, i);
    }

    public static final void putVarIntArray(ByteBuffer b, int[] a) {
        putVarInt(b, a.length);
        for (int i = 0; i < a.length; i++)
            putVarInt(b, a[i]);
    }

    public static final byte[] getByteArray(ByteBuffer b) {
        int length = getVarInt(b);

        byte[] dst = new byte[length];
        b.get(dst);

        return dst;
    }

    public static final short[] getShortArray(ByteBuffer b) {
        int length = getVarInt(b);

        short[] dst = new short[length];
        b.asShortBuffer().get(dst);

        return dst;
    }

    public static final int[] getIntArray(ByteBuffer b) {
        int length = getVarInt(b);

        int[] dst = new int[length];
        b.asIntBuffer().get(dst);

        return dst;
    }

    public static final long[] getLongArray(ByteBuffer b) {
        int length = getVarInt(b);

        long[] dst = new long[length];
        b.asLongBuffer().get(dst);

        return dst;
    }

    public static final float[] getFLoatArray(ByteBuffer b) {
        int length = getVarInt(b);

        float[] dst = new float[length];
        b.asFloatBuffer().get(dst);

        return dst;
    }

    public static final double[] getDoubleArray(ByteBuffer b) {
        int length = getVarInt(b);

        double[] dst = new double[length];
        b.asDoubleBuffer().get(dst);

        return dst;
    }

    public static final int[] getUByteArray(ByteBuffer b) {
        int length = getVarInt(b);
        int[] dst = new int[length];

        for (int i = 0; i < dst.length; i++)
            dst[i] = getUByte(b);

        return dst;
    }

    public static final int[] getUShortArray(ByteBuffer b) {
        int length = getVarInt(b);
        int[] dst = new int[length];

        for (int i = 0; i < dst.length; i++)
            dst[i] = getUShort(b);

        return dst;
    }

    public static final long[] getUIntArray(ByteBuffer b) {
        int length = getVarInt(b);
        long[] dst = new long[length];

        for (int i = 0; i < dst.length; i++)
            dst[i] = getUInt(b);

        return dst;
    }

    public static final BigInteger[] getULongArray(ByteBuffer b) {
        int length = getVarInt(b);
        BigInteger[] dst = new BigInteger[length];

        for (int i = 0; i < dst.length; i++)
            dst[i] = getLargeULong(b);

        return dst;
    }

    public static final int[] getVarIntArray(ByteBuffer b) {
        int length = getVarInt(b);
        int[] dst = new int[length];

        for (int i = 0; i < dst.length; i++)
            dst[i] = getVarInt(b);

        return dst;
    }

    public static final void putStringArray(ByteBuffer b, String[] a) {
        putVarInt(b, a.length);
        for (int i = 0; i < a.length; i++)
            putString(b, a[i]);
    }

    public static final String[] getStringArray(ByteBuffer b) {
        int length = getVarInt(b);
        String[] dst = new String[length];

        for (int i = 0; i < dst.length; i++)
            dst[i] = getString(b);

        return dst;
    }

    public static final void putBitArray(ByteBuffer b, BitSet bits) {
        byte[] bitSetAsBytes = bits.toByteArray();

        putVarInt(b, bitSetAsBytes.length);
        putVarInt(b, bits.length());
        b.put(bitSetAsBytes);
    }

    public static final BitSet getBitArray(ByteBuffer b) {
        int lengthInBytes = getVarInt(b);
        int nBits = getVarInt(b);

        byte[] bitSetAsBytes = new byte[lengthInBytes];
        b.get(bitSetAsBytes);

        return BitSet.valueOf(bitSetAsBytes).get(0, nBits);
    }

    /**
     * Writes a binary.
     *
     * @param b,
     *
     * @param bin the remaining bytes of the bin buffer as the binary
     */
    public static final void put(ByteBuffer b, ByteBuffer bin) {
        putVarInt(b, bin.remaining());
        b.put(bin);
    }

    public static final ByteBuffer get(ByteBuffer b) {
        int lengthInBytes = getVarInt(b);

        byte[] dst = new byte[lengthInBytes];
        b.get(dst);

        return ByteBuffer.wrap(dst);
    }

    public static final void put(ByteBuffer b, LocalDate t) {
        byte[] stringAsBytes = t.format(DateTimeFormatter.BASIC_ISO_DATE)
                .getBytes(StandardCharsets.US_ASCII);
        b.put(stringAsBytes);
    }

    public static final LocalDate getLocalDate(ByteBuffer b) {
        byte[] dst = new byte[8];
        b.get(dst);
        return LocalDate.parse(new String(dst, StandardCharsets.US_ASCII),
                DateTimeFormatter.BASIC_ISO_DATE);
    }
    
    public static final void putEncodedLocalDate(ByteBuffer b, LocalDate t){
        int date = Integer.parseInt(t.format(DateTimeFormatter.BASIC_ISO_DATE));
        put(b, date);
    }

    public static final LocalDate getEncodedLocalDate(ByteBuffer b) {
        int date = getInt(b);
        String dateString = String.format("%08d", date);
        return LocalDate.parse(dateString, DateTimeFormatter.BASIC_ISO_DATE);
    }
    
     public static final void put(ByteBuffer b, LocalTime t){
         byte[] stringAsBytes = t.format(LOCAL_TIME).getBytes(StandardCharsets.US_ASCII);
         b.put(stringAsBytes);
     }
    
     public static final LocalTime getLocalTime(ByteBuffer b){
         byte[] dst = new byte[6];
         b.get(dst);
         return LocalTime.parse(new String(dst, StandardCharsets.US_ASCII), 
         LOCAL_TIME);
     }
      
    public static final void putSimpleLocalDateTime(ByteBuffer b, LocalDateTime t){
        byte[] stringAsBytes = t.format(LOCAL_DATETIME)
        .getBytes(StandardCharsets.US_ASCII);
        b.put(stringAsBytes);
    }
    
    public static final LocalDateTime getSimpleLocalDateTime(ByteBuffer b){
        byte[] dst = new byte[14];
        b.get(dst);
        return LocalDateTime.parse(new String(dst, StandardCharsets.US_ASCII), 
        LOCAL_DATETIME);
    }
    
    public static final void putISOLocalDateTime(ByteBuffer b, LocalDateTime t) {
        byte[] stringAsBytes = t.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                .getBytes(StandardCharsets.US_ASCII);
        putVarInt(b, stringAsBytes.length);
        b.put(stringAsBytes);
    }

    public static final LocalDateTime getISOLocalDateTime(ByteBuffer b) {
        int lengthInBytes = getVarInt(b);

        byte[] dst = new byte[lengthInBytes];
        b.get(dst);

        return LocalDateTime.parse(new String(dst, StandardCharsets.US_ASCII), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

}
