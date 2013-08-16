package ir.utopia.core.form.annotation;

import ir.utopia.core.form.annotation.InputItem.InputItemLogicOnAction;
import ir.utopia.core.persistent.annotation.LOVConfiguration;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;





@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface SearchItem {
	public enum SearchType { SHOW_IN_SEARCH_GRID, SHOW_IN_SEARCH_ITEM,  BOTH };
	/**
	 * search Item showing type
	 * @return
	 */
	SearchType searchConfiguration() default SearchType.BOTH;
	/**
	 * identifies field name in UI 
	 */
	String method() default "";
	
	int searchOnIndex() default -1;
	
	int searchGridIndex() default -1;
	
	int index() default -1  ;
	
	String defaultValue() default "";
	String dynamicValidation() default "";
	String readonlyLogic() default "";
	String displayLogic() default "";
	NativeEvent [] nativeEvents() default {};
	InputItemLogicOnAction[] displayOnAction() 
	default {
			 InputItemLogicOnAction.OnReport,
			 InputItemLogicOnAction.OnSearch};
	LOVConfiguration LOVconfiguration() default  @LOVConfiguration(searchLOVForm=LOVConfiguration.DummyLOVFormClass.class);		 
}
