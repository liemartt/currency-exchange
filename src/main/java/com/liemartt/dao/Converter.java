package com.liemartt.dao;

import com.liemartt.model.Currency;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Converter {
    public static Currency ConvertResulSetToCurrency(ResultSet rs) throws SQLException {
        Currency currency = new Currency(rs.getInt(1),
                rs.getString(2),
                rs.getString(3),
                rs.getString(4));
        return currency;
    }
}
