package com.hzh.config;

import com.hzh.RootConfig;
import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Properties;

/**
 * @Description fastdfs的配置文件
 * @auther hzh
 * @create 2018-11-22 15:18
 */
@Configuration
public class FastDfsConfig extends RootConfig {
    @Bean
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
