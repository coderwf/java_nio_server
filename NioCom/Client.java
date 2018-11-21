package NioCom;

import java.io.IOException;
import java.net.Socket;

import NioCom.utils.ByteUtil;

public class Client {
    public static void main(String []args) {
        try {
			Socket socket = new Socket("127.0.0.1", 9999) ;
			byte[] msgBytes = "i love you . 王东威".getBytes("UTF-8") ;
			byte[] lengthBytes = ByteUtil.integerToBytes(msgBytes.length);
			ByteUtil.printBytes(lengthBytes);
			socket.getOutputStream().write(lengthBytes);
			socket.getOutputStream().write(msgBytes);
			while(true) {}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }//
}
