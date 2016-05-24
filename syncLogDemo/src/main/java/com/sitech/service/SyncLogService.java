package com.sitech.service;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import com.sitech.po.DmAdpositionidLog1Custom;
import com.sitech.po.DmAdpositionidLog2Custom;

/** 

 * TODO 

 * @author zhanglic

 * @date 2016年4月15日 下午3:21:26 

 * 
 

 */

public interface SyncLogService {
	
	public void syncLog1(String fileDate) throws Exception;
	public void syncLog2(String fileDate) throws Exception;
	
	//public void gatherByHour(String fileDate) throws Exception;
	public void gatherByday(String fileDate) throws Exception;
	
}
