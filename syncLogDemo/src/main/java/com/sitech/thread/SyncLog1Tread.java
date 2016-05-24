package com.sitech.thread;



import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sitech.service.SyncLogService;

/** 

 * TODO 

 * @author zhanglic

 * @date 2016年4月19日 下午5:21:44 

 * 
 

 */
@Service("syncLog1Tread")
public class SyncLog1Tread implements Runnable,Cloneable{
	
	
	private String fileDate;
	@Autowired
	private SyncLogService syncLogService;
	
	private static  Logger logger = Logger.getLogger(SyncLog1Tread.class.getName()); 
	/* (非 Javadoc) 
	
	* <p>Title: run</p> 
	
	* <p>Description: </p> 
	 
	
	* @see java.lang.Runnable#run() 
	
	*/ 
	
	public void setFileDate(String fileDate) {
		this.fileDate = fileDate;
	}

	public void run() {
		try {
			logger.info("--子线程执行开始【"+fileDate+"】");
			syncLogService.syncLog1(fileDate);
			logger.info("--子线程执行结束【"+fileDate+"】");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 浅复制
	 */
	@Override  
    public Object clone()  {  
		SyncLog1Tread syncLog1Tread = null;
		try {
			syncLog1Tread = (SyncLog1Tread) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();	
		}
        return syncLog1Tread;  
    }  
	
}
