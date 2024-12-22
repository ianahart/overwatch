package com.hart.overwatch.notfound;

import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;


@RestController
public class NotFoundController implements HandlerMapping {
    @Override
    public HandlerExecutionChain getHandler(HttpServletRequest request) {
        if ("/api/v1/".equals(request.getRequestURI())) {
            HttpRequestHandler handler = new HttpRequestHandler() {
                @Override
                public void handleRequest(HttpServletRequest req, HttpServletResponse res) {
                    try {
                        res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        res.getWriter().write("Not Found");
                        res.getWriter().flush();
                    } catch (IOException e) {
                        throw new RuntimeException("Error handling request", e);
                    }
                }
            };
            return new HandlerExecutionChain(handler);
        }
        return null;
    }
}
