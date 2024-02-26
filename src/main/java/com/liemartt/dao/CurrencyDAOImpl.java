package com.liemartt.dao;

import com.liemartt.exceptions.DBErrorException;
import com.liemartt.exceptions.NoCurrencyException;
import com.liemartt.exceptions.NonUniqueCurrencyException;
import com.liemartt.model.Currency;
import com.liemartt.utilities.Converter;
import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CurrencyDAOImpl implements CurrencyDAO {
    private static final SQLiteDataSource dataSource = DataSourceSql.dataSource;

    public List<Currency> getAllCurrencies()  {
        String sql = "SELECT * FROM Currencies";
        List<Currency> currencies = new ArrayList<>();
        try (Connection con = dataSource.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                currencies.add(Converter.convertResulSetToCurrency(rs));
            }
        }
        catch (SQLException e){
            throw new DBErrorException();
        }
        return currencies;
    }

    @Override
    public Currency getCurrencyByCode(String code) {
        String sql = "SELECT * FROM Currencies WHERE Code = ?";
        try (Connection con = dataSource.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Converter.convertResulSetToCurrency(rs);
            else throw new NoCurrencyException();
        } catch (SQLException e) {
            throw new DBErrorException();
        }
    }

    @Override
    public Currency getCurrencyById(int id) {
        String sql = "SELECT * FROM Currencies WHERE id = ?";
        try (Connection con = dataSource.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Converter.convertResulSetToCurrency(rs);
            else throw new NoCurrencyException();
        } catch (SQLException e) {
            throw new DBErrorException();
        }
    }

    @Override
    public Currency addNewCurrency(Currency currency) {
        String sql = "INSERT INTO Currencies (Code, FullName, Sign) VALUES (?, ?, ?)";
        try (Connection con = dataSource.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, currency.getCode());
            ps.setString(2, currency.getName());
            ps.setString(3, currency.getSign());
            int countOfRows = ps.executeUpdate();
            if (countOfRows == 0){
                throw new NonUniqueCurrencyException();
            }
            currency.setId(ps.getGeneratedKeys().getInt(1));
            return currency;
        } catch (SQLException e) {
            throw new DBErrorException();
        }
    }
}
