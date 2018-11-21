package NioCom.handler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.rmi.server.SkeletonNotFoundException;

import NioCom.loop.IoLoop;
import NioCom.utils.ByteUtil;

public class StreamHandler implements IoHandler{
    private SocketChannel socketChannel ;
    private boolean closed            = true ;
    private ReadHandler readHandler   = null;
    private ByteBuffer readBuffer     = null;
    private ByteBuffer writeBuffer    = null;
    private Integer readBytesNum      = null;
    
	public StreamHandler(SocketChannel socketChannel,Integer MAX_READ_BUFFER_SIZE , Integer MAX_WRITE_BUFFER_SIZE) throws IOException {
		this.socketChannel = socketChannel ;
		this.socketChannel.configureBlocking(false);
		this.readBuffer  = ByteBuffer.allocate(MAX_READ_BUFFER_SIZE);
		this.writeBuffer = ByteBuffer.allocate(MAX_WRITE_BUFFER_SIZE);
		this.closed = false ;
	}//
	
	public StreamHandler(SocketChannel socketChannel) throws IOException {
		this(socketChannel, 1024 * 1024, 1024 * 1024) ; //default
	}//
	
	@Override
	public void handle(SelectionKey key) {
		if(this.closed)
			return ;
	    if(key.isWritable() && key.isValid())
	    	this.HandleWrite();
	    if(this.closed)
	    	return ;
	    if(key.isReadable() && key.isValid())
	    	this.handleRead();
	}//handle
    
	public void handleRead(){
		try {
			this.socketChannel.read(this.readBuffer) ;
		}catch (IOException e) {
            e.printStackTrace();
            this.close();
            return ;
		}
		if(this.readBytesNum != null) {
			if(this.readBuffer.position() < this.readBytesNum)
				return ;
			readBytes(this.readBytesNum, this.readHandler);
			this.readBytesNum = null ;
			this.readHandler  = null ;
		}//if
	}
	
	public void HandleWrite() {
		try {
			this.socketChannel.write(writeBuffer) ;
		} catch (IOException e) {
			e.printStackTrace();
			this.close();
			return ;
		}
	}
	
	public void write(byte[] bytes) {
		if(this.closed) {
			System.out.println("stream closed.");
			return ;
		}
		try {
			this.writeBuffer.put(bytes) ;
		}catch (Exception e) {
			e.printStackTrace();
			this.close();
			return ;
		}
	}//
	
	public void close() {
		if(this.closed) {
			System.out.println("stream already closed.");
			return ;
		}
		try {
			socketChannel.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
		    this.closed = true ;
		}
	}
	
	public void readBytes(int bytesNum , ReadHandler readHandler){
		if(this.closed) {
			System.out.println("stream closed.");
			return ;
		}
		if(this.readBuffer.position() < bytesNum) {
			this.readBytesNum = bytesNum ;
	        this.readHandler  = readHandler ;
	        return ;
		}//if
		byte[] bytes = new byte[bytesNum] ;
		this.readBuffer.flip() ;
		System.out.println("---------------------");
		ByteUtil.printBytebuffer(readBuffer);
	    readBuffer.get(bytes,0,bytesNum) ;
		System.out.println("---------------------");
		this.readBuffer.position(bytesNum) ;
		this.readBuffer.compact() ;
		readHandler.handleRead(bytes);
	}//

}//HandleStream