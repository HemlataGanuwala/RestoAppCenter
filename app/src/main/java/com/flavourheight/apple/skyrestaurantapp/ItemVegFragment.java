package com.flavourheight.apple.skyrestaurantapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.flavourheight.apple.skyrestaurantapp.Adapter.ItemAdapter;
import com.flavourheight.apple.skyrestaurantapp.Model.ItemPlanet;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ItemVegFragment extends Fragment {

    View view;
    String path,itemname,subitem,rate,img,user,pass,subitem1,rate1,imagefav;
    ProgressDialog progress;
    ServiceHandler shh;
    List<ItemPlanet> mPlanetlist1 = new ArrayList<ItemPlanet>();
    ItemAdapter adapter;
    RecyclerView recyclerView;
    String cnt,Response;
    int Status = 1;
    int count=0;
    String favsubitem,favrate;

    public ActivityCommunicator activityCommunicatorveg;
    public Context context;

    public interface ActivityCommunicator{
        public void passDataActivityveg(String someValue);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_item_veg, container, false);
        final GlobalClass globalVariable = (GlobalClass) getActivity().getApplicationContext();
        path = globalVariable.getconstr();
        user = globalVariable.getUsername();
        pass = globalVariable.getloginPassword();

        Displayitem();

        mPlanetlist1.clear();

        new getAllCount().execute();

        new getVegItem().execute();
        mPlanetlist1.clear();

        new getAllItemcount().execute();


        recyclerView = (RecyclerView) view.findViewById(R.id.recycleVeg);
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

    class getVegItem extends AsyncTask<Void, Void, String>
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
            String url = path + "Products/GetVegNonVegProducts";
            Log.d("Url: ", "> " + url);

            try{
                List<NameValuePair> params2 = new ArrayList<>();
                params2.add(new BasicNameValuePair("ProductTypeName", "Biryani"));
                params2.add(new BasicNameValuePair("ProductType", "Veg"));
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
//                int t1 =0;
//                t1 = recyclerView.getAdapter().getItemCount();
//
//                for (int i=0; i<t1; i++)
//                {
                    if (user != null && pass != null)
                    {
                        ItemPlanet planet1 = mPlanetlist1.get(position);
                        subitem1 = planet1.getSubItemname();
                        rate1 = planet1.getRate();
//                }
                        new getAllItemcount().execute();

                        try {
                            Thread.sleep(1000);
                        } catch (Exception e) {}

                        if (Response.equals("0"))
                        {
                            count++;
                            cnt = String.valueOf(count);
                            new RegisterData().execute();
                        }
                        else
                        {
                            Toast.makeText(getActivity(), "Item Already Added", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getActivity(),EditCartActivity.class);
                            startActivity(intent);
                        }

                        activityCommunicatorveg.passDataActivityveg(cnt);

                    }else {
                        Toast.makeText(getActivity(), "Login first", Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void iconFavouriteImageViewOnClick(View v, int position) {

                }

                @Override
                public void iconDarkFavouriteImageViewOnClick(View v, int position) {

                }

            });



        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = getActivity();
        activityCommunicatorveg = (ActivityCommunicator)context;


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

    class getAllCount extends AsyncTask<Void, Void, String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progress=new ProgressDialog(MainActivity.this);
//            progress.setMessage("Loading...");
//            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            progress.setIndeterminate(true);
//            progress.setProgress(0);
//            progress.show();
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

    public class RegisterData extends AsyncTask<String,String,String>
    {
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

            if (Status == 1)
            {
                Toast.makeText(getActivity(), "Item add in cart", Toast.LENGTH_LONG).show();
                //new getAllItemcount().execute();
            }

        }

    }


}
