
# Online Chat Application

## 1. Project Description

This project is a simple online chat application developed
using Java socket programming. It allows multiple users to
connect to a central server, exchange text messages, and
receive messages from other connected users.

## 2. Features

- Multiple clients can connect to one server.
- Each connection receives a unique user ID.
- Messages are broadcast to all connected clients.
- Threads allow simultaneous communication.
- The client provides a text-based interface.
- Users can type /quit to leave the chat.
- The server removes disconnected clients.

## 3. Requirements

- Java Development Kit (JDK).
- A terminal or an IDE such as Visual Studio Code.
- Two or more terminal windows for testing multiple clients.

## 4. Project Structure

OnlineChatApplication/
├── ChatServer.java
├── ChatClient.java
└── README.md

## 5. How to Run

First, open a terminal in the project directory.

Compile the Java files:

    javac ChatServer.java ChatClient.java

Start the server in the first terminal:

    java ChatServer

Open another terminal and start the first client:

    java ChatClient

Open a third terminal and start another client:

    java ChatClient

Type a message in either client terminal to send it to
all connected clients.

Type /quit to leave the chat.

## 6. Implementation Details

ChatServer uses ServerSocket to listen for connections
and Socket to communicate with clients.

Each connected client is managed by a separate thread.
The server assigns unique IDs and stores active connections
in a thread-safe collection.

ChatClient connects to the server and uses input and output
streams to exchange messages. A receiving thread displays
incoming messages while the user types.

## 7. Limitations

This is an educational application. It does not include
user authentication, encrypted communication, persistent
message history, or permanent user accounts.

## 8. Conclusion

The project demonstrates Java socket programming,
client-server communication, multithreading, message
broadcasting, and a basic text-based user interface.
