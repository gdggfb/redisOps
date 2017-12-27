package cc.xkxk.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisAutoConfig {

	@Value("${spring.redis.host}")
	private String host;

	@Value("${spring.redis.port}")
	private int port;

	// @Value("${spring.redis.timeout}")
	// private int timeout;
	//
	// @Value("${spring.redis.pool.max-idle}")
	// private int maxIdle;
	//
	// @Value("${spring.redis.pool.max-total}")
	// private int maxTotal;
	//
	// @Value("${spring.redis.pool.max-wait}")
	// private long maxWaitMillis;
	//
	// @Value("${spring.redis.password}")
	// private String password;

	@Bean
	public JedisPool jedisPool() {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxWaitMillis(5);
		jedisPoolConfig.setMaxTotal(500);

		return new JedisPool(jedisPoolConfig, host, port);
	}

	@Bean
	public RedisAutoConfigBean redisOps(JedisPool jedisPool) {
		return new RedisAutoConfigBean(jedisPool);
	}
}
