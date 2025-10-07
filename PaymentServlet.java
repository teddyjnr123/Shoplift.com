package com.shoplift;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class PaymentServlet extends HttpServlet {
    private static final String PAYSTACK_SECRET_KEY = "sk_test_xxxxxxxxxxxxxxxxxxxxx"; // Replace with your secret key

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        BufferedReader reader = req.getReader();
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        String requestBody = sb.toString();
        String reference = requestBody.replaceAll(".*\"reference\":\"(.*?)\".*", "$1");

        // Verify payment with Paystack API
        URL url = new URL("https://api.paystack.co/transaction/verify/" + reference);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + PAYSTACK_SECRET_KEY);

        int responseCode = conn.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder responseText = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            responseText.append(inputLine);
        }
        in.close();

        resp.setContentType("application/json");
        if (responseCode == 200 && responseText.toString().contains("\"status\":true")) {
            resp.getWriter().write("{\"message\":\"Payment verified successfully!\"}");
        } else {
            resp.getWriter().write("{\"message\":\"Payment verification failed!\"}");
        }
    }
}