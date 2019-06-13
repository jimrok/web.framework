package ewhine.cache;

public interface DataFetcher<T> {
	
	public T fetch(String key);

}
