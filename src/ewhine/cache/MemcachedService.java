package ewhine.cache;

import com.dehuinet.activerecord.cache.DataFetcher;
import com.dehuinet.activerecord.cache.ICache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class MemcachedService implements ICache {

	final static private Logger LOG = LoggerFactory
			.getLogger(MemcachedService.class.getName());

	public MemcachedService() {
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> fetchMuilt(Collection<String> keys,
			DataFetcher<T> fetcher) {
		Map<String, byte[]> raw = null;

		List<String> lostKeys = new ArrayList<String>();
		List<T> result = new ArrayList<T>(keys.size());
		try {
			raw = Memcached.getInstance().getMultiRaw(keys);
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
			if (LOG.isErrorEnabled())
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

	@Override
	public <T> T fetch(final String key, DataFetcher<T> fetcher) {
		byte[] raw = null;
		final Memcached inst = Memcached.getInstance();
		try {
			raw = inst.getRaw(key);
			if (raw != null) {
				@SuppressWarnings("unchecked")
				T result = (T) FSTSerialization.asObject(raw);
				return result;
			}
			// System.out.println(Secure.bytesToHex(raw));
		} catch (Throwable e) {
				
			if (LOG.isErrorEnabled())
				LOG.error("read memcached failed,key:" + key, e);
		}

		T v = fetcher.fetch(key);
		
		if (v != null) {
			
			try {
				inst.writeRaw(key, FSTSerialization.toByteArray(v));
			} catch (KeyValueStoreClientException e) {
				if (LOG.isErrorEnabled())
					LOG.error("write memcached failed", e);
			}
			return v;
		}

		return null;
	}
	
	

	@Override
	public <T> void expire_cache(String key) {
		final Memcached inst = Memcached.getInstance();
		inst.expire_cache(key);
	}

	@Override
	public <T> void write_cache(String key, T object) {
		final Memcached inst = Memcached.getInstance();
		inst.write_cache(key, object);
	}

	public Object read_cache(String key) {
		final Memcached inst = Memcached.getInstance();
		return inst.read_cache(key);
	}

}
