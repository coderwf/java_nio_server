package NioCom;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import NioCom.errors.ChannelRegisterd;
import NioCom.handler.IoHandler;
import NioCom.loop.IoLoop;

public class Server {
	private int port = 9999 ;
	private ServerSocketChannel serverSocketChannel ;
	private Selector selector ;
	private InetSocketAddress address ;
	private IoLoop looper ;
    public Server(int port) {
    	this.port  = port ;
    }
    
    class HandleConnection implements IoHandler{

		@Override
		public void handle(SelectionKey key) {
			if(key.isAcceptable() && key.isValid()) {
				for(int i=0;i<10;++i) {
				    try {
						SocketChannel socketChannel = serverSocketChannel.accept() ;
						if(socketChannel == null)
							continue ;
						System.out.println("welcome."+socketChannel.getLocalAddress());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}//for
			}//if
		}//handle
    }//class
    
    
    private void initServer() throws IOException, ChannelRegisterd {
    	serverSocketChannel    = ServerSocketChannel.open();
    	address                = new InetSocketAddress(this.port);
    	looper                 = IoLoop.instance() ;
    	//System.out.println(looper);
    	serverSocketChannel.bind(address);
    	serverSocketChannel.configureBlocking(false);
    	looper.register(serverSocketChannel, SelectionKey.OP_ACCEPT, new HandleConnection());
    }
    
    public void startServer() throws IOException, ChannelRegisterd {
    	this.initServer() ;
    	
    }
    
}
