package NioCom;

import java.io.IOException;
import java.nio.ByteBuffer;

import NioCom.errors.ChannelRegisterd;
import NioCom.loop.IoLoop;
import NioCom.utils.ByteUtil;

public class Main {
    public static void main(String []args) {        
    	Server server = new Server(9999) ;
        server.startServer();
        IoLoop.instance().startIoLoop();
    }//main
}//class
