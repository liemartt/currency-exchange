package com.liemartt.servlet;

import com.liemartt.dao.CurrencyDAOImpl;
import com.liemartt.exceptions.NoCurrencyException;
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

@WebServlet(urlPatterns = "/currency/*")
public class CurrencyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (!pathInfo.matches("^/[A-Z]{3}$")) {
            ErrorSender.send(resp, 400, "path must be /currency/***");
            return;
        }
        String currencyCode = pathInfo.split("/")[1];
        try {
            Currency currency = new CurrencyDAOImpl().getCurrencyByCode(currencyCode);
            Renderer.render(resp, currency);
        } catch (SQLException e) {
            ErrorSender.send(resp, 500, "");
        } catch (NoCurrencyException e) {
            ErrorSender.send(resp, 404, "No such currency");
        }
    }
}
