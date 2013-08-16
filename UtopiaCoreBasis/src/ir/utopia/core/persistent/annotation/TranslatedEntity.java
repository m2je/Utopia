package ir.utopia.core.persistent.annotation;

import ir.utopia.core.persistent.UtopiaBasicPersistent;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface TranslatedEntity {
	Class<? extends UtopiaBasicPersistent> translationEntity() default UtopiaBasicPersistent.class;
}
