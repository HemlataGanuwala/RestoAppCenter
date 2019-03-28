package com.flavourheight.apple.skyrestaurantapp;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.flavourheight.apple.skyrestaurantapp.Adapter.AddressAdapter;
import com.flavourheight.apple.skyrestaurantapp.Model.AddressPlanet;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

public class PlaceOrderActivity extends AppCompatActivity implements AddressAdapter.OnItemClickListner, AddressAdapter.OnAddressImageViewClickListner{

    TextView textViewcost, textViewdate, textViewenteraddress, textViewselectaddress;
    EditText editTextaddress1,editTextaddress2,editTextaddress3,textViewmobileno;
    ImageView imageViewback;
    RadioButton radioButtoncash, radioButtononline, radioButtoncashondelivery;
    Button buttonproceed,buttonaddaddress;
    private int mYear, mMonth, mDay, mHour, mMinite, sday, smonth, syear, shour, sminute;
    String path, addresstype,  cartstatus, totalcount, password, stime, mdate, sdate, mobileno, address,totalamount, cdate, paymentmode, user, mtime, refferamount, custname, custfname, custlname, totalcost, amount;
    boolean valid=true;
    ServiceHandler shh;
    int Status=1;
    String pincode,landmark,houseno,locality,city,datastate,datapincode,datalandmark,datahouseno,datalocality,datacity, dataaddresstype, datausername;
    RecyclerView recyclerView;
    List<AddressPlanet> mPlanetlist= new ArrayList<AddressPlanet>();
    AddressAdapter adapter;
    ProgressDialog progress;
    String getaddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        path = globalVariable.getconstr();
        user = globalVariable.getUsername();
        mobileno = globalVariable.getMobileNo();
        password = globalVariable.getloginPassword();
        amount = globalVariable.getOrderCost();


        new getAllItem().execute();

        buttonproceed=(Button)findViewById(R.id.btnproceed);

        textViewcost = (TextView) findViewById(R.id.tvcost);
        textViewdate = (TextView) findViewById(R.id.tvdate);
        textViewmobileno = (EditText) findViewById(R.id.etmobileno);
        textViewenteraddress = (TextView) findViewById(R.id.tventeraddress);
        textViewselectaddress = (TextView) findViewById(R.id.tvselectaddress);

        textViewcost.setText(amount);
        textViewmobileno.setText(mobileno);
//        editTextaddress1 = (EditText)findViewById(R.id.etaddress1);
//        editTextaddress2 = (EditText)findViewById(R.id.etaddress2);
//        editTextaddress3 = (EditText)findViewById(R.id.etaddress3);
        buttonaddaddress = (Button) findViewById(R.id.btnaddaddress);

        radioButtoncash=(RadioButton)findViewById(R.id.rbcashpay);
        radioButtononline=(RadioButton)findViewById(R.id.rbonlinepay);
        radioButtoncashondelivery = (RadioButton) findViewById(R.id.rbcashondelivery);

        recyclerView=(RecyclerView)findViewById(R.id.rvaddaddress);
        recyclerView.setLayoutManager(new LinearLayoutManager(PlaceOrderActivity.this));

        new getCustomerData().execute();

        new getAddressData().execute();

//        display();
        //
//        sdate = DateFormat.getDateInstance().format(new Date());
//        sday = String.format((String) DateFormat, );
//        stime = DateFormat.getTimeInstance().format(new Date());
////        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
////        try {
////            Date newDate = simpleDateFormat.parse(sdate);
////            simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
////            sdate = simpleDateFormat.format(newDate);
////        } catch (ParseException e) {
////            e.printStackTrace();
////



        buttonproceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                insertData();

//                new UpdateCartStatus().execute();

            }
        });

        buttonaddaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlaceOrderActivity.this,AddAddressActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });


        Calendar c= Calendar.getInstance();
        sday = c.get(Calendar.DAY_OF_MONTH);
        smonth = c.get(Calendar.MONTH);
        syear = c.get(Calendar.YEAR);
        shour = c.get(Calendar.HOUR);
        sminute = c.get(Calendar.MINUTE);

        mdate=sday+"-"+ (smonth+1) +"-"+syear;
        mtime=shour+":"+sminute;
        sdate = mdate+"  "+mtime;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(sdate);
        simpleDateFormat.applyPattern("dd/mm/yyyy hh:mm");
        textViewdate.setText(sdate);


//        textViewdate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                datePicker();
//
//            }
//        });


        imageViewback = (ImageView) findViewById(R.id.imgback);
        imageViewback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PlaceOrderActivity.this, CartListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();

            }
        });


    }

    public void display()
    {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle!=null)
        {
            totalamount = (String) bundle.get("Cost");
//            totalcount = (String) bundle.get("Count");
        }
        textViewcost.setText(totalamount);
        textViewmobileno.setText(mobileno);
    }

    public void displayAddress()
    {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle!=null)
        {
            pincode = (String) bundle.get("Pincode");
            landmark = (String) bundle.get("AppartmentName");
            houseno = (String) bundle.get("HouseNo");
            locality = (String) bundle.get("Locality");
            city = (String) bundle.get("City");
        }
        if (pincode != null && landmark != null && houseno != null && locality != null && city != null)
        {
            editTextaddress1.setText(houseno);
            editTextaddress2.setText(landmark+" , "+locality);
            editTextaddress3.setText(city +"-"+ pincode);
        }

    }

    private void datePicker(){

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {

                        mdate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        //*************Call Time Picker Here ********************
                        tiemPicker();
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void tiemPicker(){
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinite = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,int minute) {

                        mHour = hourOfDay;
                        mMinite = minute;

                        mtime= mHour+ ":" + mMinite;

                        cdate= mdate + "  " +mtime;

                        textViewdate.setText(cdate);
                    }
                }, mHour, mMinite, false);
        timePickerDialog.show();
    }

    public void insertData()
    {
//        address=editTextaddress1.getText().toString() +""+ editTextaddress2.getText().toString() +""+ editTextaddress3.getText().toString();
//        address = datahouseno+" "+datalandmark+" "+datalocality+" "+datacity+" "+datapincode;
        mobileno = textViewmobileno.getText().toString();

        if (radioButtoncash.isChecked()==true)
        {
            paymentmode = "Cash Payment";
        }else if (radioButtononline.isChecked() == true){

            paymentmode = "Online Payment";
        }else if (radioButtoncashondelivery.isChecked() == true){
            paymentmode = "Cash on Delivery";
        }else {
            Toast.makeText(PlaceOrderActivity.this, "Select your payment mode", Toast.LENGTH_LONG).show();
        }

        if (validation())
        {

            new PlaceOrderData().execute();


        }
    }

    public boolean validation()
    {

//        if (getaddress.isEmpty())
//        {
//            editTextaddress1.setError("Enter your Address");
//            valid=false;
//        }
         if (mobileno.isEmpty() || !Pattern.matches("^(?:(?:\\+|0{0,2})91(\\s*[\\-]\\s*)?|[0]?)?[789]\\d{9}$",mobileno)){

            textViewmobileno.setError("Enter valid mobile no");
            valid = false;

        }

        else {
            valid=true;
        }

        return valid;
    }

    @Override
    public void onItemClick(int position) {



    }

    @Override
    public void onAddressItemClick(String position) {
        getaddress = position;
//        insertData();

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

                String Jsonstr = shh.makeServiceCall(url ,ServiceHandler.POST , params2);

                if (Jsonstr != null) {
                    JSONObject c1 = new JSONObject(Jsonstr);
                    JSONArray classArray = c1.getJSONArray("response");

                    for (int i = 0; i < classArray.length(); i++) {

                        JSONObject a1 = classArray.getJSONObject(i);
                        custfname = a1.getString("custFirstName");
                        custlname = a1.getString("custLastName");
                        mobileno = a1.getString("phoneNo");
                        refferamount = a1.getString("rAmount");

                        custname = custfname + " " + custlname;
                    }
                }
                else{
                    Toast.makeText(PlaceOrderActivity.this, "Data not Found", Toast.LENGTH_SHORT).show();
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
            textViewmobileno.setText(mobileno);

        }

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
            String url = path + "Address/getAddressData";

            try {
                List<NameValuePair> params2 = new ArrayList<>();

                params2.add(new BasicNameValuePair("UserName",user));
                String Jsonstr = shh.makeServiceCall(url ,ServiceHandler.POST , params2);

                if (Jsonstr != null) {
                    JSONObject c1 = new JSONObject(Jsonstr);
                    JSONArray classArray = c1.getJSONArray("response");

                    for (int i = 0; i < classArray.length(); i++) {


                        JSONObject a1 = classArray.getJSONObject(i);
                        datahouseno = a1.getString("houseNo");
                        datalandmark = a1.getString("appartmentName");
                        datalocality = a1.getString("location");
                        datacity = a1.getString("city");
                        dataaddresstype = a1.getString("addressType");
                        datapincode = a1.getString("pinCode");
                        datausername = a1.getString("customerName");
                        datastate = a1.getString("state");

                        addresstype = "[" + dataaddresstype + "]";

                        AddressPlanet planet = new AddressPlanet(datausername,addresstype,datahouseno, datalandmark,datalocality,datastate,datacity,datapincode);
                        mPlanetlist.add(planet);


                    }
                }
                else{
                    Toast.makeText(PlaceOrderActivity.this, "Data not Found", Toast.LENGTH_SHORT).show();
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


            if (mPlanetlist.isEmpty())
            {
                textViewselectaddress.setVisibility(View.GONE);
            }else {
                textViewenteraddress.setVisibility(View.GONE);
                textViewselectaddress.setVisibility(View.VISIBLE);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter=new AddressAdapter(mPlanetlist);
                    recyclerView.setAdapter(adapter);

                }
            });

            adapter.setOnItemClickListner(PlaceOrderActivity.this);
            adapter.setOnAddressImageViewClickListner(PlaceOrderActivity.this);

        }

    }

    class PlaceOrderData extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progress=new ProgressDialog(PlaceOrderActivity.this);
//            progress.setMessage("Loading...");
//            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            progress.setIndeterminate(true);
//            progress.setProgress(0);
//            progress.show();
        }


        @Override
        protected String doInBackground(String... strings) {

            shh = new ServiceHandler();
            String url = path + "OrderMasters/InsertOrderMaster";

            try {
                List<NameValuePair> params2 = new ArrayList<>();
                params2.add(new BasicNameValuePair("CustomerName",custname));
                params2.add(new BasicNameValuePair("UserName",user));
                params2.add(new BasicNameValuePair("OrderDate",mdate));
                params2.add(new BasicNameValuePair("OrderTime",mtime));
                params2.add(new BasicNameValuePair("TotalAmount",amount));
                params2.add(new BasicNameValuePair("NoOfProduct",totalcount));
                params2.add(new BasicNameValuePair("PhoneNo",mobileno));
                params2.add(new BasicNameValuePair("Address",getaddress));
                params2.add(new BasicNameValuePair("RAmount",refferamount));
                params2.add(new BasicNameValuePair("PaymentMode",paymentmode));
                params2.add(new BasicNameValuePair("Status","1"));

                String Jsonstr = shh.makeServiceCall(url ,ServiceHandler.POST , params2);

                if (Jsonstr != null)
                {
                    JSONObject c1= new JSONObject(Jsonstr);
                    Status =c1.getInt("status");
                }
                else{
                    Toast.makeText(PlaceOrderActivity.this, "Data not Found", Toast.LENGTH_SHORT).show();
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
            progress.dismiss();

            if (Status == 1) {
                if (radioButtoncash.isChecked() || radioButtoncashondelivery.isChecked()) {
//                        Toast.makeText(PlaceOrderActivity.this, "Your Order Place Successfully", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(PlaceOrderActivity.this, MakePaymentActivity.class);
                    intent.putExtra("Cost", amount);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();

                } else if (radioButtononline.isChecked()) {
                    Intent intent = new Intent(PlaceOrderActivity.this, OnlinePaymentActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(PlaceOrderActivity.this, "Please select your payment option", Toast.LENGTH_LONG).show();
                }
            }
        }

    }

//    class OrderData extends AsyncTask<String,String,String>
//    {
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//
//        @Override
//        protected String doInBackground(String... strings) {
//
//            shh = new ServiceHandler();
//            String url = path + "Orders/InsertOrder";
//
//            try {
//                List<NameValuePair> params2 = new ArrayList<>();
//
//                params2.add(new BasicNameValuePair("CustomerName",custname));
//                params2.add(new BasicNameValuePair("UserName",user));
////                params2.add(new BasicNameValuePair("OrderDate",mdate));
////                params2.add(new BasicNameValuePair("OrderTime",""));
//                params2.add(new BasicNameValuePair("TotalAmount",""));
//                params2.add(new BasicNameValuePair("NoOfProduct", totalcount));
//                params2.add(new BasicNameValuePair("PhoneNo",mobileno));
//                params2.add(new BasicNameValuePair("Address",getaddress));
//                params2.add(new BasicNameValuePair("RAmount",refferamount));
//                params2.add(new BasicNameValuePair("PaymentMode",paymentmode));
//                params2.add(new BasicNameValuePair("Status","1"));
//
//                String Jsonstr = shh.makeServiceCall(url ,ServiceHandler.POST , params2);
//
//                if (Jsonstr != null)
//                {
//                    JSONObject c1= new JSONObject(Jsonstr);
//                    Status =c1.getInt("status");
//                }
//                else{
//                    Toast.makeText(PlaceOrderActivity.this, "Data not Found", Toast.LENGTH_SHORT).show();
//                }
//            }
//            catch ( JSONException e){
//                e.printStackTrace();
//            }
//
//
//            return null;
//        }
//
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//
//            if (Status == 1) {
//                  if (radioButtoncash.isChecked() || radioButtoncashondelivery.isChecked()) {
////                        Toast.makeText(PlaceOrderActivity.this, "Your Order Place Successfully", Toast.LENGTH_LONG).show();
//                        Intent intent = new Intent(PlaceOrderActivity.this, MakePaymentActivity.class);
//                        intent.putExtra("Cost", amount);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                        startActivity(intent);
//                        finish();
//
//                    } else if (radioButtononline.isChecked()) {
//                        Intent intent = new Intent(PlaceOrderActivity.this, OnlinePaymentActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                        startActivity(intent);
//                        finish();
//                    } else {
//                        Toast.makeText(PlaceOrderActivity.this, "Please select your payment option", Toast.LENGTH_LONG).show();
//                  }
//            }
//
//        }
//
//
//    }

    class UpdateCartStatus extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progress = new ProgressDialog(PlaceOrderActivity.this);
//            progress.setMessage("Loading...");
//            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            progress.setIndeterminate(true);
//            progress.setProgress(0);
//            progress.show();
        }


        @Override
        protected String doInBackground(String... strings) {

            shh = new ServiceHandler();
            String url = path + "Carts/UpdateStatus";

            try {
                List<NameValuePair> params2 = new ArrayList<>();

                params2.add(new BasicNameValuePair("UserName", user));
                params2.add(new BasicNameValuePair("Status", "1"));

                String Jsonstr = shh.makeServiceCall(url, ServiceHandler.POST, params2);

                if (Jsonstr != null) {
                    JSONObject c1 = new JSONObject(Jsonstr);
                    Status = c1.getInt("Status");
                } else {
                    Toast.makeText(PlaceOrderActivity.this, "Data not Found", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            new getCartData().execute();


        }

    }


    class getCartData extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... strings) {

            shh = new ServiceHandler();
            String url = path + "Cart/GetCartItems";

            try {
                List<NameValuePair> params2 = new ArrayList<>();

                params2.add(new BasicNameValuePair("Username", user));
                params2.add(new BasicNameValuePair("Password", password));
                String Jsonstr = shh.makeServiceCall(url, ServiceHandler.POST, params2);

                if (Jsonstr != null) {
                    JSONObject c1 = new JSONObject(Jsonstr);
                    JSONArray classArray = c1.getJSONArray("Response");

                    for (int i = 0; i < classArray.length(); i++) {

                        JSONObject a1 = classArray.getJSONObject(i);
                        cartstatus = a1.getString("Status");

                    }
                } else {
                    Toast.makeText(PlaceOrderActivity.this, "Data not Found", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            new deleteCartItem().execute();

        }

    }


        class deleteCartItem extends AsyncTask<String, String, String> {
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
                String url = path + "Cart/DeleteCartItem";

                try {
                    List<NameValuePair> params2 = new ArrayList<>();

                    params2.add(new BasicNameValuePair("Username", user));
                    params2.add(new BasicNameValuePair("Status", cartstatus));

                    String Jsonstr = shh.makeServiceCall(url, ServiceHandler.POST, params2);

                    if (Jsonstr != null) {
                        JSONObject c1 = new JSONObject(Jsonstr);
                        Status = c1.getInt("Status");
                    } else {
                        Toast.makeText(PlaceOrderActivity.this, "Data not Found", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                return null;
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

            }

        }


    class getAllItem extends AsyncTask<Void, Void, String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=new ProgressDialog(PlaceOrderActivity.this);
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
//                params2.add(new BasicNameValuePair("Password", password));
                String jsonStr = shh.makeServiceCall(url, ServiceHandler.POST , params2);

                if (jsonStr != null) {
                    JSONObject c1 = new JSONObject(jsonStr);
                    totalcount = c1.getString("response");
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







