package NioCom;

import java.io.IOException;

import NioCom.errors.ChannelRegisterd;
import NioCom.loop.IoLoop;

public class Main {
    public static void main(String []args) {
        IoLoop looper = null ;
        try {
  	         looper = IoLoop.instance() ;
  	    } catch (IOException e) {
  		// TODO Auto-generated catch block
  		     e.printStackTrace();
  		     return ;
  	    }
        Server server = new Server(9999) ;
        //System.out.println(looper);
        try {
			server.startServer();
		} catch (IOException | ChannelRegisterd e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        looper.startIoLoop();
    }//main
}//class
