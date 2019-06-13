package ewhine.app.http.module;

import javax.servlet.http.HttpServletResponse;

import spark.Response;

public class WebResponse {

	private Response resp;

	public WebResponse(Response resp) {
		this.resp = resp;
	}

	public void type(String contentType) {
		
		resp.type(contentType);
	}

	public void header(String header, String value) {
		resp.header(header, value);	
	}

	public HttpServletResponse raw() {
		return resp.raw();
	}

	public void status(int statusCode) {
		resp.status(statusCode);		
	}

}
