/*
let stompClient = null;
let currentRoom = null;
let username = null;
let joinedRooms = [];
let currentRoom = null;

const el = (id) => document.getElementById(id);

const statusEl = el("status");
const logEl = el("log");
const privateLogEl = el("privateLog");
const usersEl = el("users");
const roomBadgeEl = el("roomBadge");

function now() {
  return new Date().toLocaleTimeString();
}

function escapeHtml(str) {
  return String(str)
    .replaceAll("&", "&amp;")
    .replaceAll("<", "&lt;")
    .replaceAll(">", "&gt;")
    .replaceAll('"', "&quot;")
    .replaceAll("'", "&#039;");
}

function appendChatLine(container, from, content, ts = now()) {
  const row = document.createElement("div");

  const isMe = (from === username) || (String(from).startsWith("private to "));
  row.className = "msg-row " + (isMe ? "me" : "other");

  row.innerHTML =
    `<span class="msg-user">${escapeHtml(from)}:</span> ` +
    `<span class="msg-text">${escapeHtml(content)}</span>` +
    `<div class="msg-time">${escapeHtml(ts)}</div>`;

  container.appendChild(row);
  container.scrollTop = container.scrollHeight;
}

function appendSystemLine(text, ts = now()) {
  const row = document.createElement("div");
  row.className = "msg-system";
  row.textContent = `[${ts}] ${text}`;
  logEl.appendChild(row);
  logEl.scrollTop = logEl.scrollHeight;
}

function setStatus(text) {
  statusEl.textContent = text;
}

function renderUsers(users) {
  usersEl.innerHTML = "";
  [...users].sort().forEach((u) => {
    const li = document.createElement("li");
    li.textContent = u;
    usersEl.appendChild(li);
  });
}

function connect() {
  username = el("username").value.trim();
  currentRoom = (el("room").value || "general").trim();

  if (!username) {
    alert("Моля, въведи username");
    return;
  }
  if (!currentRoom) currentRoom = "general";

  roomBadgeEl.textContent = currentRoom;

  const socket = new SockJS(`/ws?username=${encodeURIComponent(username)}`);
  stompClient = Stomp.over(socket);
  stompClient.debug = null;

  setStatus("Connecting...");

  stompClient.connect(
    {},
    () => {
      setStatus(`Connected as ${username}`);
      el("connectBtn").disabled = true;
      el("disconnectBtn").disabled = false;
      el("sendBtn").disabled = false;
      el("switchRoomBtn").disabled = false;
      el("pmSendBtn").disabled = false;

      subscribeRoom(currentRoom);
      subscribePrivate();
      joinRoom(currentRoom);
    },
    (err) => {
      setStatus("Connection error");
      console.error(err);
    }
  );
}

function disconnect() {
  if (!stompClient) return;

  if (currentRoom) leaveRoom(currentRoom);

  stompClient.disconnect(() => {
    setStatus("Disconnected");
    stompClient = null;

    el("connectBtn").disabled = false;
    el("disconnectBtn").disabled = true;
    el("sendBtn").disabled = true;
    el("switchRoomBtn").disabled = true;
    el("pmSendBtn").disabled = true;

    renderUsers([]);
  });
}

function subscribeRoom(room) {
  // room messages
  stompClient.subscribe(`/topic/rooms/${room}`, (frame) => {
    const msg = JSON.parse(frame.body);

    if (msg.type === "CHAT") {
      appendChatLine(logEl, msg.from, msg.content);
    } else if (msg.type === "JOIN") {
      appendSystemLine(`+ ${msg.from} joined ${msg.room}`);
    } else if (msg.type === "LEAVE") {
      appendSystemLine(`- ${msg.from} left ${msg.room}`);
    }
  });

  // users list
  stompClient.subscribe(`/topic/rooms/${room}/users`, (frame) => {
    const payload = JSON.parse(frame.body);
    renderUsers(payload.users || []);
  });

  // history
  stompClient.subscribe(`/user/queue/history`, (frame) => {
    const payload = JSON.parse(frame.body);
    if (payload.room !== currentRoom) return;

    appendSystemLine(`--- history for room: ${payload.room} ---`);
    (payload.messages || []).forEach((m) => {
      if (m.type === "CHAT") {
        const ts = m.timestamp ? new Date(m.timestamp).toLocaleTimeString() : now();
        appendChatLine(logEl, m.from, m.content, ts);
      }
    });
    appendSystemLine(`--- end history ---`);
  });
}

function subscribePrivate() {
  stompClient.subscribe(`/user/queue/private`, (frame) => {
    const msg = JSON.parse(frame.body);
    const label = (msg.from === username)
      ? `private to ${msg.to}`
      : `private from ${msg.from}`;

    appendChatLine(privateLogEl, label, msg.content);
  });
}

function joinRoom(room) {

  // ако стаята не е вече добавена
  if (!joinedRooms.includes(room)) {
    joinedRooms.push(room);
    updateRoomDropdown();
  }

  stompClient.send("/app/chat.join", {}, JSON.stringify({ room }));
}

function leaveRoom(room) {
  stompClient.send("/app/chat.leave", {}, JSON.stringify({ room }));
}
function updateRoomDropdown() {

  const select = document.getElementById("roomSelect");
  select.innerHTML = "";

  joinedRooms.forEach(room => {
    const option = document.createElement("option");
    option.value = room;
    option.textContent = room;
    select.appendChild(option);
  });

  select.disabled = joinedRooms.length === 0;
}

function switchRoom() {
  // взима избраната стая от dropdown менюто
  const select = document.getElementById("roomSelect");
  const newRoom = (select?.value || "").trim();

  if (!newRoom) {
    alert("Izberi staq ot menuto (roomSelect).");
    return;
  }

  if (newRoom === currentRoom) return;

  leaveRoom(currentRoom);

  currentRoom = newRoom;
  roomBadgeEl.textContent = currentRoom;

  appendSystemLine(`=== switched to room: ${currentRoom} ===`);
  subscribeRoom(currentRoom);
  joinRoom(currentRoom); // ще добави стаята в joinedRooms ако не е добавена
}


function sendRoomMessage() {
  const text = el("message").value;
  if (!text.trim()) return;

  stompClient.send("/app/chat.send", {}, JSON.stringify({
    room: currentRoom,
    content: text
  }));

  el("message").value = "";
}

function sendPrivate() {
  const to = el("pmTo").value.trim();
  const content = el("pmText").value;

  if (!to) { alert("Към кого? (username)"); return; }
  if (!content.trim()) return;

  stompClient.send("/app/chat.private", {}, JSON.stringify({ to, content }));
  el("pmText").value = "";
}

// bindings
el("connectBtn").addEventListener("click", connect);
el("disconnectBtn").addEventListener("click", disconnect);

el("sendBtn").addEventListener("click", sendRoomMessage);
el("message").addEventListener("keydown", (e) => {
  if (e.key === "Enter") sendRoomMessage();
});

el("switchRoomBtn").addEventListener("click", switchRoom);

el("clearBtn").addEventListener("click", () => {
  logEl.innerHTML = "";
  privateLogEl.innerHTML = "";
});

el("pmSendBtn").addEventListener("click", sendPrivate);
el("pmText").addEventListener("keydown", (e) => {
  if (e.key === "Enter") sendPrivate();
});
*/
let stompClient = null;
let currentRoom = null;
let username = null;

// rooms list for dropdown
let joinedRooms = [];

const el = (id) => document.getElementById(id);

const statusEl = el("status");
const logEl = el("log");
const privateLogEl = el("privateLog");
const usersEl = el("users");
const roomBadgeEl = el("roomBadge");

// dropdown button + menu
const switchBtn = el("switchRoomBtn");
const roomMenu = el("roomMenu");

function now() {
  return new Date().toLocaleTimeString();
}

function escapeHtml(str) {
  return String(str)
    .replaceAll("&", "&amp;")
    .replaceAll("<", "&lt;")
    .replaceAll(">", "&gt;")
    .replaceAll('"', "&quot;")
    .replaceAll("'", "&#039;");
}

function appendChatLine(container, from, content, ts = now()) {
  const row = document.createElement("div");

  const isMe = (from === username) || (String(from).startsWith("private to "));
  row.className = "msg-row " + (isMe ? "me" : "other");

  row.innerHTML =
    `<span class="msg-user">${escapeHtml(from)}:</span> ` +
    `<span class="msg-text">${escapeHtml(content)}</span>` +
    `<div class="msg-time">${escapeHtml(ts)}</div>`;

  container.appendChild(row);
  container.scrollTop = container.scrollHeight;
}

function appendSystemLine(text, ts = now()) {
  const row = document.createElement("div");
  row.className = "msg-system";
  row.textContent = `[${ts}] ${text}`;
  logEl.appendChild(row);
  logEl.scrollTop = logEl.scrollHeight;
}

function setStatus(text) {
  statusEl.textContent = text;
}

function renderUsers(users) {
  usersEl.innerHTML = "";
  [...users].sort().forEach((u) => {
    const li = document.createElement("li");
    li.textContent = u;
    usersEl.appendChild(li);
  });
}

/* ---------------------------
   Dropdown UI (Switch room ▼)
--------------------------- */

function toggleRoomMenu(open) {
  if (!roomMenu) return;
  const shouldOpen = typeof open === "boolean" ? open : roomMenu.classList.toggle("open");
  roomMenu.classList.toggle("open", shouldOpen);
}

function closeRoomMenu() {
  toggleRoomMenu(false);
}

function updateRoomDropdown() {
  if (!roomMenu) return;

  roomMenu.innerHTML = "";

  // enable/disable switch button depending on rooms
  if (switchBtn) switchBtn.disabled = joinedRooms.length === 0;

  // build menu items
  [...joinedRooms].sort().forEach((room) => {
    const item = document.createElement("div");
    item.className = "dropdown-item";
    item.textContent = room;

    // highlight current room
    if (room === currentRoom) item.classList.add("active");

    item.addEventListener("click", () => {
      closeRoomMenu();
      switchToRoom(room);
    });

    roomMenu.appendChild(item);
  });
}

// switch logic when picking from dropdown
function switchToRoom(newRoom) {
  const room = (newRoom || "").trim();
  if (!room) return;
  if (room === currentRoom) return;

  if (currentRoom) leaveRoom(currentRoom);

  currentRoom = room;
  roomBadgeEl.textContent = currentRoom;

  appendSystemLine(`=== switched to room: ${currentRoom} ===`);

  subscribeRoom(currentRoom);
  joinRoom(currentRoom); // adds to joinedRooms if missing + sends join
  updateRoomDropdown();
}

/* ---------------------------
   Connection / STOMP
--------------------------- */

function connect() {
  username = el("username").value.trim();
  currentRoom = (el("room").value || "general").trim();

  if (!username) {
    alert("Моля, въведи username");
    return;
  }
  if (!currentRoom) currentRoom = "general";

  roomBadgeEl.textContent = currentRoom;

  const socket = new SockJS(`/ws?username=${encodeURIComponent(username)}`);
  stompClient = Stomp.over(socket);
  stompClient.debug = null;

  setStatus("Connecting...");

  stompClient.connect(
    {},
    () => {
      setStatus(`Connected as ${username}`);

      el("connectBtn").disabled = true;
      el("disconnectBtn").disabled = false;
      el("sendBtn").disabled = false;

      // switchBtn exists and becomes enabled when we have joined rooms
      if (switchBtn) switchBtn.disabled = false;

      el("pmSendBtn").disabled = false;

      subscribeRoom(currentRoom);
      subscribePrivate();
      joinRoom(currentRoom);
      updateRoomDropdown();
    },
    (err) => {
      setStatus("Connection error");
      console.error(err);
    }
  );
}

function disconnect() {
  if (!stompClient) return;

  if (currentRoom) leaveRoom(currentRoom);

  stompClient.disconnect(() => {
    setStatus("Disconnected");
    stompClient = null;

    el("connectBtn").disabled = false;
    el("disconnectBtn").disabled = true;
    el("sendBtn").disabled = true;

    if (switchBtn) switchBtn.disabled = true;

    el("pmSendBtn").disabled = true;

    renderUsers([]);

    // reset rooms dropdown
    joinedRooms = [];
    updateRoomDropdown();
    closeRoomMenu();
  });
}

function subscribeRoom(room) {
  // room messages
  stompClient.subscribe(`/topic/rooms/${room}`, (frame) => {
    const msg = JSON.parse(frame.body);

    if (msg.type === "CHAT") {
      appendChatLine(logEl, msg.from, msg.content);
    } else if (msg.type === "JOIN") {
      appendSystemLine(`+ ${msg.from} joined ${msg.room}`);
    } else if (msg.type === "LEAVE") {
      appendSystemLine(`- ${msg.from} left ${msg.room}`);
    }
  });

  // users list
  stompClient.subscribe(`/topic/rooms/${room}/users`, (frame) => {
    const payload = JSON.parse(frame.body);
    renderUsers(payload.users || []);
  });

  // history (per user)
  stompClient.subscribe(`/user/queue/history`, (frame) => {
    const payload = JSON.parse(frame.body);
    if (payload.room !== currentRoom) return;

    appendSystemLine(`--- history for room: ${payload.room} ---`);
    (payload.messages || []).forEach((m) => {
      if (m.type === "CHAT") {
        const ts = m.timestamp ? new Date(m.timestamp).toLocaleTimeString() : now();
        appendChatLine(logEl, m.from, m.content, ts);
      }
    });
    appendSystemLine(`--- end history ---`);
  });
}

function subscribePrivate() {
  stompClient.subscribe(`/user/queue/private`, (frame) => {
    const msg = JSON.parse(frame.body);
    const label =
      (msg.from === username)
        ? `private to ${msg.to}`
        : `private from ${msg.from}`;

    appendChatLine(privateLogEl, label, msg.content);
  });
}

function joinRoom(room) {
  const r = (room || "").trim();
  if (!r) return;

  // add to local joined rooms list for dropdown
  if (!joinedRooms.includes(r)) {
    joinedRooms.push(r);
    updateRoomDropdown();
  }

  stompClient.send("/app/chat.join", {}, JSON.stringify({ room: r }));
}

function leaveRoom(room) {
  const r = (room || "").trim();
  if (!r) return;
  stompClient.send("/app/chat.leave", {}, JSON.stringify({ room: r }));
}

/* ---------------------------
   Sending messages
--------------------------- */

function sendRoomMessage() {
  const text = el("message").value;
  if (!text.trim()) return;

  stompClient.send(
    "/app/chat.send",
    {},
    JSON.stringify({ room: currentRoom, content: text })
  );

  el("message").value = "";
}

function sendPrivate() {
  const to = el("pmTo").value.trim();
  const content = el("pmText").value;

  if (!to) { alert("Към кого? (username)"); return; }
  if (!content.trim()) return;

  stompClient.send("/app/chat.private", {}, JSON.stringify({ to, content }));
  el("pmText").value = "";
}

/* ---------------------------
   Bindings
--------------------------- */

el("connectBtn").addEventListener("click", connect);
el("disconnectBtn").addEventListener("click", disconnect);

el("sendBtn").addEventListener("click", sendRoomMessage);
el("message").addEventListener("keydown", (e) => {
  if (e.key === "Enter") sendRoomMessage();
});

if (switchBtn) {
  // click opens/closes dropdown menu
  switchBtn.addEventListener("click", (e) => {
    e.preventDefault();
    if (switchBtn.disabled) return;
    toggleRoomMenu();
  });
}

// click outside closes menu
document.addEventListener("click", (e) => {
  if (!roomMenu || !switchBtn) return;
  const inside = roomMenu.contains(e.target) || switchBtn.contains(e.target);
  if (!inside) closeRoomMenu();
});

el("clearBtn").addEventListener("click", () => {
  logEl.innerHTML = "";
  privateLogEl.innerHTML = "";
});

el("pmSendBtn").addEventListener("click", sendPrivate);
el("pmText").addEventListener("keydown", (e) => {
  if (e.key === "Enter") sendPrivate();
});
