package com.sitech.service;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/** 

 * TODO 

 * @author zhanglic

 * @date 2016年4月15日 下午4:05:44 

 * 
 

 */

public class SyncLogServiceImplTest {

	ApplicationContext applicationContext = null;
	/** 
	 * TODO(这里用一句话描述这个方法的作用) 
	
	 * @param @throws java.lang.Exception    设定文件 
	
	 * @return void    返回类型 
	
	 * @throws 
	
	 */
	@Before
	public void setUp() throws Exception {
		
		applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext.xml");
	}

	/** 
	 * TODO(这里用一句话描述这个方法的作用) 
	
	 * @param @throws java.lang.Exception    设定文件 
	
	 * @return void    返回类型 
	
	 * @throws 
	
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.sitech.service.impl.SyncLogServiceImpl#syncLog1()}.
	 */

}
