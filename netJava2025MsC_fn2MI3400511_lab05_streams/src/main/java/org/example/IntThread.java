package org.example;

public class IntThread extends Thread{


    private int num;

    IntThread(int num){
        this.num = num;
    }

    @Override
    public void run() {
        for (int i = 0; i < 15; i++) {
            System.out.println("Thread " + num + " is running " + i);
        }
    }
}
