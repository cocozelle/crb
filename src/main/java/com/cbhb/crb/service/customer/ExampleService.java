package com.cbhb.crb.service.customer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cbhb.crb.bean.LoginUser;
import com.cbhb.crb.mapper.customer.ExampleMapper;
import com.cbhb.crb.service.BaseService;
import com.cbhb.crb.util.Constant;
import com.cbhb.crb.util.Converter;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("exampleService")
public class ExampleService extends BaseService
{
	@Resource(name = "exampleMapper")
	private ExampleMapper exampleMapper;
	
	public Map<String, Object> getMyCustomer(Map<String, String> mapParam,
			LoginUser loginUser) throws Exception
	{
		String strPage = mapParam.get("page");
		String strRows = mapParam.get("rows");
		
		int iPage = Integer.parseInt(strPage);
		int iRows = Integer.parseInt(strRows);
		
		mapParam.put("userId", loginUser.getUserId());
				
		Map<String, Object> MapResult = new HashMap<>();
		
		PageHelper.startPage(iPage, iRows);
		List<Map<String, Object>> listCustomer = exampleMapper.getMyCustomer(mapParam);
		PageInfo<Map<String, Object>> info = new PageInfo<>(listCustomer);
		long lTotal = info.getTotal();
		logger.info("客户总数：" + lTotal);
		
		List<Map<String, Object>> listTranslateParam = new ArrayList<Map<String,Object>>();
		
		Map<String, Object> mapTranslateParam = new HashMap<String,Object>(); 
		mapTranslateParam.put("key", Constant.DEF_REDIS_KEY_USER);
		mapTranslateParam.put("fieldKey", "managerId");
		mapTranslateParam.put("resultName", "managerName");
		listTranslateParam.add(mapTranslateParam);
		
		mapTranslateParam = new HashMap<String,Object>(); 
		mapTranslateParam.put("key", Constant.DEF_REDIS_KEY_ORG);
		mapTranslateParam.put("fieldKey", "managerOrgId");
		mapTranslateParam.put("resultName", "managerOrgName");
		mapTranslateParam.put("converter", new Converter() {
			@Override
			public String dealString(String strOriginal)
			{
				String strResult = strOriginal.replaceAll("渤海银行", "")
						.replaceAll("股份有限公司", "")
						.replaceAll("汇总", "")
						.replaceAll("天津分行市内辖区", "天津分行")
						.replaceAll("滨海新区辖内", "滨海分行")
						.replaceAll("\\[\\d{8}\\]", "");
				
				return strResult;
			}
		});
		listTranslateParam.add(mapTranslateParam);
		
		listCustomer = translateByRedis(listCustomer, listTranslateParam);
		
		MapResult.put("total", lTotal);
		MapResult.put("rows", listCustomer);		
		
		redisTemplate.opsForValue().set("test", listCustomer);
		
		return MapResult;
	}
	
	public String testBatch() throws Exception
	{
		ExampleMapper testMapper = batchSqlSession.getMapper(
				ExampleMapper.class);
		
		long time = System.currentTimeMillis();
		
		for(int i = 0; i < 100000; i++)
		{
			Map<String, String> mapParam = new HashMap<String, String>();
			mapParam.put("id", Integer.toString(i));
			mapParam.put("jobNumber", Integer.toString(i));
			mapParam.put("name", Integer.toString(i));

			testMapper.batchAdd(mapParam);
		}
		
		return "用时：" + (System.currentTimeMillis() - time) + "毫秒";
	}
}
