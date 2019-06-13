package ewhine.app;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import spark.ModelAndView;
import spark.TemplateEngine;

/**
 * Template Engine based on Apache Velocity.
 */
public class VelocityTemplateEngine extends TemplateEngine {

	private final VelocityEngine velocityEngine;
	private String encoding;

	/**
	 * Constructor
	 */
	public VelocityTemplateEngine() {
		Properties properties = new Properties();
		properties.setProperty("resource.loader", "class");
		properties
				.setProperty("class.resource.loader.class",
						"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		this.velocityEngine = new org.apache.velocity.app.VelocityEngine(
				properties);
	}

	/**
	 * Constructor
	 *
	 * @param encoding
	 *            The encoding to use
	 */
	public VelocityTemplateEngine(String encoding) {
		this();
		this.encoding = encoding;
	}

	/**
	 * Constructor
	 *
	 * @param velocityEngine
	 *            The velocity engine, must not be null.
	 */
	public VelocityTemplateEngine(VelocityEngine velocityEngine) {
		if (velocityEngine == null) {
			throw new IllegalArgumentException(
					"velocityEngine must not be null");
		}
		this.velocityEngine = velocityEngine;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String render(ModelAndView modelAndView) {

		Object model = modelAndView.getModel();
		if (model instanceof Map) {
			Map<String, Object> modelMap = (Map<String, Object>) model;
			if (modelMap.get("layout") != null) {
				return render(modelAndView, (String) modelMap.get("layout"));
			}

			String templateEncoding = Optional.ofNullable(this.encoding)
					.orElse(StandardCharsets.UTF_8.name());
			Template template = velocityEngine.getTemplate(
					modelAndView.getViewName(), templateEncoding);

			VelocityContext context = new VelocityContext(modelMap);

			StringWriter writer = new StringWriter();
			template.merge(context, writer);

			return writer.toString();
		} else {
			throw new IllegalArgumentException(
					"modelAndView must be of type java.util.Map");
		}
	}

	public String render(ModelAndView modelAndView, String layout) {
		String templateEncoding = Optional.ofNullable(this.encoding).orElse(
				StandardCharsets.UTF_8.name());
		Template template = velocityEngine.getTemplate(
				modelAndView.getViewName(), templateEncoding);

		Object model = modelAndView.getModel();
		if (model instanceof Map) {
			Map<String, Object> modelMap = (Map<String, Object>) model;
			VelocityContext context = new VelocityContext(modelMap);

			StringWriter writer = new StringWriter();
			template.merge(context, writer);

			Template layoutTemplate = velocityEngine.getTemplate(layout,
					templateEncoding);
			String bodyContent = writer.toString();

			modelMap.put("bodyContent", bodyContent);
			StringWriter layoutWriter = new StringWriter();
			layoutTemplate.merge(context, layoutWriter);
			return layoutWriter.toString();

		} else {
			throw new IllegalArgumentException(
					"modelAndView must be of type java.util.Map");
		}
	}

}