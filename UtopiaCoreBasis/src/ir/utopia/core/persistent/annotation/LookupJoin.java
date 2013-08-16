package ir.utopia.core.persistent.annotation;

import ir.utopia.core.persistent.UtopiaPersistent;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface LookupJoin {
	Class<? extends UtopiaPersistent> joinToClass();
	LookupConfiguration lookupConfiguration()  ;
	String condition() default "";
	
}
