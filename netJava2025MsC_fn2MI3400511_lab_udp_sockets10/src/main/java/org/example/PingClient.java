package org.example;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class PingClient {

    public static void main(String[] args) {
        final int PORT = 9876;
        final String HOST = "localhost";
        final int TIMEOUT = 10000; // 10 секунди
        final int MAX_TRIES = 4;

        int sent = 0;
        int received = 0;

        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(TIMEOUT);

            InetAddress serverAddress = InetAddress.getByName(HOST);

            for (int i = 1; i <= MAX_TRIES; i++) {
                sent++;

                byte[] data = "iPING!".getBytes();
                DatagramPacket packet =
                        new DatagramPacket(
                                data,
                                data.length,
                                serverAddress,
                                PORT
                        );

                long startTime = System.currentTimeMillis();
                socket.send(packet);
                System.out.println("Ping #" + i + " sent");

                try {
                    byte[] buffer = new byte[1024];
                    DatagramPacket response =
                            new DatagramPacket(buffer, buffer.length);

                    socket.receive(response); // блокира до 10 сек

                    long endTime = System.currentTimeMillis();
                    long rtt = endTime - startTime;

                    String message = new String(
                            response.getData(),
                            0,
                            response.getLength()
                    );

                    System.out.println(
                            "Reply received: " + message +
                                    " | RTT = " + rtt + " ms"
                    );
                    received++;

                } catch (SocketTimeoutException e) {
                    System.out.println("Timeout, no reply.");
                }
            }

            System.out.println("\n--- STATISTICS ---");
            System.out.println("Sent: " + sent);
            System.out.println("Received: " + received);
            System.out.println("Lost: " + (sent - received));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
