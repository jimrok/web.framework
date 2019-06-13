package ewhine.cache;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.BinaryConnectionFactory;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.internal.OperationFuture;
import net.spy.memcached.transcoders.Transcoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.SU;

public class MemcachedClientConnector {
	final static private Logger LOG = LoggerFactory
			.getLogger(MemcachedClientConnector.class.getName());

	private MemcachedClient c = null;
	private String url = null;

	private Transcoder<byte[]> _nullTranscoder = new NullTranscoder();
	protected int _timeoutInMs = 1000;

	public MemcachedClientConnector(String _url) {
		this.url = _url;
		try {
			c = new MemcachedClient(new BinaryConnectionFactory(),
					AddrUtil.getAddresses(url));

		} catch (IOException e) {
			if(LOG.isErrorEnabled())
			LOG.error(SU.cat("connect to ",url," failed",e));
//			LOG.error("connect to {} failed", url, e);
		}

	}

	public Object get(String key) {
		if (c != null) {
			Object value = c.get(key);
			if (value != null) {
				return value;
			}
		}

		return null;

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

	
	public boolean writeRaw(String key,byte[] o) throws KeyValueStoreClientException {

		boolean oper = false;
		OperationFuture<Boolean> f = c.set(key, 60*60*24*7, o, _nullTranscoder);
		try {
			 
			oper =  f.get(_timeoutInMs, TimeUnit.MILLISECONDS);
		} catch (Exception error) {
			f.cancel();
			throw (new KeyValueStoreClientException(error));
		}
		return oper;
	}
	
	public void connect() {
		if (c == null) {
			try {
				c = new MemcachedClient(AddrUtil.getAddresses(url));
			} catch (IOException e) {
				if(LOG.isErrorEnabled())
				LOG.error(SU.cat("connect to ",url," failed ",e));
//				LOG.error("connect to {} failed", url, e);
			}
		} else {
			if(LOG.isInfoEnabled())
			LOG.info("connect already exist");
		}

	}

	public void disconnect() {
		// TODO Auto-generated method stub
		if (c != null) {
			c.shutdown(3, TimeUnit.SECONDS);
			c = null;
		}
	}

	public boolean isConnected() {

		return c != null;
	}

}
