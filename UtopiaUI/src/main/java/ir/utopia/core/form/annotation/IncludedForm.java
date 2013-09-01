package ir.utopia.core.form.annotation;

import ir.utopia.core.constants.Constants;
import ir.utopia.core.struts.UtopiaBasicForm;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface IncludedForm {
  String name();
  String parentLoadMethod() default "" ;
  boolean updateable() default true;
  boolean insertRowAllowed() default true;
  Class<? extends UtopiaBasicForm<?>> formClass();
  String includedFormColumnName() default "";
  String myLinkMethod() default "";
  IncludedFormType includedFormType() default IncludedFormType.Interactive;
 Constants.IncludedFormDisplayType[] includedFormDisplayType() default Constants.IncludedFormDisplayType.all;
 int preferedWidth() default 500;
 int preferedHeigth() default 300;
  public enum IncludedFormType{IncludedSearch,Interactive}
 boolean autofitColumns() default true; 
 ColorLogic [] colorLogics() default {};
}
