package com.example.myapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.TextView;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xml.sax.XMLReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.net.URL;

/**
 * Created by Martin on 11/8/2014.
 */
public class OpenUrl extends AsyncTask<String, Void, Void>
{
    private final HttpClient Client = new DefaultHttpClient();
    public static TextView text;
    public static String Content;
    private String Error = null;
        @Override
        protected void onPostExecute(Void unused) {;
            text.setText(Content);
        }

        @Override
        protected Void doInBackground(String... url) {
            try {
                HttpGet httpget = new HttpGet(url[0]);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                Content = Client.execute(httpget, responseHandler);
            } catch (ClientProtocolException e) {
                Error = e.getMessage();
                cancel(true);
            } catch (IOException e) {
                Error = e.getMessage();
                cancel(true);
            }

            return null;
        }
}