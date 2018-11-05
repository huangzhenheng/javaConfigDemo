package com.hzh.redis.core;

import redis.clients.jedis.Jedis;

public interface JedisResource {
	
	/**
	 * 
	 * @return
	 */
	public Jedis getInstance();

	public void returnResource(Jedis jedis);
}
