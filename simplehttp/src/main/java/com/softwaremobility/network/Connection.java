package com.softwaremobility.network;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import com.softwaremobility.simplehttp.BuildConfig;
import com.softwaremobility.simplehttp.NetworkConnection;
import com.softwaremobility.utilities.MultiPartUtility;
import com.softwaremobility.utilities.Utils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class Connection extends AsyncTask<Void,Void,Boolean>{

    private final String LOG_TAG = Connection.class.getSimpleName();
    private String JSONString, endpoint;
    private Map<String,String> params,headers;
    private JSONObject object;
    private REQUEST requestType;
    private ConnectionListener listener;
    private Context context;
    private int code;
    private String message;
    private String error;
    private byte [] image;

    public Connection(Context context, String endpoint, REQUEST requestType, @Nullable Map<String,String> params, @Nullable Map<String,String> headers, @Nullable JSONObject object){
        this.endpoint = endpoint;
        this.requestType = requestType;
        if (params != null) this.params = params;
        if (headers != null) this.headers = headers;
        if (object != null) this.object = object;
        this.context = context;
    }

    public Connection(Context context, String endpoint, REQUEST requestType, @Nullable Map<String,String> params, @Nullable Map<String,String> headers, @Nullable JSONObject object, @Nullable byte [] image){
        this.endpoint = endpoint;
        this.requestType = requestType;
        if (params != null) this.params = params;
        if (headers != null) this.headers = headers;
        if (object != null) this.object = object;
        if (image != null) this.image = image;

        this.context = context;
    }

    public void setListener(ConnectionListener listener) {
        this.listener = listener;
    }

    @Override
    protected Boolean doInBackground(Void... par) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        JSONString = null;

        try {

            urlConnection = getStructuredRequest(endpoint,requestType,params,headers,object,image);

            assert urlConnection != null;
            InputStream is = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (is == null) {
                //Nothing to do
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }
            if (buffer.length() == 0)
                return null;
            JSONString = buffer.toString();
            Log.d(LOG_TAG, "Server Response: " + JSONString);
            return true;

        }catch (FileNotFoundException e){
            manageError(e,urlConnection);
            return false;
        }catch (IOException e) {
            manageError(e,urlConnection);
            return false;
        } catch (Exception e) {
            manageError(e,urlConnection);
            return false;
        } finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if(reader != null){
                try {
                    reader.close();
                }catch (IOException e){
                    Log.e(LOG_TAG,"Error Closing Stream",e);
                }
            }
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (result){
            if (listener != null){
                listener.successfullyResponse(JSONString);
            }
        }else{
            if (listener != null){
                listener.errorResponse(error,message,code);
            }
        }
    }

    private HttpURLConnection getStructuredRequest(String endpoint, REQUEST type, @Nullable Map<String,String> params, @Nullable Map<String,String> headers, @Nullable JSONObject object, @Nullable byte [] image) throws Exception{
        HttpURLConnection urlConnection = null;
        URL url = null;
        boolean isTest = BuildConfig.DEBUG;
        Uri.Builder builderPath = Uri.parse("").buildUpon();
        if (NetworkConnection.getProductionPath() != null && NetworkConnection.getProductionPath().equalsIgnoreCase("")) {
            builderPath = isTest ? Uri.parse(NetworkConnection.getTestPath()).buildUpon() :
                    Uri.parse(NetworkConnection.getProductionPath()).buildUpon();
        }
        builderPath.appendPath(endpoint);
        String charset = "UTF-8";
        if (type == REQUEST.GET) { //----------------------------- GET ------------------------------------
            if (params != null) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    builderPath.appendQueryParameter(entry.getKey(), entry.getValue());
                }
            }
            url = new URL(builderPath.build().toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(type.name());
            urlConnection = setHeaders(urlConnection,headers);
            urlConnection.connect();
        }else if (type == REQUEST.POST){ //------------------------ POST ----------------------------------
            url = new URL(builderPath.build().toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(type.name());
            urlConnection = setHeaders(urlConnection,headers);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            if (object != null){ // A JSON object will be send it.
                urlConnection.connect();
                DataOutputStream dataOutputStream = new DataOutputStream(urlConnection.getOutputStream());
                dataOutputStream.write(object.toString().getBytes());
                dataOutputStream.flush();
                dataOutputStream.close();
            }else { // if there is no JSON object will create the request with encoded url params
                Uri.Builder builder = new Uri.Builder();
                if (params != null){

                    for (Map.Entry<String,String> entry : params.entrySet()){
                        builder.appendQueryParameter(entry.getKey(),entry.getValue());
                    }
                    String query = builder.build().getEncodedQuery();
                    OutputStream os = urlConnection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, charset));
                    writer.write(query);
                    writer.flush();
                    writer.close();
                    urlConnection.connect();
                }
                if(image != null){
                    MultiPartUtility multipart = new MultiPartUtility(urlConnection, charset);
                    multipart.addPhoto("image1", image);
                    urlConnection = multipart.finish();
                }
            }
        }else if(type == REQUEST.PUT){ //------------------------ PUT ----------------------------------
            url = new URL(builderPath.build().toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(type.name());
            urlConnection = setHeaders(urlConnection,headers);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            if (object != null){ // A JSON object will be send it.
                urlConnection.connect();
                DataOutputStream dataOutputStream = new DataOutputStream(urlConnection.getOutputStream());
                dataOutputStream.write(object.toString().getBytes());
                dataOutputStream.flush();
                dataOutputStream.close();
            }else {
                Uri.Builder builder = new Uri.Builder();
                if (params != null){

                    for (Map.Entry<String,String> entry : params.entrySet()){
                        builder.appendQueryParameter(entry.getKey(),entry.getValue());
                    }
                    String query = builder.build().getEncodedQuery();
                    OutputStream os = urlConnection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, charset));
                    writer.write(query);
                    writer.flush();
                    writer.close();
                    urlConnection.connect();
                }
            }
        }else if(type == REQUEST.DELETE){ //------------------------ DELETE ----------------------------------
            url = new URL(builderPath.build().toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(type.name());
            urlConnection = setHeaders(urlConnection,headers);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            if (object != null){ // A JSON object will be send it.
                urlConnection.connect();
                DataOutputStream dataOutputStream = new DataOutputStream(urlConnection.getOutputStream());
                dataOutputStream.write(object.toString().getBytes());
                dataOutputStream.flush();
                dataOutputStream.close();
            }
        }
        Log.d(LOG_TAG,url.toString());
        return urlConnection;
    }

    private HttpURLConnection setHeaders(HttpURLConnection urlConnection, @Nullable Map<String,String> headers){
        if (headers != null && urlConnection != null){
            for (Map.Entry<String,String> entry : headers.entrySet()){
                urlConnection.setRequestProperty(entry.getKey(),entry.getValue());
            }
        }
        return urlConnection;
    }

    private void manageError(Exception e, HttpURLConnection urlConnection){
        if (Utils.isNetworkAvailable(context)) {
            if (urlConnection != null) {
                try {
                    code = urlConnection.getResponseCode();
                    if (urlConnection.getErrorStream() != null) {
                        InputStream is = urlConnection.getErrorStream();
                        StringBuilder buffer = new StringBuilder();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            buffer.append(line + "\n");
                        }
                        message = buffer.toString();
                    } else {
                        message = urlConnection.getResponseMessage();
                    }
                    error = urlConnection.getErrorStream().toString();
                    Log.e(LOG_TAG, "Error: " + message + ", code: " + code);
                } catch (IOException e1) {
                    e1.printStackTrace();
                    Log.e(LOG_TAG, "Error");
                }
            }else {
                code = 105;
                message = "Error: No internet connection";
                Log.e(LOG_TAG, "code: " + code + ", " + message);
            }
        }else{
            code = 105;
            message = "Error: No internet connection";
            Log.e(LOG_TAG, "code: " + code + ", " + message);
        }
    }

    public  interface ConnectionListener{
        void successfullyResponse(String JSONStr);
        void errorResponse(String error, String message, int codeError);
    }

    public enum REQUEST{
        POST,GET,PUT,DELETE
    }
}
