package com.softwaremobility.networkconnection;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.softwaremobility.network.Connection;
import com.softwaremobility.simplehttp.NetworkConnection;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_connection);

        NetworkConnection.testPath("http://54.190.16.14:3001/api");

        Uri uri = Uri.parse("airlines");

        NetworkConnection.with(this).withListener(new NetworkConnection.ResponseListener() {
            @Override
            public void onSuccessfullyResponse(String response) {
                // code when call is successfully
            }

            @Override
            public void onErrorResponse(String error, String message, int code) {
                // code if error happened.
            }
        }).doRequest(Connection.REQUEST.GET,uri,null,null,null);
    }
}
