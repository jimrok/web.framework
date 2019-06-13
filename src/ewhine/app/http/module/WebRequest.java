package ewhine.app.http.module;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;


import spark.QueryParamsMap;
import spark.Request;

public class WebRequest {

	private Request req;

	public WebRequest(Request req) {
		this.req = req;
	}

	public String headers(String header) {
		return req.headers(header);
	}

	public void attribute(String attribute, Object value) {
		req.attribute(attribute, value);
	}

	public QueryParamsMap queryMap() {
		return req.queryMap();
	}

	public Set<String> headers() {
		return req.headers();
	}

	public String requestMethod() {

		return req.requestMethod();
	}

	public String url() {
		return req.url();
	}

	public String queryParams(String queryParam) {
		return req.queryParams(queryParam);
	}

	public String params(String param) {
		return req.params(param);
	}

	public HttpServletRequest raw() {
		return req.raw();
	}

	public String userAgent() {
		return req.userAgent();
	}

	public String ip() {
		return req.ip();
	}
	
	
	//-------------------------extend method ----------------------
	
	public Integer params(String name, Integer defaultValue) {

		try {
			String v = req.queryParams(name);
			if (v != null) {
				return Integer.valueOf(v);
			} else {
				return defaultValue;
			}
		} catch (Throwable t) {
			return 0;
		}
	}

}
