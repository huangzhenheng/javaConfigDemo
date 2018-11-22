package com.hzh.config;

import com.hzh.RootConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @Description redis的配置文件
 * @auther hzh
 * @create 2018-11-22 15:17
 */
@Configuration
public class RedisConfig extends RootConfig {

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
        jedisConnectionFactory.setPort(Integer.parseInt(environment.getProperty("redis.port")));
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
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Bean
    public RedisTemplate redisTemplate() {
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        redisTemplate.setKeySerializer(stringRedisSerializer());
        redisTemplate.setValueSerializer(stringRedisSerializer());
        return redisTemplate;
    }
}
