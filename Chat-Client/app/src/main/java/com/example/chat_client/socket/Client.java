package com.example.chat_client.socket;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

    private static Client client = null;

    private Socket socket;
    private BufferedReader input;
    private boolean isSocketRunning = false;
    private final MutableLiveData<String> responseMessage = new MutableLiveData<>();

    public static final String TAG = "ClientSocket";
    public static final String IP_ADDRESS = "192.168.1.16";
    public static final int PORT = 8080;

    private Client() {
        connect();
    }

    public synchronized static Client getInstance() {
        if (client == null) {
            client = new Client();
        }
        return client;
    }

    private void connect() {
        Thread thread = new Thread(() -> {
            try {
                // Establish connection
                Log.d(TAG, "Connecting to server...");
                InetAddress serverAddress = InetAddress.getByName(IP_ADDRESS);
                socket = new Socket(serverAddress, PORT);
                Log.d(TAG, "Connected to server!");
                Log.d(TAG, "Server address: " + serverAddress.toString());

                // Listening for response from server
                try {
                    input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    listeningForResponse();
                } catch (Exception e) {
                    Log.d(TAG, "Error getting input stream: " + e.getMessage());
                    e.printStackTrace();
                }
            } catch (Exception e) {
                Log.d(TAG, "Error unknown: " + e.getMessage());
                e.printStackTrace();
            }
        });
        thread.start();
    }

    public void sendMessage(final String message) {
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

    private void listeningForResponse() {
        isSocketRunning = true;
        Log.d(TAG, "Listening for server response...");
        while (isSocketRunning) {
            try {
                String message = input.readLine();
                if (message != null) {
                    Log.d(TAG, "Server response: " + message);
                    //responseMessage.setValue(message);
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

    public void closeSocket() {
        try {
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LiveData<String> responseMessageLiveData() {
        return responseMessage;
    }
}
