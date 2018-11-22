package redisTest;
  

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hzh.RootConfig;
import com.hzh.pojo.Document;
import com.hzh.redis.JedisUtil3;


 

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { RootConfig.class })
public class JedisStringTestCase {
 
 
	@Test
	public void testSet() {
		Document document=new Document();
		document.setName("这句话是的");
		document.setId(1);
		Map<String, Object> map=Maps.newHashMap();
		map.put("hzh", "很符合贷款");
		map.put("sds", 11);
		
		
		 JedisUtil3.setObject("gdgssdh", map);
		 
	 

	}

 
	 
}
