import java.io.*;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.*;

public class Shoplift {
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        System.out.println("Shoplift backend running at http://localhost:8080");

        // Checkout order saving
        server.createContext("/checkout", exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                InputStream is = exchange.getRequestBody();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder body = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    body.append(line);
                }

                try (FileWriter fw = new FileWriter("orders.txt", true)) {
                    fw.write(body.toString() + "\n");
                }

                String response = "Order saved!";
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        });

        server.setExecutor(null);
        server.start();
    }
}