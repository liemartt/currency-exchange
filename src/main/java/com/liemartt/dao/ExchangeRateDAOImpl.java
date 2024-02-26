package com.liemartt.dao;

import com.liemartt.exceptions.DBErrorException;
import com.liemartt.exceptions.NoCurrencyException;
import com.liemartt.exceptions.NoExchangeRateException;
import com.liemartt.exceptions.NonUniqueExchangeRateException;
import com.liemartt.model.ExchangeRate;
import com.liemartt.utilities.Converter;
import org.sqlite.SQLiteDataSource;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRateDAOImpl implements ExchangeRateDAO {
    private static final SQLiteDataSource dataSource = DataSourceSql.dataSource;
    private final CurrencyDAO currencyDAO = new CurrencyDAOImpl();

    @Override
    public List<ExchangeRate> getAllExchangeRates() {
        String sql = "SELECT * FROM ExchangeRates";
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        try (Connection con = dataSource.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                exchangeRates.add(Converter.convertResulSetToExchangeRate(rs));
            }
        } catch (SQLException e) {
            throw new DBErrorException();
        }
        return exchangeRates;
    }

    @Override
    public ExchangeRate getExchangeRate(String baseCurrencyCode, String targetCurrencyCode) {
        String sql = "SELECT er.id, er.BaseCurrencyId, er.TargetCurrencyId, er.Rate, " +
                "cr1.id, cr1.Code, cr1.FullName, " +
                "cr1.Sign, cr2.id, cr2.Code, cr2.FullName, cr2.Sign from ExchangeRates er " +
                "INNER JOIN Currencies cr1 on cr1.id = BaseCurrencyId " +
                "INNER JOIN Currencies cr2 on cr2.id = TargetCurrencyId " +
                "WHERE cr1.Code = ? and cr2.Code = ?";
        try (Connection con = dataSource.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, baseCurrencyCode);
            ps.setString(2, targetCurrencyCode);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return Converter.convertResulSetToExchangeRate(rs);
            else throw new NoExchangeRateException();
        } catch (SQLException e) {
            throw new DBErrorException();
        }
    }

    @Override
    public ExchangeRate addNewExchangeRate(ExchangeRate exchangeRate) {
        String baseCurrencyCode = exchangeRate.getBaseCurrency().getCode();
        String targetCurrencyCode = exchangeRate.getTargetCurrency().getCode();
        BigDecimal rate = exchangeRate.getRate();
        String sql = "INSERT INTO ExchangeRates (BaseCurrencyId, TargetCurrencyId, Rate) " +
                "VALUES ((select id from Currencies where Code = ?), (select id from Currencies where Code = ?), ?)";
        if (currencyDAO.getCurrencyByCode(baseCurrencyCode) == null || currencyDAO.getCurrencyByCode(targetCurrencyCode) == null)
            throw new NoCurrencyException();
        try (Connection con = dataSource.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, baseCurrencyCode);
            ps.setString(2, targetCurrencyCode);
            ps.setBigDecimal(3, rate);
            try {
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new NonUniqueExchangeRateException();
            }
            return this.getExchangeRate(baseCurrencyCode, targetCurrencyCode);
        } catch (SQLException e) {
            throw new DBErrorException();
        }
    }

    @Override
    public ExchangeRate updateExchangeRate(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate) {
        String sql = "UPDATE ExchangeRates SET Rate = ? " +
                "WHERE BaseCurrencyId =  (select id from Currencies where Code = ?) " +
                "and TargetCurrencyId = (select id from Currencies where Code = ?)";
        try (Connection con = dataSource.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setBigDecimal(1, rate);
            ps.setString(2, baseCurrencyCode);
            ps.setString(3, targetCurrencyCode);
            int countOfRows = ps.executeUpdate();
            if (countOfRows == 0) throw new NoExchangeRateException();
            return this.getExchangeRate(baseCurrencyCode, targetCurrencyCode);
        } catch (SQLException e) {
            throw new DBErrorException();
        }
    }
}
