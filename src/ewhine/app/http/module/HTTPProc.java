package ewhine.app.http.module;

import static spark.Spark.halt;

import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.Request;
import spark.Response;
import tools.SU;

public abstract class HTTPProc {

	private Request request;
	private Response response;

	public abstract void routes();

	public static final Logger LOG = LoggerFactory.getLogger(HTTPProc.class);

	public HTTPProc(Request _request, Response _response) {
		this.request = _request;
		this.response = _response;
	}
	
	public abstract String execute();
	

	public Integer params(String name, Integer defaultValue) {

		try {
			String v = request.queryParams(name);
			if (v != null) {
				return Integer.valueOf(v);
			} else {
				return defaultValue;
			}
		} catch (Throwable t) {
			return 0;
		}
	}

	public Long params(String name) {
		String v = request.queryParams(name);
		if (v != null) {
			return Long.valueOf(v);
		} else {
			return null;
		}
	}

	public String params(String name, String defaultValue) {
		String v = request.queryParams(name);
		if (v != null) {
			return v;
		} else {
			return defaultValue;
		}
	}

	public String paramsString(String name, int limit_length) {
		String v = request.queryParams(name);
		if (v != null) {
			if (v.length() > limit_length) {
				return v.substring(0, limit_length);
			}
			return v;
		} else {
			return null;
		}
	}

	public void error(int http_code, String message) {
		Object user_id = request.attribute("request_tag");

		if (LOG.isErrorEnabled()) {
			StringBuilder params = new StringBuilder();
			Map<String, String[]> _map = request.queryMap().toMap();
			for (String k : _map.keySet()) {
				params.append(k);
				params.append("=");
				String[] _o = _map.get(k);
				if (_o != null) {
					params.append(String.join(",", _o));
				}
				params.append("&");
			}
			if (params.length() > 0) {
				params.deleteCharAt(params.length() - 1);
			}
			String headers = java.lang.String.join(", ", request.headers()
					.stream().map(x -> {
						return x + ":" + request.headers(x);
					}).collect(Collectors.toList()));

			LOG.error(SU.cat("[user:", user_id, "] ", request.requestMethod(),
					" ", request.url(), " params[", params.toString(), "]",
					" Remote-IP:", request.ip(), " X-Real-IP:",
					request.headers("X-Real-IP"), " headers[", headers, "]",
					" - error: ", http_code, " ", message));
		}

		halt(http_code, "{\"errors\":{\"code\":" + http_code
				+ ",\"message\":\"" + message + "\"}}");
	}

	public void logRequest(String tag, Request request, Response response) {
		if (LOG.isInfoEnabled()) {
			StringBuilder params = new StringBuilder();
			Map<String, String[]> _map = request.queryMap().toMap();

			_map.forEach((k, v) -> {
				params.append(k);
				params.append("=<");

				if (k.startsWith("password")) {
					params.append("****>, ");
					return;
				}

				String[] _o = _map.get(k);
				if (_o != null) {
					params.append(String.join(",", _o));
				}
				params.append(">, ");

			});

			if (params.length() > 0) {
				params.deleteCharAt(params.length() - 2);
			}
			LOG.info(SU.cat(tag, request.requestMethod(), " ",
					response.status(), " ", request.url(), " params[",
					params.toString(), "] ", request.headers("X-Real-IP")));
		}
	}

}
