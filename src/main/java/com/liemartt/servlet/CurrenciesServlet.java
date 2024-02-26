package com.liemartt.servlet;

import com.liemartt.dao.CurrencyDAO;
import com.liemartt.dao.CurrencyDAOImpl;
import com.liemartt.exceptions.DBErrorException;
import com.liemartt.exceptions.NoCurrencyException;
import com.liemartt.exceptions.NonUniqueCurrencyException;
import com.liemartt.model.Currency;
import com.liemartt.utilities.ErrorSender;
import com.liemartt.utilities.Renderer;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@WebServlet(urlPatterns = "/currencies")
public class CurrenciesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Currency> currencies = new CurrencyDAOImpl().getAllCurrencies();
            Renderer.render(resp, currencies);
        } catch (DBErrorException e) {
            ErrorSender.send(resp, 500, "Server error");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CurrencyDAO currencyDAO = new CurrencyDAOImpl();
        String name = req.getParameter("name");
        String code = req.getParameter("code");
        String sign = req.getParameter("sign");
        if (Objects.equals(name, "") || Objects.equals(code, "") || Objects.equals(sign, "")) {
            ErrorSender.send(resp, 400, "empty field in form");
            return;
        }
        try {
            Currency addedCurrency = currencyDAO.addNewCurrency(new Currency(0, code, name, sign));
            resp.setStatus(201);
            Renderer.render(resp, addedCurrency);
        } catch (DBErrorException | NoCurrencyException e) {
            ErrorSender.send(resp, 500, "Server error");
        } catch (NonUniqueCurrencyException e) {
            ErrorSender.send(resp, 409, "There is already a currency with this code");
        }
    }
}
