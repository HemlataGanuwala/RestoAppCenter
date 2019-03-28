package com.flavourheight.apple.skyrestaurantapp;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.flavourheight.apple.skyrestaurantapp.Model.OfferPlanet;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private static final String PREFS_NAME = "PrefsFile";
    ViewFlipper viewFlipper;
    Fragment fragment = null;
    ImageView imageViewshow;
    NavigationView navigationView;
    View headerview;
    Class fragmentClass;
    private SharedPreferences preferences;
    String user,pass,path,festnm,fromdt,todt,disc,date,date1,festival,discount,fromdate,todate,count;
    ServiceHandler shh;
    TextView first,second,textViewitemcount;
    OfferPlanet offerPlanet;
    List<OfferPlanet> mPlanetlist1 = new ArrayList<OfferPlanet>();
    ProgressDialog progress;
    DatabaseHelpher databaseHelpher;
    int datastatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCenter.start(getApplication(), "ccbb2947-e93e-4088-b3d4-1b5a184e936d",
                Analytics.class, Crashes.class);

        databaseHelpher = new DatabaseHelpher(this);

        count = "0";

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        path = globalVariable.getconstr();
        user = globalVariable.getUsername();
        pass = globalVariable.getloginPassword();

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        first = (TextView) findViewById(R.id.first);
//        second = (TextView) findViewById(R.id.second);

//        new getofferlist().execute();

        navigationView=(NavigationView)findViewById(R.id.nav_view);
        headerview=navigationView.getHeaderView(0);
        imageViewshow=(ImageView)headerview.findViewById(R.id.imgshow);
        imageViewshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (user != null && pass != null)
                {
                    Intent i = new Intent(getApplicationContext(), AddAddress2Activity.class);
                    startActivity(i);
                    finish();

                }else {

                    Toast.makeText(MainActivity.this, "Login First", Toast.LENGTH_LONG).show();
                }

            }
        });

        preferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        int images[] = {R.drawable.rest_slide1, R.drawable.rest_slide2, R.drawable.rest_slide1, R.drawable.rest_slide2};
        viewFlipper = (ViewFlipper)findViewById(R.id.viewflipper);
        for(int image: images)
        {
            Fipperimage(image);
        }

        new getAllItem().execute();



        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragmentClass = MenuFragment.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();

        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    public  void Fipperimage(int image)
    {
        ImageView imageView = new ImageView(MainActivity.this);
        imageView.setBackgroundResource(image);

        viewFlipper.addView(imageView);
        viewFlipper.setFlipInterval(2000);
        viewFlipper.setAutoStart(true);

        viewFlipper.setInAnimation(MainActivity.this,android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(MainActivity.this,android.R.anim.slide_out_right);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        final MenuItem menuItem = menu.findItem(R.id.cartlist);

        View actionView = MenuItemCompat.getActionView(menuItem);
        textViewitemcount = (TextView) actionView.findViewById(R.id.cart_badge);

//        if (count.equals("0") )
//        {
//            textViewitemcount.setVisibility(View.GONE);
//        }
//        else {
//            textViewitemcount.setVisibility(View.VISIBLE);
//            textViewitemcount.setText(count);
//        }

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cartlist:
                new getAllItem().execute();

                try {

                    if (count.equals("0")) {
                        setContentView(R.layout.message);
//                    Toast.makeText(this, "No Item in Cart", Toast.LENGTH_LONG).show();
                    }else {

                        Intent intent = new Intent(MainActivity.this, CartListActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        finish();
                        return true;

                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }


    }

    public void GetData()
    {
        Cursor c = databaseHelpher.GetRegData();

        if (c != null)
        {
            if (c.moveToFirst()) {
                do {
                    datastatus = c.getInt(c.getColumnIndex("Status"));

                }
                while (c.moveToNext());
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment fragment = null;
        //Class fragmentClass = null;

        switch(item.getItemId()) {
            case R.id.nav_menu:
                fragmentClass = MenuFragment.class;
                break;

//            case R.id.imgshow:
//                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
//                startActivity(i);
//                break;

            case R.id.nav_login:

                GetData();

                if (datastatus == 1)
                {
//                    Toast.makeText(MainActivity.this,"You are already login",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Intent intent= new Intent(getApplicationContext(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
//                    finish();
                }
                break;

            case R.id.nav_about:
                Intent intent1 = new Intent(getApplicationContext(),AboutActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent1);
//                finish();
                break;

            case R.id.nav_offer:
                Intent intent2 = new Intent(getApplicationContext(),OfferActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent2);
//                finish();
                break;

            case R.id.nav_share:
                Intent intent3 = new Intent(Intent.ACTION_SEND);
                intent3.setType("text/plain");
                String shareBody = "https://drive.google.com/open?id=1baC8GWcGn16YmEgcWhUYphcaYyhLJ7Ht";
                String shareSub = "Your Sub Here";
                intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                intent3.putExtra(Intent.EXTRA_SUBJECT, shareSub);
                intent3.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(intent3, "Share Using"));
//                finish();
                break;

            case R.id.nav_wallet:

                Intent intent4= new Intent(getApplicationContext(), MoneyWalletActivity.class);
                intent4.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent4);
//                finish();
                break;

            case R.id.nav_refer:

                Intent intent5= new Intent(getApplicationContext(), ReferCodeActivity.class);
                intent5.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent5);
//                finish();
                break;

            case R.id.nav_rateus:

//                AppRate.with(this).setInstallDays(0).setLaunchTimes(2).setRemindInterval(2).monitor();
//                AppRate.showRateDialogIfMeetsConditions(this);

                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+"com.android.crome")));
                }catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id="+getPackageName())));
                }
                break;

            case R.id.nav_cartlist:

                if (count.equals("0"))
                {
                    Toast.makeText(getApplicationContext(), "No Item in cart", Toast.LENGTH_LONG).show();
                }else {

                    Intent intent6 = new Intent(getApplicationContext(), CartList2Activity.class);
                    intent6.putExtra("Username", user);
                    intent6.putExtra("Password", pass);
                    intent6.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent6);
//                    finish();
                }
                break;

            case R.id.nav_orderhis:

                Intent intent7= new Intent(getApplicationContext(), OrderHistoryActivity.class);
                startActivity(intent7);
//                finish();
                break;

            case R.id.nav_myaccount:

                Intent intent8= new Intent(getApplicationContext(), MyAccountActivity.class);
                intent8.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent8);
//                finish();
                break;

            default:
                fragmentClass = MenuFragment.class;
                break;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        item.setChecked(true);
        // Set action bar title
        setTitle(item.getTitle());
        // Close the navigation drawer
        drawerLayout.closeDrawers();
        return true;

    }



    class getAllItem extends AsyncTask<Void, Void, String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... params) {
            shh = new ServiceHandler();
            String url = path + "Carts/GetCartItemWiseCount";
            Log.d("Url: ", "> " + url);

            try{
                List<NameValuePair> params2 = new ArrayList<>();
                params2.add(new BasicNameValuePair("UserName", user));
//                params2.add(new BasicNameValuePair("Password", pass));
                String jsonStr = shh.makeServiceCall(url, ServiceHandler.POST , params2);

                if (jsonStr != null) {
                    JSONObject c1 = new JSONObject(jsonStr);
                    count = c1.getString("response");
                    if (count == null)
                    {
                        count = "0";
                    }
                }

                else
                {
                    //Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
                }

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


        }
    }

}
