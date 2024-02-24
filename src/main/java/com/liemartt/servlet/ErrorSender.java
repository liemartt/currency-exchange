package com.liemartt.servlet;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ErrorSender {
    public static void send(HttpServletResponse resp, int code, String message) throws IOException {
        resp.setContentType("application/json");
        String jsonMessage = new Gson().toJson(message);
        resp.sendError(code, jsonMessage);
    }
}
