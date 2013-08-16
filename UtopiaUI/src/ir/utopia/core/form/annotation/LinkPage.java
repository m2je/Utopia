package ir.utopia.core.form.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface LinkPage {

	public abstract String linkedUrl() default "";
	
	public abstract boolean isInternal() default true;
	
	public abstract String actionName() ;
	
	public abstract linkToPageType[] linkToPages();
	
	public enum linkToPageType{
		search,edit,save,view,all
	}
	public abstract boolean perRecord() default true;
	
	public abstract String icon() ;
}
