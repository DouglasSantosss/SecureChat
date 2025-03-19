import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


// Import your cryptographic utilities (make sure you have CryptoUtils.java implemented as shown earlier)
 
public class ChatClientGUI {
    private JFrame frame;
    private JTextArea chatArea;
    private JTextField messageField;
    private JButton sendButton;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private SecretKey aesKey; // For encrypting/decrypting messages

    public ChatClientGUI() {
        // --- Set up the GUI ---
        frame = new JFrame("Secure Chat Client");
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        
        messageField = new JTextField(30);
        sendButton = new JButton("Send");

        // Create a panel for the chat area and input field
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(chatArea), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.add(messageField);
        inputPanel.add(sendButton);
        panel.add(inputPanel, BorderLayout.SOUTH);

        frame.getContentPane().add(panel);
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // --- Set up networking connection ---
        try {
            // Change host and port if needed – ensure a matching ChatServer is running
            socket = new Socket("localhost", 6000);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            // Generate an AES key for this session (in a real app, you’d exchange this securely)
            
            // A hardcoded key (must be 32 bytes for AES-256)
            byte[] keyBytes = "0123456789abcdef0123456789abcdef".getBytes();
            aesKey = new SecretKeySpec(keyBytes, "AES");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error connecting to server: " + e.getMessage());
            System.exit(1);
        }

        // --- Set up Action Listeners for sending messages ---
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        messageField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        // --- Start a thread to listen for incoming messages ---
        new Thread(new Runnable() {
            public void run() {
                listenForMessages();
            }
        }).start();
    }

    // Encrypts and sends the message
    private void sendMessage() {
        String message = messageField.getText().trim();
        if (!message.isEmpty()) {
            try {
                // Encrypt the message using AES (implemented in your CryptoUtils)
                String encryptedMessage = CryptoUtils.aesEncrypt(message, aesKey);
                out.println(encryptedMessage);
                // Optionally, show your own sent message in the chat area
                chatArea.append("Me: " + message + "\n");
                messageField.setText("");
            } catch (Exception ex) {
                ex.printStackTrace();
                chatArea.append("Error sending message: " + ex.getMessage() + "\n");
            }
        }
    }

    // Continuously listens for incoming messages from the server
    private void listenForMessages() {
        String incoming;
        try {
            while ((incoming = in.readLine()) != null) {
                try {
                    // Decrypt the incoming message using AES
                    String decrypted = CryptoUtils.aesDecrypt(incoming, aesKey);
                    chatArea.append("Friend: " + decrypted + "\n");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    chatArea.append("Failed to decrypt message.\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            chatArea.append("Connection lost.\n");
        }
    }

    public static void main(String[] args) {
        // Use SwingUtilities.invokeLater for thread safety in Swing applications
        SwingUtilities.invokeLater(() -> new ChatClientGUI());
    }
}
