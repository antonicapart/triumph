package com.antonicapart.triumph.database.creator.types;

public enum TableTypes {
    IF_EXIST("IF EXISTS"),
    ON_CASCADE("CASCADE"),
    DELETE_TABLE("DROP TABLE"),
    CREATE_TABLE("CREATE TABLE"),
    PRIMARY_KEY("PRIMARY KEY");

    private String value;

    TableTypes(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
