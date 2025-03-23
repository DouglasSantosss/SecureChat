# Secure Chat Application with End-to-End Encryption

## Overview
This project is a Java-based secure chat application that implements end-to-end encryption to protect message confidentiality. It uses RSA for secure key exchange and AES for encrypting and decrypting chat messages. The application features a user-friendly Swing-based GUI and utilizes Java Sockets for real-time communication between clients through a server.

## Features
- End-to-End Encryption:  
  Messages are encrypted on the client side using AES before being sent, ensuring that only the intended recipient can decrypt them.
- RSA Key Exchange:  
  Securely exchanges encryption keys using RSA, ensuring that the AES key remains confidential.
- Real-Time Chat:  
  Supports real-time communication via Java Sockets.
- Graphical User Interface (GUI):
  A simple Java Swing-based interface allows users to send and receive messages easily.
- Multithreading: 
  The server handles multiple client connections simultaneously, and the client listens for incoming messages on a separate thread.

## Technologies Used
- **Programming Language:** Java
- **Cryptography:** AES (for message encryption) and RSA (for key exchange)
- **Networking:** Java Sockets
- **GUI:** Java Swing

## Project Structure

## How to Run

### Prerequisites
- Ensure that the port configured in `ChatServer.java` (default: 5000) 

### Steps

1. Compile the Code:
   javac CryptoUtils.java ChatServer.java ChatClientGUI.java
