package com.example.chat_client.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;

public class ClientHandler implements Runnable {

    private final Socket socket;
    private final String name;
    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;
    private boolean isLoggedIn;

    public static final String LOG_OUT = "logout";
    public static final String DELIMITER = "#";

    public ClientHandler(Socket socket, String name) throws IOException {
        // Obtain input and output streams
        inputStream = new DataInputStream(socket.getInputStream());
        outputStream = new DataOutputStream(socket.getOutputStream());
        this.name = name;
        this.socket = socket;
        this.isLoggedIn = true;
    }

    @Override
    public void run() {
        String received;
        while (true) {
            try {
                // Receive the string and print to console
                received = inputStream.readUTF();
                System.out.println(received);

                // Check received string equals to "logout"
                if (received.equals(LOG_OUT)) {
                    this.isLoggedIn = false;
                    this.socket.close();
                    break;
                }

                // Break the string into message and recipient part
                StringTokenizer tokenizer = new StringTokenizer(received, DELIMITER);
                String msgToSend = tokenizer.nextToken();
                String recipient = tokenizer.nextToken();

                // Get the recipient in the connected devices hash map.
                int index = Server.clientsNameToIndex.get(recipient);
                ClientHandler clientHandler = Server.clientsIndexToObject.get(index);

                // if the recipient is found, write on its output stream
                if (clientHandler.name.equalsIgnoreCase(recipient) && clientHandler.isLoggedIn) {
                    clientHandler.outputStream.writeUTF(this.name + " : " + msgToSend);
                }
            } catch (IOException e) {
                try {
                    this.socket.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                e.printStackTrace();
            }
        }

        try {
            // Closing resources
            this.inputStream.close();
            this.outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
