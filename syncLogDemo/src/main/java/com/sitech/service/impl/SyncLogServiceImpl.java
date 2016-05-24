package com.sitech.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.aspectj.weaver.NewConstructorTypeMunger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.sitech.dao.DmAdpositionidHourNumCustomMapper;
import com.sitech.dao.DmAdpositionidLog1CustomMapper;
import com.sitech.dao.DmAdpositionidLog2CustomMapper;
import com.sitech.dao.DmReportAdpositionidNumCustomMapper;
import com.sitech.po.DmAdpositionidHourNum;
import com.sitech.po.DmAdpositionidHourNumCustom;
import com.sitech.po.DmAdpositionidLog1;
import com.sitech.po.DmAdpositionidLog1Custom;
import com.sitech.po.DmAdpositionidLog2Custom;
import com.sitech.po.DmReportAdpositionidNumCustom;
import com.sitech.service.BatchService;
import com.sitech.service.SyncLogService;
import com.sitech.util.PropertyUtils;

/** 

 * TODO 

 * @author zhanglic

 * @date 2016年4月15日 下午3:21:54 

 * 
 

 */
@Service("syncLogService")
public class SyncLogServiceImpl implements SyncLogService{

	/* (非 Javadoc) 
	
	* <p>Title: syncLog1</p> 
	
	* <p>Description: </p> 
	 
	
	* @see com.sitech.service.SyncLogService#syncLog1() 
	
	*/ 
	
	@Autowired
	private DmAdpositionidLog1CustomMapper dmAdpositionidLog1CustomMapper;
	
	@Autowired
	private DmAdpositionidLog2CustomMapper dmAdpositionidLog2CustomMapper;
	
	@Autowired
	private DmAdpositionidHourNumCustomMapper dmAdpositionidHourNumCustomMapper;
	
	@Autowired
	private DmReportAdpositionidNumCustomMapper dmReportAdpositionidNumCustomMapper;
	
	@Autowired
	private BatchService batchService;
	
	private static ThreadLocal<DateFormat> dateFormatLocal = new ThreadLocal<DateFormat>(){
		 protected DateFormat initialValue() {
	            return new SimpleDateFormat("yyyyMMddHH");
	     };
	};
	
	private static ThreadLocal<DateFormat> dateFormatyyyyMMLocal = new ThreadLocal<DateFormat>(){
		 protected DateFormat initialValue() {
	            return new SimpleDateFormat("yyyyMM");
	     };
	};
	
	private static ThreadLocal<DateFormat> dateFormatyyyyMMddLocal = new ThreadLocal<DateFormat>(){
		 protected DateFormat initialValue() {
	            return new SimpleDateFormat("yyyyMMdd");
	     };
	};
	
	
	Logger logger = Logger.getLogger(SyncLogServiceImpl.class); 
	
	
	
	//多线程同步调用1
	public void syncLog1(String fileDate) throws Exception{
		//删除小时日志
		deleteByHourOfLog1(fileDate);
		
		//入库小时日志
		readFileToDb1(fileDate);
		
		
	}
	
	private void readFileToDb1(String fileDate) throws Exception{
		
		String fileStr = PropertyUtils.getString("LOG1_FILE_PATH")+PropertyUtils.getString("LOG1_FILE_NAME_PREFIX")+fileDate;
		logger.info("日志文件:"+fileStr);
		int batchCommitNum = PropertyUtils.getInt("BATCH_COMMIT_NUM");
		//读取文件内容
		File file  = new File(fileStr);
		
		BufferedReader reader  = null;
		long lineNum = 0;
		long syncNum = 0;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"GBK"));
			
			List<DmAdpositionidLog1Custom> dmAdpositionidLog1CustomList = new ArrayList<DmAdpositionidLog1Custom>();
			String line = "";	
			while ((line = reader.readLine())!=null) {
				++lineNum;
				try {
					logger.debug("行日志信息 :"+line);
					
					String jsonStr = line.split("\\[platformReceiveLogger\\]")[1];
					
					logger.debug("json串 :"+jsonStr);
					
					Gson gson = new Gson();
					DmAdpositionidLog1Custom dmAdpositionidLog1Custom = gson.fromJson(jsonStr,DmAdpositionidLog1Custom.class);
					
					//字符'特殊处理
					dmAdpositionidLog1Custom.setMonitorReq(dmAdpositionidLog1Custom.getMonitorReq().replace("'", "\\'"));
					
					dmAdpositionidLog1Custom.setInfo(jsonStr);
					dmAdpositionidLog1Custom.setHourStr(fileDate);
					dmAdpositionidLog1Custom.setDayStr(dateFormatyyyyMMddLocal.get().format(dateFormatLocal.get().parse(fileDate)));
					dmAdpositionidLog1Custom.setTableDateParam(dateFormatyyyyMMLocal.get().format(dateFormatLocal.get().parse(fileDate)));
					dmAdpositionidLog1CustomList.add(dmAdpositionidLog1Custom);
					if(lineNum%batchCommitNum == 0){
						
						batchService.batchAddLog1(dmAdpositionidLog1CustomList);
						syncNum += dmAdpositionidLog1CustomList.size();
						dmAdpositionidLog1CustomList = new ArrayList<DmAdpositionidLog1Custom>();
						
					}
				}catch(Exception e){
					logger.error(e.getMessage());
					logger.error("error line: "+line);
					continue;
				}
				
			}	
			try{
				if(lineNum%batchCommitNum > 0){
					batchService.batchAddLog1(dmAdpositionidLog1CustomList);
					syncNum += dmAdpositionidLog1CustomList.size();
					dmAdpositionidLog1CustomList = new ArrayList<DmAdpositionidLog1Custom>();
				}
			}catch(Exception e){
				logger.error(e.getMessage());
			}
			
			
		}catch(FileNotFoundException e){
			logger.error(e.getMessage());	
			throw new Exception(e);
		}finally{
			
			if(reader!=null){
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();		
				}
			}
			logger.info("同步数据文件【"+PropertyUtils.getString("LOG1_FILE_NAME_PREFIX")+fileDate+"】总行数："+lineNum);
			logger.info("文件数据文件【"+PropertyUtils.getString("LOG1_FILE_NAME_PREFIX")+fileDate+"】总行数："+syncNum);
			
		}
	}
	
	//多线程同步调用2
	public void syncLog2(String fileDate) throws Exception{
		//删除小时日志
		deleteByHourOfLog2(fileDate);
		//入库小时日志
		readFileToDb2(fileDate);
	}
	
	
	public void readFileToDb2(String fileDate){

		String fileStr = PropertyUtils.getString("LOG2_FILE_PATH")+PropertyUtils.getString("LOG2_FILE_NAME_PREFIX")+fileDate;
		logger.info("日志文件:"+fileStr);
		int batchCommitNum = PropertyUtils.getInt("BATCH_COMMIT_NUM");
		Gson gson = new Gson();
		//读取文件内容
		File file  = new File(fileStr);
		
		
		BufferedReader reader = null;
		long lineNum = 0;
		long syncNum = 0;
		
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"GBK"));
			
			String line = "";	
			
			List<DmAdpositionidLog2Custom> dmAdpositionidLog2CustomList = new ArrayList<DmAdpositionidLog2Custom>();
			while ((line = reader.readLine())!=null) {
				++lineNum;
				try {
					String jsonStr = line.split("\\[platformLogger\\]")[1];
					
					DmAdpositionidLog2Custom dmAdpositionidLog2Custom = gson.fromJson(jsonStr,DmAdpositionidLog2Custom.class);
					
					//字符'特殊处理
					dmAdpositionidLog2Custom.setMonitorReq(dmAdpositionidLog2Custom.getMonitorReq().replace("'", "\\'"));
					
					dmAdpositionidLog2Custom.setInfo(jsonStr);
					dmAdpositionidLog2Custom.setHourStr(fileDate);
					
					dmAdpositionidLog2Custom.setDayStr(dateFormatyyyyMMddLocal.get().format(dateFormatLocal.get().parse(fileDate)));
					
					dmAdpositionidLog2Custom.setTableDateParam(dateFormatyyyyMMLocal.get().format(dateFormatLocal.get().parse(fileDate)));
					
					
					dmAdpositionidLog2CustomList.add(dmAdpositionidLog2Custom);
					if(lineNum%batchCommitNum == 0){
						batchService.batchAddLog2(dmAdpositionidLog2CustomList);
						syncNum += dmAdpositionidLog2CustomList.size();
						dmAdpositionidLog2CustomList = new ArrayList<DmAdpositionidLog2Custom>();
					}
				}catch(Exception e){
					logger.error(e.getMessage());
					logger.error("error line: "+line);
					continue;
				}
				
			}	
			try {
				if(lineNum%batchCommitNum > 0){
					batchService.batchAddLog2(dmAdpositionidLog2CustomList);
					syncNum += dmAdpositionidLog2CustomList.size();
					dmAdpositionidLog2CustomList = new ArrayList<DmAdpositionidLog2Custom>();
				}
				
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}finally{
			if(reader!=null){
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();		
				}
			}
			logger.info("同步数据文件【"+PropertyUtils.getString("LOG2_FILE_NAME_PREFIX")+fileDate+"】总行数："+lineNum);
			logger.info("文件数据文件【"+PropertyUtils.getString("LOG2_FILE_NAME_PREFIX")+fileDate+"】总行数："+syncNum);
			
		}
		
		
	}
	
	
	
	
	
//	public void gatherByHour(String fileDate) throws Exception{
//		DmAdpositionidHourNumCustom dmAdpositionidHourNumCustom = new DmAdpositionidHourNumCustom();
//		dmAdpositionidHourNumCustom.setHourStr(fileDate);
//		dmAdpositionidHourNumCustom.setDayStr(dateFormatyyyyMMddLocal.get().format(dateFormatLocal.get().parse(fileDate)));
//		dmAdpositionidHourNumCustom.setTableDateParam(dateFormatyyyyMMLocal.get().format(dateFormatLocal.get().parse(fileDate)));
//		dmAdpositionidHourNumCustomMapper.insertGroupData(dmAdpositionidHourNumCustom);
//	}
	
//	private void deleteByHourOfHourNum(String fileDate) throws Exception{
//	DmAdpositionidHourNumCustom dmAdpositionidHourNumCustom = new DmAdpositionidHourNumCustom();
//	dmAdpositionidHourNumCustom.setHourStr(fileDate);
//	dmAdpositionidHourNumCustom.setDayStr(dateFormatyyyyMMddLocal.get().format(dateFormatLocal.get().parse(fileDate)));
//	dmAdpositionidHourNumCustom.setTableDateParam(dateFormatyyyyMMLocal.get().format(dateFormatLocal.get().parse(fileDate)));
//	dmAdpositionidHourNumCustomMapper.deleteByHour(dmAdpositionidHourNumCustom);
//}
	
	public void gatherByday(String fileDate) throws Exception{
		deleteByReportAdpositionidNum(fileDate);
		DmReportAdpositionidNumCustom dmReportAdpositionidNumCustom = new DmReportAdpositionidNumCustom();
		dmReportAdpositionidNumCustom.setDayStr(dateFormatyyyyMMddLocal.get().format(dateFormatyyyyMMddLocal.get().parse(fileDate)));
		dmReportAdpositionidNumCustom.setTableDateParam(dateFormatyyyyMMLocal.get().format(dateFormatyyyyMMddLocal.get().parse(fileDate)));
		dmReportAdpositionidNumCustomMapper.insertGroupDataByDay(dmReportAdpositionidNumCustom);
	}
	

	private void deleteByReportAdpositionidNum(String fileDate) throws Exception{
		DmReportAdpositionidNumCustom dmReportAdpositionidNumCustom = new DmReportAdpositionidNumCustom();
		dmReportAdpositionidNumCustom.setDayStr(dateFormatyyyyMMddLocal.get().format(dateFormatyyyyMMddLocal.get().parse(fileDate)));
		dmReportAdpositionidNumCustom.setTableDateParam(dateFormatyyyyMMLocal.get().format(dateFormatyyyyMMddLocal.get().parse(fileDate)));
		dmReportAdpositionidNumCustomMapper.deleteByDay(dmReportAdpositionidNumCustom);
	}
	
	
	private void deleteByHourOfLog1(String fileDate) throws Exception{
		
		DmAdpositionidLog1Custom dmAdpositionidLog1Custom = new DmAdpositionidLog1Custom();
		dmAdpositionidLog1Custom.setHourStr(fileDate);
		dmAdpositionidLog1Custom.setDayStr(dateFormatyyyyMMddLocal.get().format(dateFormatLocal.get().parse(fileDate)));
		dmAdpositionidLog1Custom.setTableDateParam(dateFormatyyyyMMLocal.get().format(dateFormatLocal.get().parse(fileDate)));
		dmAdpositionidLog1CustomMapper.deleteByHour(dmAdpositionidLog1Custom);
	}
	
	private void deleteByHourOfLog2(String fileDate) throws Exception{
		DmAdpositionidLog2Custom dmAdpositionidLog2Custom = new DmAdpositionidLog2Custom();
		dmAdpositionidLog2Custom.setHourStr(fileDate);
		dmAdpositionidLog2Custom.setDayStr(dateFormatyyyyMMddLocal.get().format(dateFormatLocal.get().parse(fileDate)));
		dmAdpositionidLog2Custom.setTableDateParam(dateFormatyyyyMMLocal.get().format(dateFormatLocal.get().parse(fileDate)));
		dmAdpositionidLog2CustomMapper.deleteByHour(dmAdpositionidLog2Custom);
	}
	

	
	
	

	
	
	
	
	
}
