package ir.utopia.core.form.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomButton {
	public String Id() ;
	public String cssClass() default "clsButton";
	public String text();
	public String onclick();
	public String readOnlyLogic() default "";
	public String displayLogic() default "";
	
}
