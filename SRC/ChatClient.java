import java.io.*;
import java.net.*;
import java.util.Scanner;
import javax.crypto.SecretKey;

public class ChatClient {
    public static void main(String[] args) {
        String host = "localhost";  // Server address
        int port = 6000;            // Same port as the server

        try (Socket socket = new Socket(host, port)) {
            // Set up I/O streams
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // For demo purposes, generate an AES key for this session
            SecretKey aesKey = CryptoUtils.generateAESKey();
            System.out.println("AES key generated for this session.");

            // Create a thread to listen for incoming messages
            new Thread(() -> {
                String incoming;
                try {
                    while ((incoming = in.readLine()) != null) {
                        // Decrypt the incoming message
                        String decrypted = CryptoUtils.aesDecrypt(incoming, aesKey);
                        System.out.println("Decrypted message: " + decrypted);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

            // Main thread: read user input, encrypt it, and send to server
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter messages to send (type 'exit' to quit):");
            while (true) {
                String message = scanner.nextLine();
                if ("exit".equalsIgnoreCase(message)) break;
                // Encrypt the message using AES
                String encryptedMessage = CryptoUtils.aesEncrypt(message, aesKey);
                out.println(encryptedMessage);
            }
            scanner.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
