package ewhine.cache;

import ewhine.config.Config;
import net.spy.memcached.AddrUtil;
import net.spy.memcached.BinaryConnectionFactory;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.internal.BulkFuture;
import net.spy.memcached.internal.OperationFuture;
import net.spy.memcached.transcoders.Transcoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tools.SU;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Memcached {
	final static private Logger LOG = LoggerFactory.getLogger(Memcached.class
			.getName());

	private static Memcached instance = new Memcached();
	protected int _timeoutInMs = 1000;

	MemcachedClientConnector connector = null;
	private Transcoder<byte[]> _nullTranscoder = new NullTranscoder();

	private MemcachedClient c = null;

	private Memcached() {

		Config config = Config.getPropertyConfig("memcached.properties");
		String url = config.get("memcached.server", "127.0.0.1:11211")
				.toString();

		// try {
		// connector = new MemcachedClientConnector(url);
		// connector.connect();
		// } catch (Throwable t) {
		// LOG.error("create mqtt connection failed!", t);
		// }

		try {
			this.c = new MemcachedClient(new BinaryConnectionFactory(),
					AddrUtil.getAddresses(url));

		} catch (IOException e) {
			if(LOG.isErrorEnabled())
			LOG.error(SU.cat("connect to ",url," failed",e));
//			LOG.error("connect to {} failed", url, e);
		}

	}

	public static Memcached getInstance() {
		return instance;
	}

	public byte[] getRaw(String key) throws KeyValueStoreClientException {

		byte[] raw;
		Future<byte[]> f = c.asyncGet(key, _nullTranscoder);
		try {

			raw = f.get(_timeoutInMs, TimeUnit.MILLISECONDS);
		} catch (Exception error) {
			f.cancel(true);
			throw (new KeyValueStoreClientException(error));
		}
		return raw;
	}

	public Map<String, byte[]> getMultiRaw(Collection<String> keys)
			throws KeyValueStoreClientException {

		Map<String, byte[]> raw;

		BulkFuture<Map<String, byte[]>> f = c.asyncGetBulk(keys,
				_nullTranscoder);
		try {

			raw = f.get(_timeoutInMs, TimeUnit.MILLISECONDS);
		} catch (Exception error) {
			f.cancel(true);
			throw (new KeyValueStoreClientException(error));
		}
		return raw;
	}

	public boolean writeRaw(String key, byte[] o)
			throws KeyValueStoreClientException {

		boolean oper = false;
		OperationFuture<Boolean> f = c.set(key, 60 * 60 * 24 * 7, o,
				_nullTranscoder);
		try {

			oper = f.get(_timeoutInMs, TimeUnit.MILLISECONDS);
		} catch (Exception error) {
			f.cancel();
			throw (new KeyValueStoreClientException(error));
		}
		return oper;
	}

	public static Object fetch(String key, ValueFetcher fetcher) {
		byte[] raw = null;
		try {
			raw = Memcached.instance.getRaw(key);

			// System.out.println(Secure.bytesToHex(raw));
		} catch (KeyValueStoreClientException e) {
			if(LOG.isErrorEnabled())
			LOG.error("read memcached failed", e);
			return null;
		}

		ValueTranscoder v = fetcher.fetch(key, raw);
		if (v != null) {
			try {
				Memcached.instance.writeRaw(key, v.toBytes());
			} catch (KeyValueStoreClientException e) {
				if(LOG.isErrorEnabled())
				LOG.error("write memcached failed", e);
			}
			return v;
		}

		return null;
	}

	public static <T> T fetch(String key, DataFetcher<T> fetcher) {
		byte[] raw = null;
		try {
			raw = Memcached.instance.getRaw(key);
			if (raw != null) {
				@SuppressWarnings("unchecked")
				T result = (T) FSTSerialization.asObject(raw);
				return result;
			}
			// System.out.println(Secure.bytesToHex(raw));
		} catch (KeyValueStoreClientException e) {
			if(LOG.isErrorEnabled())
				LOG.error("read memcached failed", e);
			return null;
		}

		T v = fetcher.fetch(key);
		if (v != null) {
			try {
				Memcached.instance.writeRaw(key,
						FSTSerialization.toByteArray(v));
			} catch (KeyValueStoreClientException e) {
				if(LOG.isErrorEnabled())
				LOG.error("write memcached failed", e);
			}
			return v;
		}

		return null;
	}

	/**
	 * @param keys
	 * @param fetcher
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> fetchMuilt(Collection<String> keys,
			DataFetcher<T> fetcher) {
		Map<String, byte[]> raw = null;

		List<String> lostKeys = new ArrayList<String>();
		List<T> result = new ArrayList<T>(keys.size());
		try {
			raw = Memcached.instance.getMultiRaw(keys);
			if (raw != null) {
				for (String k : keys) {
					byte[] value = raw.get(k);
					if (value != null) {

						result.add((T) FSTSerialization.asObject(value));

					} else {
						lostKeys.add(k);
					}
				}

				if (lostKeys.size() == 0) {
					return result;

				}

			}
			// System.out.println(Secure.bytesToHex(raw));
		} catch (KeyValueStoreClientException e) {
			if(LOG.isErrorEnabled())
			LOG.error("read memcached failed", e);
			return result;
		}

		if (lostKeys.size() > 0) {
			for (String k : lostKeys) {
				T v = fetcher.fetch(k);
				if (v != null) {
					result.add(v);
				}

			}
		}

		return result;
	}

	public void expire_cache(String key) {
		this.c.delete(key);
	}

	public <T> void write_cache(String key, T object) {
		try {
			this.writeRaw(key, FSTSerialization.toByteArray(object));
		} catch (KeyValueStoreClientException e) {
			e.printStackTrace();
		}
		// this.c.set(key, 60 * 60 * 24 * 7, object);
	}

	/**
	 * @param key
	 */
	public static void delCache(String key) {
		Memcached.getInstance().c.delete(key);
	}

	public Object read_cache(String key) {
		byte[] raw = null;
		try {
			raw = Memcached.instance.getRaw(key);
			if (raw != null) {
				@SuppressWarnings("unchecked")
				Object result = FSTSerialization.asObject(raw);
				return result;
			}
			// System.out.println(Secure.bytesToHex(raw));
		} catch (Exception e) {
			if(LOG.isErrorEnabled())
				LOG.error("read memcached failed", e);
			if (raw != null) {
				delCache(key);
			}
			return null;
		}
		return null;
		
	}
}
