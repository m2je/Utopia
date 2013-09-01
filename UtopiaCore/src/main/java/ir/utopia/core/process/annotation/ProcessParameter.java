package ir.utopia.core.process.annotation;

import ir.utopia.core.bean.ActionParameterTypes;
import ir.utopia.core.constants.Constants.DisplayTypes;
import ir.utopia.core.persistent.annotation.LOVConfiguration;
import ir.utopia.core.persistent.annotation.LookupConfiguration;

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
public @interface ProcessParameter {
	ActionParameterTypes type();
	int index();
	DisplayTypes paramDisplayType() default DisplayTypes.String;
	LookupConfiguration lookupConfiguration() default @LookupConfiguration() ;
	long minValue() default 0l;
	long maxValue() default Long.MAX_VALUE;
	String defaultValue() default ""; 
	String name();
	int displayIndex() default 0;
	boolean shouldConfirm() default false;
	boolean isMandatory() default true;
	String readOnlyLogic() default "";
	String displayLogic() default "";
    LOVConfiguration LOVconfiguration() default  @LOVConfiguration(searchLOVForm=LOVConfiguration.DummyLOVFormClass.class);
} 
