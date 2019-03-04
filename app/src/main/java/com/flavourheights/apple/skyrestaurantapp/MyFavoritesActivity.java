package com.flavourheights.apple.skyrestaurantapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.flavourheights.apple.skyrestaurantapp.Adapter.CartAdapter;
import com.flavourheights.apple.skyrestaurantapp.Adapter.FavouriteAdapter;
import com.flavourheights.apple.skyrestaurantapp.Model.CartListPlanet;
import com.flavourheights.apple.skyrestaurantapp.Model.FavouritePlanet;
import com.flavourheights.apple.skyrestaurantapp.Model.ItemPlanet;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyFavoritesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FavouriteAdapter adapter;
    List<FavouritePlanet> mPlanetList = new ArrayList<FavouritePlanet>();
    String path, username, itemname, subitemname, rate, image;
    ServiceHandler shh;
    int Status = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favorites);

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        path = globalVariable.getconstr();
        username = globalVariable.getUsername();

        recyclerView = (RecyclerView) findViewById(R.id.recyclefavourite);
        recyclerView.setLayoutManager(new LinearLayoutManager(MyFavoritesActivity.this));
    }

    public class addCartItem extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            shh= new ServiceHandler();
            String url= path + "Cart/GetCartItems";
            Log.d("Url",">"+url);

            try {
                List<NameValuePair> params2 = new ArrayList<>();
                params2.add(new BasicNameValuePair("UserName", username));


                String jsonStr = shh.makeServiceCall(url, ServiceHandler.POST, params2);

                if (jsonStr != null) {
                    JSONObject c1 = new JSONObject(jsonStr);
                    JSONArray classArray = c1.getJSONArray("Response");

                    for (int i =0; i< classArray.length();i++){
                        JSONObject a1 = classArray.getJSONObject(i);
                        itemname = a1.getString("ItemName");
                        subitemname = a1.getString("SubItemName");
                        rate = a1.getString("ItemRate");
                        image = a1.getString("ListImg");

//                        FavouritePlanet planet1 = new ItemPlanet(itemname,subitemname,rate,image);
//                        mPlanetList.add(planet1);

                    }
                }
            }catch (JSONException e){
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter=new FavouriteAdapter(mPlanetList);
                    recyclerView.setAdapter(adapter);

                }
            });

        }
    }
}
