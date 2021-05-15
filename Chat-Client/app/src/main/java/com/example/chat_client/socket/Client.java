package com.example.chat_client.socket;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

    private static Client client = null;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private final MutableLiveData<String> responseMessage = new MutableLiveData<>();

    public static final String LOCAL_HOST = "127.0.0.1";
    public static final int PORT = 54000;

    private Client() {
        try {
            // Getting localhost ip
            //InetAddress ip = InetAddress.getByName(LOCAL_HOST);

            // Establish the connection
            Socket socket = new Socket(LOCAL_HOST, PORT);

            // Obtaining input and out streams
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
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

    public void listen() {
        Thread readMessage = new Thread(() -> {
            while (true) {
                try {
                    // read the message sent to this client
                    String msg = inputStream.readUTF();
                    responseMessage.postValue(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        readMessage.start();
    }

    public void send(final String message) {
        Thread sendMessage = new Thread(() -> {
            try {
                // write on the output stream
                outputStream.writeUTF(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        sendMessage.start();
    }

    public LiveData<String> responseMessageLiveData() {
        return responseMessage;
    }
}
