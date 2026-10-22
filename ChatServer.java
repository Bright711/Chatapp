
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ChatServer manages client connections and broadcasts messages.
 */
public class ChatServer {

    private static final int PORT = 5000;

    // Thread-safe collection of connected clients.
    private static final Set<ClientHandler> clients =
            ConcurrentHashMap.newKeySet();

    // Generates a unique ID for each connection.
    private static final AtomicInteger nextId = new AtomicInteger(1);

    public static void main(String[] args) {

        System.out.println("Starting Chat Server...");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            System.out.println("Server is running on port " + PORT);
            System.out.println("Waiting for clients...");

            // Keep accepting new clients.
            while (true) {
                Socket socket = serverSocket.accept();

                // Assign a unique ID to the new client.
                String userId = "User-" + nextId.getAndIncrement();

                ClientHandler client =
                        new ClientHandler(socket, userId);

                clients.add(client);

                // Handle this client independently.
                Thread thread = new Thread(client);
                thread.start();

                System.out.println(userId + " connected.");
                broadcast(userId + " has joined the chat.");
            }

        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }

    /**
     * Sends a message to all connected clients.
     */
    private static void broadcast(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    /**
     * Handles communication with one client.
     */
    private static class ClientHandler implements Runnable {

        private final Socket socket;
        private final String userId;
        private PrintWriter out;

        public ClientHandler(Socket socket, String userId) {
            this.socket = socket;
            this.userId = userId;
        }

        @Override
        public void run() {

            try (
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()))
            ) {

                out = new PrintWriter(socket.getOutputStream(), true);

                // Tell the client its assigned ID.
                out.println("Your assigned ID is " + userId);
                out.println("Type /quit to leave the chat.");

                String message;

                // Read messages until the client disconnects.
                while ((message = in.readLine()) != null) {

                    if (message.equalsIgnoreCase("/quit")) {
                        break;
                    }

                    if (!message.trim().isEmpty()) {
                        String formattedMessage =
                                userId + ": " + message;

                        System.out.println(formattedMessage);
                        broadcast(formattedMessage);
                    }
                }

            } catch (IOException e) {
                System.out.println(
                        userId + " disconnected unexpectedly.");
            } finally {

                // Remove the client and release its resources.
                clients.remove(this);

                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println(
                            "Error closing connection for " + userId);
                }

                System.out.println(userId + " left the chat.");
                broadcast(userId + " has left the chat.");
            }
        }

        /**
         * Sends a message safely to this client.
         */
        public synchronized void sendMessage(String message) {
            if (out != null) {
                out.println(message);
            }
        }
    }
}
