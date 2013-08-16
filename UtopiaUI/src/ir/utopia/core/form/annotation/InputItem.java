package ir.utopia.core.form.annotation;

import ir.utopia.core.constants.Constants;
import ir.utopia.core.constants.Constants.predefindedActions;
import ir.utopia.core.persistent.annotation.LOVConfiguration;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface InputItem {
	public enum InputItemLogicOnAction{
		OnSave,OnUpdate,OnImport,OnReport,OnSearch,OnDashboard;
		public static InputItemLogicOnAction convert(predefindedActions action){
			switch (action) {
			case save:	return OnSave;
			case importData:return OnImport;
			case update:return OnUpdate;
			case report:return OnReport;
			case updateFromDashboard:return OnDashboard;
			case search:return OnSearch;
			default:return OnSave;
			}
		}
	}
	boolean isManadatory() default false; 
	Constants.DisplayTypes displayType() default Constants.DisplayTypes.String;
	Class<?> lookupEntityClass() default Object.class;
	int index();
	String name() default "";
	boolean breakLineAfter() default false;
	String[] lookupDependentsOn() default "" ;
	long minValue() default 0l;  
	long maxValue() default Long.MAX_VALUE;
	int decimalPrecision() default 0;
	boolean useSeperator() default true;
	String decimalSeparator() default ",";
	String defaultValue() default "";
	String dynamicValidation() default "";
	String readonlyLogic() default "";
	String displayLogic() default "";
	String endDateField() default "";
	InputItemLogicOnAction[] displayOnAction() 
								default {InputItemLogicOnAction.OnSave,
										 InputItemLogicOnAction.OnUpdate,
										 InputItemLogicOnAction.OnImport,
										 InputItemLogicOnAction.OnReport,
										 InputItemLogicOnAction.OnSearch,
										 InputItemLogicOnAction.OnDashboard};
    InputItemLogicOnAction[] readonlyOnAction() default {}; 										 
   										 
   boolean shouldConfirm() default false;				
   NativeEvent [] nativeEvents() default {};
   LOVConfiguration LOVconfiguration() default  @LOVConfiguration(searchLOVForm=LOVConfiguration.DummyLOVFormClass.class);
   int preferedWidth() default -1;
   
}
