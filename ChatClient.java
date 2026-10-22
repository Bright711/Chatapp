import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 5000;

    public static void main(String[] args) {

        System.out.println("=== ONLINE CHAT APPLICATION ===");

        try (
            Socket socket = new Socket(
                    SERVER_ADDRESS, SERVER_PORT);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            PrintWriter out = new PrintWriter(
                    socket.getOutputStream(), true);

            Scanner keyboard = new Scanner(System.in)
        ) {

            System.out.println("Connected to the chat server.");

            // Read the server's welcome messages.
            System.out.println(in.readLine());
            System.out.println(in.readLine());

            // Receive messages independently of keyboard input.
            Thread receiver = new Thread(() -> {
                try {
                    String message;

                    while ((message = in.readLine()) != null) {
                        System.out.println("\n" + message);
                        System.out.print("Message: ");
                    }

                } catch (IOException e) {
                    System.out.println(
                            "\nConnection to server lost.");
                }
            });

            receiver.setDaemon(true);
            receiver.start();

            System.out.println("\nStart chatting below.");

            // Read and send messages typed by the user.
            while (true) {
                System.out.print("Message: ");

                if (!keyboard.hasNextLine()) {
                    break;
                }

                String message = keyboard.nextLine();

                if (message.trim().isEmpty()) {
                    continue;
                }

                out.println(message);

                if (message.equalsIgnoreCase("/quit")) {
                    break;
                }
            }

        } catch (IOException e) {
            System.out.println(
                    "Could not connect to the server.");
            System.out.println(
                    "Make sure ChatServer is running on port "
                    + SERVER_PORT + ".");
        }

        System.out.println("Chat application closed.");
    }
}




