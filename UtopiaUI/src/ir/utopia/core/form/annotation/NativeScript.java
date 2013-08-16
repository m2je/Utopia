package ir.utopia.core.form.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Retention(RetentionPolicy.RUNTIME)

public @interface NativeScript {
	public String script();
	public NativeScriptType[] scriptTypes() default NativeScriptType.all;
	public String[] validationFunctions() default {};
	public String[] onLoadMethods() default {};
	public String[] afterLoadCallBacks() default {};
	
	public NativeScriptMessage[] messages() default {};
}
