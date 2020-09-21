package com.cbhb.crb.mapper.customer;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component("exampleMapper")
public interface ExampleMapper
{
	public List<Map<String, Object>> getMyCustomer(Map<String, String> mapParam);

	public void batchAdd(Map<String, String> mapParam);
}
