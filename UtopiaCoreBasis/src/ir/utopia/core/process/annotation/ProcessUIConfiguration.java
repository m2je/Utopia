package ir.utopia.core.process.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value={ElementType.TYPE})
public @interface ProcessUIConfiguration {
	boolean refreshPageAfterProcess() default false;
	boolean notifyForExecutionSuccess() default false;
}
