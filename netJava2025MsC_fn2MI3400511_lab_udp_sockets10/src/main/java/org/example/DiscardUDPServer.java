package org.example;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class DiscardUDPServer {

    public static void main(String[] args) {
        final int PORT = 9999;

        try (DatagramSocket socket = new DatagramSocket(PORT)) {
            System.out.println("Discard server running on port " + PORT);

            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket packet =
                        new DatagramPacket(buffer, buffer.length);

                socket.receive(packet);
                System.out.println("Packet discarded");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

