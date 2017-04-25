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

        NetworkConnection.testPath(getString(R.string.base_url));

        Uri uri = Uri.parse(getString(R.string.path_movie));
        Map<String, String> params = new HashMap<>();
        params.put(getString(R.string.param_api_key), getString(R.string.api_key));

        NetworkConnection.with(this).withListener(new NetworkConnection.ResponseListener() {
            @Override
            public void onSuccessfullyResponse(String response) {

            }

            @Override
            public void onErrorResponse(String error, String message, int code) {

            }
        }).doRequest(Connection.REQUEST.GET,uri,params,null,null);
    }
}
