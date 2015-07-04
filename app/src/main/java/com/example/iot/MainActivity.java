package com.example.iot;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseClassName;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class MainActivity extends Activity {

	String AppID = "TiZzls763VvknejWaNMYOivYpqSR3v5LsPwQiLlr";
	String ClientID = "7rMoLh5ONPAq4663ou0DmkU2uQszwPG48HSPheEl";
	Intent i ;
	SharedPreferences sharedpreferences ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // Parse.enableLocalDatastore(this);
       // Parse.initialize(this, AppID, ClientID);
        
        
    }
    public void done(View v)
    {
    		
    		int max ,min;
    		max = 3 ;
    		min = 1;
    	  	Random rand = new Random();
    	    int num = rand.nextInt((max - min) + 1) + min;
    	    String value = Integer.toString(num);
    	    i = new Intent(this,Product.class);
    	    i.putExtra("id", value);
    		startActivity(i);
    		
    }
    
    public void submit(View v)
    {
        Log.d("inside submit function", "inside submit function");
        sharedpreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        Map<String, ?> allEntries = sharedpreferences.getAll();
        final List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
            nameValuePair.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
        }
        new Thread(new Runnable() {
            public void run() {
                makePostRequest(nameValuePair);
            }
        }).start();
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();
    }

    private void makePostRequest(List<NameValuePair> nameValuePair) {


        HttpClient httpClient = new DefaultHttpClient();

        HttpPost httpPost = new HttpPost("http://172.16.1.42:8000/submit_order/");


        //Post Data


        //Encoding POST data
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
        } catch (UnsupportedEncodingException e) {
            // log exception
            e.printStackTrace();
        }

        //making POST request.
        try {
            HttpResponse response = httpClient.execute(httpPost);
            Log.d("Http Post Response:", response.toString());

        } catch (ClientProtocolException e) {
            // Log exception
            e.printStackTrace();
        } catch (IOException e) {
            // Log exception
            e.printStackTrace();
        }

    }



}
