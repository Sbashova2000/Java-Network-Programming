package org.example;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class PongServer {

    public static void main(String[] args) {
        final int PORT = 9876;
        int receivedPackets = 0;

        try (DatagramSocket socket = new DatagramSocket(PORT)) {
            System.out.println("PONG server started on port " + PORT);

            while (receivedPackets < 3) {
                byte[] buffer = new byte[1024];
                DatagramPacket packet =
                        new DatagramPacket(buffer, buffer.length);

                //waiting for a packet
                socket.receive(packet);
                receivedPackets++;

                // reading the message
                String message = new String(
                        packet.getData(),
                        0,
                        packet.getLength()
                );

                System.out.println("Received packet #" + receivedPackets +
                        " : " + message);

                if (message.equals("iPING!")) {
                    byte[] response = "iPONG!".getBytes();

                    DatagramPacket responsePacket =
                            new DatagramPacket(
                                    response,
                                    response.length,
                                    packet.getAddress(),
                                    packet.getPort()
                            );

                    socket.send(responsePacket);
                }
            }

            System.out.println("Server finished after 3 packets.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
