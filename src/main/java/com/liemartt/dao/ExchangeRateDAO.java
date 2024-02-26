package com.liemartt.dao;

import com.liemartt.exceptions.NoCurrencyException;
import com.liemartt.exceptions.NoExchangeRateException;
import com.liemartt.exceptions.NonUniqueExchangeRateException;
import com.liemartt.model.ExchangeRate;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public interface ExchangeRateDAO {

    List<ExchangeRate> getAllExchangeRates();

    ExchangeRate getExchangeRate(String baseCurrencyCode, String targetCurrencyCode);

    ExchangeRate addNewExchangeRate(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate);

    ExchangeRate updateExchangeRate(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate);


}
