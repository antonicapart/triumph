package com.antonicapart.triumph.errors.creator;

import com.antonicapart.triumph.database.creator.types.TypeTypes;
import com.antonicapart.triumph.errors.TriumphError;

import java.lang.reflect.Field;

public class TypeNotMatchingError extends TriumphError {
    public TypeNotMatchingError(Field field, Class<?> wanted, TypeTypes actual) {
        super("Type of " + field.getName() + " of class : " + field.getDeclaringClass().getName() + " not matching, wanted : " + wanted.getName() + ", actual : " + actual + ", matching with : " + actual.getListOfMappedType());
    }
}
