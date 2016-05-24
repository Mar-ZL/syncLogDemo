package com.sitech.dao;


import com.sitech.po.DmReportAdpositionidNumCustom;

public interface DmReportAdpositionidNumCustomMapper {
  
	int insertGroupDataByDay(DmReportAdpositionidNumCustom record);
    
	int deleteByDay(DmReportAdpositionidNumCustom record);
}