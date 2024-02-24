package com.liemartt.dao;

import com.liemartt.model.Currency;
import com.liemartt.model.ExchangeRate;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public interface ExchangeRateDAO {

    List<ExchangeRate> getAllExchangeRates() throws SQLException;

    ExchangeRate getExchangeRate(String baseCurrencyCode, String targetCurrencyCode)throws SQLException;

    ExchangeRate addNewExchangeRate(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate)throws SQLException;

    ExchangeRate updateExchangeRate(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate)throws SQLException;


}
