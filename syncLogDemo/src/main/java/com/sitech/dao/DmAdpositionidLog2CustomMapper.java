package com.sitech.dao;

import com.sitech.po.DmAdpositionidLog2Custom;

public interface DmAdpositionidLog2CustomMapper {
	
    int insert(DmAdpositionidLog2Custom record);

    int deleteByHour(DmAdpositionidLog2Custom record);
}