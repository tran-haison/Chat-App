package com.example.chat_client.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

    public static final String LOCAL_HOST = "localhost";
    public static final int PORT = 3000;

    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    public Client() {
        // Obtaining input and out streams
        inputStream = new DataInputStream(socket.getInputStream());
        outputStream = new DataOutputStream(socket.getOutputStream());
    }

    public void connect() {
        try {
            // Getting localhost ip
            InetAddress ip = InetAddress.getByName(LOCAL_HOST);

            // Establish the connection
            Socket socket = new Socket(ip, PORT);



            // TODO: close resources
            // ...
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void request(final String message) {
        Thread sendMessage = new Thread(() -> {
            while (true) {
                // read the message to deliver.
                try {
                    // write on the output stream
                    outputStream.writeUTF(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        sendMessage.start();
    }

    public String response() {
        Thread readMessage = new Thread(() -> {
            while (true) {
                try {
                    // read the message sent to this client
                    String msg = inputStream.readUTF();
                    System.out.println(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        readMessage.start();
    }
}
