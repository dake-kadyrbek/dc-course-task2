package org.example;

import java.net.Socket;

public class Input {
    public Socket socket;
    public HttpRequest request;

    public Input(Socket socket, HttpRequest request){
        this.socket = socket;
        this.request = request;
    }
}
