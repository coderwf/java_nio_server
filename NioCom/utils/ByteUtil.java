package NioCom.utils;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class ByteUtil {
	public static int ByteLength     =  8 ;
	public static int ByteMask       =  255 ;
	
    public static int bytesToInteger(byte[]bytes) {
    	int num = 0 ;
    	num |= ( bytes[0] & ByteMask) ;
    	for(int i = 1 ; i< 4 ;++i) {
    	    num <<= ByteLength ;
    	    num |=( bytes[i] & ByteMask) ;
    	}
    	return num ;
    }
    
    public static byte[] integerToBytes(int num) {
    	byte[] bytes = new byte[4];
    	for(int i=3 ; i>= 0 ;--i) {
    		bytes[i]  = (byte)(num & ByteMask);
    		num       >>>= ByteLength ;
    	}//
    	return bytes ;
    }
    
    public static void printBytes(byte []bytes) {
    	List<Byte> byteList = new ArrayList<>();
    	for(int i= 0 ;i < bytes.length ; i++)
    		byteList.add(bytes[i]) ;
        System.out.println(byteList);
    }
    
    public static byte[] longToBytes(long num) {
    	byte[] bytes = new byte[8];
    	for(int i=7 ; i>= 0 ;--i) {
    		bytes[i]  = (byte)(num & ByteMask);
    		num       >>>= ByteLength ;
    	}//
    	return bytes ;
    }
    
    public static long bytesToLong(byte[]bytes) {
    	long num = 0 ;
    	num |= ( bytes[0] & ByteMask) ;
    	for(int i = 1 ; i< 8 ;++i) {
    	    num <<= ByteLength ;
    	    num |=( bytes[i] & ByteMask) ;
    	}
    	return num ;
    }
    
    public static void printBytebuffer(ByteBuffer byteBuffer) {
    	System.out.println("position :"+ byteBuffer.position());
    	System.out.println("limit :" + byteBuffer.limit());
    }//
}
