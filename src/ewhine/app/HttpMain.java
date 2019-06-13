package ewhine.app;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.DispatcherType;
import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ewhine.annotation.Delete;
import ewhine.annotation.Get;
import ewhine.annotation.Post;
import ewhine.annotation.Put;
import ewhine.app.http.module.HTTPModule;
import ewhine.app.server.ApplicationServer;
import ewhine.config.Config;
import io.undertow.Undertow;
import io.undertow.server.handlers.PathHandler;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.FilterInfo;
import io.undertow.servlet.api.ServletContainer;
import io.undertow.servlet.util.DefaultClassIntrospector;
import io.undertow.websockets.jsr.JsrWebSocketFilter;
import io.undertow.websockets.jsr.ServerWebSocketContainer;
import spark.Request;
import spark.Response;
import tools.SU;

public class HttpMain {

	private static final Logger LOG = LoggerFactory.getLogger(HttpMain.class);
	private Runnable _run = null;
	private List<Class> modules = new ArrayList<Class>();
	private Undertow server = null;

	public HttpMain() {

	}

	public void config(Runnable run) {
		this._run = run;
	}

	private void logEorror(String logTag, Request request, Response response, Exception exception) {
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
			String headers = java.lang.String.join(", ", request.headers().stream().map(x -> {
				return x + ":" + request.headers(x);
			}).collect(Collectors.toList()));
			StringBuilder sb = new StringBuilder();
			sb.append("Server error:[").append(logTag).append("] ").append(request.requestMethod()).append(" ")
					.append(request.url()).append(" params[").append(params.toString()).append("] IP:")
					.append(request.headers("X-Real-IP")).append(" headers[").append(headers).append("]\n");

			LOG.error(sb.toString(), exception);
		}
	}

	public void start() {

//		String public_file = Config.getServerRootDir() + File.separator
//				+ "public";
//		File public_file_dir = new File(public_file);
//		if (public_file_dir.exists()) {
//			spark.Spark.staticFiles.externalLocation(public_file);
//		}

		ApplicationServer.setConfig(() -> {
			if (_run != null) {
				_run.run();
				LOG.info("load run code finished!");
			}

			for (Class c : modules) {
				try {
					Object obj = c.newInstance();
					Method routesMethod = c.getDeclaredMethod("routes");
					if (routesMethod != null) {
						routesMethod.invoke(obj);
					}

					Method[] methods = c.getDeclaredMethods();
					for (Method m : methods) {
						if (m.isAnnotationPresent(Get.class)) {
							Get aclass = m.getAnnotation(Get.class);
							String path = aclass.path();

							spark.Spark.get(path, (request, response) -> m.invoke(obj, request, response));

						} // get

						if (m.isAnnotationPresent(Put.class)) {
							Put aclass = m.getAnnotation(Put.class);
							String path = aclass.path();

							spark.Spark.put(path, (request, response) -> m.invoke(obj, request, response));

						} // put

						if (m.isAnnotationPresent(Post.class)) {
							Post aclass = m.getAnnotation(Post.class);
							String path = aclass.path();

							spark.Spark.post(path, (request, response) -> m.invoke(obj, request, response));

						} // post

						if (m.isAnnotationPresent(Delete.class)) {
							Delete aclass = m.getAnnotation(Delete.class);
							String path = aclass.path();

							spark.Spark.delete(path, (request, response) -> m.invoke(obj, request, response));

						} // delete
					}
					LOG.info("load module: [" + c.getName() + "].");

				} catch (InstantiationException e) {
					LOG.error("addModule class:" + c.getName() + " error.", e);
				} catch (IllegalAccessException e) {
					LOG.error("addModule class:" + c.getName() + " error.", e);
				} catch (NoSuchMethodException e) {
					LOG.error("addModule class:" + c.getName() + " error.", e);
				} catch (SecurityException e) {
					LOG.error("addModule class:" + c.getName() + " error.", e);
				} catch (IllegalArgumentException e) {
					LOG.error("addModule class:" + c.getName() + " error.", e);
				} catch (InvocationTargetException e) {
					LOG.error("addModule class:" + c.getName() + " error.", e);
				}

			}
		});

		// start a undertow server...
		// start undertow server.
		int maxThreads = Integer.parseInt(Config.getPropertyConfig("server.properties").get("server.max_thread", "32"));

		int port = Integer.parseInt(Config.getPropertyConfig("server.properties").get("http.port", "3001"));

		// deploy the server
		DeploymentInfo builder = Servlets.deployment().setClassLoader(ApplicationServer.class.getClassLoader())
				.setClassIntrospecter(DefaultClassIntrospector.INSTANCE).setContextPath("/").setDeploymentName("spark")
				.setDefaultEncoding("UTF-8");

		ServletContainer container = ApplicationServer.getServerContainer();
		ServerWebSocketContainer deployment = ApplicationServer.getWebSocketDeployment();
		if (deployment != null) {
			builder.addFilter(new FilterInfo("filter", JsrWebSocketFilter.class)).addFilterUrlMapping("filter", "/*",
					DispatcherType.REQUEST);

			builder.addServletContextAttribute(javax.websocket.server.ServerContainer.class.getName(), deployment);
		}

		FilterInfo filter = new FilterInfo("SparkFilter", spark.servlet.SparkFilter.class);
		filter.addInitParam("applicationClass", ApplicationServer.class.getName());

		builder.addFilter(filter).addFilterUrlMapping("SparkFilter", "/*", DispatcherType.REQUEST);

		DeploymentManager manager = container.addDeployment(builder);
		manager.deploy();
		final PathHandler root = new PathHandler();

		try {
			PathHandler path = root.addPrefixPath(builder.getContextPath(), manager.start());
			server = Undertow.builder().addHttpListener(port, "0.0.0.0").setHandler(path).setWorkerThreads(maxThreads)
					.build();
			server.start();

		} catch (ServletException e) {
			LOG.error("Server start failed", e);
			throw new RuntimeException("Server start failed", e);
		}
		// spark.Spark.awaitInitialization();

		if (LOG.isInfoEnabled())
			LOG.info(SU.cat("undertow server started. port:", port, ", max_thread:", maxThreads));

	}

	public void stop() {

		LOG.info("undertow server is shutdown.");
		if (server != null) {
			server.stop();
		}

	}

	public String getLogTag(Request request) {
		Object rtag = request.attribute("request_tag");

		if (rtag == null) {
			return "";
		}

		return rtag.toString();
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
			LOG.info(SU.cat(tag, request.requestMethod(), " ", response.status(), " ", request.url(), " params[",
					params.toString(), "] ", request.headers("X-Real-IP")));
		}
	}

	public static void main(String[] args) {

	}

	public void addModule(Class<? extends HTTPModule> clazz) {
		modules.add(clazz);
	}

}
