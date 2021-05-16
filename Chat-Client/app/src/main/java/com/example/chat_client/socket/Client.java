package com.example.chat_client.socket;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

    private static Client client = null;
    private ClientThread clientThread;
    private final MutableLiveData<String> responseMessage = new MutableLiveData<>();

    public static final String TAG = "ClientSocket";
    public static final String LOCAL_HOST = "192.168.1.16";
    public static final int PORT = 8080;

    public synchronized static Client getInstance() {
        if (client == null) {
            client = new Client();
        }
        return client;
    }

    private Client() {
        try {
            // Connect to server
            Log.d(TAG, "Connecting to server...");
            clientThread = new ClientThread();
            Thread thread = new Thread(clientThread);
            thread.start();
            Log.d(TAG, "Connected to server...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(final String message) {
        clientThread.sendMessage(message);
    }

    public LiveData<String> responseMessageLiveData() {
        return responseMessage;
    }

    private class ClientThread implements Runnable {

        private Socket socket;

        @Override
        public void run() {
            try {
                // Establish connection
                InetAddress serverAddress = InetAddress.getByName(LOCAL_HOST);
                socket = new Socket(serverAddress, PORT);
                Log.d(TAG, serverAddress.toString());

                // Listen for upcoming message
                while (!Thread.currentThread().isInterrupted()) {
                    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String message = input.readLine();
                    if (message == null || message.contentEquals("Disconnect")) {
                        Thread.interrupted();
                        message = "Server disconnected";
                        Log.d(TAG, message);
                        socket.close();
                        break;
                    }
                    Log.d(TAG, message);
                    responseMessage.setValue(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        void sendMessage(final String message) {
            Thread thread = new Thread(() -> {
                try {
                    if (socket != null) {
                        PrintWriter printWriter = new PrintWriter(new BufferedWriter(
                                new OutputStreamWriter(socket.getOutputStream())), true);
                        printWriter.println(message);
                        Log.d(TAG, message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            thread.start();
        }
    }
}
