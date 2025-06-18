// Java Program to Set up a Basic HTTP Web Server

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        // MAPPING/ROUTES(PATHS)
        server.createContext("/", new HomePageHandler());
        server.createContext("/data/", new CitiesHandler());

        server.start();
        System.out.println("Server is running on port 8000");
    }

    // HANDLERS
    static class HomePageHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "Hello, this is a simple HTTP server response!";
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    static class CitiesHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = getJSON();
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    static String getJSON() {
        return String.format("[\n%s\n]\n", getData().stream().map(
                o -> String.format("\t{\n%s\n\t}", getProps(o))
        ).collect(Collectors.joining(",\n")));
    }

    static String getProps(HashMap<String, String> o) {
        return o.keySet().stream().map(
                k -> String.format("\t\t\"%s\" : \"%s\"", k, o.get(k))
        ).collect(Collectors.joining(",\n"));
    }

    static ArrayList<HashMap<String, String>> getData() {
        var list = new ArrayList<HashMap<String, String>>();
        list.add(getObject("Olympia", "WA"));
        list.add(getObject("Salem", "OR"));
        return list;
    }

    static Integer id = 1;

    static HashMap<String, String> getObject(String city, String state) {
        var o = new HashMap<String, String>();
        o.put("ID", id.toString());
        o.put("CITY", city);
        o.put("STATE", state);
        id++;
        return o;
    }
}
