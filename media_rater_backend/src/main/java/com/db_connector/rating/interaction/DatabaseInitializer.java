/**
 * 
 * File: DatabaseInitalizer.java
 * @author: Junshin Purganan
 * 
 * This class is used to intialize the Database, but also
 * allows for the DDL statements to be procssed to the database.
 * This includes alter, create, drop, and a generalized statement
 * executor for the database.
 */

package com.db_connector.media_rater.interaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer {
    DatabaseConnection dbConnection;

    /**
     * Default Constructor, simply to allow statements to connect to Database
     * @param: DatabaseConnection, allowing statements to be executed
     */
    public DatabaseInitializer(DatabaseConnection dbConnection){
        this.dbConnection = dbConnection;
    }

    /**
     * Creates table from the inputted parameters, allowing for better parsing
     * with SQL database tools (Postgres or SQLite)
     * @param tableName, the correlating table name
     * @param varNames, the names of the columns in the table
     * @param types, the specific variable types
     * @throws SQLException, if driver is not initalized or connection fails
     */
    public void createTable(String tableName, String[] varNames, String[] types) throws SQLException{
        // Simple checks to ensure that statement is correctly formatted for the table.
        if (varNames == null || types == null || varNames.length != types.length){
            throw new IllegalArgumentException("Statement is not correct format.");
        }

        // Create the statement from its parts.
        StringBuilder createString = new StringBuilder();

        // Attaches the correlating tableName variable.
        // createString will be attached as the following:
        // createString = "create table if not exists [TABLENAME] (""
        createString.append("create table if not exists ").append(tableName).append(" (");
        
        // now adding all the varNames and types attached to it, including all the constraints
        for (int i = 0; i < varNames.length; i++){
            // formatted like:
            // "fruit VARCHAR(25) PRIMARY KEY"
            createString.append(varNames[i]).append(" ").append(types[i]);
            
            // simple check to ensure that the last pairing doesn't insert a comma
            if (i < varNames.length - 1){
                createString.append(", ");
            }
        }

        // additionally add the closing ");"
        createString.append(");");

        // try, catch block for both statements as both can result in SQLException errors.
        try {
            statementExecutor(createString);
            System.out.println("Table '" + tableName + "' verified/created successfully.");
        } catch (SQLException e) {
            throw new SQLException("Table could not be created for " + tableName, e);
        }
    }

    /**
     * String combiner  for dropping a table, allowing the translation between databases to 
     * be universally processed between SQL types.
     * @param tableName, the correlating table name
     * @param cascade, whether dropping cascades on correlating tables or not
     * @throws SQLException, if driver is not initalized or connection fails
     */
    public void dropTable(String tableName, boolean cascade) throws SQLException{
        // Create the statement from its parts.
        StringBuilder createString = new StringBuilder();

        // Attaches the correlating tableName variable.
        // createString will be attached as the following:
        // createString = "drop table if exists [TABLENAME] [cascade (optional)];
        // where cascade is removed from SQLite statements, being ignored automatically.
        createString.append("drop table if exists ").append(tableName);
        if (cascade){
            createString.append(" cascade");
        }
        createString.append(";");

        // try, catch block for both statements as both can result in SQLException errors.
        try {
            statementExecutor(createString);
            System.out.println("Table '" + tableName + "' dropped successfully.");
        } catch (SQLException e) {
            throw new SQLException("Table could not be dropped for " + tableName, e);
        }
    }

    /**
     * String combiner for altering a table with the specified parameters.
     * @param tableName, the correlating table name
     * @param alterType, the specific alter parameters (connected by an ENUM declaration)
     * @param items, any items that follows after alterTypes, assuming correct
     * @throws SQLException, if driver is not initalized or connection fails
     */
    public void alterTable(String tableName, AlterTypes alterType, String[] items) throws SQLException {
        // Create the statement from its parts
        StringBuilder createString = new StringBuilder();

        // Attaches the correlating tableName variable.
        // createString will be attached as the following:
        // createString = "alter table [TABLENAME]"
        createString.append("alter table ").append(tableName).append(" ");

        // Could range from multiple different Strings:
        // Check AlterTypes.java for all possible values.
        // createString = "alter table [TABLENAME] [ALTER TYPE] "
        createString.append(alterType.toString()).append(" ");
        
        // Now adds all the items together to the createString
        for (int i = 0; i < items.length; i++){
            createString.append(items[i]);

            if (i < items.length - 1){
                createString.append(" ");
            }
        }

        // finally appends the closing semi-colon
        // createString = "alter table [TABLENAME] [ALTER TYPE] [ITEMS];"
        createString.append(";");

        // try, catch block for both statements as both can result in SQLException errors.
        try {
            statementExecutor(createString);
            System.out.println("Table '" + tableName + "' altered successfully.");
        } catch (SQLException e) {
            throw new SQLException("Table could not be altered for " + tableName, e);
        }
    }

    /**
     * Generalized Statement executor connected to the database, allowing for easier processing
     * from any statement being processed.
     * @param createString, the StringBuilder statement object
     * @return boolean, whether statement could be processed or not.
     * @throws SQLException, if driver is not initalized or connection fails
     */
    private boolean statementExecutor(StringBuilder createString) throws SQLException{
        // try, catch block for both statements as both can result in SQLException errors.
        try (Connection database = dbConnection.getConnection(); Statement stmt = database.createStatement()) {
            // attempt to execute this string to the database file.
            stmt.executeUpdate(createString.toString());
            return true;
        } catch (SQLException e) { 
            throw new SQLException(e);
        }
    }
}
