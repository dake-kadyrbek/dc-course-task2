package org.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.File;

/**
 * Processor of HTTP request.
 */
public class Processor extends Thread{
    private final Socket socket;
    private final HttpRequest request;

    public Processor(Socket socket, HttpRequest request) {
        this.socket = socket;
        this.request = request;
    }

    @Override
    public void run(){
        try{
            process();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public void process() throws IOException {
        // Print request that we received.
        System.out.println("Got request:");
        System.out.println(request.toString());
        System.out.flush();

        PrintWriter output = new PrintWriter(socket.getOutputStream());

        output.println("HTTP/1.1 200 OK");
        output.println("Content-Type: text/html; charset=utf-8");
        output.println();
        output.println("<html>");
        output.println("<head><title>Hello</title></head>");
        output.println("<body><p>Hello, world!</p></body>");
        output.println("</html>");
        System.out.println("REQUEST: " + request.getRequestLine());

        if(request.getRequestLine().contains("create")){
            createTxt(request.getRequestLine());
            output.println();
            output.println("<html>");
            output.println("<head><title>CREATE</title></head>");
            output.println("<body><p>You have created a txt file!</p></body>");
            output.println("</html>");
        }
        else if(request.getRequestLine().contains("delete")){
            deleteTxt(request.getRequestLine());
            output.println();
            output.println("<html>");
            output.println("<head><title>DELETE</title></head>");
            output.println("<body><p>You have deleted a txt file!</p></body>");
            output.println("</html>");
        }
        else if(request.getRequestLine().contains("prime")){
            output.println();
            output.println("<html>");
            output.println("<head><title>PRIME</title></head>");
            output.println("<body><p>The sum of the prime numbers from 1 to 100000: " + primeSum() + "</p></body>");
            output.println("</html>");
        }

        output.flush();

        socket.close();
    }

    public void createTxt(String request){
        request = request.replaceAll("GET /create/", " ");
        request = request.replaceAll(".txt HTTP/1.1", " ");
        try {
            File myObj = new File(request +".txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());

            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    public void deleteTxt(String request){
        request = request.replaceAll("GET /delete/", " ");
        request = request.replaceAll(".txt HTTP/1.1", " ");
        File myObj = new File(request +".txt");
        if (myObj.delete()) {
            System.out.println("File deleted: " + myObj.getName());
        } else {
            System.out.println("File doesn't exist.");
        }
    }
    public int primeSum(){
        int i, number, count, sum = 0;

        for(number = 1; number <= 100000; number++) {
            count = 0;
            for (i = 2; i <= number/2; i++) {
                if(number % i == 0) {
                    count++;
                    break;
                }
            } if(count == 0 && number != 1 ) {
                sum = sum + number;
            }
        }
        return sum;

    }
}
