package com.antonicapart.triumph.database.model.annotations;

import com.antonicapart.triumph.database.model.Model;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

@Retention(RetentionPolicy.RUNTIME)
@Target(FIELD)
public @interface Relation {
    
    Class<? extends Model> model();
    String relatedField() default "";
}
