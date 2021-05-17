package com.example.chat_client.socket;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.BufferedReader;
import java.io.DataInputStream;
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
    public static final String IP_ADDRESS = "192.168.1.14";
    public static final int PORT = 54000;

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

                // Listen for response from server
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                listenForResponse();
            } catch (Exception e) {
                Log.d(TAG, "Error establish connection: " + e.getMessage());
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

    private void listenForResponse() {
        isSocketRunning = true;
        Log.d(TAG, "Listening for server response...");
        int count = 0;
        while (isSocketRunning) {
            try {
                //input.readLine().wait();
                String message = input.readLine();
                if (message != null) {
                    Log.d(TAG, "Server response: " + message);
                    //responseMessage.setValue(message);
                } else {
                    Log.d(TAG, "Server response nothing!");
                }
                Log.d(TAG, "Counting: " + count++);
                Thread.sleep(1000);
            } catch (Exception e) {
                isSocketRunning = false;
                Log.d(TAG, "Error: " + e.getMessage());
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
