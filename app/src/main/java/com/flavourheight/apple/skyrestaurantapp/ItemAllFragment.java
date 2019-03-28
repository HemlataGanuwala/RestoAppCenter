package com.flavourheight.apple.skyrestaurantapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.flavourheight.apple.skyrestaurantapp.Adapter.ItemAdapter;
import com.flavourheight.apple.skyrestaurantapp.Model.ItemPlanet;
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


public class ItemAllFragment extends Fragment{


    View view;
    List<ItemPlanet> mPlanetlist1 = new ArrayList<ItemPlanet>();
    ItemAdapter adapter;
    RecyclerView recyclerView;
    ProgressDialog progress;
    String path,itemname,subitem,rate,img,user,pass,subitem1,rate1,imagefav,mobileno;
    ServiceHandler shh;
    ImageView imageViewcart;
    int Status = 1;
    int count=0;
    TextView textViewcount;
    String cnt,Response;
    public ActivityCommunicator activityCommunicator;
    public Context context;
    DatabaseHelpher databaseHelpher;
    String favsubitem,favrate;


    public interface ActivityCommunicator{
        void passDataActivity(String someValue);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_itemall, container, false);

        AppCenter.start(getActivity().getApplication(), "ccbb2947-e93e-4088-b3d4-1b5a184e936d",
                Analytics.class, Crashes.class);
        databaseHelpher = new DatabaseHelpher(getActivity());

        final GlobalClass globalVariable = (GlobalClass) getActivity().getApplicationContext();
        path = globalVariable.getconstr();
        user = globalVariable.getUsername();
        mobileno = globalVariable.getMobileNo();
        pass = globalVariable.getloginPassword();

//        textViewcount = (TextView) view.findViewById(R.id.tvcount);

        Displayitem();

//        mPlanetlist1.clear();

        new getAllCount().execute();
        mPlanetlist1.clear();
        new getAllItem().execute();

        new getAllItemcount().execute();


        recyclerView = (RecyclerView) view.findViewById(R.id.recycleAll);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));




        return view;
    }

    public void Displayitem()
    {
        Intent intent = getActivity().getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle!=null){
            itemname=(String)bundle.get("a2");
        }
    }

//    public void Displaylogin()
//    {
//        Intent intent = getActivity().getIntent();
//        Bundle bundle = intent.getExtras();
//        if (bundle != null)
//        {
//            user = (String)bundle.get("User");
//            pass = (String)bundle.get("Password");
//        }
//    }

    class getAllItem extends AsyncTask<Void, Void, String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=new ProgressDialog(getContext());
            progress.setMessage("Loading...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setProgress(0);
            progress.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            shh = new ServiceHandler();
            String url = path + "Products/GetProductWiseRecord";
            Log.d("Url: ", "> " + url);

            try{
                List<NameValuePair> params2 = new ArrayList<>();
                params2.add(new BasicNameValuePair("ProductTypeName", "Biryani"));
                params2.add(new BasicNameValuePair("UserName", user));
                String jsonStr = shh.makeServiceCall(url, ServiceHandler.POST , params2);

                if (jsonStr != null) {
                    JSONObject c1 = new JSONObject(jsonStr);
                    JSONArray classArray = c1.getJSONArray("response");
                    for (int i = 0; i < classArray.length(); i++) {
                        JSONObject a1 = classArray.getJSONObject(i);
                        itemname = a1.getString("productTypeName");
                        subitem = a1.getString("productName");
                        rate = a1.getString("productCost");
                        img = a1.getString("productImage");
                        imagefav = a1.getString("favouriteStatus");
//                        if (a1.getString("favouriteStatus") != null)
//                        {
//                            imagefav = a1.getString("favouriteStatus");
//                        }
//                       else
//                        {
//                            imagefav = "Light";
//                        }

                        ItemPlanet planet1 = new ItemPlanet(itemname,subitem,rate,img,imagefav);
                        mPlanetlist1.add(planet1);

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
            progress.dismiss();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter = new ItemAdapter(mPlanetlist1);
                    recyclerView.setAdapter(adapter);
                }
            });

            adapter.setOnClickImageFavourite(new ItemAdapter.OnClickImageFavourite() {
                @Override
                public void iconFavouriteImageViewOnClick(int position,String itemname,String rate) {

                    int dark = position;

                    favsubitem = itemname;
                    favrate = rate;

                    if (user != null)
                    {
                        if (dark == 1)
                        {
                            new AddFavouriteData().execute();

                        }
                    }
                    else
                    {
                        Toast.makeText(getActivity(),"Login First",Toast.LENGTH_LONG).show();
                    }


                }
            });

            adapter.setOnItemClickListner(new ItemAdapter.OnItemClickListner() {
                @Override
                public void onItemClick(int position) {

                }

                @Override
                public void iconImageViewOnClick(View v, int position) {

                    if (user != null)
                    {
                        ItemPlanet planet1 = mPlanetlist1.get(position);
                        subitem1 = planet1.getSubItemname();
                        rate1 = planet1.getRate();
//                }
                        new getAllItemcount().execute();

                        if (Response.equals("0"))
                        {
                            count++;
                            cnt = String.valueOf(count);
                            databaseHelpher.CardData(itemname,subitem1,rate1,user,pass,1,rate1,0);
                            new RegisterData().execute();

                        }
                        else
                        {
                            Toast.makeText(getActivity(), "Item Already Added", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getActivity(), EditCartActivity.class);
                            startActivity(intent);

                        }

                        activityCommunicator.passDataActivity(cnt);


                    }else {
                        Toast.makeText(getActivity(), "Login first", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void iconFavouriteImageViewOnClick(View v, int position) {


                }

                @Override
                public void iconDarkFavouriteImageViewOnClick(View v, int position) {

                    new deleteFavouriteItem().execute();
                }


            });



        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = getActivity();
        activityCommunicator = (ActivityCommunicator)context;


    }


    class getAllCount extends AsyncTask<Void, Void, String>
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
                    count = c1.getInt("response");
                    String cnt = String.valueOf(count);
                    if (cnt == null)
                    {
                        count = 0;
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
//            progress.dismiss();
//            textViewcount.setText(count);

        }
    }

    class getAllItemcount extends AsyncTask<Void, Void, String>
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
                params2.add(new BasicNameValuePair("ProductName", subitem1));
                String jsonStr = shh.makeServiceCall(url, ServiceHandler.POST , params2);

                if (jsonStr != null) {
                    JSONObject c1 = new JSONObject(jsonStr);
                    Response  = c1.getString("response");


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
            // progress.dismiss();
        }
    }

    public class RegisterData extends AsyncTask<String,String,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... strings) {

            shh = new ServiceHandler();
            String url = path + "Carts/AddToCart";

            try {
                List<NameValuePair> params2 = new ArrayList<>();
                params2.add(new BasicNameValuePair("ProductTypeName", itemname));
                params2.add(new BasicNameValuePair("ProductName", subitem1));
                params2.add(new BasicNameValuePair("ProductCost", rate1));
                params2.add(new BasicNameValuePair("UserName", user));
//                params2.add(new BasicNameValuePair("Password", pass));
                params2.add(new BasicNameValuePair("TotalCount", "1"));
                params2.add(new BasicNameValuePair("TotalAmt", rate1));
                params2.add(new BasicNameValuePair("Status", "0"));

                String Jsonstr = shh.makeServiceCall(url, ServiceHandler.POST, params2);

                if (Jsonstr != null) {
                    JSONObject c1 = new JSONObject(Jsonstr);
                    Status = c1.getInt("status");
                } else {
                    Toast.makeText(getActivity(), "Data not Found", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (Status == 1) {

                Toast.makeText(getActivity(), "Item add in cart", Toast.LENGTH_LONG).show();

            }

        }
    }


    public class AddFavouriteData extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... strings) {

            shh = new ServiceHandler();
            String url = path + "Favourites/InsertFavouriteData";

            try {
                List<NameValuePair> params2 = new ArrayList<>();
                params2.add(new BasicNameValuePair("UserName",user));
                params2.add(new BasicNameValuePair("ProductTypeName",itemname));
                params2.add(new BasicNameValuePair("ProductName",favsubitem));
                params2.add(new BasicNameValuePair("ProductCost",favrate));
                params2.add(new BasicNameValuePair("FavouriteStatus","Dark"));

                String Jsonstr = shh.makeServiceCall(url ,ServiceHandler.POST , params2);

                if (Jsonstr != null)
                {
                    JSONObject c1= new JSONObject(Jsonstr);
                    Status =c1.getInt("status");
                }
                else{
                    Toast.makeText(getActivity(), "Data not Found", Toast.LENGTH_SHORT).show();
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
            if (Status == 1)
            {
                Toast.makeText(getActivity(), "Add to Favourite", Toast.LENGTH_SHORT).show();
                mPlanetlist1.clear();
            }
        }

    }

    class deleteFavouriteItem extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//                progress = new ProgressDialog(PlaceOrderActivity.this);
//                progress.setMessage("Loading...");
//                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                progress.setIndeterminate(true);
//                progress.setProgress(0);
//                progress.show();
        }


        @Override
        protected String doInBackground(String... strings) {

            shh = new ServiceHandler();
            String url = path + "Registration/DeleteFavouriteData";

            try {
                List<NameValuePair> params2 = new ArrayList<>();

                params2.add(new BasicNameValuePair("UserName", user));
                params2.add(new BasicNameValuePair("ProductName", subitem));

                String Jsonstr = shh.makeServiceCall(url, ServiceHandler.POST, params2);

                if (Jsonstr != null) {
                    JSONObject c1 = new JSONObject(Jsonstr);
                    Status = c1.getInt("status");
                } else {
                    Toast.makeText(getActivity(), "Data not Found", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (Status == 1)
            {
                Toast.makeText(getActivity(), "Item deleted form favourite", Toast.LENGTH_LONG).show();
            }

        }

    }



}
