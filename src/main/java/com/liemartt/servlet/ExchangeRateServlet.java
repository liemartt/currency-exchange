package com.liemartt.servlet;

import com.liemartt.dao.ExchangeRateDAO;
import com.liemartt.dao.ExchangeRateDAOImpl;
import com.liemartt.exceptions.NoExchangeRateException;
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

@WebServlet(urlPatterns = "/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!req.getMethod().equals("PATCH")) {
            this.doGet(req, resp);
        } else this.doPatch(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (!pathInfo.matches("^/[A-Z]{6}$")) {
            ErrorSender.send(resp, 400, "invalid currencies");
            return;
        }
        String baseCurrencyCode = pathInfo.split("/")[1].substring(0, 3);
        String targetCurrencyCode = pathInfo.split("/")[1].substring(3, 6);
        try {
            ExchangeRate exchangeRate = new ExchangeRateDAOImpl().getExchangeRate(baseCurrencyCode, targetCurrencyCode);
            Renderer.render(resp, exchangeRate);
        } catch (SQLException e) {
            ErrorSender.send(resp, 500, "");
        } catch (NoExchangeRateException e) {
            ErrorSender.send(resp, 404, "No such exchange rate");
        }
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ExchangeRateDAO exchangeRateDAO = new ExchangeRateDAOImpl();
        String rate = req.getParameter("rate");
        if (rate == null) {
            ErrorSender.send(resp, 400, "empty field in form");
            return;
        }
        String pathInfo = req.getPathInfo();
        if (!pathInfo.matches("^/[A-Z]{6}$")) {
            ErrorSender.send(resp, 404, "invalid currencies");
            return;
        }
        String baseCurrencyCode = pathInfo.split("/")[1].substring(0, 3);
        String targetCurrencyCode = pathInfo.split("/")[1].substring(3, 6);
        try {
            ExchangeRate updatedExchangeRate = exchangeRateDAO.updateExchangeRate(baseCurrencyCode, targetCurrencyCode, new BigDecimal(rate));
            Renderer.render(resp, updatedExchangeRate);
        } catch (SQLException e) {
            ErrorSender.send(resp, 500, "");
        } catch (NoExchangeRateException e) {
            ErrorSender.send(resp, 404, "No such Exchange rate");
        }
    }
}
