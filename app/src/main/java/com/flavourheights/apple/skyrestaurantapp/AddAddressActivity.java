package com.flavourheights.apple.skyrestaurantapp;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AddAddressActivity extends AppCompatActivity{

    //    Spinner spinnercity, spinnerlocation;
    ImageView imageViewback;
    EditText editTexthouseno, editTextlocality, editTextlandmark,editTextpincode, editTextname, editTextmobileno, editTextalternateno;
    TextView textViewcity, textViewlocation,  textViewselectlocation, textViewstate;
    Spinner textViewselectcity, spinnerstate;
    RadioButton radioButtonhome, radioButtonoffice;
    Button buttonsave, buttonlocation;
    ServiceHandler shh;
    String path, houseno, landmark, locality, city, pincode, area, custname, addresstype, selectedcity, state;
    int Status = 1;
    boolean valid = true;
    private LocationManager locationManager = null;
    private LocationListener locationListener = null;
    private Boolean flag = false;
    ArrayAdapter adapter;
    AppLocationService appLocationService;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    String user, custfname, custlname, mobileno, alternateno, phoneno;
    GPSTracker gpsTracker;
    double latitude,longitude;
    LinearLayout linearLayout;
    ArrayAdapter arrayAdaptercity;
    ArrayAdapter arrayAdapterstate;

    String spincity[]={"Select City","Nagpur","Amravati","Akola","Pune","Mumbai","Thane","Aurangabad","Kolhapur","Solapur","Nashik","Agra",
    "Allahabad", "Jhansi", "Lucknow", "Kanpur", "Meerut", "Varanasi", "Raipur", "Bhilai","Bilaspur", "Rajnandgaon"};

    String spinstate[]={"Select State","Maharashtra", "Uttar Pradesh", "Chhattisgadh"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        path = globalVariable.getconstr();
        user = globalVariable.getUsername();
//        mobileno = globalVariable.getMobileNo();
        linearLayout = (LinearLayout)findViewById(R.id.linearlayout);
        editTextname = (EditText) findViewById(R.id.etaddressname);
        editTextmobileno = (EditText) findViewById(R.id.etaddressmobileno);
        editTextalternateno = (EditText) findViewById(R.id.etaddressalternateno);

//        display();

        new getCustomerData().execute();

        gpsTracker = new GPSTracker(AddAddressActivity.this);

        // check if GPS enabled
        if(gpsTracker.canGetLocation()){

            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
        }

        else{
            gpsTracker.showSettingsAlert();
        }


        imageViewback = (ImageView) findViewById(R.id.imgback);

        editTextlandmark = (EditText) findViewById(R.id.etlandmark);
        editTexthouseno = (EditText) findViewById(R.id.ethouseno);
        editTextlocality = (EditText) findViewById(R.id.etlocality);
        editTextpincode = (EditText) findViewById(R.id.etpincode);


        textViewselectcity = (Spinner) findViewById(R.id.spincity);
        spinnerstate = (Spinner) findViewById(R.id.spinstate);
        textViewcity = (TextView) findViewById(R.id.tvcity);
        textViewstate = (TextView) findViewById(R.id.tvstate);

        radioButtonhome = (RadioButton)findViewById(R.id.rbhome);
        radioButtonoffice = (RadioButton)findViewById(R.id.rboffice);

        arrayAdaptercity = new ArrayAdapter(this, android.R.layout.simple_spinner_item, spincity);
        arrayAdaptercity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        textViewselectcity.setAdapter(arrayAdaptercity);

        arrayAdapterstate = new ArrayAdapter(this, android.R.layout.simple_spinner_item, spinstate);
        arrayAdapterstate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerstate.setAdapter(arrayAdapterstate);

//        textViewselectcity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String selectedItemText = (String) parent.getItemAtPosition(position);
//                textViewcity.setText(selectedItemText);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        checkLocationPermission();




        buttonsave = (Button) findViewById(R.id.btnsave);
        buttonlocation = (Button) findViewById(R.id.btnselectlocation);
        buttonlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
                if(gpsTracker.canGetLocation()){

                    latitude = gpsTracker.getLatitude();
                    longitude = gpsTracker.getLongitude();
                    Geocoder geocoder = new Geocoder(AddAddressActivity.this, Locale.getDefault());
                    List<Address> addresses = null;
                    try {
                        addresses = geocoder.getFromLocation(latitude, longitude, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//                    String address = addresses.get(0).getAddressLine(0);
                     pincode = addresses.get(0).getPostalCode();
                     area = addresses.get(0).getSubLocality();
                     houseno = addresses.get(0).getPremises();
                     city = addresses.get(0).getLocality();
                     state = addresses.get(0).getAdminArea();

                    //textViewcity.setText(addresses.get(0).getFeatureName() + ", " + addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName());
//                    String result = "Latitude: " + gpsTracker.getLatitude() +
//                            " Longitude: " + gpsTracker.getLongitude();

                    linearLayout.setVisibility(View.VISIBLE);
                    textViewcity.setVisibility(View.VISIBLE);
                    textViewstate.setVisibility(View.VISIBLE);

                    textViewcity.setText(city);
                    textViewstate.setText(state);
//                    textViewlocation.setText(address);
                    editTextpincode.setText(pincode);
                    editTextlocality.setText(area);
                    editTexthouseno.setText(houseno);

//                    LocationAddress locationAddress = new LocationAddress();
//                    locationAddress.getAddressFromLocation(latitude, longitude,
//                            getApplicationContext(), new GeocoderHandler());
                }

                else{
                    gpsTracker.showSettingsAlert();
                }
//                if (gpsLocation != null) {
//                    double latitude = gpsLocation.getLatitude();
//                    double longitude = gpsLocation.getLongitude();
//                    String result = "Latitude: " + gpsLocation.getLatitude() +
//                            " Longitude: " + gpsLocation.getLongitude();
//                    textViewcity.setText(result);
//
//                    LocationAddress locationAddress = new LocationAddress();
//                    locationAddress.getAddressFromLocation(latitude, longitude,
//                            getApplicationContext(), new GeocoderHandler());
//                } else {
//                    showSettingsAlert();
//                }


            }
        });


//        spinnercity=(Spinner)findViewById(R.id.spincity);
//        spinnerlocation=(Spinner)findViewById(R.id.spinlocation);
//
//        adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, spcity);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnercity.setAdapter(adapter);
//        spinnercity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                TextView spin_city=(TextView) view;
//                textViewcity.setText(spinnercity.getSelectedItem().toString());
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
////        spinnerbranch.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
//        adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, splocation);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerlocation.setAdapter(adapter);
//        spinnerlocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                TextView spin_location=(TextView) view;
//                textViewlocation.setText(spinnerlocation.getSelectedItem().toString());
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

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


    public void display()
    {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null)
        {
            custname = (String)bundle.get("CustName");
            phoneno = (String)bundle.get("PhoneNo");
        }

        editTextname.setText(custname);
        editTextmobileno.setText(phoneno);
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(AddAddressActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
//                        locationManager.requestLocationUpdates(provider, 400, 1,this);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }






    public void insertData()
    {
        city=textViewcity.getText().toString();
        pincode = editTextpincode.getText().toString();
        houseno=editTexthouseno.getText().toString();
        landmark=editTextlandmark.getText().toString();
        locality=editTextlocality.getText().toString();
        custname = editTextname.getText().toString();
        phoneno = editTextmobileno.getText().toString();
        alternateno = editTextalternateno.getText().toString();

        city = textViewselectcity.getSelectedItem().toString();
        state = spinnerstate.getSelectedItem().toString();

        if (radioButtonhome.isChecked() == true)
        {
            addresstype="Home";
        }else {
            addresstype = "Office";
        }

        if (validation())
        {
            new getAddress().execute();
        }

    }

    public boolean validation()
    {
        if (houseno.isEmpty())
        {
            editTexthouseno.setError("Enter House No");
            valid=false;
        }
        else if (landmark.isEmpty())
        {
            editTextlandmark.setError("Enter Appartment Name or LandMark");
            valid=false;
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
            String url = path + "Registration/InsertAddress";

            try {
                List<NameValuePair> params2 = new ArrayList<>();
//                params2.add(new BasicNameValuePair("City",city));
//                params2.add(new BasicNameValuePair("Location",location));
                params2.add(new BasicNameValuePair("HouseNo",houseno));
                params2.add(new BasicNameValuePair("AppartmentName",landmark));
                params2.add(new BasicNameValuePair("City",city));
                params2.add(new BasicNameValuePair("Pincode",pincode));
                params2.add(new BasicNameValuePair("UserName",user));
                params2.add(new BasicNameValuePair("Location",locality));
                params2.add(new BasicNameValuePair("AddressType",addresstype));
                params2.add(new BasicNameValuePair("CustName",custname));
                params2.add(new BasicNameValuePair("PhoneNo",phoneno));
                params2.add(new BasicNameValuePair("AlternameNo",alternateno));
                params2.add(new BasicNameValuePair("State",state));


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

                if (radioButtonhome.isChecked() || radioButtonoffice.isChecked()) {

                    Toast.makeText(AddAddressActivity.this, "Address Save Successfully", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AddAddressActivity.this, PlaceOrderActivity.class);
//                    intent.putExtra("City", city);
//                    intent.putExtra("Pincode", pincode);
//                    intent.putExtra("HouseNo", houseno);
//                    intent.putExtra("AppartmentName", landmark);
//                    intent.putExtra("Locality", locality);
//                    intent.putExtra("AddressType", addresstype);
//                    intent.putExtra("CustName", custname);
                    startActivity(intent);
                }else {

                    Toast.makeText(AddAddressActivity.this, "Select Address Type", Toast.LENGTH_LONG).show();
                }

            }
        }
    }

    class getCustomerData extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... strings) {

            shh = new ServiceHandler();
            String url = path + "Registration/getCustDetail";

            try {
                List<NameValuePair> params2 = new ArrayList<>();

                params2.add(new BasicNameValuePair("Email",user));
                String Jsonstr = shh.makeServiceCall(url ,ServiceHandler.POST , params2);

                if (Jsonstr != null) {
                    JSONObject c1 = new JSONObject(Jsonstr);
                    JSONArray classArray = c1.getJSONArray("Response");

                    for (int i = 0; i < classArray.length(); i++) {

                        JSONObject a1 = classArray.getJSONObject(i);
                        custfname = a1.getString("CustFirstName");
                        custlname = a1.getString("CustLastName");
                        mobileno = a1.getString("PhoneNo");

                        custname = custfname + " " + custlname;
                    }
                }
                else{
                    Toast.makeText(AddAddressActivity.this, "Data not Found", Toast.LENGTH_SHORT).show();
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

            editTextname.setText(custname);
            editTextmobileno.setText(mobileno);

        }

    }

}
