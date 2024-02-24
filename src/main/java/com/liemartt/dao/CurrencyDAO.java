package com.liemartt.dao;

import com.liemartt.model.Currency;

import java.sql.SQLException;
import java.util.List;

public interface CurrencyDAO {
    List<Currency> getAllCurrencies() throws SQLException;

    Currency getCurrencyByCode(String code) throws SQLException;
    Currency getCurrencyById(int id) throws SQLException;

    int addNewCurrency(Currency currency) throws SQLException;

}
