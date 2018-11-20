package NioCom.handler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import NioCom.loop.IoLoop;

public class HandleStream implements IoHandler{
    private IoLoop looper ;
    private SocketChannel socketChannel ;
    private boolean closed     = true ;
    private IoHandler readHandler ;
    private ByteBuffer readBuffer = null;
    private ByteBuffer writeBuffer  = null;
	public HandleStream(SocketChannel socketChannel) throws IOException {
		this.socketChannel = socketChannel ;
		this.socketChannel.configureBlocking(false);
		looper = IoLoop.instance() ;
		readBuffer = ByteBuffer.allocate(1024 * 1024);
		writeBuffer = ByteBuffer.allocate(1024 * 1024);
		this.closed = false ;
	}//
	
	@Override
	public void handle(SelectionKey key) {
	    if(key.isWritable() && key.isValid())
	    	this.HandleWrite();
	    if(key.isReadable() && key.isValid())
	    	this.handleRead();
	}//handle
    
	public void handleRead() {
		
	}
	
	public void HandleWrite() {
		
	}
	
	public void write() {
		
	}//
	
	public void close() {
		
	}
	

}//HandleStream