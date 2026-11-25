package org.example;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class Client {
    private Socket socket = null;
    private DataInputStream input = null;
    private DataOutputStream out = null;

    public Client(String address, int port) {
        try {

            socket = new Socket(address, port);
            System.out.println("Connected to server");

            System.out.println("Please write something, the server is listening..");
            //take input from terminal
            input = new DataInputStream(System.in);

            //sends the output to the socket
            out = new DataOutputStream(socket.getOutputStream());

            //string to read the message from input
            String line = "";

            while (!line.equals("END")) {
                try {
                    line = input.readLine();
                    out.writeUTF(line);
                }
                catch (Exception e) {
                    System.out.println(e);
                }
            }

            input.close();
            out.close();

            socket.close();
            System.out.println("Disconnected from the server");

        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        Client client = new Client("127.0.0.1", 7050);
    }

}
