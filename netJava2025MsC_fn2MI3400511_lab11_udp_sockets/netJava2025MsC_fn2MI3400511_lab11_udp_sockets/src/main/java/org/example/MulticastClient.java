package org.example;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastClient {

    public static void main(String[] args) {

        final String MULTICAST_IP = "230.0.0.1";
        final int PORT = 50000;

        try (MulticastSocket socket = new MulticastSocket(PORT)) {

            InetAddress group = InetAddress.getByName(MULTICAST_IP);
            socket.joinGroup(group);

            System.out.println("Client joined multicast group...");

            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket packet =
                        new DatagramPacket(buffer, buffer.length);

                socket.receive(packet);

                String message = new String(
                        packet.getData(),
                        0,
                        packet.getLength()
                );

                System.out.println("Received: " + message);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
