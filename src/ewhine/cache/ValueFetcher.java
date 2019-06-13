package ewhine.cache;

public interface ValueFetcher {
	
	public ValueTranscoder fetch(String key, byte[] rawValue);

}
