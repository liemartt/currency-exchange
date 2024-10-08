package com.liemartt.utilities;

import com.liemartt.dao.CurrencyDAOImpl;
import com.liemartt.exceptions.DBErrorException;
import com.liemartt.exceptions.NoCurrencyException;
import com.liemartt.model.Currency;
import com.liemartt.model.ExchangeRate;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Converter {
    public static Currency convertResulSetToCurrency(ResultSet rs) throws SQLException {
        return new Currency(rs.getInt(1),
                rs.getString(2),
                rs.getString(3),
                rs.getString(4));
    }

    public static ExchangeRate convertResulSetToExchangeRate(ResultSet rs) {
        Currency baseCurrency = new Currency();
        try {
            baseCurrency.setId(rs.getInt("cr1_id"));
            baseCurrency.setCode(rs.getString("cr1_Code"));
            baseCurrency.setName(rs.getString("cr1_FullName"));
            baseCurrency.setSign(rs.getString("cr1_Sign"));
            Currency targetCurrency = new Currency();
            targetCurrency.setId(rs.getInt("cr2_id"));
            targetCurrency.setCode(rs.getString("cr2_Code"));
            targetCurrency.setName(rs.getString("cr2_FullName"));
            targetCurrency.setSign(rs.getString("cr2_Sign"));
            return new ExchangeRate(rs.getInt(1), baseCurrency, targetCurrency, rs.getBigDecimal(4));
        } catch (SQLException e) {
            throw new DBErrorException();
        }
    }
}
