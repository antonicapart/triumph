package com.antonicapart.triumph.errors.creator;

import com.antonicapart.triumph.errors.TriumphError;

import java.lang.reflect.Field;

public class TypeNotMatchingError extends TriumphError {
    public TypeNotMatchingError(Field field, Class<?> wanted, Class<?> actual) {
        super("Type of " + field + " not matching, wanted : " + wanted.getName() + ", actual : " + actual);
    }
}
