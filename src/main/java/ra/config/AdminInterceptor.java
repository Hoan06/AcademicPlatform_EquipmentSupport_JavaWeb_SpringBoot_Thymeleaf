package ra.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class AdminInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userLogin") == null) {
            response.sendRedirect("/auth/login");
            return false;
        }

        String role = (String) session.getAttribute("role");

        if (!"admin".equals(role)) {
            if ("lecturer".equals(role)) {
                response.sendRedirect("/lecturer/dashboard");
            } else {
                response.sendRedirect("/client/profile");
            }
            return false;
        }

        return true;
    }
}

