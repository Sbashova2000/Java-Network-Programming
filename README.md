# WebSocket Chat (Rooms, Online Users, History & Private Messages)

WebSocket Chat for Network programming in Java - FMI

## Basic components of a WebSocket chatRoom
- **Rooms (стаи)**: messages are broadcasted to `/topic/rooms/{room}`
- **Online users** (for each room): `/topic/rooms/{room}/users`
- **History** (for each room): when user JOIN the server is sending the last N messages to `/user/queue/history`
- **Private messages**: `/user/queue/private` + `convertAndSendToUser(...)`

## Used technologies
- Spring Boot 3 (WebSocket + STOMP)
- SockJS + stomp.js (в браузъра)
- Files history (JSONL) in directory `./data/`

## Starts the application
### 1)Requirements
- Java 17+
- Maven

### 2) Run
```bash
mvn spring-boot:run
```

### 3) Open
- http://localhost:8080

Open 2 tabs and log in with different usernames to test.

## The main idea: Username като Principal
Username is passed as a query parameter during the WebSocket handshake:

`/ws?username=pesho`

The server takes it and makes it a `Principal` so that `convertAndSendToUser(...)` can works.

## Paths 

Client -> Server (MessageMapping):
- `/app/chat.join`    { room }
- `/app/chat.leave`   { room }
- `/app/chat.send`    { room, content }
- `/app/chat.private` { to, content }

Server -> clients:
- `/topic/rooms/{room}`        (chat + join/leave events)
- `/topic/rooms/{room}/users`  (online users list)
- `/user/queue/history`        (history after join)
- `/user/queue/private`        (private messages)



Author: Sibel Bashova, 
    FN: 2MI3400511, 
course: II, 2026

project for: WebSocket Chat for Network programming in Java, 
Master of: Distributed systems and mobile technologies

