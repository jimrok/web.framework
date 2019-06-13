package ewhine.redis;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;
import tools.StringUtil;
import ewhine.config.Config;

public class RedisPool {

	// private static JedisPool pool;
	//
	// static{
	// String host = Config.getConfig("redis.host");
	// int port = Integer.valueOf(Config.getConfig("redis.port"));
	// pool = new JedisPool( new JedisPoolConfig(),host,port);
	// }

	final static private Logger LOG = LoggerFactory.getLogger(RedisPool.class.getName());
	//private JedisShardInfo info = null;
	private static RedisPool instance = null;
	private JedisPool pool;

	private RedisPool() {

		final Config config = Config.getPropertyConfig("redis.properties");
		String host = config.get("redis.host");
		int port = Integer.valueOf(config.get("redis.port"));
		String pass = config.get("redis.password");
		if ("".equals(pass)) {
			pass = null;
		}

		// info = new JedisShardInfo( host, port );
		// info.setPassword( pass );
		JedisPoolConfig c = new JedisPoolConfig();

		//是否启用后进先出, 默认true
		c.setLifo(true);
		//最大空闲连接数, 默认8个
		c.setMaxIdle(Integer.parseInt(config.get("redis.maxIdle")));
		//最大连接数, 默认8个
		c.setMaxTotal(Integer.parseInt(config.get("redis.maxTotal")));
		//获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
		c.setMaxWaitMillis(-1);
		//逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
		c.setMinEvictableIdleTimeMillis(1800000);
		//最小空闲连接数, 默认0
		c.setMinIdle(0);
		//每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3
		c.setNumTestsPerEvictionRun(3);
		//对象空闲多久后逐出, 当空闲时间>该值 且 空闲连接>最大空闲数 时直接逐出,不再根据MinEvictableIdleTimeMillis判断  (默认逐出策略)
		c.setSoftMinEvictableIdleTimeMillis(1800000);
		//在获取连接的时候检查有效性, 默认false
		c.setTestOnBorrow(false);
		//在空闲时检查有效性, 默认false
		c.setTestWhileIdle(false);
		//逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
		c.setTimeBetweenEvictionRunsMillis(-1);

		pool = new JedisPool(c, host, port, Protocol.DEFAULT_TIMEOUT, pass, Protocol.DEFAULT_DATABASE, null);
		if(LOG.isInfoEnabled())
			LOG.info(StringUtil.concat("create redis use host: ",host,",port: ",port));
//		LOG.info("create redis use host: {},port: {}", host, port);
	}

	public static void start() {
		RedisPool.instance();
	}

	private static RedisPool instance() {

		if (instance == null) {

			synchronized (RedisPool.class) {
				instance = new RedisPool();
			}
		}

		return instance;
	}

	private Jedis getConnection() {

		Jedis jedis = pool.getResource();//info.createResource();
		return jedis;
	}

	final static Jedis getJedis() {

		Jedis conn = RedisPool.instance().getConnection();
		return conn;
	}


	public static void release(Jedis jedis) {
		jedis.close();
//    	instance.pool.returnResource(jedis);
//        if (jedis != null) {
//            jedis.disconnect();
//            jedis.quit();
//            jedis = null;
//        }
	}

	public static void stop() {
		instance.pool.destroy();
	}

	public static void main(String[] ss) {
		Jedis jedis = RedisPool.getJedis();
		Set<String> set = jedis.keys("*");
		System.out.println(set);
		RedisPool.release(jedis);
	}
}
