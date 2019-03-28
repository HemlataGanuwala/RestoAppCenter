package com.flavourheight.apple.skyrestaurantapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.flavourheight.apple.skyrestaurantapp.GoogleHelper.RC_SIGN_IN;

public class MyProfileActivity extends AppCompatActivity {

    EditText editTextfirstname, editTextlastname,editTextemailid, editTextmobileno;
    TextView textVieweatingpreference;
    Spinner spinnereatingpreference;
    ImageView imageViewback, imageViewprofile;
    RadioButton radioButtonmale, radioButtonfemale;
    Button buttonsave;
    String name, emailid, mobileno, gender, eatingpreferance,path,user,custfname,custlname;
    ServiceHandler shh;
    int Status = 1;
    ProgressDialog progress;
    boolean valid = true;
    public String EMAIL_ADDRESS_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String spineatingpreference[] = {"Select Eatingpreferance", "Veg", "Veg/Non-veg"};
    ArrayAdapter arrayAdapter;
    GoogleSignInClient googleSignInClient;
    CallbackManager callbackManager;
    GlobalClass globalVariable;
    private static final String TAG = MyProfileActivity.class.getSimpleName();
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        globalVariable = (GlobalClass) getApplicationContext();
        path = globalVariable.getconstr();
        user = globalVariable.getUsername();
        mobileno = globalVariable.getMobileNo();

        editTextfirstname = (EditText) findViewById(R.id.etmyprofilefname);
        editTextlastname = (EditText) findViewById(R.id.etmyprofillastename);
        editTextmobileno = (EditText) findViewById(R.id.etprofilemobileno);
        editTextemailid = (EditText) findViewById(R.id.etmyprofileemail);

        textVieweatingpreference = (TextView) findViewById(R.id.tveatingpreferance);

        spinnereatingpreference = (Spinner) findViewById(R.id.eteatingpreference);

        radioGroup = (RadioGroup) findViewById(R.id.rggender);
        radioButtonfemale = (RadioButton)findViewById(R.id.rgbfemale);
        radioButtonmale = (RadioButton)findViewById(R.id.rgbmale);

        buttonsave = (Button)findViewById(R.id.btnsaveprofile);

        imageViewback = (ImageView) findViewById(R.id.imgmyprofileback);
        imageViewprofile = (ImageView) findViewById(R.id.imgprofilepic);

//        spinnereatingpreference.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MyProfileActivity.this, EatingPreferenceActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });

//        display();

//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .build();
//        googleSignInClient = GoogleSignIn.getClient(this, gso);
//
//        signIn();

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, spineatingpreference);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnereatingpreference.setAdapter(arrayAdapter);

        new getCustomerData().execute();

        imageViewback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProfileActivity.this, MyAccountActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });

        buttonsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();
            }
        });

    }

//    public void display()
//    {
//        Intent intent = getIntent();
//        Bundle bundle = intent.getExtras();
//        if (bundle != null)
//        {
//            eatingpreferance = (String)bundle.get("EatingPreference");
//        }
//
//        spinnereatingpreference.setText(eatingpreferance);
//    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
            user=editTextemailid.getText().toString().trim();
            globalVariable.setUsername(user);

        }
        else
        {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String email = account.getEmail();
            String FirstName = account.getGivenName();
            String LastName = account.getFamilyName();
            Uri givenname1 = account.getPhotoUrl();
            user = email;
            editTextemailid.setText(user);
            editTextfirstname.setText(FirstName);
            editTextlastname.setText(LastName);

            if (givenname1 != null) {
                imageViewprofile.setImageURI(givenname1);
            }else {
                Drawable image = imageViewprofile.getResources().getDrawable(R.drawable.myprofile);
                imageViewprofile.setImageDrawable(image);
            }


        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG,"signInResult:failed code=" + e.getStatusCode());
        }
    }


    public void insertData()
    {
        custfname = editTextfirstname.getText().toString();
        custlname = editTextlastname.getText().toString();
        user = editTextemailid.getText().toString();
        mobileno = editTextmobileno.getText().toString();
        eatingpreferance = spinnereatingpreference.getSelectedItem().toString();

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
            String url = path + "Registrations/GetLogin";

            try {
                List<NameValuePair> params2 = new ArrayList<>();

                params2.add(new BasicNameValuePair("Email", user));
                String Jsonstr = shh.makeServiceCall(url, ServiceHandler.POST, params2);

                if (Jsonstr != null) {
                    JSONObject c1 = new JSONObject(Jsonstr);
                    JSONArray classArray = c1.getJSONArray("response");

                    for (int i = 0; i < classArray.length(); i++) {

                        JSONObject a1 = classArray.getJSONObject(i);
                        custfname = a1.getString("custFirstName");
                        custlname = a1.getString("custLastName");
                        eatingpreferance = a1.getString("eatingPreferance");
                        gender = a1.getString("gender");
                        emailid = a1.getString("email");
                        mobileno = a1.getString("phoneNo");
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
            editTextemailid.setText(emailid);
            editTextmobileno.setText(mobileno);
//            eatingpreferance = spinnereatingpreference.getSelectedItem().toString();

//            if (!eatingpreferance.equals("null"))
//            {
//                editTexteatingpreference.setText(eatingpreferance);
//            }else {
//                editTexteatingpreference.setText("");
//            }

            if (eatingpreferance.equals("Veg"))
            {
                spinnereatingpreference.setVisibility(View.GONE);
                textVieweatingpreference.setVisibility(View.VISIBLE);
                textVieweatingpreference.setText(eatingpreferance);
            }else if(eatingpreferance.equals("Veg/Non-veg"))
            {
                spinnereatingpreference.setVisibility(View.GONE);
                textVieweatingpreference.setVisibility(View.VISIBLE);
                textVieweatingpreference.setText(eatingpreferance);
            }else {
                spinnereatingpreference.setVisibility(View.VISIBLE);
                textVieweatingpreference.setVisibility(View.GONE);
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
            String url = path + "Registrations/UpdateRegistrations";

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
                    Status =c1.getInt("status");
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
                Toast.makeText(MyProfileActivity.this, "Your profile has been saved", Toast.LENGTH_LONG).show();
            }else {

                Toast.makeText(MyProfileActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }

    }
}
