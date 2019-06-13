package ewhine.json;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tools.SU;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONObject {

	final static private Logger LOG = LoggerFactory.getLogger(JSONObject.class
			.getName());

	final static JSONObject empty = new JSONObject("");

	private String json = null;
	private JsonNode node = null;
	private boolean isJsonObject = false;

	private JSONObject(String jsonString) {
		this.json = jsonString;
	}

	private JSONObject(String jsonString, JsonNode node) {
		this.json = jsonString;
		this.node = node;
		this.isJsonObject = true;
	}

	public String toJson() {
		return json;
	}

	public static JSONObject empty() {
		return empty;
	}

	public String getString(String key) {
		JsonNode jnode = node.get(key);
		if (jnode != null) {
			return jnode.asText();
		}
		return null;
	}

	public JsonNode getNode(String key) {
		JsonNode jnode = node.get(key);
		if (jnode != null) {
			return jnode;
		}
		return null;
	}
	
	public JsonNode getNode() {
		return node;
	}

	public static JSONObject string(String html) {
		if (html == null || html.equals("")) {
			return JSONObject.empty();
		}

		JSONObject jsonObject = new JSONObject(html);

		return jsonObject;
	}

	public static JSONObject fromString(String jsonString) {
		if (jsonString == null || jsonString.equals("")) {
			return JSONObject.empty();
		}

		JSONObject jsonObject = new JSONObject(jsonString);
		jsonObject.isJsonObject = true;
		return jsonObject;
	}

	public static JSONObject parser(String json) {
		return parser(json, false);
	}

	public boolean isJSONObject() {
		return isJsonObject;
	}

	public static JSONObject parser(String json, boolean withNode) {

		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode tree = mapper.readTree(json);
			JSONObject jsonObject = new JSONObject(json, tree);

			return jsonObject;
		} catch (JsonProcessingException e) {
			if(LOG.isWarnEnabled())
				LOG.warn(SU.cat("parser error, json:",json));
//			LOG.warn("parser error, json:{}", json);
		} catch (IOException e) {
			if(LOG.isWarnEnabled())
				LOG.warn(SU.cat("parser error, json:",json));
//			LOG.warn("parser error, json:{}", json);
		}
		return null;
	}


	public JSONObject update() {
		if (node != null) {
			this.json = node.toString();
		}
		return this;
	}

}
