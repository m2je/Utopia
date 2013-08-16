package ir.utopia.core.process.annotation;

@java.lang.annotation.Retention(value=java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Target(value={java.lang.annotation.ElementType.PARAMETER})
public abstract @interface ProcessParam {
	
	String name(); 
	
	ParameterType type() default ParameterType.dynamicParameter;
	
	public enum ParameterType{
		context,dynamicParameter
		
	}
}
