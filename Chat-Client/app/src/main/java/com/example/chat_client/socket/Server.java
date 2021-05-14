package com.example.chat_client.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {

    // Map to store clients in list of active clients
    // 2 levels of Map for quick searching when the number of clients increase
    public static Map<String, Integer> clientsNameToIndex = new HashMap<>();
    public static Map<Integer, ClientHandler> clientsIndexToObject = new HashMap<>();

    public static int numberOfClients = 0;
    public static final int PORT = 3000;

    public static void main(String[] args) throws IOException {
        // Server is listening on port 3000
        ServerSocket serverSocket = new ServerSocket(PORT);

        // Running infinite loop for getting client request
        while (true) {
            Socket socket = null;
            try {
                // Accept the incoming request
                socket = serverSocket.accept();
                System.out.println("New client connected: " + socket);

                // Create a new handler object for handling this request.
                System.out.println("Creating a new handler for this client...");
                String clientName = "Client " + numberOfClients;
                ClientHandler clientHandler = new ClientHandler(socket, clientName);

                // Create a new Thread with this object.
                Thread clientThread = new Thread(clientHandler);

                // Add this client to active clients list
                System.out.println("Adding this client to active clients list");
                clientsNameToIndex.put(clientName, numberOfClients);
                clientsIndexToObject.put(numberOfClients, clientHandler);

                // Start the thread.
                clientThread.start();

                // Increment numberOfClients for new client.
                numberOfClients++;
            } catch (Exception e) {
                assert socket != null;
                socket.close();
                e.printStackTrace();
            }
        }
    }
}
