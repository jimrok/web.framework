package ewhine.json;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

public abstract class Entity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	final static private Logger LOG = LoggerFactory.getLogger(Entity.class
			.getName());

	protected HashMap<String, Object> options = null;
	// protected ArrayList<Object[]> values = new ArrayList<Object[]>(20);
	private boolean already_render = false;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");

	private JsonGenerator gen;

	public Entity() {

	}

	public Entity(HashMap<String, Object> _options) {

		this.options = _options;

	}

	protected void expose(String key, Object val) {

		try {

			if (val == null) {
				gen.writeNullField(key);
				return;
			}

			if (val.getClass().isArray()) {

				Class<?> componentType = val.getClass().getComponentType();
				if (componentType.isPrimitive()) {

					if (int.class.isAssignableFrom(componentType)) {
						/* ... For long[] type can not convert Object[] */
						int[] items = (int[]) val;

						gen.writeArrayFieldStart(key);
						for (int i : items) {
							gen.writeObject(Integer.valueOf(i));
						}
						gen.writeEndArray();
					} else if (long.class.isAssignableFrom(componentType)) {
						/* ... For int[] type can not convert Object[] */
						long[] items = (long[]) val;

						gen.writeArrayFieldStart(key);
						for (long i : items) {
							gen.writeObject(i);
						}
						gen.writeEndArray();
					}

				} else {
					// For Object[] value.
					Object[] items = (Object[]) val;

					gen.writeArrayFieldStart(key);

					for (Object item : items) {
						dumpJsonObject(gen, item);
					}
					gen.writeEndArray();
				}

				return;
			}

		} catch (Throwable t) {
			LOG.error("expose arrays error, key:" + key + " value:" + val, t);
			throw new RuntimeException(t);
		}

		try {

			if (val instanceof Entity) {
				gen.writeFieldName(key);
				((Entity) val).toJson(gen);
				return;
			}

			if (val instanceof Collection<?>) {

				gen.writeArrayFieldStart(key);
				Collection<?> items = (Collection<?>) val;
				for (Object i : items) {
					dumpJsonObject(gen, i);
				}
				gen.writeEndArray();
				return;
			}
		} catch (Throwable e) {
			LOG.error("expose error-->!", e);
			LOG.error("expose key-->:" + key + " value:" + val + " error!");
			throw new RuntimeException(e);
			
		}

		try {

			// if (val instanceof HashJSON) {
			// HashJSON hashJSON = (HashJSON) val;
			// gen.writeFieldName(key);
			// gen.writeRawValue(hashJSON.toJson());
			// continue;
			// }

			if (val instanceof JSONObject) {
				JSONObject jsonObject = (JSONObject) val;
				gen.writeFieldName(key);
				if (jsonObject.isJSONObject()) {
					// gen.writeRawValue(jsonObject.toJson());
					gen.writeRawValue(jsonObject.toJson());
					// System.out.println("string:::==>"+jsonObject.toJson());
				} else {
					gen.writeString(jsonObject.toJson());
					// System.out.println("object:::==>"+jsonObject.toJson());
				}

				return;
			}

			if (val instanceof Map<?, ?>) {
				gen.writeFieldName(key);
				dumpMapObjectToJson(gen, (Map<?, ?>) val);
				return;
			}

			if (val instanceof Timestamp) {
				Date date = new Date(((Timestamp) val).getTime());
				gen.writeObjectField(key, sdf.format(date));
				return;
			}
			gen.writeObjectField(key, val);

		} catch (Throwable e) {
			LOG.error("expose error", e);
			LOG.error("expose key:" + key + " value:" + val + " error!");
			throw new RuntimeException(e);
		}
	}

	protected void exposeDate(String attribue, Date value) {

		expose(attribue, sdf.format(value));
	}

	protected void exposeHash(String key, Map<String, Object> value) {

		try {
			gen.writeFieldName(key);
			dumpMapObjectToJson(gen, (Map<String, ?>) value);
		} catch (IOException e) {
			LOG.error("expose hash error", e);
		}

	}

	protected abstract void present(HashMap<String, Object> options);

	// public abstract void represent(T o, HashMap<String, Object> options2);

	private void toJson(JsonGenerator gen) {
		this.setOutputGenerator(gen);
		try {
			gen.writeStartObject();
			this.present(options);
			gen.writeEndObject();
		} catch (IOException e) {
			LOG.error("write json error.", e);
		}

	}

	public String toJson() {
		if (this.already_render == true) {
			throw new RuntimeException("Already render this json object.");
		}

		JsonFactory jfactory = new JsonFactory();

		ByteArrayOutputStream jsonOutput = new ByteArrayOutputStream();
		try {

			JsonGenerator gen = jfactory.createGenerator(jsonOutput,
					JsonEncoding.UTF8);
			this.setOutputGenerator(gen);
			gen.writeStartObject();
			this.present(options);

			gen.writeEndObject();
			gen.close();

			byte[] out = jsonOutput.toByteArray();
			already_render = true;
			return new String(out);

		} catch (Throwable e) {
			if (LOG.isErrorEnabled()) {
				LOG.error("entity to json error.", e);
				LOG.error("output:" + new String(jsonOutput.toByteArray()));
			}
			throw new RuntimeException(e);
		}
	}

	private void setOutputGenerator(JsonGenerator g) {
		this.gen = g;

	}

	private void toJsonValue(JsonGenerator jg, String k, Object val)
			throws IOException {

		if (val != null && val.getClass().isArray()) {
			jg.writeFieldName(k);

			int arrayLen = Array.getLength(val);
			jg.writeStartArray(arrayLen);

			for (int i = 0; i < arrayLen; i++) {
				Object arrayObject = Array.get(val, i);
				dumpJsonObject(jg, arrayObject);
			}
			jg.writeEndArray();

			return;
		}

		if (val != null && val instanceof Map<?, ?>) {
			jg.writeFieldName(k);

			dumpMapObjectToJson(jg, (Map<String, ?>) val);

			return;
		}

		if (val instanceof Collection<?>) {
			jg.writeFieldName(k);
			
			jg.writeStartArray(((Collection<?>) val).size());
			dumpJsonObject(jg,val);
			jg.writeEndArray();
			return;
		}

		if (val instanceof Entity) {
			jg.writeFieldName(k);
			((Entity) val).toJson(gen);

			return;
		}

		jg.writeObjectField(k, val);

	}

	

	private void dumpJsonObject(JsonGenerator gen, Object val)
			throws IOException {

		if (val instanceof Entity) {
			((Entity) val).toJson(gen);
			return;
		}

		if (val instanceof Map<?, ?>) {
			
			this.dumpMapObjectToJson(gen, (Map<String, ?>) val);
			return;
		}
		

		if (val != null && val.getClass().isArray()) {

			int arrayLen = Array.getLength(val);
			gen.writeStartArray(arrayLen);

			for (int i = 0; i < arrayLen; i++) {
				Object arrayObject = Array.get(val, i);
				if (arrayObject instanceof Map<?, ?>) {
					dumpMapObjectToJson(gen, (Map<String, ?>) arrayObject);
				} else if (arrayObject instanceof java.sql.Timestamp) {
					Date date = new Date(((Timestamp) arrayObject).getTime());
					gen.writeObject(sdf.format(date));
				} else {
					gen.writeObject(arrayObject);
				}
			}
			gen.writeEndArray();

			return;
		}
		
		if (val instanceof Collection<?>) {
			
			Collection<?> items = (Collection<?>) val;
			for (Object i : items) {
				
				dumpJsonObject(gen, i);
			}
			return;
		}
		
		gen.writeObject(val);
		// gen.writeRaw(val.toString());

		// return val.toString();
	}
	
	private void dumpMapObjectToJson(JsonGenerator gen, Map<?, ?> val)
			throws IOException {

		gen.writeStartObject();

		Map<?, ?> mapObject = (Map<?, ?>) val;

		Set<?> kset = mapObject.keySet();

		for (Object key : kset) {
			// gen.writeObjectField(key.toString());
			toJsonValue(gen, key.toString(), mapObject.get(key));
		}

		gen.writeEndObject();

	}

}
