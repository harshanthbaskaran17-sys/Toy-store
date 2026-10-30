package com.toystore.common;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

@Component
public class SessionUtil {

    public void loginUser(HttpSession session, String userId, String username, String role) {
        session.setAttribute("loggedInUserId", userId);
        session.setAttribute("loggedInUsername", username);
        session.setAttribute("userRole", role);
    }

    public void logoutUser(HttpSession session) {
        session.invalidate();
    }

    public boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("loggedInUserId") != null;
    }

    public String getLoggedInUserId(HttpSession session) {
        return (String) session.getAttribute("loggedInUserId");
    }

    public String getLoggedInUsername(HttpSession session) {
        return (String) session.getAttribute("loggedInUsername");
    }
    
    public String getUserRole(HttpSession session) {
        return (String) session.getAttribute("userRole");
    }

    public boolean isAdmin(HttpSession session) {
        return "ADMIN".equals(getUserRole(session));
    }
}
