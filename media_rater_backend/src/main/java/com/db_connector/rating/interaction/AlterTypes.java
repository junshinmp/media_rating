/**
 * File: AlterTypes.java
 * @author: Junshin Purganan
 * 
 * Enum representation of the alter statements in order to
 * allow clearer transfer of meaning for the DDL statement.
 * Attached is a constructor and toString() method for the
 * DatabaseInitalizer class to create statement.
 */

package com.db_connector.rating.interaction;

public enum AlterTypes {
    ADD("add"),
    RENAME_COLUMN("rename column"),
    ADD_CONSTRAINT("add constraint"),
    ALTER_COLUMN("alter column"),
    RENAME_TO("rename to"),
    DROP_COLUMN("drop column");

    private final String type;

    private AlterTypes(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return this.type;
    }
}
