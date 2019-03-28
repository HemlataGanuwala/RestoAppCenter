package com.flavourheight.apple.skyrestaurantapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.flavourheight.apple.skyrestaurantapp.Adapter.FavouriteAdapter;
import com.flavourheight.apple.skyrestaurantapp.Adapter.FavouriteAdapterOff;
import com.flavourheight.apple.skyrestaurantapp.Model.FavouritePlanet;

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
    String path, username, itemname, subitemname, rate, image, pass;
    ServiceHandler shh;
    int Status = 1;
    ProgressDialog progress;
    ImageView imageViewback;
    LinearLayout linearLayout, linearLayoutfav;
    DatabaseHelpher databaseHelpher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favorites);

        databaseHelpher = new DatabaseHelpher(this);

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        path = globalVariable.getconstr();
        username = globalVariable.getUsername();
        pass = globalVariable.getloginPassword();

        imageViewback = (ImageView) findViewById(R.id.imgfavouriteback);
        linearLayout = (LinearLayout) findViewById(R.id.linerlayout);
        linearLayoutfav = (LinearLayout) findViewById(R.id.linearlayoutfab);

        imageViewback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyFavoritesActivity.this, MyAccountActivity.class);
                startActivity(intent);
                finish();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclefavourite);
        recyclerView.setLayoutManager(new LinearLayoutManager(MyFavoritesActivity.this));

        mPlanetList.clear();
        new getFavouriteItem().execute();

//        if (username != null && pass != null) {
//            mPlanetList = databaseHelpher.getFavouritelist(username,pass);
//            adapter = new FavouriteAdapterOff(mPlanetList);
//            recyclerView.setAdapter(adapter);
////            new getFavouriteItem().execute();
//            linearLayout.setVisibility(View.GONE);
//            linearLayoutfav.setVisibility(View.VISIBLE);
//        }else{
//            linearLayoutfav.setVisibility(View.GONE);
//            linearLayout.setVisibility(View.VISIBLE);
//        }




    }

    public class getFavouriteItem extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=new ProgressDialog(MyFavoritesActivity.this);
            progress.setMessage("Loading...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setProgress(0);
            progress.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            shh= new ServiceHandler();
            String url= path + "Favourites/GetFavouriteData";
            Log.d("Url",">"+url);

            try {
                List<NameValuePair> params2 = new ArrayList<>();

                params2.add(new BasicNameValuePair("UserName", username));
                String jsonStr = shh.makeServiceCall(url, ServiceHandler.POST, params2);

                if (jsonStr != null) {
                    JSONObject c1 = new JSONObject(jsonStr);
                    JSONArray classArray = c1.getJSONArray("response");

                    for (int i =0; i< classArray.length();i++){
                        JSONObject a1 = classArray.getJSONObject(i);
                        itemname = a1.getString("productTypeName");
                        subitemname = a1.getString("productName");
                        rate = a1.getString("productCost");
                        image = a1.getString("productImage");

                       FavouritePlanet planet = new FavouritePlanet(itemname, subitemname, rate, image);
                       mPlanetList.add(planet);
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

            progress.dismiss();

            if (Status == 1)
            {
                linearLayout.setVisibility(View.GONE);
                linearLayoutfav.setVisibility(View.VISIBLE);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter=new FavouriteAdapter(mPlanetList);
                        recyclerView.setAdapter(adapter);

                    }
                });

            }else {
                linearLayoutfav.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
            }


        }
    }


}
