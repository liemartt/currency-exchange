package com.liemartt.dao;

import com.liemartt.exceptions.NoCurrencyException;
import com.liemartt.exceptions.NonUniqueCurrencyException;
import com.liemartt.model.Currency;

import java.sql.SQLException;
import java.util.List;

public interface CurrencyDAO {
    List<Currency> getAllCurrencies();

    Currency getCurrencyByCode(String code);

    Currency getCurrencyById(int id);

    Currency addNewCurrency(Currency currency);

}
