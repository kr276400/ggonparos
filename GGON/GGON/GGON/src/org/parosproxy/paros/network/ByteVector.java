package org.parosproxy.paros.network;

public class ByteVector {

    private static final int INITIAL_SIZE = 4096;
    private byte[] buf = null;
    private int bufLen = 0;
    private boolean changed = true;
    
    public ByteVector() {
        buf = new byte[INITIAL_SIZE];
            
    }
    
    public ByteVector(int capacity) {
        buf = new byte[capacity];
    }

    
    void ensureCapacity(int minimumCapacity) {

        if (buf.length - bufLen >= minimumCapacity) {
            return;
        }
        
        int expandLength = buf.length * 2;
        
        if (expandLength - bufLen < minimumCapacity) {
            expandLength = bufLen + minimumCapacity;
        }
        
        byte[] newBuf = new byte[expandLength];
        System.arraycopy(buf, 0, newBuf, 0, bufLen);
        buf = newBuf;
    }
    
    public synchronized void append(byte[] b, int offset, int length) {

        ensureCapacity(length);
        System.arraycopy(b, offset, buf, bufLen, length);
        bufLen += length;
        changed = true;
    }

    public void append(byte[] b, int length) {
        append(b, 0, length);
        
    }
    
    public void append(byte[] b) {
        append(b, 0, b.length);
    }

    public byte[] getBytes() {
        if (!changed) {
            return buf;
        }
        
        byte[] newBuf = new byte[bufLen];
        System.arraycopy(buf, 0, newBuf, 0, bufLen);
        buf = newBuf;
        return buf;
    }
    
    public int length() {
        return bufLen;
    }
    
    public void setLength(int length) {

        if (length <= bufLen) {
            bufLen = length;
            return;
        } else {
            byte[] newBuf = new byte[length];
            System.arraycopy(buf, 0, newBuf, 0, bufLen);
            buf = newBuf;
        }

    }
}
