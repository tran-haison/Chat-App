package com.example.chat_client.socket;

import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.chat_client.models.User;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

    private static Client client = null;

    private Socket socket;
    private BufferedReader input;

    private final Handler handler = new Handler();
    private final MutableLiveData<String> responseMessage = new MutableLiveData<>();
    private final MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();

    public static final String TAG = "ClientSocket";
    public static final String IP_ADDRESS = "192.168.1.14";
    public static final int PORT = 54000;

    private Client() {
        connectToServer();
    }

    public synchronized static Client getInstance() {
        if (client == null) {
            client = new Client();
        }
        return client;
    }

    private void connectToServer() {
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

    private void listenForResponse() {
        Log.d(TAG, "Listening for server response...");

        int charsRead;
        char[] buffer = new char[1024];

        // Infinite loop listening for response from server
        while (true) {
            try {
                charsRead = input.read(buffer);
                String message = new String(buffer).substring(0, charsRead);
                Log.d(TAG, ">>>>>>>>>> Server response: " + message);
                handler.post(() -> responseMessage.setValue(message));
            } catch (Exception e) {
                Log.d(TAG, "Error: " + e.getMessage());
                closeSocket();
                break;
            }
        }
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

    public void closeSocket() {
        try {
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUser(User user) {
        this.userMutableLiveData.setValue(user);
    }

    public LiveData<User> userLiveData() {
        return userMutableLiveData;
    }

    public LiveData<String> responseMessageLiveData() {
        return responseMessage;
    }

}
