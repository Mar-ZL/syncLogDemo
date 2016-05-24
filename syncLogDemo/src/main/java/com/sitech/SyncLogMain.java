package com.sitech;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.sitech.service.BatchService;
import com.sitech.service.SyncLogService;
import com.sitech.service.impl.SyncLogServiceImpl;
import com.sitech.thread.SyncLog1Tread;
import com.sitech.thread.SyncLog2Tread;
import com.sitech.util.PropertyUtils;

/** 

 * TODO 

 * @author zhanglic

 * @date 2016年4月19日 下午8:32:02 

 * 
 

 */

public class SyncLogMain {
	
	private static  Logger logger = Logger.getLogger(SyncLogServiceImpl.class); 
	
	private static DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHH");
	private static DateFormat dateFormatyyyyMMdd = new SimpleDateFormat("yyyyMMdd");
	
	public static void main(String[] args){
		if(!chkArgs(args))
			return;
		try{
			
			String syncType = args[0];
			String gatherType = args[1];
			String date = args[2];
			ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext.xml");
			
			SyncLogService syncLogService = (SyncLogService)applicationContext.getBean("syncLogService");
			
			
			ExecutorService newFixedThreadPool = null;
			//按小时同步
			if("1".equals(syncType)){
				logger.info("----入库小时日志表开始【"+date+"】(按小时同步)");
				int SYNC_OF_HOUR_POOL_SIZE = PropertyUtils.getInt("SYNC_OF_HOUR_POOL_SIZE");
				newFixedThreadPool = Executors.newFixedThreadPool(SYNC_OF_HOUR_POOL_SIZE);
				
				String fileDate = date;
				logger.info("----入库小时日志表1(线程池)执行【"+fileDate+"】");
				
				SyncLog1Tread syncLog1Tread = (SyncLog1Tread)applicationContext.getBean("syncLog1Tread");
				syncLog1Tread.setFileDate(fileDate);
				newFixedThreadPool.execute(syncLog1Tread);
				logger.info("----入库小时日志表2(线程池)执行【"+fileDate+"】");
				SyncLog2Tread syncLog2Tread = (SyncLog2Tread)applicationContext.getBean("syncLog2Tread");
				syncLog2Tread.setFileDate(fileDate);
				newFixedThreadPool.execute(syncLog2Tread);
				logger.info("----关闭（按小时同步）线程池【"+fileDate+"】");
				newFixedThreadPool.shutdown();
				logger.info("----入库小时日志表结束【"+date+"】(按小时同步)");
				
			}
			
			
			//按天同步,提交任务到线程池
			if("2".equals(syncType)){
				int SYNC_OF_DAY_POOL_SIZE = PropertyUtils.getInt("SYNC_OF_DAY_POOL_SIZE");
				
				newFixedThreadPool = Executors.newFixedThreadPool(SYNC_OF_DAY_POOL_SIZE);
				logger.info("----入库小时日志表开始【"+date+"】(按天同步)");
				
				for(int i=0;i<24;i++){
					String fileDate = "";
					if(i<10){
						fileDate = date+"0"+i;
					}else{
						fileDate = date+i;
					}
					
					
					
					logger.info("----入库小时日志表1(线程池)提交【"+fileDate+"】");
					
					SyncLog1Tread syncLog1Tread = (SyncLog1Tread)applicationContext.getBean("syncLog1Tread");
					
					SyncLog1Tread syncLog11Tread = (SyncLog1Tread)syncLog1Tread.clone();
					syncLog11Tread.setFileDate(fileDate);				
					newFixedThreadPool.execute(syncLog11Tread);
					
					logger.info("----入库小时日志表2(线程池)提交【"+fileDate+"】");
					
					SyncLog2Tread syncLog2Tread = (SyncLog2Tread)applicationContext.getBean("syncLog2Tread");
					
					SyncLog2Tread SyncLog22Tread = (SyncLog2Tread)syncLog2Tread.clone();
					SyncLog22Tread.setFileDate(fileDate);
					newFixedThreadPool.execute(SyncLog22Tread);
					
				}
				logger.info("----关闭（按天同步）线程池【"+date+"】");
				newFixedThreadPool.shutdown();
				logger.info("----入库小时日志表结束【"+date+"】(按天同步)");
				
			}
			
			if("Y".equals(gatherType)){
				//判断线程池的任务是否执行完
				if(newFixedThreadPool!=null){
					String day = date.substring(0,8);
					
					boolean isTerminated = newFixedThreadPool.isTerminated();
					
					while (!isTerminated) {
						logger.info("等待线程池所有任务执行完成 【isTerminated=" + isTerminated+"】......");
						isTerminated = newFixedThreadPool.isTerminated();
						Thread.sleep(2000);
					}
					logger.info("线程池所有任务执行完成 【isTerminated=" + isTerminated+"】!");
					logger.info("--汇总每天日志数据开始【"+day+"】");
					syncLogService.gatherByday(day);
					logger.info("--汇总每天日志数据结束【"+day+"】");
					
				}
				
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public static boolean chkArgs(String[] args) {
		
		if(args.length!=3){
			logger.info("请输入3个参数[1:类型(0:不同步数据;1:按每小时同步;2:按天同步);2:是否每天汇总(Y:汇总， N:不汇总);3:同步或者汇总时间(小时同步:YYYYMMDDHH)]");
			return false;
		}
		//校验第一个参数
		if(!args[0].equals("0")&&!args[0].equals("1")&&!args[0].equals("2")){
			logger.info("第1个参数:【"+args[0]+"】");
			logger.info("第1个参数不合法吗，请参照[1:类型(0:不同步数据;1:小时同步;2:按天同步);");
			return false;
		}
		
		if(!args[1].equals("Y")&&!args[1].equals("N")){
			logger.info("第2个参数:【"+args[1]+"】");
			logger.info("第2个参数不合法吗，请参照[2:是否每天汇总(Y:汇总， N:不汇总)]");
			return false;
		}
		
		if(args[0].equals("1")){
			try {
				dateFormat.parse(args[2]);
				if(args[2].length()!=10){
					logger.info("第3个参数:【"+args[2]+"】");
					logger.info("按小时汇总,第3个参数不合法，请参照时间格式[YYYYMMDD)]");
					return false;
				}
			} catch (ParseException e) {
				e.printStackTrace();
				logger.info("第3个参数:【"+args[2]+"】");
				logger.info("按小时汇总,第3个参数不合法，请参照时间格式[YYYYMMDDHH)]");
				return false;
			}
		}
		
		if(args[0].equals("0")||args[0].equals("2")){
			try {
				
				dateFormatyyyyMMdd.parse(args[2]);
				if(args[2].length()!=8){
					logger.info("第3个参数:【"+args[2]+"】");
					logger.info("不汇总和按天汇总,第3个参数不合法，请参照时间[YYYYMMDD)]");
					return false;
				}
			} catch (ParseException e) {
				e.printStackTrace();
				logger.info("第3个参数:【"+args[2]+"】");
				logger.info("不汇总和按天汇总,第3个参数不合法，请参照时间[YYYYMMDD)]");
				return false;
			}
		}
		return true;
	}
	
}
