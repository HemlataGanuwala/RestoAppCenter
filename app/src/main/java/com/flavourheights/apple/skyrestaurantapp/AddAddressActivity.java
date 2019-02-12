package com.flavourheights.apple.skyrestaurantapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddAddressActivity extends AppCompatActivity {

    Spinner spinnercity, spinnerlocation;
    ImageView imageViewback;
    EditText editTexthouseno, editTextapartment, editTextotheraddress;
    Button buttonsave;
    ServiceHandler shh;
    String path, houseno, appartmentname, otheraddress, city, location;
    int Status=1;
    boolean valid=true;
    private String spcity[]={"Select City","Nagpur"};
    private String splocation[]={"Select Location","Nandanvan", "New Nandanva", "Manish Nagar", "Ganesh Peth", "Bhande Plot", "Jagnade Square", "Sakardara", "Mahal", "Zashi Rani Square Bardi", "Railway Station", "Sadar"};

    ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        path = globalVariable.getconstr();

        imageViewback=(ImageView)findViewById(R.id.imgback);

        editTextotheraddress=(EditText)findViewById(R.id.etotheradd);
        editTexthouseno=(EditText)findViewById(R.id.ethouseno);
        editTextapartment=(EditText)findViewById(R.id.etlocality);

        buttonsave=(Button)findViewById(R.id.btnsave);

        spinnercity=(Spinner)findViewById(R.id.spincity);
        spinnerlocation=(Spinner)findViewById(R.id.spinlocation);

        adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, spcity);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnercity.setAdapter(adapter);

//        spinnerbranch.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, splocation);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerlocation.setAdapter(adapter);

        imageViewback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(AddAddressActivity.this, PlaceOrderActivity.class);
                startActivity(intent);
            }
        });

        buttonsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();
            }
        });
    }

    public void insertData()
    {
        city=spinnercity.getSelectedItem().toString();
        location=spinnerlocation.getSelectedItem().toString();
        houseno=editTexthouseno.getText().toString();
        appartmentname=editTextapartment.getText().toString();
        otheraddress=editTextotheraddress.getText().toString();

        if (validation())
        {
            new getAddress().execute();
        }

    }

    public boolean validation()
    {
        if (city.isEmpty())
        {
            ((TextView)spinnercity.getSelectedView()).setError("Please Select City");
        }
        if (location.isEmpty())
        {
            ((TextView)spinnerlocation.getSelectedView()).setError("Please Select Location");
        }
        if (houseno.isEmpty())
        {
            editTexthouseno.setError("Enter House No");
            valid=false;
        }
        if (appartmentname.isEmpty())
        {
            editTextapartment.setError("Enter Appartment Name");
        }
        return valid;
    }

    public class getAddress extends AsyncTask<String, String, String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            shh = new ServiceHandler();
            String url = path + "Registration/SaveAddress";

            try {
                List<NameValuePair> params2 = new ArrayList<>();
                params2.add(new BasicNameValuePair("City",city));
                params2.add(new BasicNameValuePair("Location",location));
                params2.add(new BasicNameValuePair("HouseNo",houseno));
                params2.add(new BasicNameValuePair("AppartmentName",appartmentname));
                params2.add(new BasicNameValuePair("OtherAdd",otheraddress));

                String Jsonstr = shh.makeServiceCall(url ,ServiceHandler.POST , params2);

                if (Jsonstr != null)
                {
                    JSONObject c1= new JSONObject(Jsonstr);
                    Status =c1.getInt("Status");
                }
                else{

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

            if (Status==1){

                Toast.makeText(AddAddressActivity.this, "Address Save Successfully", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AddAddressActivity.this, PlaceOrderActivity.class);
                intent.putExtra("City", city);
                intent.putExtra("Location", location);
                intent.putExtra("HouseNo", houseno);
                intent.putExtra("AppartmentName", appartmentname);
                intent.putExtra("OtherAddress", otheraddress);
                startActivity(intent);

            }
        }
    }
}
