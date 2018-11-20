package NioCom.task;

//定时任务 ms精度
public abstract class TimedTask implements Task {
    public long deadline  = 0L ;
    
    public TimedTask(long deadline) {
    	this.deadline  = deadline ;
    }
    //
    
    public TimedTask(long timeout,long timeu) {
    	this(System.currentTimeMillis() + timeout * timeu) ;
    }
    public abstract void doTask() ;
}
