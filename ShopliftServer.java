import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ShopliftServer {

    private static Map<String, Double> products = new HashMap<>();

    public static void main(String[] args) throws IOException {
        // Sample products
        products.put("iPhone 15", 1200.0);
        products.put("Samsung Galaxy S23", 1100.0);
        products.put("MacBook Pro", 2500.0);
        products.put("Dell XPS 13", 2000.0);
        products.put("AirPods Pro", 250.0);
        products.put("Smartwatch", 300.0);

        // Start HTTP server
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        System.out.println("Server started at http://localhost:8000");

        // Serve static HTML pages
        server.createContext("/", new StaticFileHandler("index.html"));
        server.createContext("/products", new StaticFileHandler("products.html"));
        server.createContext("/about", new StaticFileHandler("about.html"));
        server.createContext("/contact", new StaticFileHandler("contact.html"));
        server.createContext("/common.css", new StaticFileHandler("common.css"));
        server.createContext("/script.js", new StaticFileHandler("script.js"));

        // API endpoint for products JSON
        server.createContext("/api/products", exchange -> {
            StringBuilder response = new StringBuilder("[");
            for (Map.Entry<String, Double> entry : products.entrySet()) {
                response.append("{\"name\":\"")
                        .append(entry.getKey())
                        .append("\",\"price\":")
                        .append(entry.getValue())
                        .append("},");
            }
            if (!products.isEmpty()) response.deleteCharAt(response.length() - 1); // remove last comma
            response.append("]");
            sendResponse(exchange, response.toString(), "application/json");
        });

        // Start server
        server.setExecutor(null);
        server.start();
    }

    static void sendResponse(HttpExchange exchange, String response, String contentType) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    // Handler for static files
    static class StaticFileHandler implements HttpHandler {
        private String filePath;

        public StaticFileHandler(String filePath) {
            this.filePath = filePath;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (Files.exists(Paths.get(filePath))) {
                byte[] bytes = Files.readAllBytes(Paths.get(filePath));
                String contentType = "text/html";
                if (filePath.endsWith(".css")) contentType = "text/css";
                if (filePath.endsWith(".js")) contentType = "application/javascript";
                exchange.getResponseHeaders().set("Content-Type", contentType);
                exchange.sendResponseHeaders(200, bytes.length);
                OutputStream os = exchange.getResponseBody();
                os.write(bytes);
                os.close();
            } else {
                String notFound = "404 Not Found";
                exchange.sendResponseHeaders(404, notFound.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(notFound.getBytes());
                os.close();
            }
        }
    }
}