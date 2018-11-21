package NioCom.session;

import java.io.UnsupportedEncodingException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import NioCom.errors.ChannelRegisterd;
import NioCom.handler.ReadHandler;
import NioCom.handler.StreamHandler;
import NioCom.loop.IoLoop;
import NioCom.utils.ByteUtil;

//持有一个StreamHandler
abstract public class Session{
	
	private SocketChannel socketChannel =   null ;
	private boolean closed              =   true ;
	private StreamHandler streamHandler =   null ;
	private IoLoop looper               =   null ;
	private ReadHandler readLengthHandler;
	private ReadHandler processHandler   ;
	private Object attachment            ;  // 关于session的附加信息,方便查找session
	private StringBuilder  sBuilder      ;
	
	private class ReadLengthHandler implements ReadHandler{

		@Override
		public void handleRead(byte[] bytes) {
			// TODO Auto-generated method stub
			int msgLength = ByteUtil.bytesToInteger(bytes) ;
			ByteUtil.printBytes(bytes);
			ReadHandler processHandler  = getProcessHandler();
			StreamHandler streamHandler = getStreamHandler() ;
			streamHandler.readBytes(msgLength, processHandler);
		}
	}//
	
	private class ProcessHandler implements ReadHandler{

		@Override
		public void handleRead(byte[] bytes) {
			// TODO Auto-generated method stub
			processMessageBytes(bytes);
			
		}
		
	}//
	
	private void processMessageBytes(byte[] bytes) {
		this.processMsgBytes(bytes);
		this.streamHandler.readBytes(4,this.readLengthHandler);
	}
	
    private ReadHandler getProcessHandler() {
  	    return this.processHandler ;
    }
  
    private StreamHandler getStreamHandler() {
  	    return this.streamHandler ;
    }
  
    //可以重写
    abstract public void processMsgBytes(byte[]bytes) ;//
  
    private void write(byte[]bytes) {
	    byte[] lengthBytes = ByteUtil.integerToBytes(bytes.length);
	    this.streamHandler.write(lengthBytes);
        this.streamHandler.write(bytes);
    }
  
    //utf-8编码
    public void write(String msg) {
        this.sBuilder.append(msg);
    }//
 
    public void flush(String msg) {
	    this.sBuilder.append(msg) ;
	    String mString = this.sBuilder.toString() ;
	    this.sBuilder.delete(0, sBuilder.length());
	    try {
			byte[] msgBytes = mString.getBytes("UTF-8");
			this.write(msgBytes);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ;
		}
	    
    }
    
    public void flush() {
	    if(sBuilder.length() == 0)
	    	return ;
	    this.flush("");
    }
    
    abstract protected void initialAttachment() ;  //需要覆盖
    
    public void start() {
    	this.closed   = false ;
    	this.streamHandler.readBytes(4, this.readLengthHandler);
    }
	public Session(SocketChannel socketChannel) {
		try {
			this.streamHandler = new StreamHandler(socketChannel) ;
			this.looper        = IoLoop.instance();
			this.looper.register(socketChannel, SelectionKey.OP_READ | SelectionKey.OP_WRITE, streamHandler);
			this.readLengthHandler = new ReadLengthHandler();
			this.processHandler    = new ProcessHandler() ;
			initialAttachment(); // 初始化一个attachment
			this.sBuilder          = new StringBuilder(1024);
			
		}catch (Exception | ChannelRegisterd e) {
			// TODO: handle exception
			if(this.streamHandler != null)
				this.streamHandler.close();
			if(this.looper != null)
				this.looper.stop();
			this.closed  = true ;
		}
	}//public
	
	public Object getAttachment() {
		return this.attachment ;
	}
	
}//class