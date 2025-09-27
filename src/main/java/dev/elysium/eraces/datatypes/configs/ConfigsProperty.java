package dev.elysium.eraces.datatypes.configs;

import dev.elysium.eraces.datatypes.FieldType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ConfigsProperty {
    String path();
    FieldType type() default FieldType.STRING;
    String defaultString() default "";
    int defaultInt() default 0;
    boolean defaultBoolean() default false;
    double defaultDouble() default 0.0;
}
