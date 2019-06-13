package ewhine.annotation;
@java.lang.annotation.Target( { java.lang.annotation.ElementType.METHOD })
@java.lang.annotation.Retention(value=java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Get {

	String path();

}
