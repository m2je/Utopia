package ir.utopia.core.form.annotation;

import ir.utopia.core.bean.UtopiaBean;
import ir.utopia.core.struts.FormUtil;
import ir.utopia.core.util.tags.datainputform.converter.DefaultUtopiaUIHandler;
import ir.utopia.core.util.tags.datainputform.converter.UtopiaUIHandler;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface UsecaseForm {
	Class<? extends UtopiaUIHandler> UIHandlerClass() default DefaultUtopiaUIHandler.class;
	Class<? extends UtopiaBean>remoteUsecaseClass() default UtopiaBean.class;
	String  usecaseLoadMethod() default FormUtil.DEFAULT_USECASE_LOAD_METHOD;
	public abstract IncludedForm[] includedForms() default {};
	public boolean isLanguageSupport() default false;
	public boolean useSearchObjectForReport() default true;
	public boolean useSearchObjectForView() default true;
	public boolean supportAttachment() default false;
	public NativeScript nativeScripts() default @NativeScript(script="");
	public DisplayFieldsConfiguration title() default @DisplayFieldsConfiguration;
	public DisplayFieldsConfiguration description() default @DisplayFieldsConfiguration;
	public DisplayFieldsConfiguration index() default @DisplayFieldsConfiguration;
	public DisplayFieldsConfiguration modifiedDate() default @DisplayFieldsConfiguration;
	public String [] CSSFiles() default {};
	public ColorLogic [] colorLogics() default {};
	public boolean hasCustomViewPage() default false;
	public boolean hasCustomDataInputPage() default false;
	public boolean hasCustomSearchPage() default false;
}
