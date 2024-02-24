package com.liemartt.dao;

import javax.sql.DataSource;
import java.net.URL;
import java.sql.DriverManager;

import org.sqlite.SQLiteDataSource;

public class DataSourceSql {
    public final static SQLiteDataSource dataSource;

    static {
        String URL = "jdbc:sqlite:D:\\JavaLearning\\currency-exchange\\src\\main\\resources\\currency-exchange-db.sqlite";
        dataSource = new SQLiteDataSource();
        dataSource.setUrl(URL);
    }
}
