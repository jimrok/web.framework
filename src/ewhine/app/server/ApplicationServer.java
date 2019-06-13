package ewhine.app.server;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.websocket.server.ServerEndpointConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xnio.OptionMap;
import org.xnio.Options;
import org.xnio.Xnio;
import org.xnio.XnioWorker;

import ewhine.config.Config;
import io.undertow.connector.ByteBufferPool;
import io.undertow.server.DefaultByteBufferPool;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.ServletContainer;
import io.undertow.servlet.util.DefaultClassIntrospector;
import io.undertow.websockets.jsr.ServerWebSocketContainer;
import io.undertow.websockets.jsr.UndertowContainerProvider;
import spark.Request;
import spark.Response;
import tools.SU;

/**
 * 适配javaspark的启动，当javaspark使用非jetty的容器时，需要一个类实现这个接口
 * @author liujiang
 *
 */
public class ApplicationServer implements spark.servlet.SparkApplication {

	private static final Logger LOG = LoggerFactory.getLogger(ApplicationServer.class);
	private static Runnable _run = null;
	private static ServerWebSocketContainer deployment;
	private static final ServletContainer container = Servlets.defaultContainer();

	public static void setConfig(Runnable _run_config) {
		_run = _run_config;
	}

	public static void addWebSocketDeployment(String ws_url, Class<? extends javax.websocket.Endpoint> clazz) {

		try {

			DefaultClassIntrospector inst = DefaultClassIntrospector.INSTANCE;
			boolean directBuffers = Boolean.getBoolean("io.undertow.websockets.direct-buffers");
			boolean invokeInIoThread = Boolean.getBoolean("io.undertow.websockets.invoke-in-io-thread");

			ByteBufferPool buffers = new DefaultByteBufferPool(directBuffers, 1024, 100, 12);
			ServerWebSocketContainer builder = new ServerWebSocketContainer(inst,
					UndertowContainerProvider.class.getClassLoader(), new Supplier<XnioWorker>() {
						volatile XnioWorker worker;

						@Override
						public XnioWorker get() {
							if (worker == null) {
								synchronized (this) {
									if (worker == null) {
										try {
											worker = Xnio.getInstance()
													.createWorker(OptionMap.create(Options.THREAD_DAEMON, true));
										} catch (IOException e) {
											throw new RuntimeException(e);
										}
									}
								}
							}
							return worker;
						}
					}, buffers, Collections.EMPTY_LIST, !invokeInIoThread);

			
			builder.addEndpoint(ServerEndpointConfig.Builder.create(clazz, ws_url)
					.configurator(new InstanceConfigurator(clazz.newInstance())).build());

			deployment = builder;

		} catch (Exception e) {
			LOG.error("deploy websocket error", e);

		}

	}

	public static ServletContainer getServerContainer() {
		return container;
	}

	public static ServerWebSocketContainer getWebSocketDeployment() {
		if (deployment == null) {
			LOG.info("deployment is null");
		}
		return deployment;
	}

	@Override
	public void init() {
		
		//由sparkjava执行回调

		String public_file = Config.getServerRootDir() + File.separator + "public";
		File public_file_dir = new File(public_file);
		if (public_file_dir.exists()) {
			spark.Spark.staticFiles.externalLocation(public_file);
		}

		if (_run != null) {
			_run.run();
		}

		spark.Spark.afterAfter((request, response) -> {

			String requestTag = getLogTag(request);
			logRequest(requestTag, request, response);

		});

		spark.Spark.exception(Exception.class, (exception, request, response) -> {
			String requestTag = getLogTag(request);
			logEorror(requestTag, request, response, exception);
			response.body("Server error!");
			response.status(500);

		});
		LOG.info("load exception process.");

	}

	public String getLogTag(Request request) {
		Object rtag = request.attribute("request_tag");

		if (rtag == null) {
			return "";
		}

		return rtag.toString();
	}

	public static void logRequest(String tag, Request request, Response response) {
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
	
	public static void logEorror(String logTag, Request request,
			Response response, Exception exception) {
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
			StringBuilder sb = new StringBuilder();
			sb.append("Server error:[").append(logTag).append("] ")
					.append(request.requestMethod()).append(" ")
					.append(request.url()).append(" params[")
					.append(params.toString()).append("] IP:")
					.append(request.headers("X-Real-IP")).append(" headers[")
					.append(headers).append("]\n");

			LOG.error(sb.toString(), exception);
		}
	}

	private static class InstanceConfigurator extends ServerEndpointConfig.Configurator {

		private final Object endpoint;

		private InstanceConfigurator(final Object endpoint) {
			this.endpoint = endpoint;
		}

		@Override
		public <T> T getEndpointInstance(final Class<T> endpointClass) throws InstantiationException {
			return (T) endpoint;
		}
	}

}
