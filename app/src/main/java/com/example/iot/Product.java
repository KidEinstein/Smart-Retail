package com.example.iot;

import com.parse.GetCallback;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Product extends Activity {

    String productName = null;
    String price = null;
    String manufacturer = null;
    String quantity = null;

    TextView x;
    TextView y;
    TextView z;
    EditText w;

    Intent i;
    String value = null;

    SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        Bundle extras = getIntent().getExtras();


        if (extras != null) {
            value = extras.getString("id");
            Log.d("ID", value);
        }


	    /*new Thread(new Runnable() {
            public void run() {
            	makePostRequest();;
            }
        }).start();*/
        new request().execute();


    }


    public void addToCart(View v) {
        sharedpreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedpreferences.edit();


        w = (EditText) findViewById(R.id.QuantityR);
        quantity = w.getText().toString();
        editor.putString(value, quantity);
        editor.commit();
        i = new Intent(getBaseContext(), MainActivity.class);

        int temp = Integer.parseInt(price)*Integer.parseInt(quantity);
        i.putExtra("priceKey", Integer.toString(temp));
        Toast.makeText(this,"Added to Cart",Toast.LENGTH_LONG).show();
        startActivity(i);
    }

    class request extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            HttpClient httpClient = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost("http://192.168.43.63:8000/get_product/");

            HttpResponse response;

            String str = null; //store json string

            JSONObject json = null;

		       /* StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
		        .detectDiskWrites()
		        .detectNetwork()
		        .penaltyDeath()
		        .build());

		        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll()
		        .penaltyLog()
		        .build());
		         */

            //Post Data to server : sending product id
            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
            nameValuePair.add(new BasicNameValuePair("product_id", value));

            //Encoding POST data
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
            } catch (UnsupportedEncodingException e) {
                // log exception
                e.printStackTrace();
            }

            //making POST request and getting response
            try {
                response = httpClient.execute(httpPost);
                str = EntityUtils.toString(response.getEntity(), "UTF-8");
                Log.d("Http Post Response:", response.toString());
                Log.d("Http Post Response:", str);

            } catch (ClientProtocolException e) {
                // Log exception
                e.printStackTrace();
            } catch (IOException e) {
                // Log exception
                e.printStackTrace();
            }

            try {
                json = new JSONObject(str);
                productName = json.getString("product_name");
                //manufacturer = json.getString("Manufacturer");
                //y.setText(manufacturer);
                price = json.getString("product_price");
                manufacturer = json.getString("product_manufacturer");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    x = (TextView) findViewById(R.id.ProductNameR);
                    y = (TextView) findViewById(R.id.ManufacturerR);
                    z = (TextView) findViewById(R.id.PriceR);

                    x.setText(productName);
                    y.setText(manufacturer);
                    z.setText(price);
//stuff that updates ui

                }
            });


            return null;
        }

    }

}



