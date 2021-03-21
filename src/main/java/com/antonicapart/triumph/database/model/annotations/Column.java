package com.antonicapart.triumph.database.model.annotations;

import com.antonicapart.triumph.database.creator.types.TypeTypes;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

@Retention(RetentionPolicy.RUNTIME)
@Target(FIELD)
public @interface Column {
    String name() default "";
    TypeTypes type() default TypeTypes.NOT_DEFINE;
    String args() default "";
}
