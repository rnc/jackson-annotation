package org.goots.jackson.deserializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for methods to be called directly after deserialization of the object.
 */
@Target( { ElementType.METHOD })
@Retention( RetentionPolicy.RUNTIME)
public @interface JsonPostDeserialize {
}
