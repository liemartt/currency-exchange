package com.liemartt.dao;

import com.liemartt.exceptions.NoCurrencyException;
import com.liemartt.exceptions.NonUniqueCurrencyException;
import com.liemartt.model.Currency;

import java.sql.SQLException;
import java.util.List;

public interface CurrencyDAO {
    List<Currency> getAllCurrencies() throws SQLException;

    Currency getCurrencyByCode(String code) throws SQLException, NoCurrencyException;

    Currency getCurrencyById(int id) throws SQLException, NoCurrencyException;

    Currency addNewCurrency(Currency currency) throws SQLException, NonUniqueCurrencyException, NoCurrencyException;

}
