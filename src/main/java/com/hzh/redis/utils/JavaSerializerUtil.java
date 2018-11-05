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
 

public class JavaSerializerUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(JavaSerializerUtil.class);
    /**
     * 序列化
     * 
     * @param object
     * @return
     */
    public static byte[] serialize(Object object) {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try {
            // 序列化
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            return  baos.toByteArray();
        } catch (Exception e) {
        	 e.printStackTrace();
        	 logger.error("SerializeUtil.serialize序列化出错");
             throw new RedisException("序列化出错",e);
        } 
    }
    
    
    /**
     * 反序列化
     * 
     * @param bytes
     * @return
     */
    @SuppressWarnings("unchecked")
	public static <T> T unserialize(byte[] bytes) {
    	if (bytes == null || bytes.length == 0) {
            return null;
        }
        ByteArrayInputStream bais = null;
        try {
            // 反序列化
            bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (T) ois.readObject();
        } catch (Exception e) {
        	e.printStackTrace();
        	 logger.error("SerializeUtil.unserialize反序列化出错");
        	throw new RedisException("反序列化出错",e);
        }
    }
}