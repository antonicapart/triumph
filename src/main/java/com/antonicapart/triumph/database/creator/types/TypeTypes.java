package com.antonicapart.triumph.database.creator.types;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum TypeTypes {
    BOOLEAN(false, Boolean.TYPE, Boolean.class),
    INT(false, Integer.TYPE, Integer.class),
    BIGINT(false, Long.TYPE, Long.class),
    FLOAT(false, Float.TYPE, Float.class),
    DOUBLE(false, Double.TYPE, Double.class),
    VARCHAR(true, String.class),

    NOT_DEFINE(false),

    /*
    CHAR,
    TINYTEXT,
    TEXT,
    MEDIUMTEXT,
    LONGTEXT,
    MEDIUMBLOB,
    BLOB,
    LONGBLOB,
    TINYINT,
    SMALLINT,
    MEDIUMINT,

    DECIMAL,
    DATE,
    DATETIME,
    TIMESTAMP,
    TIME,
    ENUM,
    SET
    */
    ;

    private final boolean asArg;

    private final List<? extends Class<?>> listOfMappedType;

    TypeTypes(boolean asArg, Class<?>... listOfMappedType) {
        this.listOfMappedType = Arrays.asList(listOfMappedType);
        this.asArg = asArg;
    }

    public List<? extends Class<?>> getListOfMappedType() {
        return this.listOfMappedType;
    }

    public boolean asArg() {
        return this.asArg;
    }

    public static TypeTypes getTypeFromType(Type t){
        for (TypeTypes tt: values()) {
            if (tt.getListOfMappedType().stream().map(Class::getName).collect(Collectors.toList()).contains(t.getTypeName()))
                return tt;
        }
        return NOT_DEFINE;
    }
}
