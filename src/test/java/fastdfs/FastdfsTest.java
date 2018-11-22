package fastdfs;

import java.io.*;
import java.util.*;

import com.hzh.RootConfig;
import org.apache.commons.io.IOUtils;
import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


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
	public void guavaTest() throws IOException, MyException {
		InputStream inputStream=new FileInputStream(new File("D:\\es6标准入门.pdf"));
		byte[] bytes = IOUtils.toByteArray(inputStream);
		String[] strings =  storageClient().upload_file(bytes,  "pdf", null);
		for (String string : strings) {
			System.out.println(string);
		}
	}

	public StorageClient storageClient() {

		// 初始化配置
		try {
			Properties props = new Properties();
			props.put(ClientGlobal.PROP_KEY_TRACKER_SERVERS, "123.206.88.207:22122");
			ClientGlobal.initByProperties(props);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MyException e) {
			e.printStackTrace();
		}
		TrackerClient trackerClient = new TrackerClient();
		TrackerServer trackerServer = null;
		try {
			trackerServer = trackerClient.getConnection();
			StorageClient storageClient = new StorageClient(trackerServer, null);

			return storageClient;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}





}
