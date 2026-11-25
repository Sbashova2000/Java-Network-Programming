package org.example.websocketchat.model;

import java.time.Instant;

public class ChatMessage {
    public enum Type { CHAT, JOIN, LEAVE, PRIVATE }

    private Type type;
    private String room;      // за CHAT/JOIN/LEAVE
    private String from;
    private String to;        // за PRIVATE
    private String content;
    private Instant timestamp;

    public ChatMessage() {}

    public ChatMessage(Type type, String room, String from, String to, String content, Instant timestamp) {
        this.type = type;
        this.room = room;
        this.from = from;
        this.to = to;
        this.content = content;
        this.timestamp = timestamp;
    }

    public Type getType() { return type; }
    public void setType(Type type) { this.type = type; }

    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }

    public String getFrom() { return from; }
    public void setFrom(String from) { this.from = from; }

    public String getTo() { return to; }
    public void setTo(String to) { this.to = to; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
}
