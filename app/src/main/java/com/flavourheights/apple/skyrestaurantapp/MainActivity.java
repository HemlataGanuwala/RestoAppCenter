package com.flavourheights.apple.skyrestaurantapp;

import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.flavourheights.apple.skyrestaurantapp.Adapter.OfferAdapter;
import com.flavourheights.apple.skyrestaurantapp.Model.ItemPlanet;
import com.flavourheights.apple.skyrestaurantapp.Model.OfferPlanet;

import org.apache.http.NameValuePair;
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
    String user,pass,path,festnm,fromdt,todt,disc,date,date1,festival,discount,fromdate,todate;
    ServiceHandler shh;
    TextView first,second;
    OfferPlanet offerPlanet;
    List<OfferPlanet> mPlanetlist1 = new ArrayList<OfferPlanet>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        path = globalVariable.getconstr();
        user = globalVariable.getUsername();
        pass = globalVariable.getloginPassword();

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        first = (TextView) findViewById(R.id.first);
        second = (TextView) findViewById(R.id.second);

//        new getofferlist().execute();

        navigationView=(NavigationView)findViewById(R.id.nav_view);
        headerview=navigationView.getHeaderView(0);
        imageViewshow=(ImageView)headerview.findViewById(R.id.imgshow);
        imageViewshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
            }
        });

        final ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 1.0f);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(9000L);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final float progress = (float) animation.getAnimatedValue();
                final float width = first.getWidth();
                final float translationX = width * progress;
                first.setTranslationX(translationX);
                second.setTranslationX(translationX - width);
            }
        });
        animator.start();

//        Display();


        preferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        int images[] = {R.drawable.rest_slide1, R.drawable.rest_slide2, R.drawable.rest_slide3, R.drawable.rest_slide4, R.drawable.rest_slide5};
        viewFlipper = (ViewFlipper)findViewById(R.id.viewflipper);
        for(int image: images)
        {
            Fipperimage(image);
        }


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

//    public void Display()
//    {
//        Intent intent = getIntent();
//        Bundle bundle = intent.getExtras();
//        if (bundle != null)
//        {
//            user = (String)bundle.get("User");
//            pass = (String)bundle.get("Password");
//        }
//    }

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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cartlist:
                Intent intent = new Intent(MainActivity.this, CartListActivity.class);
                intent.putExtra("Username",user);
                intent.putExtra("Password",pass);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
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
                Intent intent= new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                break;
//                try {
//                    fragment = (Fragment) fragmentClass.newInstance();
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();


            case R.id.nav_about:
                Intent intent1 = new Intent(getApplicationContext(),AboutActivity.class);
                startActivity(intent1);
                break;

            case R.id.nav_offer:
                Intent intent2 = new Intent(getApplicationContext(),OfferActivity.class);
                startActivity(intent2);
                break;

            case R.id.nav_share:
                Intent intent3 = new Intent(Intent.ACTION_SEND);
                intent3.setType("text/plain");
                String shareBody = "https://drive.google.com/open?id=15roJeXDa2CbIaeou6ObR6aWc-_k5xyEA";
                String shareSub = "Your Sub Here";
                intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                intent3.putExtra(Intent.EXTRA_SUBJECT, shareSub);
                intent3.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(intent3, "Share Using"));
                break;

            case R.id.nav_wallet:

                Intent intent4= new Intent(getApplicationContext(), MoneyWalletActivity.class);
                startActivity(intent4);
                break;

            case R.id.nav_refer:

                Intent intent5= new Intent(getApplicationContext(), ReferCodeActivity.class);
                startActivity(intent5);
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

                Intent intent6= new Intent(getApplicationContext(), CartList2Activity.class);
                intent6.putExtra("Username",user);
                intent6.putExtra("Password",pass);
                startActivity(intent6);
                break;

            case R.id.nav_orderhis:

                Intent intent7= new Intent(getApplicationContext(), OrderHistoryActivity.class);
                startActivity(intent7);
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

    class getofferlist extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progress=new ProgressDialog(OfferActivity.this);
//            progress.setMessage("Loading...");
//            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            progress.setIndeterminate(true);
//            progress.setProgress(0);
//            progress.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            shh = new ServiceHandler();
            String url = path + "Registration/getOffer";
            Log.d("Url: ", "> " + url);

            try {
                List<NameValuePair> params2 = new ArrayList<>();
                String jsonStr = shh.makeServiceCall(url, ServiceHandler.GET, null);

                if (jsonStr != null) {
                    JSONObject c1 = new JSONObject(jsonStr);
                    JSONArray classArray = c1.getJSONArray("Response");
                    for (int i = 0; i < classArray.length(); i++) {
                        JSONObject a1 = classArray.getJSONObject(i);
                        festnm = a1.getString("FestivalName");
                        fromdt = a1.getString("ValidFrom");
                        todt = a1.getString("Validto");
                        disc = a1.getString("Discount");


//                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
//                        String date = formatter.format(Date.parse(fromdt));


                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date d = sdf.parse(fromdt);
                        String df = d.toString();
                        System.out.println(d);

                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
                        date = formatter.format(Date.parse(df));

                        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                        Date d1 = sdf1.parse(todt);
                        String dt = d1.toString();
                        System.out.println(d);

                        SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
                        date1 = formatter1.format(Date.parse(dt));


                    }


                } else {
                    //Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
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
