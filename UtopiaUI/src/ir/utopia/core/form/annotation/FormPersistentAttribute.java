package ir.utopia.core.form.annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 
 */

/**
 * @author Salarkia
 *
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface FormPersistentAttribute {
	public enum FormToEntityMapType{real,virtual}
	public enum FormToEntityDataTypeMap{EQUIVALENT,STRING_TO_DATE,ISACTIVE,LONG_TO_LOOKUP,BOOLEAN_TO_ENUM,STRING_TO_TIME,STRING_TO_DATE_TIME,LOOKUP_PARENT}
	String method() default "";
	FormToEntityDataTypeMap formToEntityMap() default FormToEntityDataTypeMap.EQUIVALENT;
	FormToEntityMapType formToEntityMapType() default FormToEntityMapType.real;
}
