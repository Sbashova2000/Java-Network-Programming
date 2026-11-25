package org.example;

public class TestThread {

    public static void main(String[] args) {
        IntThread thread1 = new IntThread(1);
        IntThread thread2 = new IntThread(2);
        IntThread thread3 = new IntThread(3);

        thread1.start();
        thread2.start();
        thread3.start();
    }


}