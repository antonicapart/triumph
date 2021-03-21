package com.antonicapart.triumph.errors.creator;

import com.antonicapart.triumph.errors.TriumphError;

import java.lang.reflect.Field;

public class RelationFieldsNotExist extends TriumphError {
    public RelationFieldsNotExist(Field fields, Class<?> relatedTo)  {
        super("The class " + relatedTo.getName() + " don't have fields for references off " + fields.getName() + " of class : " + fields.getDeclaringClass().getSimpleName());
    }
}
