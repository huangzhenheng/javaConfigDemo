package fastdfs;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.io.Files;

public class FastdfsTest {
	public static void main(String[] args) {
		///谷歌Guava
		// 普通Collection的创建
		List<String> list = Lists.newArrayList();
		Set<String> set = Sets.newHashSet();
		Map<String, String> map = Maps.newHashMap();
		// 不变Collection的创建
		ImmutableList<String> iList = ImmutableList.of("a", "b", "c");
		ImmutableSet<String> iSet = ImmutableSet.of("e1", "e2");
		ImmutableMap<String, String> iMap = ImmutableMap.of("k1", "v1", "k2", "v2");
	}
	
	
	@Test
	public void guavaTest(){
		
	}
	
	
	
}
