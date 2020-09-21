package com.cbhb.crb.service.customer;

import static org.junit.Assert.assertEquals;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.cbhb.crb.service.customer.ExampleService;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ExampleServiceTester
{
	@Resource(name="testService")
	private ExampleService testService;
	
	@Test
	public void testTestBatch() throws Exception
	{
		String strMessage = testService.testBatch();
		System.err.println(strMessage);
		assertEquals(strMessage, strMessage);
	}
}
