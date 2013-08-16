package ir.utopia.core.form.annotation;

import ir.utopia.core.util.exceptionhandler.DefaultExceptionHandler;
import ir.utopia.core.util.exceptionhandler.ExceptionHandler;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Inherited 
@Documented
@Target(value={ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ActionConfig {
Class<? extends ExceptionHandler> exceptionHandler() default DefaultExceptionHandler.class;
}
