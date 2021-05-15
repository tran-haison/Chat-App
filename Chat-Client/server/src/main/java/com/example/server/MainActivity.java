package com.example.server;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.server.socket.Server;

public class MainActivity extends AppCompatActivity {

    private Server server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Run server
        server = Server.getInstance();
    }
}