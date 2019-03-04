package com.flavourheights.apple.skyrestaurantapp;

import android.content.Intent;
        import android.os.AsyncTask;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.view.View;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.flavourheights.apple.skyrestaurantapp.Adapter.AddressAdapter;
        import com.flavourheights.apple.skyrestaurantapp.Adapter.MyAddressAdapter;
        import com.flavourheights.apple.skyrestaurantapp.Model.AddressPlanet;

        import org.apache.http.NameValuePair;
        import org.apache.http.message.BasicNameValuePair;
        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.ArrayList;
        import java.util.List;

public class MyAddressActivity extends AppCompatActivity {

    ImageView imageViewaddressback;
    TextView textViewaddaddress;
    String path,user;
    RecyclerView recyclerView;
    List<AddressPlanet> mPlanetlist= new ArrayList<AddressPlanet>();
    String datahouseno, datalandmark, addresstype, datalocality, datacity, dataaddresstype, datapincode, datausername;
    MyAddressAdapter adapter;
    ServiceHandler shh;
    int Status = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_address);

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        path = globalVariable.getconstr();
        user = globalVariable.getUsername();

        imageViewaddressback = (ImageView) findViewById(R.id.imgvaddressback);
        textViewaddaddress = (TextView) findViewById(R.id.tvaddaddress);

        recyclerView = (RecyclerView) findViewById(R.id.rvmyaddress);
        recyclerView.setLayoutManager(new LinearLayoutManager(MyAddressActivity.this));


        imageViewaddressback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyAddressActivity.this, MyAccountActivity.class);
                startActivity(intent);
            }
        });

        textViewaddaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyAddressActivity.this, AddAddressActivity.class);
                startActivity(intent);
            }
        });

        mPlanetlist.clear();
        new getAddressData().execute();
    }

    class getAddressData extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... strings) {

            shh = new ServiceHandler();
            String url = path + "Registration/getAddressData";

            try {
                List<NameValuePair> params2 = new ArrayList<>();

                params2.add(new BasicNameValuePair("UserName",user));
                String Jsonstr = shh.makeServiceCall(url ,ServiceHandler.POST , params2);

                if (Jsonstr != null) {
                    JSONObject c1 = new JSONObject(Jsonstr);
                    JSONArray classArray = c1.getJSONArray("Response");

                    for (int i = 0; i < classArray.length(); i++) {


                        JSONObject a1 = classArray.getJSONObject(i);
                        datahouseno = a1.getString("HouseNo");
                        datalandmark = a1.getString("AppartmentName");
                        datalocality = a1.getString("Location");
                        datacity = a1.getString("City");
                        dataaddresstype = a1.getString("AddressType");
                        datapincode = a1.getString("Pincode");
                        datausername = a1.getString("CustName");

                        addresstype = "[" + dataaddresstype + "]";

                        AddressPlanet planet = new AddressPlanet(datausername,addresstype,datahouseno, datalandmark,datalocality,datacity,datapincode);
                        mPlanetlist.add(planet);


                    }
                }
                else{
                    Toast.makeText(MyAddressActivity.this, "Data not Found", Toast.LENGTH_SHORT).show();
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

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter=new MyAddressAdapter(mPlanetlist);
                    recyclerView.setAdapter(adapter);

                }
            });

            adapter.setOnItemClickListner(new MyAddressAdapter.OnItemClickListner() {
                @Override
                public void onItemClick(int position) {

                }

                @Override
                public void icondeleteImageViewOnClick(View v, int position) {
                    new DeleteAddress().execute();
                }
            });

        }

    }

    public class DeleteAddress extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... strings) {

            shh = new ServiceHandler();
            String url = path + "Registration/DeleteAddressData";

            try {
                List<NameValuePair> params2 = new ArrayList<>();

                params2.add(new BasicNameValuePair("UserName",user));

                String Jsonstr = shh.makeServiceCall(url ,ServiceHandler.POST , params2);

                if (Jsonstr != null)
                {
                    JSONObject c1= new JSONObject(Jsonstr);
                    Status =c1.getInt("Status");
                }
                else{
                    Toast.makeText(MyAddressActivity.this, "Data not Found", Toast.LENGTH_SHORT).show();
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
}
