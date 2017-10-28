package com.themis.test.user;



import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.coreopsis.filter.Filter_0_FirstFilter;
import com.themis.admin.controller.AdminController;
import com.themis.base.BaseTest;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;
import com.xiaoleilu.hutool.log.StaticLog;

public class AdminUserControllerTest extends BaseTest{
	
	// hutool
	Log log = LogFactory.get();
	
	private MockMvc mvc;
	
	@Before
	public void setUp() throws Exception {
		// 此处可以添加过滤器
		mvc = MockMvcBuilders.standaloneSetup(new AdminController()).addFilters(new Filter_0_FirstFilter()).build(); 
	}
	
	@Test
	public void test() {
		System.out.println();
	}
	
	@Test
	public void testController () throws Exception {
		ResultActions andExpect = mvc.perform(MockMvcRequestBuilders.post("/index/login").param("usename", "hi").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json;charset=UTF-8"))
        .andExpect(content().json("{'foo':'bar'}"));
		StaticLog.info("", andExpect);
	}
	
}
