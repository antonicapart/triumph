package com.antonicapart.triumph.errors.creator;

import com.antonicapart.triumph.database.creator.types.TypeTypes;
import com.antonicapart.triumph.errors.TriumphError;

import java.lang.reflect.Field;

public class AttributeNeededError  extends TriumphError {
    public AttributeNeededError(Field field, TypeTypes typeTypes) {
        super("The field " + field.getName() + " need one args because of type : " + typeTypes.name());
    }
}
