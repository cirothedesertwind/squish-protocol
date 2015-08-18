/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codingcrucible.squishprotocol;

import java.nio.ByteBuffer;
import java.util.BitSet;
import java.util.Map;

public interface SquishMap extends Map{

    void writeMap(ByteBuffer b);
    
    void put(byte k, boolean v);
    void put(byte k, byte v);
    void put(byte k, short v);
    void put(byte k, int v);
    void put(byte k, long v);
    void put(byte k, float v);
    void put(byte k, double v);
    void put(byte k, String v);
    
    void put(byte k, byte[] v);
    void put(byte k, short[] v);
    void put(byte k, int[] v);
    void put(byte k, long[] v);
    void put(byte k, float[] v);
    void put(byte k, double[] v);
    void put(byte k, String[] v);
    
    void put(byte k, BitSet v);
    void put(byte k, ByteBuffer v);
    
    void put(short k, boolean v);
    void put(short k, byte v);
    void put(short k, short v);
    void put(short k, int v);
    void put(short k, long v);
    void put(short k, float v);
    void put(short k, double v);
    void put(short k, String v);
    
    void put(short k, byte[] v);
    void put(short k, short[] v);
    void put(short k, int[] v);
    void put(short k, long[] v);
    void put(short k, float[] v);
    void put(short k, double[] v);
    void put(short k, String[] v);
    
    void put(short k, BitSet v);
    void put(short k, ByteBuffer v);
    
    void put(int k, boolean v);
    void put(int k, byte v);
    void put(int k, short v);
    void put(int k, int v);
    void put(int k, long v);
    void put(int k, float v);
    void put(int k, double v);
    void put(int k, String v);
    
    void put(int k, byte[] v);
    void put(int k, short[] v);
    void put(int k, int[] v);
    void put(int k, long[] v);
    void put(int k, float[] v);
    void put(int k, double[] v);
    void put(int k, String[] v);
    
    void put(int k, BitSet v);
    void put(int k, ByteBuffer v);
    
    void put(long k, boolean v);
    void put(long k, byte v);
    void put(long k, short v);
    void put(long k, int v);
    void put(long k, long v);
    void put(long k, float v);
    void put(long k, double v);
    void put(long k, String v);
    
    void put(long k, byte[] v);
    void put(long k, short[] v);
    void put(long k, int[] v);
    void put(long k, long[] v);
    void put(long k, float[] v);
    void put(long k, double[] v);
    void put(long k, String[] v);
    
    void put(long k, BitSet v);
    void put(long k, ByteBuffer v);
    
    void put(String k, boolean v);
    void put(String k, byte v);
    void put(String k, short v);
    void put(String k, int v);
    void put(String k, long v);
    void put(String k, float v);
    void put(String k, double v);
    void put(String k, String v);
    
    void put(String k, byte[] v);
    void put(String k, short[] v);
    void put(String k, int[] v);
    void put(String k, long[] v);
    void put(String k, float[] v);
    void put(String k, double[] v);
    void put(String k, String[] v);
    
    void put(String k, BitSet v);
    void put(String k, ByteBuffer v);
    
}
