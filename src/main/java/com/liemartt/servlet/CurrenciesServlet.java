package com.liemartt.servlet;

import com.google.gson.Gson;
import com.liemartt.dao.CurrencyDAO;
import com.liemartt.dao.CurrencyDAOImpl;
import com.liemartt.model.Currency;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(urlPatterns = "/currencies")
public class CurrenciesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Currency> currencies = new CurrencyDAOImpl().getAllCurrencies();
            Renderer.render(resp, currencies);
        } catch (SQLException e) {
            ErrorSender.send(resp, 500, "");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CurrencyDAO currencyDAO = new CurrencyDAOImpl();
        String name = req.getParameter("name");
        String code = req.getParameter("code");
        String sign = req.getParameter("sign");
        if (name == null || code == null || sign == null) {
            ErrorSender.send(resp, 400, "empty field in form");
            return;
        }
        Currency currencyToAdd = new Currency(0, code, name, sign);
        try {
            int addedCurrencyId = currencyDAO.addNewCurrency(currencyToAdd);
            if (addedCurrencyId == -1) {
                ErrorSender.send(resp, 409, "There is already a currency with this code");
                return;
            }
            Currency addedCurrency = currencyDAO.getCurrencyById(addedCurrencyId);
            resp.setStatus(201);
            Renderer.render(resp, addedCurrency);
        } catch (SQLException e) {
            ErrorSender.send(resp, 500, "");
        }
    }
}
