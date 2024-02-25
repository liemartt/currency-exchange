package com.liemartt.dao;

import com.liemartt.exceptions.NoCurrencyException;
import com.liemartt.exceptions.NoExchangeRateException;
import com.liemartt.exceptions.NonUniqueExchangeRateException;
import com.liemartt.model.ExchangeRate;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public interface ExchangeRateDAO {

    List<ExchangeRate> getAllExchangeRates() throws SQLException;

    ExchangeRate getExchangeRate(String baseCurrencyCode, String targetCurrencyCode) throws SQLException, NoExchangeRateException;

    ExchangeRate addNewExchangeRate(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate) throws SQLException, NonUniqueExchangeRateException, NoExchangeRateException, NoCurrencyException;

    ExchangeRate updateExchangeRate(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate) throws SQLException, NoExchangeRateException;


}
