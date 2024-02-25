package com.liemartt.utilities;

import com.liemartt.dao.CurrencyDAOImpl;
import com.liemartt.exceptions.NoCurrencyException;
import com.liemartt.model.Currency;
import com.liemartt.model.ExchangeRate;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Converter {
    public static Currency ConvertResulSetToCurrency(ResultSet rs) throws SQLException {
        return new Currency(rs.getInt(1),
                rs.getString(2),
                rs.getString(3),
                rs.getString(4));
    }

    public static ExchangeRate ConvertResulSetToExchangeRate(ResultSet rs) throws SQLException {
        try {
            return new ExchangeRate(rs.getInt(1),
                    new CurrencyDAOImpl().getCurrencyById(rs.getInt(2)),
                    new CurrencyDAOImpl().getCurrencyById(rs.getInt(3)),
                    rs.getBigDecimal(4));
        } catch (NoCurrencyException ignored) {
            return null;
        }
    }
}
