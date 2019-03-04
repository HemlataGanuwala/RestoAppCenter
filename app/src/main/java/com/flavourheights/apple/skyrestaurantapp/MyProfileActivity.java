package com.flavourheights.apple.skyrestaurantapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class MyProfileActivity extends AppCompatActivity {

    EditText editTextfirstname, editTextlastname,editTextemailid, editTextmobileno, editTexteatingpreference;
    ImageView imageViewback;
    RadioButton radioButtonmale, radioButtonfemale;
    Button buttonsave;
    String name, emailid, mobileno, gender, eatingpreferance,path,user,custfname,custlname;
    ServiceHandler shh;
    int Status = 1;
    ProgressDialog progress;
    boolean valid = true;
    public String EMAIL_ADDRESS_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        path = globalVariable.getconstr();
        user = globalVariable.getUsername();
        mobileno = globalVariable.getMobileNo();

        editTextfirstname = (EditText) findViewById(R.id.etmyprofilefname);
        editTextlastname = (EditText) findViewById(R.id.etmyprofillastename);
        editTextmobileno = (EditText) findViewById(R.id.etprofilemobileno);
        editTextemailid = (EditText) findViewById(R.id.etmyprofileemail);
        editTexteatingpreference = (EditText) findViewById(R.id.eteatingpreference);

        radioButtonfemale = (RadioButton)findViewById(R.id.rgbfemale);
        radioButtonmale = (RadioButton)findViewById(R.id.rgbmale);

        buttonsave = (Button)findViewById(R.id.btnsaveprofile);

        imageViewback = (ImageView) findViewById(R.id.imgmyprofileback);

        editTexteatingpreference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProfileActivity.this, EatingPreferenceActivity.class);
                startActivity(intent);
            }
        });



        new getCustomerData().execute();

        display();

        imageViewback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProfileActivity.this, MyAccountActivity.class);
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
            eatingpreferance = (String)bundle.get("EatingPreference");
        }

        editTexteatingpreference.setText(eatingpreferance);
    }

    public void insertData()
    {
        custfname = editTextfirstname.getText().toString();
        custlname = editTextlastname.getText().toString();
        user = editTextemailid.getText().toString();
        mobileno = editTextmobileno.getText().toString();

        if (radioButtonmale.isChecked() == true)
        {
            gender = "Male";

        }else {
            gender = "Female";
        }

        if (validation()) {
            new UpdateRegisterData().execute();
        }
    }

    public boolean validation()
    {
        if (custfname.isEmpty() || !Pattern.matches("[A-Z][a-zA-Z]*",custfname))
        {
            editTextfirstname.setError("Enter Valid Name");
            valid = false;

        }

        else if (custlname.isEmpty() || !Pattern.matches("[a-zA-z]+([ '-][a-zA-Z]+)*",custlname))
        {
            editTextlastname.setError("Enter Valid Surname");
            valid = false;

        }

        else if (user.isEmpty() || (!user.matches(EMAIL_ADDRESS_PATTERN) && user.length()>0))
        {

            editTextemailid.setError("Enter valid Email id");
            valid = false;

        }

        else if (mobileno.isEmpty() || !Pattern.matches("^(?:(?:\\+|0{0,2})91(\\s*[\\-]\\s*)?|[0]?)?[789]\\d{9}$",mobileno)){

            editTextmobileno.setError("Number should be 10-digit");
            valid = false;

        }
        return valid;
    }

    class getCustomerData extends AsyncTask<String,String,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            super.onPreExecute();
            progress=new ProgressDialog(MyProfileActivity.this);
            progress.setMessage("Loading...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setProgress(0);
            progress.show();
        }


        @Override
        protected String doInBackground(String... strings) {

            shh = new ServiceHandler();
            String url = path + "Registration/getCustDetail";

            try {
                List<NameValuePair> params2 = new ArrayList<>();

                params2.add(new BasicNameValuePair("Email", user));
                String Jsonstr = shh.makeServiceCall(url, ServiceHandler.POST, params2);

                if (Jsonstr != null) {
                    JSONObject c1 = new JSONObject(Jsonstr);
                    JSONArray classArray = c1.getJSONArray("Response");

                    for (int i = 0; i < classArray.length(); i++) {

                        JSONObject a1 = classArray.getJSONObject(i);
                        custfname = a1.getString("CustFirstName");
                        custlname = a1.getString("CustLastName");
                        eatingpreferance = a1.getString("EatingPreferance");
                        gender = a1.getString("Gender");
//                        mobileno = a1.getString("PhoneNo");
//                        emailid = a1.getString("Email");

                    }
                } else {
                    Toast.makeText(MyProfileActivity.this, "Data not Found", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progress.dismiss();

            editTextfirstname.setText(custfname);
            editTextlastname.setText(custlname);
            editTextemailid.setText(user);
            editTextmobileno.setText(mobileno);
            if (!eatingpreferance.equals("null"))
            {
                editTexteatingpreference.setText(eatingpreferance);
            }else {
                editTexteatingpreference.setText("");
            }

            if (gender.equals("Female")){

                radioButtonfemale.isChecked();
            }else {
                radioButtonmale.isChecked();
            }


        }

    }

    class UpdateRegisterData extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=new ProgressDialog(MyProfileActivity.this);
            progress.setMessage("Loading...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setProgress(0);
            progress.show();
        }


        @Override
        protected String doInBackground(String... strings) {

            shh = new ServiceHandler();
            String url = path + "Registration/UpdateRegData";

            try {
                List<NameValuePair> params2 = new ArrayList<>();
                params2.add(new BasicNameValuePair("Email",user));
                params2.add(new BasicNameValuePair("CustFirstName",custfname));
                params2.add(new BasicNameValuePair("CustLastName",custlname));
                params2.add(new BasicNameValuePair("EatingPreferance",eatingpreferance));
                params2.add(new BasicNameValuePair("Gender",gender));
                params2.add(new BasicNameValuePair("PhoneNo",mobileno));


                String Jsonstr = shh.makeServiceCall(url ,ServiceHandler.POST , params2);

                if (Jsonstr != null)
                {
                    JSONObject c1= new JSONObject(Jsonstr);
                    Status =c1.getInt("Status");
                }
                else{
                    Toast.makeText(MyProfileActivity.this, "Data not Found", Toast.LENGTH_SHORT).show();
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

            if (Status == 1)
            {
                Toast.makeText(MyProfileActivity.this, "Your profile has been updated", Toast.LENGTH_LONG).show();
            }else {

                Toast.makeText(MyProfileActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }

    }
}
