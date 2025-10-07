package com.shoplift;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class LoginServlet extends LoginServerlet {
    @Override
    protected void doPost(LoginServerletRequest req, LoginServerletResponse resp)
            throws LoginServerletException, IOException {

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        if ("admin@shoplift.com".equals(email) && "password123".equals(password)) {
            resp.getWriter().write("Login successful! Welcome, admin.");
        } else {
            resp.getWriter().write("Invalid email or password.");
        }
    }
}