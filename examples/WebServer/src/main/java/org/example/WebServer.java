package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Simple web server.
 */
public class WebServer {
    public static void main(String[] args) {
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 8081;

        int queueLength = args.length > 2 ? Integer.parseInt(args[2]) : 50;;

        try (ServerSocket serverSocket = new ServerSocket(port, queueLength)) {
            System.out.println("Web Server is starting up, listening at port " + port + ".");
            System.out.println("You can access http://localhost:" + port + " now.");

            while (true) {
                Socket socket = serverSocket.accept();

                System.out.println("Got connection!");

                BufferedReader input = new BufferedReader(
                        new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));

                try {
                    HttpRequest request = HttpRequest.parse(input);

                    Processor proc = new Processor(socket, request);

                    proc.start();

                    ThreadPool threadPool = new ThreadPool(5, 15);

                    for(int i=0; i<15; i++) {

                        int taskNo = i;
                        threadPool.execute( () -> {
                            String message =
                                    Thread.currentThread().getName()
                                            + ": Task " + taskNo ;
                            System.out.println(message);
                        });
                    }

                    threadPool.waitUntilAllTasksFinished();
                    threadPool.stop();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        finally {
            System.out.println("Server has been shutdown!");
        }
    }
}
