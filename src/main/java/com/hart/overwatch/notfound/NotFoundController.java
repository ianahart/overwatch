package com.hart.overwatch.notfound;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class NotFoundController implements HandlerMapping {
    @Override
    public HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
        if ("/api/v1/".equals(request.getRequestURI())) {
            return new HandlerExecutionChain((HttpServletRequest req, HttpServletResponse res) -> {
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                res.getWriter().write("Not Found");
                res.getWriter().flush();
            });
        }
        return null;
    }
}
