package com.shoplift;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class ContactServlet extends ContactServerlet {
    @Override
    protected void doPost(ContactServerletRequest req, ContactServerletResponse resp)
            throws ContactServerletException, IOException {

        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String message = req.getParameter("message");

        System.out.println("📩 Contact Form Submission:");
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Message: " + message);

        resp.setContentType("text/plain");
        resp.getWriter().write("Message received! Thank you, " + name);
    }
}