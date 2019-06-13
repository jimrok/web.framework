package tools;

import java.util.Arrays;
import java.util.Collection;

public class Array {

	public static Long[] mapToLong(String[] stringArrays) {
		Long[] ret = new Long[stringArrays.length];
		for (int i = 0, n = stringArrays.length; i < n; i++) {
			ret[i] = Long.valueOf(stringArrays[i]);
		}

		return ret;
	}

	public static Long[] mapToLong(Collection<String> stringArrays) {
		Long[] ret = new Long[stringArrays.size()];
		int i = 0;
		for (String s : stringArrays) {
			ret[i] = Long.valueOf(s);
			i++;
		}

		return ret;
	}

	/**
	 * @param arrays
	 * @param params
	 * @return
	 */
	public static <T> T[] append(T[] arrays, T... params) {
		int len = params.length;
		int alen = arrays.length;
		int new_length = len + alen;
		T[] new_array = Arrays.copyOf(arrays, new_length);
		for (int i = len; i < new_length; i++) {
			new_array[i] = params[i - len];
		}
		return new_array;
	}

	public static byte[] concat(byte[] arrays, byte[] two) {

		int len = two.length;
		int alen = arrays.length;
		byte[] new_array = new byte[alen + len];
		System.arraycopy(arrays, 0, new_array, 0, alen);
		System.arraycopy(two, 0, new_array, alen, len);

		return new_array;
	}

	public static void reverse(Object[] long_arrays) {

		for (int i = 0, n = long_arrays.length / 2; i < n; i++) {
			Object temp = long_arrays[i];
			long_arrays[i] = long_arrays[n - i];
			long_arrays[n - i] = temp;
		}

	}

	public static String inspect(Object[] s) {
		StringBuilder sb = new StringBuilder("");
		int i = 0;
		for (Object o : s) {
			if (i > 0) {
				sb.append(",");
			}
			sb.append(o.toString());
			i++;
		}
		return sb.toString();
	}

	public static String join(long[] ids) {
		StringBuilder sb = new StringBuilder("");
		int i = 0;
		for (long o : ids) {
			if (i > 0) {
				sb.append(",");
			}
			sb.append(o);
			i++;
		}
		return sb.toString();
	}

	public static <T> String join(Collection<T> ids, String ch) {
		StringBuilder sb = new StringBuilder("");
		int i = 0;
		for (T o : ids) {
			if (i > 0) {
				sb.append(ch);
			}
			sb.append(o);
			i++;
		}
		return sb.toString();
	}
	
	public static String join(Object[] ids, String ch) {
		StringBuilder sb = new StringBuilder("");
		int i = 0;
		for (Object o : ids) {
			if (i > 0) {
				sb.append(ch);
			}
			sb.append(o);
			i++;
		}
		return sb.toString();
	}

}
