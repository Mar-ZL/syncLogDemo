package com.sitech.dao;

import com.sitech.po.DmAdpositionidHourNum;

public interface DmAdpositionidHourNumMapper {
   
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dm_adpositionid_hour_num
     *
     * @mbggenerated Tue Apr 19 11:01:19 CST 2016
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dm_adpositionid_hour_num
     *
     * @mbggenerated Tue Apr 19 11:01:19 CST 2016
     */
    int insert(DmAdpositionidHourNum record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dm_adpositionid_hour_num
     *
     * @mbggenerated Tue Apr 19 11:01:19 CST 2016
     */
    int insertSelective(DmAdpositionidHourNum record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dm_adpositionid_hour_num
     *
     * @mbggenerated Tue Apr 19 11:01:19 CST 2016
     */
    DmAdpositionidHourNum selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dm_adpositionid_hour_num
     *
     * @mbggenerated Tue Apr 19 11:01:19 CST 2016
     */
    int updateByPrimaryKeySelective(DmAdpositionidHourNum record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dm_adpositionid_hour_num
     *
     * @mbggenerated Tue Apr 19 11:01:19 CST 2016
     */
    int updateByPrimaryKey(DmAdpositionidHourNum record);
    
}