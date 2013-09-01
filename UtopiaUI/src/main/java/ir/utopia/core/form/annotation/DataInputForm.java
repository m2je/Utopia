package ir.utopia.core.form.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface DataInputForm {
	public int columnCount() default 3;
	public abstract IncludedForm[] includedForms() default {};
	public InputPageLayout pageLayout() default InputPageLayout.Simple;
	public NativeScript nativeScript() default @NativeScript(script="");
	String [] validationFunctions() default{};
	public enum InputPageLayout{
		Simple,DetailMaster
	}
	public CustomButton[] customButtons() default {}; 
	public DisplayFieldsConfiguration title() default @DisplayFieldsConfiguration;
	public DisplayFieldsConfiguration description() default @DisplayFieldsConfiguration;
	public DisplayFieldsConfiguration index() default @DisplayFieldsConfiguration;
	public DisplayFieldsConfiguration modifiedDate() default @DisplayFieldsConfiguration;
	public String [] CSSFiles() default {};
	public ColorLogic [] colorLogics() default {};
}

