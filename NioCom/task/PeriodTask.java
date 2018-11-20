package NioCom.task;

import NioCom.loop.IoLoop;

//周期任务 ms精度
public abstract class PeriodTask implements Task {
    private long period = 0L ; // 每隔period ms执行一次
    private boolean stopped     = true;
    private IoLoop looper ;
    
    private static class WrapperTask extends TimedTask{
    	private PeriodTask pTask  ;
        public WrapperTask(long deadline , PeriodTask pTask) {
    	    super(deadline) ;
    	    this.pTask = pTask ;
    	}
    	    //
    	public WrapperTask(long timeout,long timeu,PeriodTask pTask) {
    	    super(timeout, timeu) ;
    	    this.pTask  = pTask ;
    	}
    	    
    	public void doTask() {
    		this.pTask.doTask();
    		this.pTask.run();
    	}//
		
    }
    
    private void run() {
    	if(this.stopped == false)
    	    this.looper.addTimedTask(new WrapperTask(this.period, 1, this));
    }
    
    public void start() {
    	this.stopped  = false ;
    	this.run();
    }//
    
    public void stop() {
    	this.stopped  = true ;
    }
    
    public PeriodTask(IoLoop looper,long period) {
    	this.looper = looper ;
    	this.period = period ;
    }
    
    public abstract void doTask() ;
}
