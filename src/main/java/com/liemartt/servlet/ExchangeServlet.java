package com.liemartt.servlet;

import com.liemartt.dao.CurrencyDAO;
import com.liemartt.dao.CurrencyDAOImpl;
import com.liemartt.exceptions.NoCurrencyException;
import com.liemartt.exceptions.NoExchangeRateException;
import com.liemartt.dto.ExchangeRequestDTO;
import com.liemartt.dto.ExchangeResponseDTO;
import com.liemartt.service.ExchangeService;
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

@WebServlet(urlPatterns = "/exchange")
public class ExchangeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CurrencyDAO currencyDAO = new CurrencyDAOImpl();
        String baseCurrencyCode = req.getParameter("from");
        String targetCurrencyCode = req.getParameter("to");
        String amount = req.getParameter("amount");
        if (baseCurrencyCode == null || targetCurrencyCode == null || amount == null) {
            ErrorSender.send(resp, 400, "empty field in form");
            return;
        }
        if (!baseCurrencyCode.matches("^[A-Z]{3}$") || !targetCurrencyCode.matches("^[A-Z]{3}$")) {
            ErrorSender.send(resp, 400, "invalid currency");
            return;
        }
        try {
            new BigDecimal(amount);
        } catch (NumberFormatException e) {
            ErrorSender.send(resp, 400, "invalid amount");
            return;
        }
        ExchangeRequestDTO exchangeRequestDTO = new ExchangeRequestDTO(baseCurrencyCode, targetCurrencyCode, new BigDecimal(amount));
        try {
            ExchangeResponseDTO exchangeResponseDTO = new ExchangeService().exchange(exchangeRequestDTO);
            Renderer.render(resp, exchangeResponseDTO);
        } catch (SQLException e) {
            ErrorSender.send(resp, 500, "");
        } catch (NoCurrencyException e) {
            ErrorSender.send(resp, 404, "No such currencies");
        } catch (NoExchangeRateException e) {
            ErrorSender.send(resp, 404, "Cannot exchange");
        }
    }
}
