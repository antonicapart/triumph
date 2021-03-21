package com.antonicapart.triumph.errors.creator;

import com.antonicapart.triumph.errors.TriumphError;

import java.lang.reflect.Field;

public class RelationFieldsNotMatching extends TriumphError {
    public RelationFieldsNotMatching(Field fields, Class<?> relatedClass, String relatedFields)  {
        super("The class " + relatedClass.getName() + " don't have field : " + relatedFields + ", for references off " + fields.getName() + " of class : " + fields.getDeclaringClass().getSimpleName());
    }
}
