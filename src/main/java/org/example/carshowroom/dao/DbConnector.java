package org.example.carshowroom.dao;

import org.example.carshowroom.model.ConnectionProperties;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DbConnector {

    private final Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public DbConnector() {
        try {
            var props = readProperties("db.properties");
            Class.forName(props.getDriver());
            connection = DriverManager.getConnection(props.getUrl(), props.getUser(), props.getPassword());
        } catch (Exception e) {
            throw new RuntimeException("Ошибка подключения к БД", e);
        }
    }

    private ConnectionProperties readProperties(String fileName) {
        try (var is = DbConnector.class.getClassLoader().getResourceAsStream(fileName)) {
            var props = new Properties();
            props.load(is);
            return new ConnectionProperties(
                    props.getProperty("db.driver"),
                    props.getProperty("db.url"),
                    props.getProperty("db.user"),
                    props.getProperty("db.password")
            );
        } catch (Exception e) {
            throw new RuntimeException("Ошибка чтения файла настроек " + fileName, e);
        }
    }
}
