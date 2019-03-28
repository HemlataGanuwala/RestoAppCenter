package com.flavourheight.apple.skyrestaurantapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;
import com.flavourheight.apple.skyrestaurantapp.Model.ItemPlanet;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.List;

public class MainDashActivity extends AppCompatActivity implements ItemAllFragment.ActivityCommunicator,ItemNonVegFragment.ActivityCommunicator,ItemVegFragment.ActivityCommunicator {

    ViewFlipper viewFlipper;
    String itemname,path;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    ImageView  imageViewback, imageViewcart;
    String subitem, rate,user,pass;
    int countitem;
    List<ItemPlanet> mPlanetlist = new ArrayList<ItemPlanet>();
    String count;
    TextView textViewcount,textViewitemcount;
    ServiceHandler shh;
    ProgressDialog progress;
    ItemAllFragment fragment;
    int mCartItemCount;


    Menu defaultMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dash);

        AppCenter.start(getApplication(), "ccbb2947-e93e-4088-b3d4-1b5a184e936d",
                Analytics.class, Crashes.class);

        count = "0";

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        path = globalVariable.getconstr();

        user = globalVariable.getUsername();
        pass = globalVariable.getloginPassword();

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.dashtoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int images[] = {R.drawable.rest_slide1, R.drawable.rest_slide2, R.drawable.rest_slide1, R.drawable.rest_slide2};
        viewFlipper = (ViewFlipper)findViewById(R.id.dashviewflipper);
        for(int image: images)
        {
            Fipperimage(image);
        }

        tabLayout = (TabLayout)findViewById(R.id.dashtablayout);
        viewPager = (ViewPager)findViewById(R.id.dashviewpager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.AddFragment(new ItemAllFragment(),"All");
        adapter.AddFragment(new ItemVegFragment(),"Veg");
        adapter.AddFragment(new ItemNonVegFragment(),"Non-Veg");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        createTab();
        Display();

        new getAllItem().execute();

    }

    public  void Fipperimage(int image)
    {
        ImageView imageView = new ImageView(MainDashActivity.this);
        imageView.setBackgroundResource(image);

        viewFlipper.addView(imageView);
        viewFlipper.setFlipInterval(2000);
        viewFlipper.setAutoStart(true);

        viewFlipper.setInAnimation(MainDashActivity.this,android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(MainDashActivity.this,android.R.anim.slide_out_right);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_main_dash, menu);
        final MenuItem menuItem = menu.findItem(R.id.cartlistmaindash);

        View actionView = MenuItemCompat.getActionView(menuItem);
        textViewitemcount = (TextView) actionView.findViewById(R.id.cart_badge);

        if (count.equals("0"))
        {
            textViewitemcount.setVisibility(View.GONE);
        }
        else {
            textViewitemcount.setVisibility(View.VISIBLE);
            textViewitemcount.setText(count);
        }

//        setupBadge();

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
            case android.R.id.home:
                Intent intent1 = new Intent(MainDashActivity.this, MainActivity.class);
                startActivity(intent1);
                return true;


            case R.id.cartlistmaindash:

                new getAllItem().execute();

                try {
                    Thread.sleep(2000);
                } catch (Exception e) {}

                if (count.equals("0")) {
                    setContentView(R.layout.message);
//                    Toast.makeText(this, "No Item in Cart", Toast.LENGTH_LONG).show();
                }else {

                    Intent intent = new Intent(MainDashActivity.this, CartListActivity.class);
                    startActivity(intent);
                    return true;
                }

            default:
                return super.onOptionsItemSelected(item);
        }
//        if (item.getItemId() == android.R.id.home) {
//            finish(); // close this activity and return to preview activity (if there is any)
//        }


    }

    private void setupBadge() {

        if (textViewitemcount != null) {
            if (mCartItemCount == 0) {
                if (textViewitemcount.getVisibility() != View.GONE) {
                    textViewitemcount.setVisibility(View.GONE);
                }
            } else {
                textViewitemcount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (textViewitemcount.getVisibility() != View.VISIBLE) {
                    textViewitemcount.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public void createTab()
    {

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.item_tab, null);
        tabOne.setText("All");
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.item_tab, null);
        tabTwo.setText("Veg");
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.item_tab, null);
        tabThree.setText("Non-Veg");
        tabLayout.getTabAt(2).setCustomView(tabThree);
    }

    public void Display()
    {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null)
        {
            itemname = (String)bundle.get("a2");
        }
    }

//    public void Displaylogin()
//    {
//        Intent intent = getIntent();
//        Bundle bundle = intent.getExtras();
//        if (bundle != null)
//        {
//            user = (String)bundle.get("User");
//            pass = (String)bundle.get("Password");
//        }
//    }




    @Override
    public void passDataActivity(String someValue) {
        if (textViewitemcount.equals("0"))
        {
            textViewitemcount.setVisibility(View.GONE);
        }
        else {
            textViewitemcount.setVisibility(View.VISIBLE);
            textViewitemcount.setText(someValue);
        }

        //countitem = Integer.parseInt(someValue);
    }

    @Override
    public void passDataActivitynonveg(String someValue) {
        if (textViewitemcount.equals("0"))
        {
            textViewitemcount.setVisibility(View.GONE);
        }
        else {
            textViewitemcount.setVisibility(View.VISIBLE);
            textViewitemcount.setText(someValue);
        }

        //countitem = Integer.parseInt(someValue);
    }

    @Override
    public void passDataActivityveg(String someValue) {
        if (textViewitemcount.equals("0"))
        {
            textViewitemcount.setVisibility(View.GONE);
        }
        else {
            textViewitemcount.setVisibility(View.VISIBLE);
            textViewitemcount.setText(someValue);
        }

        //countitem = Integer.parseInt(someValue);
    }





    class getAllItem extends AsyncTask<Void, Void, String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=new ProgressDialog(MainDashActivity.this);
            progress.setMessage("Loading...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setProgress(0);
            progress.show();
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
            progress.dismiss();
//            textViewcount.setText(count);

        }
    }




}
