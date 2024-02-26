package com.liemartt.dao;

import javax.sql.DataSource;
import java.net.URL;
import java.sql.DriverManager;

import org.sqlite.SQLiteDataSource;

public class DataSourceSql {
    public final static SQLiteDataSource dataSource;

    static {
        String URL = "jdbc:sqlite:" + DataSourceSql.class.getClassLoader().getResource("currency-exchange-db.sqlite").getPath();
        dataSource = new SQLiteDataSource();
        dataSource.setUrl(URL);
    }
}
