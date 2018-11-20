package NioCom;

import java.io.IOException;
import java.net.Socket;

public class Client {
    public static void main(String []args) {
        try {
			Socket socket = new Socket("127.0.0.1", 9999) ;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }//
}
