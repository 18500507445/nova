package com.nova.tools.java8.grow.jdk6;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;

/**
 * Http Server
 *
 * @author: wzh
 * @date: 2018/2/8
 */
class HttpServerAPI {

    private static int count = 0;

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange he) throws IOException {
            System.err.println("Request " + count++);
            System.err.println(he.getHttpContext().getPath());

            InputStream is = he.getRequestBody();
            String response = "<font color='red'>Lets Learn Java8.</font>";
            he.sendResponseHeaders(200, response.length());
            OutputStream os = he.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    public static void main(String[] args) {
        try {
            HttpServer hs = HttpServer.create(new InetSocketAddress(8080), 0);
            hs.createContext("/", new MyHandler());
            hs.createContext("/java", new MyHandler());
            hs.setExecutor(null);
            hs.start();
            System.err.println("---begin---");
            System.err.println("Listening on " + hs.getAddress());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

}  