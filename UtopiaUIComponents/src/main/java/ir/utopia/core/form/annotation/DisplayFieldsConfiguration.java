package ir.utopia.core.form.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface DisplayFieldsConfiguration {

	String []displayFields() default "";
	String seperator() default "-";
}
