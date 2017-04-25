package com.softwaremobility.simplehttp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;
import com.softwaremobility.network.Connection;
import org.json.JSONObject;
import java.util.Map;

/**
 * Created by darkgeat on 5/24/16.
 */
public class NetworkConnection implements Connection.ConnectionListener{

    @SuppressLint("StaticFieldLeak")
    private static NetworkConnection ourInstance = null;
    private static final String TAG = NetworkConnection.class.getSimpleName();
    private ResponseListener responseListener = null;
    private String PRODUCTION_PATH = null;
    private String TEST_PATH = null;
    private Context context;
    private boolean isDecodedUrl = false;

    private NetworkConnection() {}

    private synchronized static void createInstance(){
        if (ourInstance == null){
            ourInstance = new NetworkConnection();
        }
    }

    private static NetworkConnection getInstance(){
        if (ourInstance == null) createInstance();
        return ourInstance;
    }

    public static NetworkConnection with(Context c){
        getInstance().setContext(c);
        return getInstance();
    }

    public NetworkConnection withListener(ResponseListener listener){
        getInstance().setResponseListener(listener);
        return getInstance();
    }

    public static NetworkConnection useDecodedUTFInUrl(boolean value){
        getInstance().isDecodedUrl = value;
        return getInstance();
    }

    public void doRequest(Connection.REQUEST method, final Uri uri, @Nullable final Map<String, String> params, @Nullable final Map<String, String> headers, @Nullable JSONObject jsonObject){
        Connection connection = new Connection(context,uri.toString(),method,params,headers,jsonObject);
        connection.setDecodedUrlInUTF(isDecodedUrl);
        connection.setListener(this);
        connection.execute();
    }

    public void doRequest(Connection.REQUEST method, final Uri uri, @Nullable final Map<String, String> params, @Nullable final Map<String, String> headers, @Nullable JSONObject jsonObject, @Nullable byte [] image){
        Connection connection = new Connection(context,uri.toString(),method,params,headers,jsonObject,image);
        connection.setDecodedUrlInUTF(isDecodedUrl);
        connection.setListener(this);
        connection.execute();
    }

    private void setContext(Context context) {
        this.context = context;
    }

    private void setResponseListener(ResponseListener responseListener) {
        this.responseListener = responseListener;
    }

    public static String getProductionPath() {
        return getInstance().PRODUCTION_PATH;
    }

    public static NetworkConnection productionPath(String productionPath) {
        getInstance().PRODUCTION_PATH = productionPath;
        return getInstance();
    }

    public static String getTestPath() {
        return getInstance().TEST_PATH;
    }

    public static NetworkConnection testPath(String testPath) {
        getInstance().TEST_PATH = testPath;
        return getInstance();
    }

    public interface ResponseListener{
        void onSuccessfullyResponse(String response);
        void onErrorResponse(String error, String message, int code);
    }

    @Override
    public void successfullyResponse(String JSONStr) {
        if (responseListener != null){
            responseListener.onSuccessfullyResponse(JSONStr);
        }else {
            Log.e(TAG,"Error: No listener for the response");
        }
    }

    @Override
    public void errorResponse(String error, String message, int codeError) {
        if (responseListener != null){
            responseListener.onErrorResponse(error,message,codeError);
        }else {
            Log.e(TAG,"Error: No listener for the response");
        }
    }
}
