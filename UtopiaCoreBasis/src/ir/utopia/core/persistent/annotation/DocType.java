package ir.utopia.core.persistent.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface DocType {

	String statusMethod() default "getStatus"; 
	
}
