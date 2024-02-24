package com.liemartt.dao;

import com.liemartt.model.Currency;
import com.liemartt.model.ExchangeRate;
import org.sqlite.SQLiteDataSource;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRateDAOImpl implements ExchangeRateDAO {
    private static SQLiteDataSource dataSource = DataSourceSql.dataSource;

    @Override
    public List<ExchangeRate> getAllExchangeRates() throws SQLException {
        String sql = "SELECT * FROM ExchangeRates";
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        try (Connection con = dataSource.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                exchangeRates.add(Converter.ConvertResulSetToExchangeRate(rs));
            }
        }
        return exchangeRates;
    }

    @Override
    public ExchangeRate getExchangeRate(String baseCurrencyCode, String targetCurrencyCode) throws SQLException {
        String sql = "SELECT er.id, er.BaseCurrencyId, er.TargetCurrencyId, er.Rate from ExchangeRates er " +
                "INNER JOIN Currencies cr1 on cr1.id = BaseCurrencyId " +
                "INNER JOIN Currencies cr2 on cr2.id = TargetCurrencyId " +
                "WHERE cr1.Code = ? and cr2.Code = ?";
        try (Connection con = dataSource.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, baseCurrencyCode);
            ps.setString(2, targetCurrencyCode);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return Converter.ConvertResulSetToExchangeRate(rs);
            else return null;
        }
    }

    @Override
    public ExchangeRate addNewExchangeRate(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate) throws SQLException {
        String sql = "INSERT INTO ExchangeRates (id, BaseCurrencyId, TargetCurrencyId, Rate) " +
                "VALUES (NULL, (select id from Currencies where Code = ?), (select id from Currencies where Code = ?), ?)";
        try (Connection con = dataSource.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, baseCurrencyCode);
            ps.setString(2, targetCurrencyCode);
            ps.setBigDecimal(3, rate);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                return Converter.ConvertResulSetToExchangeRate(rs);
            }
        }
    }

    @Override
    public ExchangeRate updateExchangeRate(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate) throws SQLException {
        String sql = "UPDATE ExchangeRates SET Rate = ? " +
                "WHERE BaseCurrencyId =  (select id from Currencies where Code = ?) " +
                "and TargetCurrencyId = (select id from Currencies where Code = ?)";
        try (Connection con = dataSource.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setBigDecimal(1, rate);
            ps.setString(2, baseCurrencyCode);
            ps.setString(3, targetCurrencyCode);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                return Converter.ConvertResulSetToExchangeRate(rs);
            }
        }
    }
}
