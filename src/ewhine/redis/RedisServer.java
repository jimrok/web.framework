package ewhine.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

public class RedisServer {

	public static long lpush(String key, String... args) {

		Jedis conn = RedisPool.getJedis();

		try {
			return conn.lpush(key, args);
		} finally {
			RedisPool.release(conn);
		}

	}

	public static String lpop(String key) {

		Jedis conn = RedisPool.getJedis();

		try {
			return conn.lpop(key);
		} finally {
			RedisPool.release(conn);
		}

	}

	public static long llen(String key) {

		Jedis conn = RedisPool.getJedis();

		try {
			return conn.llen(key);
		} finally {
			RedisPool.release(conn);
		}

	}

	public static long rpush(String key, String... args) {

		Jedis conn = RedisPool.getJedis();

		try {
			return conn.rpush(key, args);
		} finally {
			RedisPool.release(conn);
		}

	}

	public static String rpop(String key) {
		Jedis conn = RedisPool.getJedis();

		try {
			return conn.rpop(key);
		} finally {
			RedisPool.release(conn);
		}
	}

	public static long zadd(String key, double score, String member) {

		Jedis conn = RedisPool.getJedis();
		try {
			return conn.zadd(key, score, member);
		} finally {
			RedisPool.release(conn);
		}
	}

	public static Set<String> zrevrangeByScore(String key, String max,
											   String min, int offset, int count) {

		Jedis conn = RedisPool.getJedis();
		try {

			return conn.zrevrangeByScore(key, max, min, offset, count);

		} finally {
			RedisPool.release(conn);
		}
	}

	public static Set<String> zrevrange(String key, long start, long end) {

		Jedis conn = RedisPool.getJedis();
		try {
			return conn.zrevrange(key, start, end);

		} finally {
			RedisPool.release(conn);
		}
	}

	public static long zcount(String key, String min, String max) {

		Jedis conn = RedisPool.getJedis();
		try {
			return conn.zcount(key, min, max);
		} finally {
			RedisPool.release(conn);
		}
	}

	public static Double zscore(String key, String member) {

		Jedis conn = RedisPool.getJedis();
		try {
			return conn.zscore(key, member);
		} finally {
			RedisPool.release(conn);
		}
	}

	public static Map<byte[], byte[]> hgetAll(byte[] key) {

		Jedis conn = RedisPool.getJedis();

		try {
			return conn.hgetAll(key);
		} finally {
			RedisPool.release(conn);
		}

	}

	public static List<String> hmget(String key, String... args) {

		Jedis conn = RedisPool.getJedis();

		try {

			return conn.hmget(key, args);
		} finally {
			RedisPool.release(conn);
		}

	}

	public static Long hset(String hkey, String key, String value) {

		Jedis conn = RedisPool.getJedis();

		try {

			return conn.hset(hkey, key, value);

		} finally {
			RedisPool.release(conn);
		}

	}

	public static Long hsetnx(String hkey, String key, String value) {

		Jedis conn = RedisPool.getJedis();

		try {

			return conn.hsetnx(hkey, key, value);

		} finally {
			RedisPool.release(conn);
		}

	}

	public static String hget(String key, String field) {

		Jedis conn = RedisPool.getJedis();

		try {
			return conn.hget(key, field);
		} finally {
			RedisPool.release(conn);
		}

	}

	public static Long hincrBy(String key, String field, long value) {

		Jedis conn = RedisPool.getJedis();

		try {
			return conn.hincrBy(key, field, value);
		} finally {
			RedisPool.release(conn);
		}

	}

	public static String get(String key) {

		Jedis conn = RedisPool.getJedis();

		try {
			return conn.get(key);
		} finally {
			RedisPool.release(conn);
		}

	}

	public static Long incr(String key) {

		Jedis conn = RedisPool.getJedis();

		try {
			return conn.incr(key);
		} finally {
			RedisPool.release(conn);
		}

	}
	
	public static Long incr(String key,long integer) {

		Jedis conn = RedisPool.getJedis();

		try {
			return conn.incrBy(key, integer);
		} finally {
			RedisPool.release(conn);
		}

	}
	
	public static Long decr(String key,long integer) {

		Jedis conn = RedisPool.getJedis();

		try {
			return conn.decrBy(key, integer);
		} finally {
			RedisPool.release(conn);
		}

	}

	public static Set<String> smembers(String key) {

		Jedis conn = RedisPool.getJedis();

		try {
			return conn.smembers(key);
		} finally {
			RedisPool.release(conn);
		}

	}

	public static String set(String key, String value) {

		Jedis conn = RedisPool.getJedis();

		try {
			return conn.set(key, value);
		} finally {
			RedisPool.release(conn);
		}

	}

	/**
	 * @param key
	 * @param val
	 * @return
	 */
	public static boolean setnx(String key, String val) {
		Jedis conn = RedisPool.getJedis();

		try {
			return conn.setnx(key, val) > 0;
		} finally {
			RedisPool.release(conn);
		}
	}
	
	public static String getSet(String key, String val) {
		Jedis conn = RedisPool.getJedis();

		try {
			return conn.getSet(key, val);
		} finally {
			RedisPool.release(conn);
		}
	}

	public static boolean exists(String key) {

		Jedis conn = RedisPool.getJedis();

		try {
			return conn.exists(key);
		} finally {
			RedisPool.release(conn);
		}

	}

	public static byte[] get(byte[] key) {

		Jedis conn = RedisPool.getJedis();

		try {
			return conn.get(key);
		} finally {
			RedisPool.release(conn);
		}

	}



	public static List<String> lrange(String key, int start, int end) {
		Jedis conn = RedisPool.getJedis();

		try {
			return conn.lrange(key, start, end);
		} finally {
			RedisPool.release(conn);
		}

	}

	public static Long hset(String hkey, String unremind_key, int value) {
		Jedis conn = RedisPool.getJedis();

		try {
			return conn.hset(hkey, unremind_key, String.valueOf(value));
		} finally {
			RedisPool.release(conn);
		}
	}

	/**
	 * 删除hash
	 *
	 * @param hkey
	 * @param fields
	 * @return
	 */
	public static Long hdel(String hkey, String... fields) {
		Jedis conn = RedisPool.getJedis();
		try {
			return conn.hdel(hkey, fields);
		} finally {
			RedisPool.release(conn);
		}
	}

	/**
	 * @param key
	 * @return
	 */
	public static Long del(String key) {
		Jedis conn = RedisPool.getJedis();
		try {
			return conn.del(key);
		} finally {
			RedisPool.release(conn);
		}
	}

	public static String watch(String conv_users_key, String reading_counting_key) {
		Jedis conn = RedisPool.getJedis();

		try {
			return conn.watch(conv_users_key, reading_counting_key);
		} finally {
			RedisPool.release(conn);
		}
	}

	public static Set<Tuple> zrevrangeWithScores(String string, int i, int j) {
		Jedis conn = RedisPool.getJedis();

		try {
			return conn.zrevrangeWithScores(string, i, j);
		} finally {
			RedisPool.release(conn);
		}
	}

	public static Long zcard(String string) {
		Jedis conn = RedisPool.getJedis();

		try {
			return conn.zcard(string);
		} finally {
			RedisPool.release(conn);
		}
	}

	public static long sadd(String key, String[] _ids) {
		Jedis conn = RedisPool.getJedis();

		try {
			return conn.sadd(key, _ids);
		} finally {
			RedisPool.release(conn);
		}
	}
	
	public static boolean sismember(String key, String member) {
		Jedis conn = RedisPool.getJedis();

		try {
			return conn.sismember(key, member);
		} finally {
			RedisPool.release(conn);
		}
	}

	public static Set<String> keys(String string) {
		Jedis conn = RedisPool.getJedis();

		try {
			return conn.keys(string);
		} finally {
			RedisPool.release(conn);
		}
	}

	public static long srem(String key, String member) {
		Jedis conn = RedisPool.getJedis();

		try {
			return conn.srem(key, member);
		} finally {
			RedisPool.release(conn);
		}
	}

	public static void zrem(String string, long id) {
		Jedis conn = RedisPool.getJedis();

		try {
			conn.zrem(string, String.valueOf(id));
		} finally {
			RedisPool.release(conn);
		}
	}



	public static Map<String, String> hgetall(String string) {
		Jedis conn = RedisPool.getJedis();

		try {
			return conn.hgetAll(string);
		} finally {
			RedisPool.release(conn);
		}
	}

	public static Double zcore(String key, String member) {
		Jedis conn = RedisPool.getJedis();
		try {
			return conn.zscore(key, member);
		} finally {
			RedisPool.release(conn);
		}
	}


	public static void main(String[] args) {
		String older_than_id=" ";
		try {
			Long.valueOf(older_than_id);
		} catch (Exception e) {
			older_than_id = "0";
		}
		System.out.println(RedisServer.zrevrangeByScore("thd:111", "(" + older_than_id, "0",  0, 1));
	}
}
