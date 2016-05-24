package com.sitech.service;

import java.util.List;

import com.sitech.po.DmAdpositionidLog1Custom;
import com.sitech.po.DmAdpositionidLog2Custom;
import com.sitech.po.DmReportAdpositionidNumCustom;

/** 

 * TODO 

 * @author zhanglic

 * @date 2016年4月19日 下午4:01:39 

 * 
 

 */

public interface BatchService {
	public void batchAddLog1(List<DmAdpositionidLog1Custom> dmAdpositionidLog1CustomList);
	public void batchAddLog2(List<DmAdpositionidLog2Custom> dmAdpositionidLog2CustomList);
}
