package com.flavourheight.apple.skyrestaurantapp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

public class AddAddress2Activity extends AppCompatActivity {

    ImageView imageViewback;
    EditText editTexthouseno, editTextlocality, editTextlandmark,editTextpincode, editTextname, editTextmobileno, editTextalternateno;
    TextView textViewcity, textViewstate,  textViewselectlocation;
    Spinner textViewselectcity, spinnerstate;
    RadioButton radioButtonhome, radioButtonoffice;
    Button buttonsave, buttonlocation;
    ServiceHandler shh;
    String path, houseno, landmark, locality, city, pincode, otheradd, area, custname, addresstype, selectedcity, pass, state, alternateno, custfname, custlname;
    int Status = 1;
    boolean valid = true;
    private LocationManager locationManager = null;
    private LocationListener locationListener = null;
    private Boolean flag = false;
    ArrayAdapter adapter;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    String user, mobileno;
    GPSTracker gpsTracker;
    double latitude,longitude;
    LinearLayout linearLayout, linearLayoutspin;
    ArrayAdapter arrayAdapter;
    ArrayAdapter arrayAdapterstate;

    String spincity[]={"Select City","Nagpur","Amravati","Akola","Pune","Mumbai","Thane","Aurangabad","Kolhapur","Solapur","Nashik","Agra",
            "Allahabad", "Jhansi", "Lucknow", "Kanpur", "Meerut", "Varanasi", "Raipur", "Bhilai","Bilaspur", "Rajnandgaon"};

    String spinstate[]={"Select State","Maharashtra", "Uttar Pradesh", "Chhattisgadh"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address2);

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        path = globalVariable.getconstr();
        user = globalVariable.getUsername();
        pass = globalVariable.getloginPassword();
        mobileno = globalVariable.getMobileNo();

        editTextname = (EditText) findViewById(R.id.etaddressname);
        editTextmobileno = (EditText) findViewById(R.id.etaddressmobileno);
        editTextalternateno = (EditText) findViewById(R.id.etaddressalternateno);

        new getCustomerData().execute();

//        display();

        gpsTracker = new GPSTracker(AddAddress2Activity.this);

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

        linearLayout = (LinearLayout)findViewById(R.id.linearlayout);
        linearLayoutspin = (LinearLayout)findViewById(R.id.lineaspin);

        textViewcity = (TextView) findViewById(R.id.tvcity);
        textViewstate = (TextView) findViewById(R.id.tvstate);

        textViewselectcity = (Spinner) findViewById(R.id.spincity);
        spinnerstate = (Spinner) findViewById(R.id.spinstate);

        radioButtonhome = (RadioButton)findViewById(R.id.rbhome);
        radioButtonoffice = (RadioButton)findViewById(R.id.rboffice);

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, spincity);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        textViewselectcity.setAdapter(arrayAdapter);

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
                    Geocoder geocoder = new Geocoder(AddAddress2Activity.this, Locale.getDefault());
                    List<Address> addresses = null;
                    try {
                        addresses = geocoder.getFromLocation(latitude, longitude, 1);
                        addresses.get(0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//                    String address = addresses.get(0).getAddressLine(0);


                    linearLayout.setVisibility(View.VISIBLE);
                    textViewcity.setVisibility(View.VISIBLE);
                    textViewstate.setVisibility(View.VISIBLE);

                    linearLayoutspin.setVisibility(View.GONE);
                    textViewselectcity.setVisibility(View.GONE);
                    spinnerstate.setVisibility(View.GONE);

                    pincode = addresses.get(0).getPostalCode();
                    area = addresses.get(0).getSubLocality();
                    houseno = addresses.get(0).getPremises();
                    city = addresses.get(0).getLocality();

                    //textViewcity.setText(addresses.get(0).getFeatureName() + ", " + addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName());
//                    String result = "Latitude: " + gpsTracker.getLatitude() +
//                            " Longitude: " + gpsTracker.getLongitude();


                    textViewcity.setText(city);
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
                Intent intent= new Intent(AddAddress2Activity.this, MainActivity.class);
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
        }
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
                                ActivityCompat.requestPermissions(AddAddress2Activity.this,
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
//        city=textViewcity.getText().toString();
        pincode = editTextpincode.getText().toString();
        houseno=editTexthouseno.getText().toString();
        landmark=editTextlandmark.getText().toString();
        locality=editTextlocality.getText().toString();
        custname = editTextname.getText().toString();
        mobileno = editTextmobileno.getText().toString();
        alternateno = editTextalternateno.getText().toString();

        if (!spinnerstate.equals("Select State") && !textViewselectcity.equals("Select City")) {
            city = textViewselectcity.getSelectedItem().toString();
            state = spinnerstate.getSelectedItem().toString();
        }else {
            city=textViewcity.getText().toString();
            state=textViewstate.getText().toString();
        }


        if (radioButtonhome.isChecked() == true)
        {
            addresstype="Home";
        }else {
            addresstype = "Office";
        }



        if (validation())
        {
            new AddAddress2Activity.getAddress().execute();
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
            String url = path + "Address/InsertAddress";

            try {
                List<NameValuePair> params2 = new ArrayList<>();
//                params2.add(new BasicNameValuePair("City",city));
//                params2.add(new BasicNameValuePair("Location",location));
                params2.add(new BasicNameValuePair("CustomerName",custname));
                params2.add(new BasicNameValuePair("PhoneNo",mobileno));
                params2.add(new BasicNameValuePair("AlternateNo",alternateno));
                params2.add(new BasicNameValuePair("City",city));
                params2.add(new BasicNameValuePair("HouseNo",houseno));
                params2.add(new BasicNameValuePair("AppartmentName",landmark));
                params2.add(new BasicNameValuePair("AddressType",addresstype));
                params2.add(new BasicNameValuePair("Location",locality));
                params2.add(new BasicNameValuePair("UserName",user));
                params2.add(new BasicNameValuePair("PinCode",pincode));
                params2.add(new BasicNameValuePair("State",state));
                params2.add(new BasicNameValuePair("Status","0"));

                String Jsonstr = shh.makeServiceCall(url ,ServiceHandler.POST , params2);

                if (Jsonstr != null)
                {
                    JSONObject c1= new JSONObject(Jsonstr);
                    Status =c1.getInt("status");
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

                    Toast.makeText(AddAddress2Activity.this, "Address Save Successfully", Toast.LENGTH_LONG).show();

                    editTextname.setText(" ");
                    editTextmobileno.setText(" ");
                    editTextalternateno.setText(" ");
                    editTextpincode.setText(" ");
                    editTexthouseno.setText(" ");
                    editTextlandmark.setText(" ");
                    editTextlocality.setText(" ");
                    radioButtonhome.setChecked(false);
                    radioButtonoffice.setChecked(false);

                }else {

                    Toast.makeText(AddAddress2Activity.this, "Something went wrong", Toast.LENGTH_LONG).show();
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
            String url = path + "Registrations/GetLogin";

            try {
                List<NameValuePair> params2 = new ArrayList<>();

                params2.add(new BasicNameValuePair("Email",user));
                params2.add(new BasicNameValuePair("PhoneNo",user));
//                params2.add(new BasicNameValuePair("Password",pass));
                String Jsonstr = shh.makeServiceCall(url ,ServiceHandler.POST , params2);

                if (Jsonstr != null) {
                    JSONObject c1 = new JSONObject(Jsonstr);
                    JSONArray classArray = c1.getJSONArray("response");

                    for (int i = 0; i < classArray.length(); i++) {

                        JSONObject a1 = classArray.getJSONObject(i);
                        custfname = a1.getString("custFirstName");
                        custlname = a1.getString("custLastName");
                        mobileno = a1.getString("phoneNo");


                        custname = custfname + " " + custlname;
                    }
                }
                else{
                    Toast.makeText(AddAddress2Activity.this, "Data not Found", Toast.LENGTH_SHORT).show();
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
