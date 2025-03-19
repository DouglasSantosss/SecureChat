import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    // List to keep track of connected client sockets
    private static List<Socket> clientSockets = new ArrayList<>();

    public static void main(String[] args) {
        int port = 6000;
  // Choose a port

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Chat Server started on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                clientSockets.add(clientSocket);
                System.out.println("New client connected: " + clientSocket.getInetAddress());

                // Handle client connection in a new thread
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Broadcast the received message to all connected clients
    private static void broadcastMessage(String message, Socket senderSocket) {
        for (Socket socket : clientSockets) {
            if (socket != senderSocket) {
                try {
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    out.println(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Handle communication with a client
    private static void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            String received;
            while ((received = in.readLine()) != null) {
                System.out.println("Received: " + received);
                // Broadcast the encrypted message to all clients
                broadcastMessage(received, clientSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            clientSockets.remove(clientSocket);
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
