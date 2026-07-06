/**
 * 
 * File: DatabaseConnection.java
 * @author: Junshin Purganan
 * 
 * This class is used to connect to the Database, being sent towards
 * the related intializer and any other necessary classes in order
 * to alter or transfer data to the database itself.
 */

package com.db_connector.rating.interaction;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.springframework.stereotype.Component;

@Component
public class DatabaseConnection {
    private static final Properties properties = new Properties();

    /**
     * Used to load the properties from the config.properties file upon being loaded into memory
     */
    static {
        try(InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream("application.properties")){
            if (input == null) {
                throw new RuntimeException("Unable to find application.properties");
            }
            properties.load(input);
        } catch (IOException e){
            throw new RuntimeException("ERROR: Couldn't load configuration file.");
        }
    }

    /**
     * Method used for establishing a connection between the desired database,
     * using properties from external file to allow for universal code between
     * specific database types.
     * @return Connection, to utilize the database
     * @throws SQLException, if driver is not initalized or connection fails
     */
    public Connection getConnection() throws SQLException{
        /**
         * Collects information from config.properties file, requires manual switch
         * for change between SQLite and PostGRES
         */
        String driver = properties.getProperty("db.driver");
        String url = properties.getProperty("db.url");
        
        // using Connection Props for better security
        Properties connectionProps = new Properties();
        connectionProps.put("user", properties.getProperty("db.user", ""));
        connectionProps.put("password", properties.getProperty("db.password", ""));
        
        // try catch for the driver, where if it cannot find the related driver, will crash.
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Database Driver missing: " + driver, e);
        }
        return DriverManager.getConnection(url, connectionProps);
    }
}
