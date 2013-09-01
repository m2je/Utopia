package ir.utopia.core.revision.annotation;

import ir.utopia.core.utilities.serialization.UtopiaSerializer;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import ir.utopia.core.utilities.serialization.DefaultUtopiaSerializer;

@Inherited 
@Documented
@Target(value={ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RevisionSupport {

	Class<? extends UtopiaSerializer> serializer() default DefaultUtopiaSerializer.class;
}
