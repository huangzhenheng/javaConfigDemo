package com.hzh;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.io.IOUtils;
import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@PropertySource("classpath:config.properties")
@MapperScan(basePackages = "com.hzh.dao")
@EnableTransactionManagement
@ComponentScan(basePackages = { "com.hzh" }, excludeFilters = {
		@Filter(type = FilterType.ANNOTATION, value = EnableWebMvc.class) })
public class RootConfig {

	@Autowired
	private Environment environment;

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

	@Bean
	public BasicDataSource dataSource() {
		BasicDataSource basicDataSource = new BasicDataSource();
		basicDataSource.setDriverClassName(environment.getProperty("jdbc.driver"));
		basicDataSource.setUrl(environment.getProperty("jdbc.url"));
		basicDataSource.setUsername(environment.getProperty("jdbc.username"));
		basicDataSource.setPassword(environment.getProperty("jdbc.password"));

		basicDataSource.setMaxWaitMillis(Integer.parseInt(environment.getProperty("jdbc.maxwait")));
		basicDataSource.setMaxIdle(Integer.parseInt(environment.getProperty("jdbc.maxidle")));
		basicDataSource.setMinIdle(Integer.parseInt(environment.getProperty("jdbc.minidle")));
		basicDataSource.setMaxTotal(Integer.parseInt(environment.getProperty("jdbc.maxtotal")));
		basicDataSource.setInitialSize(Integer.parseInt(environment.getProperty("jdbc.initsize")));
		return basicDataSource;
	}

	@Bean
	public DataSourceTransactionManager transactionManager() {
		DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
		dataSourceTransactionManager.setDataSource(dataSource());
		return dataSourceTransactionManager;
	}

	@Bean
	public SqlSessionFactoryBean sqlSessionFactory() throws IOException {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		// 设置数据库连接池
		sqlSessionFactoryBean.setDataSource(dataSource());
		// 配置别名所在的包，别名默认为类名
		sqlSessionFactoryBean.setTypeAliasesPackage("com.hzh.pojo");

		// 配置Mapper XML所在的位置
		ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
		Resource[] resources = patternResolver.getResources("classpath:mapper/*.xml");
		sqlSessionFactoryBean.setMapperLocations(resources);
		return sqlSessionFactoryBean;
	}

	// redis
	@Bean
	public JedisPool jedisPool() {
		JedisPool jedisPool = new JedisPool(poolConfig(), environment.getProperty("redis.host"), Integer.parseInt(environment.getProperty("redis.port")));
		return jedisPool;
	}
 
	@Bean
	public JedisPoolConfig poolConfig() {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMinIdle(Integer.parseInt(environment.getProperty("redis.minIdle")));
		poolConfig.setMaxTotal(Integer.parseInt(environment.getProperty("redis.maxTotal")));
		poolConfig.setMaxWaitMillis(Integer.parseInt(environment.getProperty("redis.maxWaitMillis")));
		return poolConfig;
	}

	// 下面的是Spring-data-redis的配置,可使用下面的，也可以使用上面的
	@Bean
	public JedisConnectionFactory jedisConnectionFactory() {
		JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
		jedisConnectionFactory.setHostName(environment.getProperty("redis.host"));
		jedisConnectionFactory.setPort( Integer.parseInt(environment.getProperty("redis.port")));
		jedisConnectionFactory.setUsePool(true);
		jedisConnectionFactory.setPoolConfig(poolConfig());
		return jedisConnectionFactory;
	}

	@Bean
	public StringRedisSerializer stringRedisSerializer() {

		return new StringRedisSerializer();
	}

	// 也可以使用stringRedisTemplate，已经序列化
	@Bean
	public StringRedisTemplate stringRedisTemplate() {
		StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
		stringRedisTemplate.setConnectionFactory(jedisConnectionFactory());
		return stringRedisTemplate;
	}

	// 可以使用redisTemplate，但是键和值要序列化
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
	public RedisTemplate redisTemplate() {
		RedisTemplate redisTemplate = new RedisTemplate();
		redisTemplate.setConnectionFactory(jedisConnectionFactory());
		redisTemplate.setKeySerializer(stringRedisSerializer());
		redisTemplate.setValueSerializer(stringRedisSerializer());
		return redisTemplate;
	}

}
