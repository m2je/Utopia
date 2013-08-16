package ir.utopia.core.process.annotation;

import ir.utopia.core.bean.ActionParameterTypes;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Inherited 
@Documented
@Retention(RetentionPolicy.CLASS)
@Target(value={ElementType.TYPE})
public @interface ActionParameter {

	String name();
	int index() ;
	ActionParameterTypes type() default ActionParameterTypes.Nondefined;
}
