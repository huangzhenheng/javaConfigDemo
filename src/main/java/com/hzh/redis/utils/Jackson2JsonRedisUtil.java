package com.hzh.redis.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.hzh.redis.exception.RedisException;

public class Jackson2JsonRedisUtil {

	private static final Logger logger = LoggerFactory.getLogger(JavaSerializerUtil.class);

	/**
	 * 序列化
	 * 
	 * @param object
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static byte[] serialize(Object object) {
	    //StringRedisSerializer serializer =new StringRedisSerializer();
		Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);

		try {
			return jackson2JsonRedisSerializer.serialize(object);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Jackson2JsonRedisUtil.serialize序列化出错");
			throw new RedisException("序列化出错", e);
		}
	}

	/**
	 * 反序列化
	 * 
	 * @param bytes
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> T unserialize(byte[] bytes) {
		if (bytes == null || bytes.length == 0) {
			return null;
		}
		Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
		try {
			// 反序列化
			return (T) jackson2JsonRedisSerializer.deserialize(bytes);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Jackson2JsonRedisUtil.unserialize反序列化出错");
			throw new RedisException("反序列化出错", e);
		}
	}

}
