package com.hoy.utilities;

import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * http://hc.apache.org/httpclient-3.x/preference-api.html
 *
 * @author LDicesaro
 */
public class RestClient {
    private static final String TAG = RestClient.class.getSimpleName();
    /**
     * Set the timeout in milliseconds until a connection is established.<br>
     * The default value is zero, that means the timeout is not used.
     */
    public static final int CONNECTION_TIMEOUT = 10000;

    /**
     * Set the default socket timeout (SO_TIMEOUT) in milliseconds which is the timeout for waiting for data.
     */
    public static final int SOCKET_TIMEOUT = 0;

    /**
     * This is a test function which will connects to a given rest service and prints it's response to Android Log with labels
     * "Praeda".
     */
    public static String executeHttpGetRequest(String url) {

        long t = 0;
        String resultString = null;
        try {
            HttpParams httpParameters = setTimeoutParameters();
            DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
            if (ProxyUtils.HAS_TO_PROXY) {
                ProxyUtils.setProxy(httpclient, url);
            }

            // Prepare a request object
            // Las urls GET deben ser del tipo http://hostname/app/action?param1=value1&param2=value2

            url = url.replace(" ", "%20").replace(">", "%3E");
            //Log.i(TAG, url);
            HttpGet httpGet = new HttpGet(url);

            httpGet.setHeader("Accept", "application/json");
            httpGet.setHeader("Content-type", "application/json");
            // httpGet.setHeader("Cookie", "JSESSIONID=" + sessionId);
            t = System.currentTimeMillis();
            HttpResponse response = httpclient.execute(httpGet);
            Log.i(TAG, "HTTPResponse received in [" + (System.currentTimeMillis() - t) + "ms]");

            resultString = parseResponse(response);

        } catch (Exception e) {
            parseException(t, e);
        }
        return resultString;
    }

    public static String executeHttpPostRequest(String url, String jsonString) {

        long t = 0;
        String resultString = null;
        try {
            HttpParams httpParameters = setTimeoutParameters();
            DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
            if (ProxyUtils.HAS_TO_PROXY) {
                ProxyUtils.setProxy(httpclient, url);
            }

            HttpPost httpPostRequest = new HttpPost(url);

            if (jsonString != null) {
                // Set HTTP parameters
                httpPostRequest.setEntity(new StringEntity(jsonString, HTTP.UTF_8));
            }
            httpPostRequest.setHeader("Accept", "application/json");

// 2012-08-18: Se cambio el Content-type del Header del RestClient, de "application/json" a "application/x-www-form-urlencoded",
//          porque en el Tomcat de Mochahost tira 403 server error.
//			httpPostRequest.setHeader("Content-type", "application/json");
            httpPostRequest.setHeader("Content-type", "application/x-www-form-urlencoded");
            // if (accessToken != null && accessToken.length() > 0) {
            // httpPostRequest.setHeader("Cookie", "JSESSIONID=" + accessToken);
            // }
            // only set this parameter if you would like to use gzip compression
            // httpPostRequest.setHeader("Accept-Encoding", "gzip");
            Log.d(TAG, jsonString);
            t = System.currentTimeMillis();
            HttpResponse response = httpclient.execute(httpPostRequest);
            Log.i(TAG, "HTTPResponse received in [" + (System.currentTimeMillis() - t) + "ms]");

            resultString = parseResponse(response);

        } catch (Exception e) {
            parseException(t, e);
        }
        return resultString;
    }

    private static HttpParams setTimeoutParameters() {
        HttpParams httpParameters = new BasicHttpParams();
        int timeoutConnection = CONNECTION_TIMEOUT;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        int timeoutSocket = SOCKET_TIMEOUT;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        return httpParameters;
    }

    private static String parseResponse(HttpResponse response) throws IOException {

        // Examine the response status
        Log.i(TAG, response.getStatusLine().toString());

        String resultString = null;
        // Get hold of the response entity (-> the data):
        HttpEntity entity = response.getEntity();

        if (entity != null) {
            // Read the content stream
            InputStream instream = entity.getContent();
            // Header contentEncoding = response.getFirstHeader("Content-Encoding");
            // if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
            // instream = new GZIPInputStream(instream);
            // }

            // convert content stream to a String
            resultString = convertStreamToString(instream);
            //Log.d(TAG, resultString);

            instream.close();
        }
        return resultString;
    }

    private static void parseException(long t, Exception e) {
        Log.i(TAG, "HTTPResponse received in [" + (System.currentTimeMillis() - t) + "ms]");
        if (e.getMessage() != null) {
            Log.e(TAG, e.getMessage());
        }
        // More about HTTP exception handling in another tutorial.
        // For now we just print the stack trace.
        e.printStackTrace();
    }

    /**
     * To convert the InputStream to String we use the BufferedReader.readLine() method.<br>
     * We iterate until the BufferedReader return null which means there's no more data to read.<br>
     * Each line will appended to a StringBuilder and returned as String.<br>
     */
    private static String convertStreamToString(InputStream is) {

        // BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        /**
         * The BufferedReader class provides buffering to your Reader's. Buffering can speed up IO quite a bit. Rather than read<br>
         * one character at a time from the network or disk, you read a larger block at a time. This is typically much faster,<br>
         * especially for disk access and larger data amounts.<br>
         * Source: http://www.anddev.org/default_buffer_size_in_bufferedreader-t9910.html<br>
         */
        // To avoid this warning:
        // Default buffer size used in BufferedReader constructor.
        // It would be better to be explicit if an 8k-char buffer is required.
        // Source: http://stackoverflow.com/questions/3881652/android-send-mail-application
        BufferedReader reader = new BufferedReader(new InputStreamReader(is), 2 * 1024); // ( set to 2K in this case)

        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
            }
        }

        return sb.toString();
    }
}
