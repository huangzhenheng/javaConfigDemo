package com.hzh.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.hzh.redis.exception.RedisException;
import com.hzh.redis.utils.JavaSerializerUtil;
import com.hzh.util.ConfigUtil;

import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.SortingParams;
import redis.clients.util.SafeEncoder;

public class JedisUtil3 {
	private static Logger logger = LoggerFactory.getLogger(JedisUtil3.class);
	/** 缓存生存时间 秒 */
	private final static int CACHESECONDS = 60000;
	private static JedisPool jedisPool;

	static {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(Integer.parseInt(ConfigUtil.get("redis.maxTotal")));
		config.setMaxIdle(Integer.parseInt(ConfigUtil.get("redis.maxIdle")));
		config.setMaxWaitMillis(Integer.parseInt(ConfigUtil.get("redis.maxWaitMillis")));
		config.setTestOnBorrow(true);
		jedisPool = new JedisPool(config, ConfigUtil.get("redis.host"), Integer.parseInt(ConfigUtil.get("redis.port")));

	}

	/**
	 * 从jedis连接池中获取获取jedis对象
	 * 
	 */
	public static Jedis getInstance() {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis;
		} catch (Exception e) {
			logger.error("[JedisDS] getRedisClent error:" + e.getMessage());
			if (null != jedis)
				jedis.close();
		}
		return null;
	}

	/**
	 * 释放jedis
	 */
	public static void returnResource(Jedis jedis) {
		try {
			if (null != jedis) {
				jedis.close();
			}
		} catch (Exception e) {
			logger.error("return redis resource exception", e);
		}
	}

	// ************************************************************************对键的操作

	/**
	 * 设置键的过期时间
	 * 
	 * @param key
	 * @param seconds
	 */
	public static void expire(String key, int seconds) {
		if (seconds <= 0) {
			return;
		}
		Jedis jedis = getInstance();
		try {
			jedis.expire(key, seconds);
		} catch (Exception e) {
			throw new RedisException("set(seconds) data is error", e);
		} finally {
			returnResource(jedis);
		}
	}

	/**
	 * 设置键的默认过期时间60000(一小时)
	 * 
	 * @param key
	 */
	public static void expire(String key) {
		expire(key, CACHESECONDS);
	}

	/**
	 * 设置key的过期时间,它是距历元（即格林威治标准时间 1970 年 1 月 1 日的 00:00:00，格里高利历）的偏移量。
	 * 
	 * @param String
	 *            key
	 * @param 时间,已秒为单位
	 * @return 影响的记录数
	 */
	public static Long expireAt(String key, long timestamp) {
		Jedis jedis = getInstance();
		Long count = null;
		try {
			count = jedis.expireAt(key, timestamp);
		} catch (Exception e) {
			throw new RedisException("expireAt data is error", e);
		} finally {
			returnResource(jedis);
		}
		return count;
	}

	/**
	 * 查询key的过期时间
	 * 
	 * @param String
	 *            key
	 * @return 以秒为单位的时间表示
	 */
	public static Long ttl(String key) {
		Jedis jedis = getInstance();
		Long len = null;
		try {
			len = jedis.ttl(key);
		} catch (Exception e) {
			throw new RedisException("ttl data is error", e);
		} finally {
			returnResource(jedis);
		}
		return len;
	}

	/**
	 * 取消对key过期时间的设置
	 * 
	 * @param key
	 * @return 影响的记录数
	 */
	public static Long persist(String key) {
		Jedis jedis = getInstance();
		Long count = null;
		try {
			count = jedis.persist(key);
		} catch (Exception e) {
			throw new RedisException("ttl data is error", e);
		} finally {
			returnResource(jedis);
		}
		return count;
	}

	/**
	 * 清空所有key
	 */
	public static String flushAll() {
		Jedis jedis = getInstance();
		String stata = null;
		try {
			stata = jedis.flushAll();
		} catch (Exception e) {
			throw new RedisException("flushAll is error", e);
		} finally {
			returnResource(jedis);
		}
		return stata;
	}

	/**
	 * 更改key的名字
	 * 
	 * @param String
	 *            oldkey
	 * @param String
	 *            newkey
	 * @return 状态码
	 */
	public static String rename(String oldkey, String newkey) {
		Jedis jedis = getInstance();
		String status = null;
		try {
			status = jedis.rename(oldkey, newkey);
		} catch (Exception e) {
			throw new RedisException("renamenx(oldkey, newkey)  is error", e);
		} finally {
			returnResource(jedis);
		}

		return status;
	}

	/**
	 * 更改key
	 * 
	 * @param String
	 *            oldkey
	 * @param String
	 *            newkey
	 * @return 状态码
	 */
	public static String rename(byte[] oldkey, byte[] newkey) {

		Jedis jedis = getInstance();
		String status = null;
		try {
			status = jedis.rename(oldkey, newkey);
		} catch (Exception e) {
			throw new RedisException("renamenx(oldkey, newkey)  is error", e);
		} finally {
			returnResource(jedis);
		}

		return status;
	}

	/**
	 * 更改key,仅当新key不存在时才执行
	 * 
	 * @param String
	 *            oldkey
	 * @param String
	 *            newkey
	 * @return 状态码
	 */
	public static Long renamenx(String oldkey, String newkey) {
		Jedis jedis = getInstance();
		Long status = null;
		try {
			status = jedis.renamenx(oldkey, newkey);
		} catch (Exception e) {
			throw new RedisException("renamenx(oldkey, newkey)  is error", e);
		} finally {
			returnResource(jedis);
		}
		return status;
	}

	/**
	 * 更改key,仅当新key不存在时才执行
	 * 
	 * @param String
	 *            oldkey
	 * @param String
	 *            newkey
	 * @return 状态码
	 */
	public static Long renamenx(byte[] oldkey, byte[] newkey) {
		Jedis jedis = getInstance();
		Long status = null;
		try {
			status = jedis.renamenx(oldkey, newkey);
		} catch (Exception e) {
			throw new RedisException("renamenx(oldkey, newkey)  is error", e);
		} finally {
			returnResource(jedis);
		}
		return status;
	}

	/**
	 * 删除keys对应的记录,可以是多个key
	 * 
	 * @param String
	 *            ... keys
	 * @return 删除的记录数
	 */
	public static Long del(String... keys) {
		Jedis jedis = getInstance();
		Long count = null;
		try {
			count = jedis.del(keys);
		} catch (Exception e) {
			throw new RedisException("del(String... keys)  is error", e);
		} finally {
			returnResource(jedis);
		}
		return count;
	}

	/**
	 * 删除keys对应的记录,可以是多个key
	 * 
	 * @param String
	 *            .. keys
	 * @return 删除的记录数
	 */
	public static long del(byte[]... keys) {
		Jedis jedis = getInstance();
		Long count = null;
		try {
			count = jedis.del(keys);
		} catch (Exception e) {
			throw new RedisException("del(byte[]... keys)  is error", e);
		} finally {
			returnResource(jedis);
		}
		return count;
	}

	/**
	 * 判断key是否存在
	 * 
	 * @param String
	 *            key
	 * @return boolean
	 */
	public static Boolean exists(String key) {
		Jedis jedis = getInstance();
		Boolean exis = null;
		try {
			exis = jedis.exists(key);
		} catch (Exception e) {
			throw new RedisException("exists(String key  is error", e);
		} finally {
			returnResource(jedis);
		}
		return exis;
	}

	/**
	 * 对List,Set,SortSet进行排序,如果集合数据较大应避免使用这个方法
	 * 
	 * @param String
	 *            key
	 * @return List<String> 集合的全部记录
	 **/
	public static List<String> sort(String key) {
		Jedis jedis = getInstance();
		List<String> list = Lists.newArrayList();
		try {
			list = jedis.sort(key);
		} catch (Exception e) {
			throw new RedisException("sort(String key)  is error", e);
		} finally {
			returnResource(jedis);
		}
		return list;
	}

	/**
	 * 对List,Set,SortSet进行排序或limit
	 * 
	 * @param String
	 *            key
	 * @param SortingParams
	 *            parame 定义排序类型或limit的起止位置.
	 * @return List<String> 全部或部分记录
	 **/
	public static List<String> sort(String key, SortingParams parame) {
		Jedis jedis = getInstance();
		List<String> list = Lists.newArrayList();
		try {
			list = jedis.sort(key, parame);
		} catch (Exception e) {
			throw new RedisException("sort(String key)  is error", e);
		} finally {
			returnResource(jedis);
		}
		return list;
	}

	/**
	 * 返回指定key存储的类型
	 * 
	 * @param String
	 *            key
	 * @return String string|list|set|zset|hash
	 **/
	public static String type(String key) {
		Jedis sjedis = getInstance();
		String type = sjedis.type(key);
		returnResource(sjedis);
		return type;
	}

	/**
	 * 查找所有匹配给定的模式的键
	 * 
	 * @param String
	 *            key的表达式,*表示多个，？表示一个
	 */
	public static Set<String> keys(String pattern) {
		Jedis jedis = getInstance();
		Set<String> set = jedis.keys(pattern);
		returnResource(jedis);
		return set;
	}

	// *******************************************************************String数据类型
	/**
	 * 根据key获取记录
	 * 
	 * @param String
	 *            key
	 * @return 值
	 */
	public static String get(String key) {
		Jedis jedis = getInstance();
		String value = null;
		try {
			value = jedis.get(key);
		} catch (Exception e) {
			throw new RedisException("get(String key) is error", e);
		} finally {
			returnResource(jedis);
		}
		return value;
	}

	/**
	 * 添加有过期时间的记录
	 * 
	 * @param String
	 *            key
	 * @param int
	 *            seconds 过期时间，以秒为单位
	 * @param String
	 *            value
	 * @return String 操作状态
	 */
	public static String setEx(String key, int seconds, String value) {
		Jedis jedis = getInstance();
		String str = jedis.setex(key, seconds, value);
		returnResource(jedis);
		return str;
	}

	/**
	 * 添加一条记录，仅当给定的key不存在时才插入
	 * 
	 * @param String
	 *            key
	 * @param String
	 *            value
	 * @return long 状态码，1插入成功且key不存在，0未插入，key存在
	 */
	public static long setnx(String key, String value) {
		Jedis jedis = getInstance();
		long str = jedis.setnx(key, value);
		returnResource(jedis);
		return str;
	}

	/**
	 * 添加一条记录，仅当给定的key不存在时才插入
	 * 
	 * @return
	 */
	public static Long setnxObject(String key, Object object) {
		Jedis jedis = getInstance();
		Long status = null;
		try {
			status = jedis.setnx(SafeEncoder.encode(key), JavaSerializerUtil.serialize(object));
		} catch (Exception e) {
			throw new RedisException("setnxObject data is error", e);
		} finally {
			returnResource(jedis);
		}
		return status;
	}

	/**
	 * 添加记录,如果记录已存在将覆盖原有的value
	 * 
	 * @param String
	 *            key
	 * @param String
	 *            value
	 * @return 状态码
	 */
	public static String set(String key, String value) {
		Jedis jedis = getInstance();
		String status = null;
		try {
			status = jedis.set(key, value);
		} catch (Exception e) {
			logger.error("添加记录,如果记录已存在将覆盖原有的value", e);
		} finally {
			returnResource(jedis);
		}
		return status;
	}

	// 添加记录,如果记录已存在将覆盖原有的value
	public static void setObject(String key, Object object) {
		Jedis jedis = getInstance();
		new StringRedisSerializer();
		try {
			jedis.set(SafeEncoder.encode(key), JavaSerializerUtil.serialize(object));
		} catch (Exception e) {
			throw new RedisException("set data is error", e);
		} finally {
			returnResource(jedis);
		}

	}

	// 添加记录,如果记录已存在将覆盖原有的value
	public static void setObject(String key, Object object, int seconds) {
		Jedis jedis = getInstance();
		try {
			jedis.set(SafeEncoder.encode(key), JavaSerializerUtil.serialize(object));
			if (seconds > 0) {
				jedis.expire(key, seconds);
			}
		} catch (Exception e) {
			throw new RedisException("set data is error", e);
		} finally {
			returnResource(jedis);
		}

	}

	/**
	 * 从指定位置开始插入数据，插入的数据会覆盖指定位置以后的数据<br/>
	 * 例:String str1="123456789";<br/>
	 * 对str1操作后setRange(key,4,0000)，str1="123400009";
	 * 
	 * @param String
	 *            key
	 * @param long
	 *            offset
	 * @param String
	 *            value
	 * @return long value的长度
	 */
	public static long setRange(String key, long offset, String value) {
		Jedis jedis = getInstance();
		long len = jedis.setrange(key, offset, value);
		returnResource(jedis);
		return len;
	}

	/**
	 * 在指定的key中追加value
	 * 
	 * @param String
	 *            key
	 * @param String
	 *            value
	 * @return long 追加后value的长度
	 **/
	public static long append(String key, String value) {
		Jedis jedis = getInstance();
		long len = jedis.append(key, value);
		returnResource(jedis);
		return len;
	}

	/**
	 * 将key对应的value减去指定的值，只有value可以转为数字时该方法才可用
	 * 
	 * @param String
	 *            key
	 * @param long
	 *            number 要减去的值
	 * @return long 减指定值后的值
	 */
	public static long decrBy(String key, long number) {
		Jedis jedis = getInstance();
		long len = jedis.decrBy(key, number);
		returnResource(jedis);
		return len;
	}

	/**
	 * <b>可以作为获取唯一id的方法</b><br/>
	 * 将key对应的value加上指定的值，只有value可以转为数字时该方法才可用
	 * 
	 * @param String
	 *            key
	 * @param long
	 *            number 要减去的值
	 * @return long 相加后的值
	 */
	public static long incrBy(String key, long number) {
		Jedis jedis = getInstance();
		long len = jedis.incrBy(key, number);
		returnResource(jedis);
		return len;
	}

	/**
	 * 对指定key对应的value进行截取
	 * 
	 * @param String
	 *            key
	 * @param long
	 *            startOffset 开始位置(包含)
	 * @param long
	 *            endOffset 结束位置(包含)
	 * @return String 截取的值
	 */
	public static String getrange(String key, long startOffset, long endOffset) {
		Jedis jedis = getInstance();
		String value = jedis.getrange(key, startOffset, endOffset);
		returnResource(jedis);
		return value;
	}

	/**
	 * 获取并设置指定key对应的value<br/>
	 * 如果key存在返回之前的value,否则返回null
	 * 
	 * @param String
	 *            key
	 * @param String
	 *            value
	 * @return String 原始value或null
	 */
	public static String getSet(String key, String value) {
		Jedis jedis = getInstance();
		String str = jedis.getSet(key, value);
		returnResource(jedis);
		return str;
	}

	/**
	 * 批量获取记录,如果指定的key不存在返回List的对应位置将是null
	 * 
	 * @param String
	 *            keys
	 * @return List<String> 值得集合
	 */
	public static List<String> mget(String... keys) {
		Jedis jedis = getInstance();
		List<String> str = jedis.mget(keys);
		returnResource(jedis);
		return str;
	}

	/**
	 * 批量存储记录
	 * 
	 * @param String
	 *            keysvalues 例:keysvalues="key1","value1","key2","value2";
	 * @return String 状态码
	 */
	public static String mset(String... keysvalues) {
		Jedis jedis = getInstance();
		String str = jedis.mset(keysvalues);
		returnResource(jedis);
		return str;
	}

	/**
	 * 获取key对应的值的长度
	 * 
	 * @param String
	 *            key
	 * @return value值得长度
	 */
	public static long strlen(String key) {
		Jedis jedis = getInstance();
		long len = jedis.strlen(key);
		returnResource(jedis);
		return len;
	}
	// string 结束

	/**
	 * 向Set添加一条记录，如果member已存在返回0,否则返回1
	 * 
	 * @param String
	 *            key
	 * @param String
	 *            member
	 * @return 操作码,0或1
	 */
	public static long sadd(String key, String member) {
		Jedis jedis = getInstance();
		long s = jedis.sadd(key, member);
		returnResource(jedis);
		return s;
	}

	public static long sadd(byte[] key, byte[] member) {
		Jedis jedis = getInstance();
		long s = jedis.sadd(key, member);
		returnResource(jedis);
		return s;
	}

	/**
	 * 获取给定key中元素个数
	 * 
	 * @param String
	 *            key
	 * @return 元素个数
	 */
	public static long scard(String key) {
		// ShardedJedis sjedis = getShardedJedis();
		Jedis sjedis = getInstance();
		long len = sjedis.scard(key);
		returnResource(sjedis);
		return len;
	}

	/**
	 * 返回从第一组和所有的给定集合之间的差异的成员
	 * 
	 * @param String
	 *            ... keys
	 * @return 差异的成员集合
	 */
	public static Set<String> sdiff(String... keys) {
		Jedis jedis = getInstance();
		Set<String> set = jedis.sdiff(keys);
		returnResource(jedis);
		return set;
	}

	/**
	 * 这个命令等于sdiff,但返回的不是结果集,而是将结果集存储在新的集合中，如果目标已存在，则覆盖。
	 * 
	 * @param String
	 *            newkey 新结果集的key
	 * @param String
	 *            ... keys 比较的集合
	 * @return 新集合中的记录数
	 **/
	public static long sdiffstore(String newkey, String... keys) {
		Jedis jedis = getInstance();
		long s = jedis.sdiffstore(newkey, keys);
		returnResource(jedis);
		return s;
	}

	/**
	 * 返回给定集合交集的成员,如果其中一个集合为不存在或为空，则返回空Set
	 * 
	 * @param String
	 *            ... keys
	 * @return 交集成员的集合
	 **/
	public static Set<String> sinter(String... keys) {
		Jedis jedis = getInstance();
		Set<String> set = jedis.sinter(keys);
		returnResource(jedis);
		return set;
	}

	/**
	 * 这个命令等于sinter,但返回的不是结果集,而是将结果集存储在新的集合中，如果目标已存在，则覆盖。
	 * 
	 * @param String
	 *            newkey 新结果集的key
	 * @param String
	 *            ... keys 比较的集合
	 * @return 新集合中的记录数
	 **/
	public static long sinterstore(String newkey, String... keys) {
		Jedis jedis = getInstance();
		long s = jedis.sinterstore(newkey, keys);
		returnResource(jedis);
		return s;
	}

	/**
	 * 确定一个给定的值是否存在
	 * 
	 * @param String
	 *            key
	 * @param String
	 *            member 要判断的值
	 * @return 存在返回1，不存在返回0
	 **/
	public static boolean sismember(String key, String member) {
		Jedis sjedis = getInstance();
		boolean s = sjedis.sismember(key, member);
		returnResource(sjedis);
		return s;
	}

	/**
	 * 返回集合中的所有成员
	 * 
	 * @param String
	 *            key
	 * @return 成员集合
	 */
	public static Set<String> smembers(String key) {
		Jedis sjedis = getInstance();
		Set<String> set = sjedis.smembers(key);
		returnResource(sjedis);
		return set;
	}

	public static Set<byte[]> smembers(byte[] key) {
		Jedis sjedis = getInstance();
		Set<byte[]> set = sjedis.smembers(key);
		returnResource(sjedis);
		return set;
	}

	/**
	 * 将成员从源集合移出放入目标集合 <br/>
	 * 如果源集合不存在或不包哈指定成员，不进行任何操作，返回0<br/>
	 * 否则该成员从源集合上删除，并添加到目标集合，如果目标集合中成员已存在，则只在源集合进行删除
	 * 
	 * @param String
	 *            srckey 源集合
	 * @param String
	 *            dstkey 目标集合
	 * @param String
	 *            member 源集合中的成员
	 * @return 状态码，1成功，0失败
	 */
	public static long smove(String srckey, String dstkey, String member) {
		Jedis jedis = getInstance();
		long s = jedis.smove(srckey, dstkey, member);
		returnResource(jedis);
		return s;
	}

	/**
	 * 从集合中删除成员
	 * 
	 * @param String
	 *            key
	 * @return 被删除的成员
	 */
	public static String spop(String key) {
		Jedis jedis = getInstance();
		String s = jedis.spop(key);
		returnResource(jedis);
		return s;
	}

	/**
	 * 从集合中删除指定成员
	 * 
	 * @param String
	 *            key
	 * @param String
	 *            member 要删除的成员
	 * @return 状态码，成功返回1，成员不存在返回0
	 */
	public static long srem(String key, String member) {
		Jedis jedis = getInstance();
		long s = jedis.srem(key, member);
		returnResource(jedis);
		return s;
	}

	/**
	 * 合并多个集合并返回合并后的结果，合并后的结果集合并不保存<br/>
	 * 
	 * @param String
	 *            ... keys
	 * @return 合并后的结果集合
	 * @see sunionstore
	 */
	public static Set<String> sunion(String... keys) {
		Jedis jedis = getInstance();
		Set<String> set = jedis.sunion(keys);
		returnResource(jedis);
		return set;
	}

	/**
	 * 合并多个集合并将合并后的结果集保存在指定的新集合中，如果新集合已经存在则覆盖
	 * 
	 * @param String
	 *            newkey 新集合的key
	 * @param String
	 *            ... keys 要合并的集合
	 **/
	public static long sunionstore(String newkey, String... keys) {
		Jedis jedis = getInstance();
		long s = jedis.sunionstore(newkey, keys);
		returnResource(jedis);
		return s;
	}

	/**
	 * 向集合中增加一条记录,如果这个值已存在，这个值对应的权重将被置为新的权重
	 * 
	 * @param String
	 *            key
	 * @param double
	 *            score 权重
	 * @param String
	 *            member 要加入的值，
	 * @return 状态码 1成功，0已存在member的值
	 */
	public static long zadd(String key, double score, String member) {
		Jedis jedis = getInstance();
		long s = jedis.zadd(key, score, member);
		returnResource(jedis);
		return s;
	}

	/*
	 * public long zadd(String key, Map<Double, String> scoreMembers) { Jedis
	 * jedis = getJedis(); long s = jedis.zadd(key, scoreMembers);
	 * returnJedis(jedis); return s; }
	 */

	/**
	 * 获取集合中元素的数量
	 * 
	 * @param String
	 *            key
	 * @return 如果返回0则集合不存在
	 */
	public static long zcard(String key) {
		// ShardedJedis sjedis = getShardedJedis();
		Jedis sjedis = getInstance();
		long len = sjedis.zcard(key);
		returnResource(sjedis);
		return len;
	}

	/**
	 * 获取指定权重区间内集合的数量
	 * 
	 * @param String
	 *            key
	 * @param double
	 *            min 最小排序位置
	 * @param double
	 *            max 最大排序位置
	 */
	public static long zcount(String key, double min, double max) {
		// ShardedJedis sjedis = getShardedJedis();
		Jedis sjedis = getInstance();
		long len = sjedis.zcount(key, min, max);
		returnResource(sjedis);
		return len;
	}

	/**
	 * 获得set的长度
	 * 
	 * @param key
	 * @return
	 */
	public static long zlength(String key) {
		long len = 0;
		Set<String> set = zrange(key, 0, -1);
		len = set.size();
		return len;
	}

	/**
	 * 权重增加给定值，如果给定的member已存在
	 * 
	 * @param String
	 *            key
	 * @param double
	 *            score 要增的权重
	 * @param String
	 *            member 要插入的值
	 * @return 增后的权重
	 */
	public static double zincrby(String key, double score, String member) {
		Jedis jedis = getInstance();
		double s = jedis.zincrby(key, score, member);
		returnResource(jedis);
		return s;
	}

	/**
	 * 返回指定位置的集合元素,0为第一个元素，-1为最后一个元素
	 * 
	 * @param String
	 *            key
	 * @param int
	 *            start 开始位置(包含)
	 * @param int
	 *            end 结束位置(包含)
	 * @return Set<String>
	 */
	public static Set<String> zrange(String key, int start, int end) {
		// ShardedJedis sjedis = getShardedJedis();
		Jedis sjedis = getInstance();
		Set<String> set = sjedis.zrange(key, start, end);
		returnResource(sjedis);
		return set;
	}

	/**
	 * 返回指定权重区间的元素集合
	 * 
	 * @param String
	 *            key
	 * @param double
	 *            min 上限权重
	 * @param double
	 *            max 下限权重
	 * @return Set<String>
	 */
	public static Set<String> zrangeByScore(String key, double min, double max) {
		// ShardedJedis sjedis = getShardedJedis();
		Jedis sjedis = getInstance();
		Set<String> set = sjedis.zrangeByScore(key, min, max);
		returnResource(sjedis);
		return set;
	}

	/**
	 * 获取指定值在集合中的位置，集合排序从低到高
	 * 
	 * @see zrevrank
	 * @param String
	 *            key
	 * @param String
	 *            member
	 * @return long 位置
	 */
	public static long zrank(String key, String member) {
		// ShardedJedis sjedis = getShardedJedis();
		Jedis sjedis = getInstance();
		long index = sjedis.zrank(key, member);
		returnResource(sjedis);
		return index;
	}

	/**
	 * 获取指定值在集合中的位置，集合排序从高到低
	 * 
	 * @see zrank
	 * @param String
	 *            key
	 * @param String
	 *            member
	 * @return long 位置
	 */
	public static long zrevrank(String key, String member) {
		// ShardedJedis sjedis = getShardedJedis();
		Jedis sjedis = getInstance();
		long index = sjedis.zrevrank(key, member);
		returnResource(sjedis);
		return index;
	}

	/**
	 * 从集合中删除成员
	 * 
	 * @param String
	 *            key
	 * @param String
	 *            member
	 * @return 返回1成功
	 */
	public long zrem(String key, String member) {
		Jedis jedis = getInstance();
		long s = jedis.zrem(key, member);
		returnResource(jedis);
		return s;
	}

	/**
	 * 删除
	 * 
	 * @param key
	 * @return
	 */
	public long zrem(String key) {
		Jedis jedis = getInstance();
		long s = jedis.del(key);
		returnResource(jedis);
		return s;
	}

	/**
	 * 删除给定位置区间的元素
	 * 
	 * @param String
	 *            key
	 * @param int
	 *            start 开始区间，从0开始(包含)
	 * @param int
	 *            end 结束区间,-1为最后一个元素(包含)
	 * @return 删除的数量
	 */
	public long zremrangeByRank(String key, int start, int end) {
		Jedis jedis = getInstance();
		long s = jedis.zremrangeByRank(key, start, end);
		returnResource(jedis);
		return s;
	}

	/**
	 * 删除给定权重区间的元素
	 * 
	 * @param String
	 *            key
	 * @param double
	 *            min 下限权重(包含)
	 * @param double
	 *            max 上限权重(包含)
	 * @return 删除的数量
	 */
	public static long zremrangeByScore(String key, double min, double max) {
		Jedis jedis = getInstance();
		long s = jedis.zremrangeByScore(key, min, max);
		returnResource(jedis);
		return s;
	}

	/**
	 * 获取给定区间的元素，原始按照权重由高到低排序
	 * 
	 * @param String
	 *            key
	 * @param int
	 *            start
	 * @param int
	 *            end
	 * @return Set<String>
	 */
	public static Set<String> zrevrange(String key, int start, int end) {
		Jedis sjedis = getInstance();
		Set<String> set = sjedis.zrevrange(key, start, end);
		returnResource(sjedis);
		return set;
	}

	/**
	 * 获取给定值在集合中的权重
	 * 
	 * @param String
	 *            key
	 * @param memeber
	 * @return double 权重
	 */
	public static double zscore(String key, String memebr) {
		Jedis sjedis = getInstance();
		Double score = sjedis.zscore(key, memebr);
		returnResource(sjedis);
		if (score != null)
			return score;
		return 0;
	}

	/**
	 * 从hash中删除指定的存储
	 * 
	 * @param String
	 *            key
	 * @param String
	 *            fieid 存储的名字
	 * @return 状态码，1成功，0失败
	 */
	public static long hdel(String key, String fieid) {
		Jedis jedis = getInstance();
		long s = jedis.hdel(key, fieid);
		returnResource(jedis);
		return s;
	}

	public static long hdel(String key) {
		Jedis jedis = getInstance();
		long s = jedis.del(key);
		returnResource(jedis);
		return s;
	}

	/**
	 * 测试hash中指定的存储是否存在
	 * 
	 * @param String
	 *            key
	 * @param String
	 *            fieid 存储的名字
	 * @return 1存在，0不存在
	 */
	public static boolean hexists(String key, String fieid) {
		Jedis sjedis = getInstance();
		boolean s = sjedis.hexists(key, fieid);
		returnResource(sjedis);
		return s;
	}

	/**
	 * 返回hash中指定存储位置的值
	 * 
	 * @param String
	 *            key
	 * @param String
	 *            fieid 存储的名字
	 * @return 存储对应的值
	 */
	public static String hget(String key, String fieid) {
		Jedis sjedis = getInstance();
		String s = sjedis.hget(key, fieid);
		returnResource(sjedis);
		return s;
	}

	public static byte[] hget(byte[] key, byte[] fieid) {
		Jedis sjedis = getInstance();
		byte[] s = sjedis.hget(key, fieid);
		returnResource(sjedis);
		return s;
	}

	/**
	 * 以Map的形式返回hash中的存储和值
	 * 
	 * @param String
	 *            key
	 * @return Map<Strinig,String>
	 */
	public static Map<String, String> hgetAll(String key) {
		Jedis sjedis = getInstance();
		Map<String, String> map = sjedis.hgetAll(key);
		returnResource(sjedis);
		return map;
	}

	/**
	 * 添加一个对应关系
	 * 
	 * @param String
	 *            key
	 * @param String
	 *            fieid
	 * @param String
	 *            value
	 * @return 状态码 1成功，0失败，fieid已存在将更新，也返回0
	 **/
	public static long hset(String key, String fieid, String value) {
		Jedis jedis = getInstance();
		long s = jedis.hset(key, fieid, value);
		returnResource(jedis);
		return s;
	}

	public static long hset(String key, String fieid, byte[] value) {
		Jedis jedis = getInstance();
		long s = jedis.hset(key.getBytes(), fieid.getBytes(), value);
		returnResource(jedis);
		return s;
	}

	/**
	 * 添加对应关系，只有在fieid不存在时才执行
	 * 
	 * @param String
	 *            key
	 * @param String
	 *            fieid
	 * @param String
	 *            value
	 * @return 状态码 1成功，0失败fieid已存
	 **/
	public static long hsetnx(String key, String fieid, String value) {
		Jedis jedis = getInstance();
		long s = jedis.hsetnx(key, fieid, value);
		returnResource(jedis);
		return s;
	}

	/**
	 * 获取hash中value的集合
	 * 
	 * @param String
	 *            key
	 * @return List<String>
	 */
	public static List<String> hvals(String key) {
		Jedis sjedis = getInstance();
		List<String> list = sjedis.hvals(key);
		returnResource(sjedis);
		return list;
	}

	/**
	 * 在指定的存储位置加上指定的数字，存储位置的值必须可转为数字类型
	 * 
	 * @param String
	 *            key
	 * @param String
	 *            fieid 存储位置
	 * @param String
	 *            long value 要增加的值,可以是负数
	 * @return 增加指定数字后，存储位置的值
	 */
	public static long hincrby(String key, String fieid, long value) {
		Jedis jedis = getInstance();
		long s = jedis.hincrBy(key, fieid, value);
		returnResource(jedis);
		return s;
	}

	/**
	 * 返回指定hash中的所有存储名字,类似Map中的keySet方法
	 * 
	 * @param String
	 *            key
	 * @return Set<String> 存储名称的集合
	 */
	public static Set<String> hkeys(String key) {
		Jedis sjedis = getInstance();
		Set<String> set = sjedis.hkeys(key);
		returnResource(sjedis);
		return set;
	}

	/**
	 * 获取hash中存储的个数，类似Map中size方法
	 * 
	 * @param String
	 *            key
	 * @return long 存储的个数
	 */
	public static long hlen(String key) {
		Jedis sjedis = getInstance();
		long len = sjedis.hlen(key);
		returnResource(sjedis);
		return len;
	}

	/**
	 * 根据多个key，获取对应的value，返回List,如果指定的key不存在,List对应位置为null
	 * 
	 * @param String
	 *            key
	 * @param String
	 *            ... fieids 存储位置
	 * @return List<String>
	 */
	public static List<String> hmget(String key, String... fieids) {
		Jedis sjedis = getInstance();
		List<String> list = sjedis.hmget(key, fieids);
		returnResource(sjedis);
		return list;
	}

	public static List<byte[]> hmget(byte[] key, byte[]... fieids) {
		Jedis sjedis = getInstance();
		List<byte[]> list = sjedis.hmget(key, fieids);
		returnResource(sjedis);
		return list;
	}

	/**
	 * 添加对应关系，如果对应关系已存在，则覆盖
	 * 
	 * @param Strin
	 *            key
	 * @param Map
	 *            <String,String> 对应关系
	 * @return 状态，成功返回OK
	 */
	public static String hmset(String key, Map<String, String> map) {
		Jedis jedis = getInstance();
		String s = jedis.hmset(key, map);
		returnResource(jedis);
		return s;
	}

	/**
	 * 添加对应关系，如果对应关系已存在，则覆盖
	 * 
	 * @param Strin
	 *            key
	 * @param Map
	 *            <String,String> 对应关系
	 * @return 状态，成功返回OK
	 */
	public static String hmset(byte[] key, Map<byte[], byte[]> map) {
		Jedis jedis = getInstance();
		String s = jedis.hmset(key, map);
		returnResource(jedis);
		return s;
	}

	// ***********************Lists***********************
	/**
	 * List长度
	 * 
	 * @param String
	 *            key
	 * @return 长度
	 */
	public static long llen(String key) {
		return llen(SafeEncoder.encode(key));
	}

	/**
	 * List长度
	 * 
	 * @param byte[]
	 *            key
	 * @return 长度
	 */
	public static long llen(byte[] key) {
		// ShardedJedis sjedis = getShardedJedis();
		Jedis sjedis = getInstance();
		long count = sjedis.llen(key);
		returnResource(sjedis);
		return count;
	}

	/**
	 * 覆盖操作,将覆盖List中指定位置的值
	 * 
	 * @param byte[]
	 *            key
	 * @param int
	 *            index 位置
	 * @param byte[]
	 *            value 值
	 * @return 状态码
	 */
	public static String lset(byte[] key, int index, byte[] value) {
		Jedis jedis = getInstance();
		String status = jedis.lset(key, index, value);
		returnResource(jedis);
		return status;
	}

	/**
	 * 覆盖操作,将覆盖List中指定位置的值
	 * 
	 * @param key
	 * @param int
	 *            index 位置
	 * @param String
	 *            value 值
	 * @return 状态码
	 */
	public static String lset(String key, int index, String value) {
		return lset(SafeEncoder.encode(key), index, SafeEncoder.encode(value));
	}

	/**
	 * 在value的相对位置插入记录
	 * 
	 * @param key
	 * @param LIST_POSITION
	 *            前面插入或后面插入
	 * @param String
	 *            pivot 相对位置的内容
	 * @param String
	 *            value 插入的内容
	 * @return 记录总数
	 */
	public static long linsert(String key, LIST_POSITION where, String pivot, String value) {
		return linsert(SafeEncoder.encode(key), where, SafeEncoder.encode(pivot), SafeEncoder.encode(value));
	}

	/**
	 * 在指定位置插入记录
	 * 
	 * @param String
	 *            key
	 * @param LIST_POSITION
	 *            前面插入或后面插入
	 * @param byte[]
	 *            pivot 相对位置的内容
	 * @param byte[]
	 *            value 插入的内容
	 * @return 记录总数
	 */
	public static long linsert(byte[] key, LIST_POSITION where, byte[] pivot, byte[] value) {
		Jedis jedis = getInstance();
		long count = jedis.linsert(key, where, pivot, value);
		returnResource(jedis);
		return count;
	}

	/**
	 * 获取List中指定位置的值
	 * 
	 * @param String
	 *            key
	 * @param int
	 *            index 位置
	 * @return 值
	 **/
	public static String lindex(String key, int index) {
		return SafeEncoder.encode(lindex(SafeEncoder.encode(key), index));
	}

	/**
	 * 获取List中指定位置的值
	 * 
	 * @param byte[]
	 *            key
	 * @param int
	 *            index 位置
	 * @return 值
	 **/
	public static byte[] lindex(byte[] key, int index) {
		Jedis sjedis = getInstance();
		byte[] value = sjedis.lindex(key, index);
		returnResource(sjedis);
		return value;
	}

	/**
	 * 将List中的第一条记录移出List
	 * 
	 * @param String
	 *            key
	 * @return 移出的记录
	 */
	public static String lpop(String key) {
		return SafeEncoder.encode(lpop(SafeEncoder.encode(key)));
	}

	/**
	 * 将List中的第一条记录移出List
	 * 
	 * @param byte[]
	 *            key
	 * @return 移出的记录
	 */
	public static byte[] lpop(byte[] key) {
		Jedis jedis = getInstance();
		byte[] value = jedis.lpop(key);
		returnResource(jedis);
		return value;
	}

	/**
	 * 将List中最后第一条记录移出List
	 * 
	 * @param byte[]
	 *            key
	 * @return 移出的记录
	 */
	public static String rpop(String key) {
		Jedis jedis = getInstance();
		String value = jedis.rpop(key);
		returnResource(jedis);
		return value;
	}

	/**
	 * 向List尾部追加记录
	 * 
	 * @param String
	 *            key
	 * @param String
	 *            value
	 * @return 记录总数
	 */
	public static long lpush(String key, String value) {
		return lpush(SafeEncoder.encode(key), SafeEncoder.encode(value));
	}

	/**
	 * 向List头部追加记录
	 * 
	 * @param String
	 *            key
	 * @param String
	 *            value
	 * @return 记录总数
	 */
	public static long rpush(String key, String value) {
		Jedis jedis = getInstance();
		long count = jedis.rpush(key, value);
		returnResource(jedis);
		return count;
	}

	/**
	 * 向List头部追加记录
	 * 
	 * @param String
	 *            key
	 * @param String
	 *            value
	 * @return 记录总数
	 */
	public static long rpush(byte[] key, byte[] value) {
		Jedis jedis = getInstance();
		long count = jedis.rpush(key, value);
		returnResource(jedis);
		return count;
	}

	/**
	 * 向List中追加记录
	 * 
	 * @param byte[]
	 *            key
	 * @param byte[]
	 *            value
	 * @return 记录总数
	 */
	public static long lpush(byte[] key, byte[] value) {
		Jedis jedis = getInstance();
		long count = jedis.lpush(key, value);
		returnResource(jedis);
		return count;
	}

	/**
	 * 获取指定范围的记录，可以做为分页使用
	 * 
	 * @param String
	 *            key
	 * @param long
	 *            start
	 * @param long
	 *            end
	 * @return List
	 */
	public static List<String> lrange(String key, long start, long end) {
		Jedis jedis = getInstance();
		List<String> list = jedis.lrange(key, start, end);
		returnResource(jedis);
		return list;
	}

	/**
	 * 获取指定范围的记录，可以做为分页使用
	 * 
	 * @param byte[]
	 *            key
	 * @param int
	 *            start
	 * @param int
	 *            end 如果为负数，则尾部开始计算
	 * @return List
	 */
	public static List<byte[]> lrange(byte[] key, int start, int end) {
		// ShardedJedis sjedis = getShardedJedis();
		Jedis sjedis = getInstance();
		List<byte[]> list = sjedis.lrange(key, start, end);
		returnResource(sjedis);
		return list;
	}

	/**
	 * 删除List中c条记录，被删除的记录值为value
	 * 
	 * @param byte[]
	 *            key
	 * @param int
	 *            c 要删除的数量，如果为负数则从List的尾部检查并删除符合的记录
	 * @param byte[]
	 *            value 要匹配的值
	 * @return 删除后的List中的记录数
	 */
	public static Long lrem(byte[] key, int c, byte[] value) {
		Jedis jedis = getInstance();
		Long count = null;
		try {
			count = jedis.lrem(key, c, value);
		} catch (Exception e) {
			throw new RedisException("lrem(byte[] key, int c, byte[] value) is error", e);
		} finally {
			returnResource(jedis);
		}

		return count;
	}

	/**
	 * 删除List中c条记录，被删除的记录值为value
	 * 
	 * @param String
	 *            key
	 * @param int
	 *            c 要删除的数量，如果为负数则从List的尾部检查并删除符合的记录
	 * @param String
	 *            value 要匹配的值
	 * @return 删除后的List中的记录数
	 */
	public static long lrem(String key, int c, String value) {
		return lrem(SafeEncoder.encode(key), c, SafeEncoder.encode(value));
	}

	/**
	 * 算是删除吧，只保留start与end之间的记录
	 * 
	 * @param byte[]
	 *            key
	 * @param int
	 *            start 记录的开始位置(0表示第一条记录)
	 * @param int
	 *            end 记录的结束位置（如果为-1则表示最后一个，-2，-3以此类推）
	 * @return 执行状态码
	 */
	public static String ltrim(byte[] key, int start, int end) {
		Jedis jedis = getInstance();
		String str = null;
		try {
			str = jedis.ltrim(key, start, end);
		} catch (Exception e) {
			throw new RedisException("ltrim(byte[] key, int start, int end) is error", e);
		} finally {
			returnResource(jedis);
		}
		return str;
	}

	/**
	 * 算是删除吧，只保留start与end之间的记录
	 * 
	 * @param String
	 *            key
	 * @param int
	 *            start 记录的开始位置(0表示第一条记录)
	 * @param int
	 *            end 记录的结束位置（如果为-1则表示最后一个，-2，-3以此类推）
	 * @return 执行状态码
	 */
	public static String ltrim(String key, int start, int end) {
		return ltrim(SafeEncoder.encode(key), start, end);
	}

	// ***************************
	/**
	 * @Title: getList @Description：(
	 *         根据key以及类得到redis服务器中集合字符串对应的集合) @param @param key @param @param
	 *         clazz @param @return 设定文件 @return List<T> 返回类型 @throws
	 */
	public static <T> List<T> getList(String key, Class<T> clazz) {
		String valueStr = get(key);
		if (StringUtils.isBlank(valueStr))
			return null;
		if (!isGoodJson(valueStr)) {
			valueStr = JSON.toJSONString(valueStr);
		}
		return JSONArray.parseArray(valueStr, clazz);
	}

	/**
	 * (根据key以及类得到redis服务器中对象字符串对应的对象) @param @param key @param @param
	 * clazz @param @return 设定文件 @return T 返回类型 @throws
	 */
	public static <T> T getObject(String key, Class<T> clazz) {
		if (StringUtils.isBlank(key)) {
			return null;
		}
		String valueStr = get(key);
		if (StringUtils.isBlank(valueStr))
			return null;
		if (!isGoodJson(valueStr)) {
			valueStr = JSON.toJSONString(valueStr);
		}
		return (T) JSON.parseObject(valueStr, clazz);
	}

	@SuppressWarnings("unchecked")
	public static <T> T getObJect(String key) {
		if (StringUtils.isBlank(key)) {
			return null;
		}
		Jedis jedis = getInstance();
		try {
			byte[] bytes = jedis.get(SafeEncoder.encode(key));
			return (T) JavaSerializerUtil.unserialize(bytes);
		} catch (Exception e) {
			throw new RedisException("get data is error", e);
		} finally {
			returnResource(jedis);
		}

	}

	/**
	 * 
	 * (判断是否是json格式字符串) @param json @param @return 设定文件 @return boolean
	 * 返回类型 @throws
	 */
	public static boolean isGoodJson(String json) {
		if (StringUtils.isBlank(json)) {
			return false;
		}
		try {
			JSON.parse(json);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
