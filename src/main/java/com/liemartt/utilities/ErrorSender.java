package com.liemartt.utilities;

import com.google.gson.Gson;
import com.liemartt.model.Message;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ErrorSender {
    public static void send(HttpServletResponse resp, int code, String message) throws IOException {
        String jsonMessage = new Gson().toJson(new Message(message));
        resp.setContentType("application/json");
        resp.setStatus(code);
        resp.getWriter().write(jsonMessage);
    }
}
