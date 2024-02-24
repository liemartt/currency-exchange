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
    public Currency getCurrency() {
        return null;
    }

    @Override
    public void addNewCurrency(Currency currency) {

    }

    @Override
    public void updateCurrency(Currency currency) {

    }
}
