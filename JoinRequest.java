package org.example.websocketchat.model;

public class JoinRequest {
    private String room;

    public JoinRequest() {}
    public JoinRequest(String room) { this.room = room; }

    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }
}
