package com.flavourheight.apple.skyrestaurantapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.flavourheight.apple.skyrestaurantapp.Adapter.CartMenuAdapter;
import com.flavourheight.apple.skyrestaurantapp.Model.CartListMenuPlanet;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CartList2Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    NavigationView navigationView;
    private SharedPreferences preferences;
    private DrawerLayout drawerLayout;
    View headerview;
    ImageView imageViewshow;
    Class fragmentClass;
    Button buttonorder;
    private static final String PREFS_NAME = "PrefsFile";
    Fragment fragment = null;
    ImageView imageViewback,imageViewedit;
    RecyclerView recyclerView;
    String subitem,path,user,pass,mobileno;
    ServiceHandler shh;
    List<CartListMenuPlanet> mPlanetlist= new ArrayList<CartListMenuPlanet>();
    CartMenuAdapter adapter;
    int ammount, totalamount;
    TextView textViewtotlcost;
    int rate,totcount,totalcost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list2);

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        path = globalVariable.getconstr();
        user = globalVariable.getUsername();
        mobileno = globalVariable.getMobileNo();
        pass = globalVariable.getloginPassword();

        textViewtotlcost=(TextView)findViewById(R.id.tvtotal_costmenu);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar_cartlist);
        setSupportActionBar(toolbar);

        navigationView=(NavigationView)findViewById(R.id.nav_view);
        headerview=navigationView.getHeaderView(0);
        imageViewshow=(ImageView)headerview.findViewById(R.id.imgshow);
        imageViewshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AddAddress2Activity.class);
                startActivity(i);
            }
        });

//        Display();

        new addCartItem().execute();

        imageViewedit = (ImageView)findViewById(R.id.imgmenupencile);
        imageViewedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(CartList2Activity.this, EditCartActivity.class);
                intent.putExtra("User",user);
                intent.putExtra("Mobileno",mobileno);
                intent.putExtra("Pass",pass);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        recyclerView=(RecyclerView)findViewById(R.id.recyclecartlistmenu);
        recyclerView.setLayoutManager(new LinearLayoutManager(CartList2Activity.this));

        buttonorder=(Button)findViewById(R.id.btnorder);
        buttonorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CartList2Activity.this, PlaceOrderActivity.class);
                intent.putExtra("Cost", String.valueOf(totalamount));
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        preferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_menu:
                Intent intent8= new Intent(getApplicationContext(), MainActivity.class);
                intent8.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent8);
                break;

            case R.id.nav_login:
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;


            case R.id.nav_about:
                Intent intent1 = new Intent(getApplicationContext(), AboutActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent1);
                break;

            case R.id.nav_offer:
                Intent intent2 = new Intent(getApplicationContext(), OfferActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent2);
                break;

            case R.id.nav_share:
                Intent intent3 = new Intent(Intent.ACTION_SEND);
                intent3.setType("text/plain");
                String shareBody = "https://drive.google.com/open?id=1zrD8iqWKn5YSqFTTZ4GnNcu5FbLMGH0N";
                String shareSub = "Your Sub Here";
                intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                intent3.putExtra(Intent.EXTRA_SUBJECT, shareSub);
                intent3.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(intent3, "Share Using"));
                break;

            case R.id.nav_wallet:

                Intent intent4 = new Intent(getApplicationContext(), MoneyWalletActivity.class);
                intent4.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent4);
                break;

            case R.id.nav_refer:

                Intent intent5 = new Intent(getApplicationContext(), ReferCodeActivity.class);
                intent5.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent5);
                break;

            case R.id.nav_rateus:

//                AppRate.with(this).setInstallDays(0).setLaunchTimes(2).setRemindInterval(2).monitor();
//                AppRate.showRateDialogIfMeetsConditions(this);

                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.android.crome")));
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                }
                break;

            case R.id.nav_cartlist:

                Intent intent6 = new Intent(getApplicationContext(), CartList2Activity.class);
                intent6.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent6);
                break;

            case R.id.nav_orderhis:

                Intent intent7 = new Intent(getApplicationContext(), OrderHistoryActivity.class);
                intent7.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent7);
                break;

            case R.id.nav_myaccount:

                Intent intent9 = new Intent(getApplicationContext(), OrderHistoryActivity.class);
                intent9.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent9);
                break;

            default:
                fragmentClass = MenuFragment.class;
                break;
        }
        item.setChecked(true);
        // Set action bar title
        setTitle(item.getTitle());
        // Close the navigation drawer
        drawerLayout.closeDrawers();
        return true;
    }

//    public void Display()
//    {
//        Intent intent = getIntent();
//        Bundle bundle = intent.getExtras();
//        if (bundle != null)
//        {
////            subitem = (String)bundle.get("SubItem");
////            rate = (String)bundle.get("Rate");
//            user = (String)bundle.get("Username");
//            pass = (String)bundle.get("Password");
//        }
//
//    }

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
                params2.add(new BasicNameValuePair("Username", user));
//                params2.add(new BasicNameValuePair("Username", mobileno));
                params2.add(new BasicNameValuePair("Password", pass));

                String jsonStr = shh.makeServiceCall(url, ServiceHandler.POST, params2);

                if (jsonStr != null) {
                    JSONObject c1 = new JSONObject(jsonStr);
                    JSONArray classArray = c1.getJSONArray("Response");

                    for (int i =0; i< classArray.length();i++){
                        JSONObject a1 = classArray.getJSONObject(i);
                        subitem=a1.getString("SubItemName");
                        rate=a1.getInt("ItemRate");
                        totcount = a1.getInt("TotalCount");
                        totalcost = rate * totcount;

                        CartListMenuPlanet planet = new CartListMenuPlanet(subitem, String.valueOf(rate),String.valueOf(totalcost),String.valueOf(totcount));
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
                    adapter=new CartMenuAdapter(mPlanetlist);
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


        }
    }
}
