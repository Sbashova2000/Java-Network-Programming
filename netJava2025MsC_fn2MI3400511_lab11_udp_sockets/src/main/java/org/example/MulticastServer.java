package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MulticastServer {

    public static void main(String[] args) {

        final String MULTICAST_IP = "230.0.0.1";
        final int PORT = 50000;

        try (DatagramSocket socket = new DatagramSocket();
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(
                             MulticastServer.class
                                     .getClassLoader()
                                     .getResourceAsStream("file.txt")
                     ))) {

            InetAddress group = InetAddress.getByName(MULTICAST_IP);
            String line;

            while ((line = reader.readLine()) != null) {

                byte[] data = line.getBytes();
                DatagramPacket packet =
                        new DatagramPacket(data, data.length, group, PORT);

                socket.send(packet);
                System.out.println("Sent: " + line);

                Thread.sleep(2000);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
