package ewhine.json;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HashJSON extends Entity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final List<Object[]> hash = new ArrayList<Object[]>();

	public HashJSON() {

	}

	public HashJSON(Map<String, Object> map) {
		map.forEach((k, v) -> {
			put(k, v);
		});

	}

	

	public void put(String key, Object v) {
		hash.add(new Object[] { key, v });
	}

	// protected HashJSON(HashMap _o, HashMap<String, Object> _options) {
	// super(_o, _options);
	// }
	//
	// public static String to_json(HashMap o) {
	// return new HashJSON(o, null).toJson();
	// }

	@Override
	protected void present(HashMap<String, Object> options) {
		hash.forEach(x -> {
			expose(x[0].toString(), x[1]);
		});
	}

	public static void main(String[] args) {
		BigDecimal final_price = new BigDecimal("2657.89");
		BigDecimal initial_price = new BigDecimal("2624.31");
		BigDecimal price_change = final_price
				.divide(initial_price, 7, BigDecimal.ROUND_HALF_UP)
				.subtract(new BigDecimal(1))
				.setScale(6, BigDecimal.ROUND_HALF_UP);

		System.out.println("price:" + price_change);
		BigDecimal p = price_change.multiply(new BigDecimal(100)).setScale(3,
				BigDecimal.ROUND_HALF_UP);

		HashJSON hash = new HashJSON();
		hash.put("a", "hello");
		hash.put("c", new Object[] { new String[] { "hello", "world" },
				new String[] { "java", "hah" } });
		hash.put("b", null);
		hash.put("p", p);
		hash.put("int", Integer.valueOf(1));
		hash.put("price", p);
		hash.put("code", new String[] { "66666", "10" });
		hash.put("int_array", new int[] { 66666, 10 });
		hash.put("long_array", new long[] { 66666L, 10L });

		HashJSON hashJSON = new HashJSON();
		HashJSON json = new HashJSON();

		json.put("orders", new Entity[] { hash });
		hashJSON.put("data", json);

		System.out.println(hashJSON.toJson());
	}

	@Override
	public String toString() {
		return "HashJSON [hash=" + hash + "]";
	}
	
	
}
