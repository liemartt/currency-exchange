package com.liemartt.utilities;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class Renderer {
    public static void render(HttpServletResponse resp, Object obj) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String jsonObject = new Gson().toJson(obj);
        resp.getWriter().write(jsonObject);
    }
}
