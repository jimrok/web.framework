package ewhine.app.http.module;

import spark.Request;
import spark.Response;

public class WebRouteWrapper implements spark.Route {
	private WebRoute route;

	public WebRouteWrapper(WebRoute r) {
		this.route = r;
	}

	@Override
	public Object handle(Request req, Response resp) throws Exception {
		WebRequest request = new WebRequest(req);
		WebResponse response = new WebResponse(resp);
		return route.execute(request, response);
	}

}
