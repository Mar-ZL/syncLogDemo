package com.sitech.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sitech.dao.DmAdpositionidHourNumCustomMapper;
import com.sitech.dao.DmAdpositionidLog1CustomMapper;
import com.sitech.dao.DmAdpositionidLog2CustomMapper;
import com.sitech.dao.DmReportAdpositionidNumCustomMapper;
import com.sitech.po.DmAdpositionidHourNumCustom;
import com.sitech.po.DmAdpositionidLog1Custom;
import com.sitech.po.DmAdpositionidLog2Custom;
import com.sitech.po.DmReportAdpositionidNumCustom;
import com.sitech.service.BatchService;

/** 

 * TODO 

 * @author zhanglic

 */
@Service("batchService")
public class BatchServiceImpl implements BatchService {

	@Autowired
	private DmAdpositionidLog1CustomMapper dmAdpositionidLog1CustomMapper;
	
	@Autowired
	private DmAdpositionidLog2CustomMapper dmAdpositionidLog2CustomMapper;
	
	
	
	public void batchAddLog1(List<DmAdpositionidLog1Custom> dmAdpositionidLog1CustomList){
		
		for(DmAdpositionidLog1Custom dmAdpositionidLog1Custom :dmAdpositionidLog1CustomList) {
			dmAdpositionidLog1CustomMapper.insert(dmAdpositionidLog1Custom);
		}
	}
	
	public void batchAddLog2(List<DmAdpositionidLog2Custom> dmAdpositionidLog2CustomList){
		for(DmAdpositionidLog2Custom dmAdpositionidLog2Custom :dmAdpositionidLog2CustomList) {
			dmAdpositionidLog2CustomMapper.insert(dmAdpositionidLog2Custom);
			
		}
	}
	
	
	
}
