package NioCom.loop;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import NioCom.errors.ChannelRegisterd;
import NioCom.handler.IoHandler;
import NioCom.task.Task;
import NioCom.task.TimedTask;

//循环处理各种事件
public class IoLoop {
	//selector
	private Selector selector ;
	//已经注册的通道的集合
	private Set<SelectableChannel>  registeredChannelSets ;
    //任务集合
	private List<Task>              taskList              ;
    //定时任务集合
	private List<TimedTask>              timedTaskList    ;
    
	//处理器map
	private Map<SelectableChannel, IoHandler> ioHandlerMap  ;
	
	//单例模式
	private static IoLoop looper = null;
	public static IoLoop instance() throws IOException {
		if(looper == null) {
			looper = new IoLoop();
		}
		return looper ;
	}
	
    private IoLoop() throws IOException {
    	selector              = Selector.open();
    	taskList              = new ArrayList<>();
    	registeredChannelSets = new HashSet<>() ;
    	timedTaskList         = new ArrayList<>() ;
    	ioHandlerMap          = new HashMap<>() ;
    }
    
    public void register(SelectableChannel channel,int events,IoHandler ioHandler) throws ChannelRegisterd, ClosedChannelException {
    	if(registeredChannelSets.contains(channel))
    		throw new ChannelRegisterd();
    	channel.register(this.selector, events);
    	registeredChannelSets.add(channel) ;
    	ioHandlerMap.put(channel, ioHandler);
    }//
    
    public void unregister(SelectableChannel channel) {
    	registeredChannelSets.remove(channel) ;
    	ioHandlerMap.remove(channel) ;
        //to-do //
    }
    
    public void modifyEvent(SelectableChannel channel , int events) {
    	if(!registeredChannelSets.contains(channel))
    		return ;
    	return ;
    }
    public void addTask(Task task) {
    	this.taskList.add(task);
    }//
    
    public void removeTask(Task task) {
    	this.taskList.remove(task);
    }
    
    public void addTimedTask(TimedTask task) {
        this.timedTaskList.add(task) ;
    }
    
    public void removeTimedTask(TimedTask task) {
    	this.timedTaskList.remove(task);
    }
    
    public void startIoLoop() {
    	while(true) {
    	    long timeout  = 100L ;
    	    List<Integer> list = new ArrayList<>();
    	    while(!taskList.isEmpty()) {
    	    	Task task = taskList.get(0) ;
    	    	taskList.remove(0) ;
    	    	task.doTask();
    	    }//while
    	    long now = System.currentTimeMillis() ;
    	    List<TimedTask> tempTTask = new ArrayList<>();
    	    tempTTask.addAll(timedTaskList) ;
    	    timedTaskList.clear();
    	    for(TimedTask task :tempTTask) {
    	        if(now > task.deadline) 
    	            task.doTask();  //if
    	        else
    	    	    timedTaskList.add(task); // else
    	    }//for
    	    //io
    	    Set<SelectionKey> keysSet = null;
    	    try {
    	    	//System.out.println("start select.");
				this.selector.select(timeout) ;
				keysSet = this.selector.selectedKeys();
				//System.out.println("select over.");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue ;
			}
    	    Iterator<SelectionKey> it = keysSet.iterator();
    	    while (it.hasNext()) {
    	    	System.out.println("-----------------------------");
                SelectionKey selectionKey = it.next();
                //拿到handler然后处理
                SelectableChannel channel = selectionKey.channel();
                IoHandler ioHandler       = ioHandlerMap.get(channel) ;
                //System.out.println(ioHandler);
                ioHandler.handle(selectionKey); //处理io
                it.remove();
    	    }//while
    	}//while
    }//
    
}//class
