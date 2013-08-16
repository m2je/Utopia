package ir.utopia.common.basicinformation.fiscalyear.persistent;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface FiscalYearSupport {

	String fiscalYearField() default "cmFiscalyear.cmFiscalyearId";
	
	String viewFiscalYearField() default "cmFiscalyearId";
}
