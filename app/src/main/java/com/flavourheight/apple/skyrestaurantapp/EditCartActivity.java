package com.flavourheight.apple.skyrestaurantapp;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.flavourheight.apple.skyrestaurantapp.Adapter.EditCartAdapter;
import com.flavourheight.apple.skyrestaurantapp.Model.CartListPlanet;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EditCartActivity extends AppCompatActivity implements EditCartAdapter.ClickOnItemListener, SwipeRefreshLayout.OnRefreshListener,EditCartAdapter.ClickOnDeleteListener{

    ImageView imageViewback,imageViewshow, imageViewdelete;
    NavigationView navigationView;
    private SharedPreferences preferences;
    private DrawerLayout drawerLayout;
    CartListPlanet mPlanet;
    View headerview;
    Class fragmentClass;
    private static final String PREFS_NAME = "PrefsFile";
    Fragment fragment = null;
    RecyclerView recyclerView;
    String subitem, path,user,pass,totamt,totaleditcount,editcount,mobileno;
    ServiceHandler shh;
    ArrayList<CartListPlanet> mPlanetlist= new ArrayList<CartListPlanet>();
    EditCartAdapter adapter;
    TextView textViewtotlcost;
    int Status = 1,rate,totalcost,totcount;
    String editsubitem,edittotalcount,edittotalrate;
    CartListPlanet cartListPlanet;
    ArrayList<CartListPlanet> mPlanlist= new ArrayList<CartListPlanet>();
    SwipeRefreshLayout swipeRefreshLayout;
    String itemnamedelete;
    String count;
    int totalamt = 0;
    DatabaseHelpher databaseHelpher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_cart);

        databaseHelpher = new DatabaseHelpher(this);

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        path = globalVariable.getconstr();
        user = globalVariable.getUsername();
        pass = globalVariable.getloginPassword();
        mobileno = globalVariable.getMobileNo();

//        Display();



        new addCartItem().execute();


        textViewtotlcost=(TextView)findViewById(R.id.tvedittotal_cost);

//        imageViewback=(ImageView)findViewById(R.id.img_back);

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeeditcartlist);
        swipeRefreshLayout.setOnRefreshListener(this);

        recyclerView=(RecyclerView)findViewById(R.id.recycleeditcartlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(EditCartActivity.this));

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbareditcartlist);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_editcart_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {


            case android.R.id.home:
                Intent intent1 = new Intent(EditCartActivity.this, CartListActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent1);
                return true;


            case R.id.cartcheck:

                for (int i=0; i<mPlanlist.size(); i++)
                {
                    subitem = (mPlanlist.get(i).getItemName());
                    editcount = (mPlanlist.get(i).getTotalCount());
                    totamt = (mPlanlist.get(i).getTotalCost());

                    //databaseHelpher.UpdateCartMasterData(user,subitem,editcount,totamt);
                    new UpdateCount().execute();
                    try
                    {
                        Thread.sleep(500);
                    }catch (Exception e)
                    {}

                }


//                Intent intent = new Intent(EditCartActivity.this, CartListActivity.class);
//                intent.putExtra("Totalamt", totalamt);
//                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                startActivity(intent);
//                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
//        if (item.getItemId() == android.R.id.home) {
//            finish(); // close this activity and return to preview activity (if there is any)
//        }


    }


    @Override
    public void onOrderItemClick(ArrayList<CartListPlanet> mPlist) {
        mPlanlist = mPlist;

        totalamt = 0;

        for (int i=0; i<mPlanlist.size(); i++)
        {
            totalamt = totalamt + Integer.parseInt(mPlanlist.get(i).getTotalCost());
        }

        textViewtotlcost.setText(String.valueOf(totalamt));

    }

    @Override
    public void onRefresh() {
        mPlanetlist.clear();
        new ReloadCartItem().execute();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onDeleteItemClick(String position) {
        itemnamedelete = position;

        new DeleteItem().execute();
        mPlanetlist.clear();
        new ReloadCartItem().execute();
//        new getAllItem().execute();
//        if (count.equals("null"))
//        {
//            count="";
//            if (count.equals("null"))
//            {
//                setContentView(R.layout.message);
//            }else {
//
//            }
//        }


    }


    public class UpdateCount extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... strings) {

            shh = new ServiceHandler();
            String url = path + "Carts/UpdateCartItem";

            try {
                List<NameValuePair> params2 = new ArrayList<>();
                params2.add(new BasicNameValuePair("ProductName",subitem));
                //params2.add(new BasicNameValuePair("ItemRate",String.valueOf(rate)));
                params2.add(new BasicNameValuePair("UserName",user));
//                params2.add(new BasicNameValuePair("Username",mobileno));
                params2.add(new BasicNameValuePair("TotalCount",String.valueOf(editcount)));
                params2.add(new BasicNameValuePair("TotalAmt",totamt));

                String Jsonstr = shh.makeServiceCall(url ,ServiceHandler.POST , params2);

                if (Jsonstr != null)
                {
                    JSONObject c1= new JSONObject(Jsonstr);
                    Status =c1.getInt("status");
                }
                else{
                    Toast.makeText(EditCartActivity.this, "Data not Found", Toast.LENGTH_SHORT).show();
                }
            }
            catch ( JSONException e){
                e.printStackTrace();
            }


            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(EditCartActivity.this,"Update Successfully",Toast.LENGTH_LONG).show();
        }

    }

    public class addCartItem extends AsyncTask<String, String, String> {

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
                        totalcost = a1.getInt("totalAmt");


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
                    adapter=new EditCartAdapter(mPlanetlist);
                    recyclerView.setAdapter(adapter);
                }
            });

            adapter.setOnItemClick(EditCartActivity.this);
            adapter.setOnDeleteClick(EditCartActivity.this);
            adapter.setOnItemClickListner(new EditCartAdapter.OnItemClickListner() {
                @Override
                public void onItemClick(int position) {

                }

//                @Override
//                public void plusOnClick(View v, int position) {
//
//                }
//
//                @Override
//                public void icondeleteImageViewOnClick(View v, int position) {
//
//                    new DeleteItem().execute();
//
//
//                }
//
//                @Override
//                public void iconImageViewOnClick(View v, int position) {
//
//                }
            });


        }
    }


    public class DeleteItem extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... strings) {

            shh = new ServiceHandler();
            String url = path + "Carts/DeleteEditCartItem";

            try {
                List<NameValuePair> params2 = new ArrayList<>();

                params2.add(new BasicNameValuePair("UserName",user));
//                params2.add(new BasicNameValuePair("Username",mobileno));
                params2.add(new BasicNameValuePair("ProductName",itemnamedelete));



                String Jsonstr = shh.makeServiceCall(url ,ServiceHandler.POST , params2);

                if (Jsonstr != null)
                {
                    JSONObject c1= new JSONObject(Jsonstr);
                    Status =c1.getInt("status");
                }
                else{
                    Toast.makeText(EditCartActivity.this, "Data not Found", Toast.LENGTH_SHORT).show();
                }
            }
            catch ( JSONException e){
                e.printStackTrace();
            }


            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }

    }

    public class ReloadCartItem extends AsyncTask<String, String, String> {

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
                        totalcost = a1.getInt("totalAmt");

//
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
                    adapter=new EditCartAdapter(mPlanetlist);
                    recyclerView.setAdapter(adapter);
                }
            });
            adapter.setOnItemClick(EditCartActivity.this);
        }
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

                    if (count.equals("null"))
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
