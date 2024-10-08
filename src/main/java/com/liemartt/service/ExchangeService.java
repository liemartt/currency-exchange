package com.liemartt.service;

import com.liemartt.dao.CurrencyDAO;
import com.liemartt.dao.CurrencyDAOImpl;
import com.liemartt.dao.ExchangeRateDAO;
import com.liemartt.dao.ExchangeRateDAOImpl;
import com.liemartt.exceptions.NoCurrencyException;
import com.liemartt.exceptions.NoExchangeRateException;
import com.liemartt.model.Currency;
import com.liemartt.dto.ExchangeRequestDTO;
import com.liemartt.dto.ExchangeResponseDTO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;

public class ExchangeService {
    private final CurrencyDAO currencyDAO = new CurrencyDAOImpl();
    private final ExchangeRateDAO exchangeRateDAO = new ExchangeRateDAOImpl();

    public ExchangeService() {
    }

    public ExchangeResponseDTO exchange(ExchangeRequestDTO request){
        Currency baseCurrency = currencyDAO.getCurrencyByCode(request.getBaseCurrencyCode());
        Currency targetCurrency = currencyDAO.getCurrencyByCode(request.getTargetCurrencyCode());
        BigDecimal amount = request.getAmount();
        try {// Case  A-B
            BigDecimal exchangeRate = exchangeRateDAO.getExchangeRate(baseCurrency.getCode(), targetCurrency.getCode()).getRate();
            return new ExchangeResponseDTO(baseCurrency, targetCurrency, exchangeRate, amount, amount.multiply(exchangeRate));
        } catch (NoExchangeRateException ignored) {
        }
        try { // Case  B-A
            BigDecimal inverseExchangeRate = exchangeRateDAO.getExchangeRate(targetCurrency.getCode(), baseCurrency.getCode()).getRate();
            BigDecimal exchangeRate = new BigDecimal("1.000000").divide(inverseExchangeRate,  RoundingMode.CEILING);
            return new ExchangeResponseDTO(baseCurrency, targetCurrency, exchangeRate, amount, amount.multiply(exchangeRate).setScale(2, RoundingMode.CEILING));
        } catch (NoExchangeRateException ignored) {
        }
        // Case  USD-A and USD-B
        BigDecimal exchangeRateUsdToBaseCurrency = exchangeRateDAO.getExchangeRate("USD", baseCurrency.getCode()).getRate();
        BigDecimal exchangeRateUsdToTargetCurrency = exchangeRateDAO.getExchangeRate("USD", targetCurrency.getCode()).getRate();
        BigDecimal exchangeRateBaseCurrencyToUsd = new BigDecimal("1.000000").divide(exchangeRateUsdToBaseCurrency,  RoundingMode.CEILING);
        BigDecimal exchangeRate = exchangeRateBaseCurrencyToUsd.multiply(exchangeRateUsdToTargetCurrency);
        return new ExchangeResponseDTO(baseCurrency, targetCurrency, exchangeRate, amount, amount.multiply(exchangeRate).setScale(2, RoundingMode.CEILING));
    }
}
