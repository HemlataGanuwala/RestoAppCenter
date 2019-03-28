package com.flavourheight.apple.skyrestaurantapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.flavourheight.apple.skyrestaurantapp.Adapter.OrderAdapter;
import com.flavourheight.apple.skyrestaurantapp.Adapter.OrderDetailsAdapter;
import com.flavourheight.apple.skyrestaurantapp.Model.OrderDetailsPlanet;
import com.flavourheight.apple.skyrestaurantapp.Model.OrderPlanet;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailsActivity extends AppCompatActivity {

    List<OrderDetailsPlanet> mPlanetlist = new ArrayList<OrderDetailsPlanet>();
    OrderDetailsAdapter adapter;
    RecyclerView recyclerView;
    ProgressDialog progress;
    ServiceHandler shh;
    String path,subitem,rate,totalamt,totalcnt, user,pass,orderdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        path = globalVariable.getconstr();
        user = globalVariable.getUsername();
        pass = globalVariable.getloginPassword();

        recyclerView = (RecyclerView)findViewById(R.id.recycleorderdetail);
        recyclerView.setLayoutManager(new LinearLayoutManager(OrderDetailsActivity.this));

        Display();

        new getOrderDetails().execute();
    }

    public void Display()
    {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle!=null)
        {
            orderdt = (String)bundle.get("OrderDt");
        }
    }

    class getOrderDetails extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(OrderDetailsActivity.this);
            progress.setMessage("Loading...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setProgress(0);
            progress.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            shh = new ServiceHandler();
            String url = path + "OrderDetails/GetOrderDetails";
            Log.d("Url: ", "> " + url);

            try {
                List<NameValuePair> params2 = new ArrayList<>();
                params2.add(new BasicNameValuePair("UserName", user));
                params2.add(new BasicNameValuePair("OrderDate", orderdt));

                String jsonStr = shh.makeServiceCall(url, ServiceHandler.POST, params2);

                if (jsonStr != null) {
                    JSONObject c1 = new JSONObject(jsonStr);
                    JSONArray classArray = c1.getJSONArray("response");
                    for (int i = 0; i < c1.length(); i++) {
                        JSONObject a1 = classArray.getJSONObject(i);
                        subitem = a1.getString("productName");
                        rate = a1.getString("productCost");
                        totalcnt = a1.getString("totalCount");
                        totalamt = a1.getString("totalAmount");

                        OrderDetailsPlanet planet1 = new OrderDetailsPlanet(subitem, rate, totalcnt, totalamt);
                        mPlanetlist.add(planet1);
                    }

                } else {
                    //Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progress.dismiss();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter = new OrderDetailsAdapter(mPlanetlist);
                    recyclerView.setAdapter(adapter);
                }
            });

        }
    }
}
