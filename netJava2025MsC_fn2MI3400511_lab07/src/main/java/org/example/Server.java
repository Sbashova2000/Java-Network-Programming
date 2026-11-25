package org.example;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private Socket clientSocket = null;
    private ServerSocket server = null;
    private DataInputStream in = null;

    public Server(int port) {
        try {
            server = new ServerSocket(port);
            System.out.println("Server started....");
            System.out.println("Waiting for a client to connect....");

            //The accept() method blocks(just sits there) until a client connects to the server.
            clientSocket = server.accept();
            System.out.println("Client connected");

            //take input from the client socket
            in = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));

            //reads message from client until "END" is sent
            String line = "";
            while (!line.equals("END")) {
                try {
                    line = in.readUTF();
                    System.out.println(line);
                }
                catch (IOException i) {
                    System.out.println(i);
                }
            }

            System.out.println("Connection is closed");
            clientSocket.close();
            in.close();

        }
        catch (IOException e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        Server server = new Server(7050);
    }
}