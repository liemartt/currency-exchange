package com.liemartt.servlet;

import com.liemartt.dao.CurrencyDAO;
import com.liemartt.dao.CurrencyDAOImpl;
import com.liemartt.dao.ExchangeRateDAO;
import com.liemartt.dao.ExchangeRateDAOImpl;
import com.liemartt.exceptions.DBErrorException;
import com.liemartt.exceptions.NoCurrencyException;
import com.liemartt.exceptions.NoExchangeRateException;
import com.liemartt.exceptions.NonUniqueExchangeRateException;
import com.liemartt.model.ExchangeRate;
import com.liemartt.utilities.ErrorSender;
import com.liemartt.utilities.Renderer;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@WebServlet(urlPatterns = "/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<ExchangeRate> exchangeRates = new ExchangeRateDAOImpl().getAllExchangeRates();
            Renderer.render(resp, exchangeRates);
        } catch (DBErrorException e) {
            ErrorSender.send(resp, 500, "");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String baseCurrencyCode = req.getParameter("baseCurrencyCode");
        String targetCurrencyCode = req.getParameter("targetCurrencyCode");
        String rate = req.getParameter("rate");
        ExchangeRateDAO exchangeRateDAO = new ExchangeRateDAOImpl();
        if (Objects.equals(baseCurrencyCode, "") || Objects.equals(targetCurrencyCode, "") || Objects.equals(rate, "")) {
            ErrorSender.send(resp, 400, "empty field in form");
            return;
        }
        try {
            ExchangeRate addedExchangeRate = exchangeRateDAO.addNewExchangeRate(baseCurrencyCode, targetCurrencyCode, new BigDecimal(rate));
            resp.setStatus(201);
            Renderer.render(resp, addedExchangeRate);
        } catch (DBErrorException | NoExchangeRateException e) {
            ErrorSender.send(resp, 500, "Server error");
        } catch (NoCurrencyException e) {
            ErrorSender.send(resp, 404, "No such currencies");
        } catch (NonUniqueExchangeRateException e) {
            ErrorSender.send(resp, 409, "There is already an exchange rate with this currencies");
        }
    }
}
