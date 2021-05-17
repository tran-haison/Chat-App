package com.example.chat_client.socket;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

    private static Client client = null;
    private ClientThread clientThread;
    private final MutableLiveData<String> responseMessage = new MutableLiveData<>();

    public static final String TAG = "ClientSocket";
    public static final String IP_ADDRESS = "192.168.1.14";
    public static final int PORT = 54000;

    private Client() {
        try {
            // Connect to server
            Log.d(TAG, "Connecting to server...");
            clientThread = new ClientThread();
            Thread thread = new Thread(clientThread);
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized static Client getInstance() {
        if (client == null) {
            client = new Client();
        }
        return client;
    }

    public void sendMessage(final String message) {
        clientThread.sendMessage(message);
    }

    public void closeSocket() {
        clientThread.closeSocket();
    }

    public LiveData<String> responseMessageLiveData() {
        return responseMessage;
    }

    private class ClientThread implements Runnable {

        private Socket socket;
        private BufferedReader input;
        private boolean isSocketRunning = false;

        @Override
        public void run() {
            isSocketRunning = true;
            try {
                // Establish connection
                InetAddress serverAddress = InetAddress.getByName(IP_ADDRESS);
                socket = new Socket(serverAddress, PORT);
                Log.d(TAG, "Connected to server...");
                Log.d(TAG, "Server address: " + serverAddress.toString());

                // Listen for upcoming message
                receiveMessage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        void sendMessage(final String message) {
            Thread thread = new Thread(() -> {
                try {
                    if (socket != null) {
                        // Send message to server
                        PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
                        printWriter.write(message);
                        printWriter.flush();
                        Log.d(TAG, message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            thread.start();
        }

        void receiveMessage() {
            // Get input stream from socket
            try {
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (Exception e) {
                Log.d(TAG, "Error getting input stream: " + e.getMessage());
                e.printStackTrace();
            }

            Log.d(TAG, "Listening for server response...");
            while (isSocketRunning) {
                try {
                    String message = input.readLine();
                    if (message != null) {
                        responseMessage.setValue(message);
                        Log.d(TAG, "Server response: " + message);
                    } else {
                        Log.d(TAG, "Server response nothing!");
                    }
                } catch (Exception e) {
                    Log.d(TAG, "Error receiving from server: " + e.getMessage());
                    e.printStackTrace();
                    break;
                }
            }
        }

        void closeSocket() {
            try {
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
