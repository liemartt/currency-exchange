package com.liemartt.dao;

import com.liemartt.model.Currency;
import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CurrencyDAOImpl implements CurrencyDAO {
    private static SQLiteDataSource dataSource = DataSourceSql.dataSource;

    public List<Currency> getAllCurrencies() throws SQLException {
        String sql = "SELECT * FROM Currencies";
        List<Currency> currencies = new ArrayList<>();
        try (Connection con = dataSource.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                currencies.add(Converter.ConvertResulSetToCurrency(rs));
            }
        }
        return currencies;
    }

    @Override
    public Currency getCurrencyByCode(String code) throws SQLException {
        String sql = "SELECT * FROM Currencies WHERE Code = ?";
        List<Currency> currencies = new ArrayList<>();
        try (Connection con = dataSource.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return Converter.ConvertResulSetToCurrency(rs);
            else return null;
        }
    }

    @Override
    public void addNewCurrency(Currency currency) throws SQLException {
        String sql = "INSERT INTO Currencies (id, Code, FullName, Sign) VALUES (NULL, ?, ?, ?)";
        List<Currency> currencies = new ArrayList<>();
        try (Connection con = dataSource.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, currency.getCode());
            ps.setString(2, currency.getName());
            ps.setString(3, currency.getSign());
        }
        //TODO Exception for Non-unique currency

    }
}
