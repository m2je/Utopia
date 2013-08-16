package ir.utopia.core.process.annotation;

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
public @interface Process {	
	public  ProcessParameter[] parameters() default {} ;
	public String name();
	public ProcessUIConfiguration UIConfiguration()default  
			@ProcessUIConfiguration(notifyForExecutionSuccess=false,
					refreshPageAfterProcess=false) ;
}
