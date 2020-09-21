package com.cbhb.crb.controller.customer;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ExampleControllerTester
{
	@Autowired
	private TestRestTemplate restTemplate;
	
	private String strCookie;
	
	@Before
	public void saveSession()
	{
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		Map<String, String> requestBody = new HashMap<String, String>();
		requestBody.put("userId", "BB15000511");
		requestBody.put("orgId", "07301001");
		
		HttpEntity<Map<String, String>> requestEntity = 
				new HttpEntity<>(requestBody, headers);
		
		ResponseEntity<String> responseEntity = restTemplate.postForEntity(
				"/session", requestEntity, String.class);
		
		strCookie = responseEntity.getHeaders().get("Set-Cookie")
				.get(0).replaceAll("\\[|\\]", "");
	}
	
	@Test
	public void testGetMyCustomer()
	{
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		List<String> listCookie = new ArrayList<String>();
		listCookie.add(strCookie);
		headers.put(HttpHeaders.COOKIE, listCookie);
		
		
		Map<String, String> requestBody = new HashMap<String, String>();
		requestBody.put("customerId", "100");
		requestBody.put("name", "刘");
		requestBody.put("centNo", "430");
		requestBody.put("wealthAssetAumMin", "10000");
		requestBody.put("page", "1");
		requestBody.put("rows", "10");
		
		HttpEntity<Map<String, String>> requestEntity = 
				new HttpEntity<>(requestBody, headers);
		
		String strMessage = restTemplate.postForObject(
				"/my-customer", requestEntity, String.class);
		
		assertTrue(strMessage.startsWith("{\"total\":47"));;
	}
}
