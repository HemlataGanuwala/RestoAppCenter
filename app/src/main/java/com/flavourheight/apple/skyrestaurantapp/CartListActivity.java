package com.flavourheight.apple.skyrestaurantapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.flavourheight.apple.skyrestaurantapp.Adapter.CardAdapterOff;
import com.flavourheight.apple.skyrestaurantapp.Adapter.CartAdapter;
import com.flavourheight.apple.skyrestaurantapp.Adapter.EditCartAdapter;
import com.flavourheight.apple.skyrestaurantapp.Model.CartListPlanet;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CartListActivity extends AppCompatActivity implements CartAdapter.OnItemClickListner, SwipeRefreshLayout.OnRefreshListener {

    ImageView imageViewback,imageViewedit;
    NavigationView navigationView;
    private SharedPreferences preferences;
    private DrawerLayout drawerLayout;
    Button buttonorder;
    CartListPlanet mPlanet;
    View headerview;
    ImageView imageViewshow;
    Class fragmentClass;
    private static final String PREFS_NAME = "PrefsFile";
    Fragment fragment = null;
    RecyclerView recyclerView;
    String subitem,path,user,pass, mobileno,editcount,totamt,tAmt;
    ServiceHandler shh;
    List<CartListPlanet> mPlanetlist= new ArrayList<CartListPlanet>();
    CartAdapter adapter;
    CardAdapterOff adapterOff;
    int ammount, totalamount,totalamt;
    TextView textViewtotlcost;
    int rate,totcount,totalcost;
    GlobalClass globalVariable;
    SwipeRefreshLayout swipeRefreshLayout;
    DatabaseHelpher databaseHelpher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);

        AppCenter.start(getApplication(), "ccbb2947-e93e-4088-b3d4-1b5a184e936d",
                Analytics.class, Crashes.class);

        databaseHelpher = new DatabaseHelpher(this);
        globalVariable = (GlobalClass) getApplicationContext();
        path = globalVariable.getconstr();
        user = globalVariable.getUsername();
        pass = globalVariable.getloginPassword();
        mobileno = globalVariable.getMobileNo();

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipecartlist);
        swipeRefreshLayout.setOnRefreshListener(this);

        textViewtotlcost=(TextView)findViewById(R.id.tvtotal_cost);

        recyclerView=(RecyclerView)findViewById(R.id.recyclecartlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(CartListActivity.this));

//        Display();
        mPlanetlist.clear();
//        mPlanetlist = databaseHelpher.getcardlist(user);
//        adapterOff = new CardAdapterOff(mPlanetlist);
//        recyclerView.setAdapter(adapter);

        new addCartItem().execute();
//        new Handler().postDelayed(new Runnable() {
//            public void run() {
//                // call JSON methods here
//
//            }
//        }, 3000  );

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbarcartlist);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        buttonorder=(Button)findViewById(R.id.btnorder);
        buttonorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CartListActivity.this, PlaceOrderActivity.class);
                intent.putExtra("Cost", String.valueOf(totalamount));
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });

//        preferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_cart_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                Intent intent1 = new Intent(CartListActivity.this, MainActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent1);
                return true;

            case R.id.cartpencile:
                Intent intent = new Intent(CartListActivity.this, EditCartActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
//        if (item.getItemId() == android.R.id.home) {
//            finish(); // close this activity and return to preview activity (if there is any)
//        }


    }

    public void Display()
    {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null)
        {
            if (bundle != null)
            {
                tAmt = (String)bundle.get("Totalamt");
            }
            textViewtotlcost.setText(tAmt);

        }


    }



    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onRefresh() {
        mPlanetlist.clear();
        new ReloadCartItem().execute();
        swipeRefreshLayout.setRefreshing(false);
    }


    public class addCartItem extends AsyncTask<String, String, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            shh= new ServiceHandler();
            String url= path + "Carts/GetCartItems";
            Log.d("Url",">"+url);

            try {
                List<NameValuePair> params2 = new ArrayList<>();
                params2.add(new BasicNameValuePair("UserName", user));
//                params2.add(new BasicNameValuePair("Username", mobileno));
//                params2.add(new BasicNameValuePair("Password", pass));

                String jsonStr = shh.makeServiceCall(url, ServiceHandler.POST, params2);

                if (jsonStr != null) {
                    JSONObject c1 = new JSONObject(jsonStr);
                    JSONArray classArray = c1.getJSONArray("response");

                    for (int i =0; i< classArray.length();i++){
                        JSONObject a1 = classArray.getJSONObject(i);
                        subitem=a1.getString("productName");
                        rate=a1.getInt("productCost");
                        totcount = a1.getInt("totalCount");
                        totalcost = rate * totcount;

                        CartListPlanet planet = new CartListPlanet(subitem, String.valueOf(rate),String.valueOf(totalcost),String.valueOf(totcount));
                        mPlanetlist.add(planet);

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
                    adapter=new CartAdapter(mPlanetlist);
                   recyclerView.setAdapter(adapter);

                }
            });

            int t1 = recyclerView.getAdapter().getItemCount();
            for (int i=0; i<t1; i++)
            {
                ammount = Integer.parseInt(mPlanetlist.get(i).getTotalCost());
                totalamount = totalamount + ammount;
            }
            textViewtotlcost.setText(String.valueOf(totalamount));
            globalVariable.setOrderCost(String.valueOf(totalamount));
        }
    }

    public class ReloadCartItem extends AsyncTask<String, String, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            shh= new ServiceHandler();
            String url= path + "Carts/GetCartItems";
            Log.d("Url",">"+url);

            try {
                List<NameValuePair> params2 = new ArrayList<>();
                params2.add(new BasicNameValuePair("UserName", user));
//                params2.add(new BasicNameValuePair("Username", mobileno));
//                params2.add(new BasicNameValuePair("Password", pass));

                String jsonStr = shh.makeServiceCall(url, ServiceHandler.POST, params2);

                if (jsonStr != null) {
                    JSONObject c1 = new JSONObject(jsonStr);
                    JSONArray classArray = c1.getJSONArray("response");

                    for (int i =0; i< classArray.length();i++){
                        JSONObject a1 = classArray.getJSONObject(i);
                        subitem=a1.getString("productName");
                        rate=a1.getInt("productCost");
                        totcount = a1.getInt("totalCount");
                        totalcost = rate * totcount;

                        CartListPlanet planet = new CartListPlanet(subitem, String.valueOf(rate),String.valueOf(totalcost),String.valueOf(totcount));
                        mPlanetlist.add(planet);

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
                    adapter=new CartAdapter(mPlanetlist);
                    recyclerView.setAdapter(adapter);

                }
            });



        }
    }

}
