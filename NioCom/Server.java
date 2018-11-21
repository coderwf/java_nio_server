package NioCom;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import NioCom.errors.ChannelRegisterd;
import NioCom.handler.IoHandler;
import NioCom.loop.IoLoop;
import NioCom.session.Session;
import NioCom.session.SessionManager;

//服务器
public class Server {
	private int port = 9999 ; //default
	private ServerSocketChannel serverSocketChannel ;
	private Selector selector ;
	private InetSocketAddress address ;
	private IoLoop looper ;
	private boolean closed = false;
	private List<Session> sessionList ;
	private SessionManager sessionManager ;
	
    public Server(int port) {
    	this.port  = port ;
    	sessionList = new ArrayList<>() ;
    }
    
    //处理客户端的连接
    class HandleConnection implements IoHandler{
		@Override
		public void handle(SelectionKey key) {
			if(key.isAcceptable() && key.isValid()) {
				//没有连接则返回null,出错则抛出异常直接停止程序
				for(int i=0;i<24;++i) {
					//System.out.println(i);
				    try {
						SocketChannel socketChannel = serverSocketChannel.accept() ;
						if(socketChannel == null)
							continue ;
						addSession(socketChannel); // 将会话添加进来
						//System.out.println("welcome."+socketChannel.getRemoteAddress());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace(); // 此处应该抛出异常 //to-do
						
					}//catch
				}//for
			}//if
		}//handle
    }//class
    
    private void addSession(SocketChannel socketChannel) {
        this.sessionManager.addSession(, session);
    }
    
    private void initServer() throws IOException, ChannelRegisterd {
    	serverSocketChannel    = ServerSocketChannel.open();
    	address                = new InetSocketAddress(this.port);
    	looper                 = IoLoop.instance() ;
    	//System.out.println(looper);
    	serverSocketChannel.bind(address);
    	serverSocketChannel.socket().setReuseAddress(true); //设置地址可以重用
    	serverSocketChannel.configureBlocking(false);
    	looper.register(serverSocketChannel, SelectionKey.OP_ACCEPT , new HandleConnection());
    }
    
    public void closeServer() {
    	if(this.closed) {
    		System.out.println("stream already closed.");
    		return ;
    	}//if
    	if(this.looper != null)
    		this.looper.close();
    	try {
			this.serverSocketChannel.close();
			this.closed  = true ;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }//
    
    
    public void startServer(){
    	try {
			this.initServer();
			this.closed  = false ;
		} catch (IOException | ChannelRegisterd e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.closed = true ;
			return ;
		}//catch
    }//startServer
}//class



