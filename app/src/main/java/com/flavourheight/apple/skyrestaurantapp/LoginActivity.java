package com.flavourheight.apple.skyrestaurantapp;

import android.accounts.Account;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
//import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
//import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.flavourheight.apple.skyrestaurantapp.GoogleHelper.RC_SIGN_IN;

public class LoginActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    EditText editTextuserid, editTextpassword;
    TextView textViewregister, textViewforgetpass, textViewrefercode,textView;
    CheckBox checkBoxrefercode;
    SignInButton buttongooglelogin;
    Button buttonlogin,buttonsignout;
    String username, password;
    String path, email, mobile, pass, phoneno, regrefercode, refercode, referuser, refername, referlastname, refermobile, emailid;
    int amount, ramount=15;
    int Status;
    ServiceHandler shh;
    ProgressDialog progress;
    private static final String TAG = LoginActivity.class.getSimpleName();
    NavigationView navigationView;
    private SharedPreferences preferences;
    private DrawerLayout drawerLayout;
    View headerview;
    ImageView imageViewshow;
    Class fragmentClass;
    private static final String PREFS_NAME = "PrefsFile";
    Fragment fragment = null;
    private CheckBox mcheck;
    GoogleSignInClient mGoogleSignInClient;
    private static final String EMAIL = "email";
    LoginButton loginButton;
    DatabaseHelpher databaseHelpher;
    private boolean mIsResolving = false;
    private boolean mShouldResolve = false;
    private GoogleApiClient mGoogleApiClient;
    GlobalClass globalVariable;
    CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    FacebookCallback<LoginResult> callback;

    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        databaseHelpher = new DatabaseHelpher(this);
        display();
        textViewrefercode = (EditText) findViewById(R.id.etregrefercode);
        textViewrefercode.setVisibility(View.GONE);
        buttongooglelogin = (SignInButton)findViewById(R.id.btgoogle);
        buttonsignout = (Button) findViewById(R.id.btnsignout);
        editTextuserid=(EditText)findViewById(R.id.etusername);
        editTextpassword=(EditText)findViewById(R.id.etpassword);
        loginButton = (LoginButton)findViewById(R.id.login_button);

//        FacebookSdk.sdkInitialize(getApplicationContext());

        globalVariable = (GlobalClass) getApplicationContext();
        path = globalVariable.getconstr();

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbarlogin);
        setSupportActionBar(toolbar);

        checkBoxrefercode = (CheckBox)findViewById(R.id.chkrefercode);
        checkBoxrefercode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((checkBoxrefercode).isChecked())
                {
                    textViewrefercode.setVisibility(View.VISIBLE);
//                    regrefercode = textViewrefercode.getText().toString();
//                    new ReferCodeData().execute();
                }else {
                    textViewrefercode.setVisibility(View.GONE);
                }

            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        buttongooglelogin = (SignInButton)findViewById(R.id.btgoogle);
        buttonsignout = (Button)findViewById(R.id.btnsignout);

        buttonsignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        buttongooglelogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();

            }
        });

        preferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        mcheck = (CheckBox)findViewById(R.id.chkrememberme);

        navigationView=(NavigationView)findViewById(R.id.nav_view);
        headerview=navigationView.getHeaderView(0);
        imageViewshow=(ImageView)headerview.findViewById(R.id.imgshow);
        imageViewshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
            }
        });

        preferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        textViewforgetpass=(TextView)findViewById(R.id.tvforgetpass);
        textViewforgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        textViewregister=(TextView)findViewById(R.id.tvregister);
        textViewregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this, RegistrationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });



        buttonlogin=(Button)findViewById(R.id.btnlogin);
        buttonlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new getRegData().execute();
                insertLoginData();

            }
        });

        FacebookSdk.sdkInitialize(getApplicationContext());

        callbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {

                displayMessage(newProfile);

            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();

        callback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                Profile profile = Profile.getCurrentProfile();

                displayMessage(profile);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        };

        loginButton.setReadPermissions("user_friends");
        loginButton.registerCallback(callbackManager, callback);


//        loginButton = (LoginButton) findViewById(R.id.login_button);
//        boolean loggedOut = AccessToken.getCurrentAccessToken() == null;
//
//        if (!loggedOut) {
////            Picasso.with(this).load(Profile.getCurrentProfile().getProfilePictureUri(200, 200)).into(imageView);
//            Log.d("TAG", "Username is: " + Profile.getCurrentProfile().getName());
//
//            //Using Graph API
//            getUserProfile(AccessToken.getCurrentAccessToken());
//        }

//        loginButton.setReadPermissions(Arrays.asList("email", "public_profile"));
//        callbackManager = CallbackManager.Factory.create();
//
//        LoginManager.getInstance().registerCallback(callbackManager,
//                new FacebookCallback<LoginResult>() {
//                    @Override
//                    public void onSuccess(LoginResult loginResult) {
//                        textView.setText("Successfully logged in");
////                        getUserProfile();
//                    }
//
//                    @Override
//                    public void onCancel() {
//                        textView.setText("Login canceled");
//                    }
//
//                    @Override
//                    public void onError(FacebookException exception) {
//                        // App code
//                    }
//                });
//        loginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (AccessToken.getCurrentAccessToken() == null)
//                    textView.setText("Logged out");
//            }
//        });
//
        getPreferencedata();


    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        updateUI(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
            username=editTextuserid.getText().toString().trim();
            globalVariable.setUsername(username);

//            new LoginData().execute();
            databaseHelpher.UpdateRegistrationData(username,1);
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
            username = email;
            editTextuserid.setText(username);
            databaseHelpher.RegistrationData(FirstName,LastName,username,"","",0);
            // Signed in successfully, show authenticated UI.
            updateUI(true);

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(false);
        }
    }

    public void displayMessage(Profile profile)
    {
        if (profile != null)
        {
            String fname = profile.getFirstName();
            String lname = profile.getLastName();
            editTextuserid.setText(profile.getId());
            String url = profile.getProfilePictureUri(150,150).toString();
            Glide.with(getApplicationContext()).load(url).into(imageView);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        Profile profile = Profile.getCurrentProfile();
        displayMessage(profile);
    }

    private void updateUI(boolean signedIn) {
        if (signedIn) {
            buttongooglelogin.setVisibility(View.GONE);
            buttonsignout.setVisibility(View.VISIBLE);
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i);
        } else {

            buttongooglelogin.setVisibility(View.VISIBLE);
            buttonsignout.setVisibility(View.GONE);
        }
    }

    public void display(){
        Intent intent = getIntent();
        Bundle bundle  = intent.getExtras();
        if (bundle != null)
        {
//            mobile = (String)bundle.get("MobileNo");
            emailid = (String)bundle.get("Email");
            pass= (String)bundle.get("Password");
        }
    }

//    private void getUserProfile(AccessToken currentAccessToken) {
//        GraphRequest request = GraphRequest.newMeRequest(
//                currentAccessToken, new GraphRequest.GraphJSONObjectCallback() {
//                    @Override
//                    public void onCompleted(JSONObject object, GraphResponse response) {
//                        Log.d("TAG", object.toString());
//                        try {
//                            String first_name = object.getString("first_name");
//                            String last_name = object.getString("last_name");
//                            String email = object.getString("email");
//                            String id = object.getString("id");
//                            String image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";
//
//                           // txtUsername.setText("First Name: " + first_name + "\nLast Name: " + last_name);
//                            editTextuserid.setText(email);
//                            //Picasso.with(LoginActivity.this).load(image_url).into(imageView);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                });
//
//        Bundle parameters = new Bundle();
//        parameters.putString("fields", "first_name,last_name,email,id");
//        request.setParameters(parameters);
//        request.executeAsync();
//
//    }


    private void getPreferencedata() {
        SharedPreferences sp = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if (sp.contains("pref_name")) {
            String u = sp.getString("pref_name", "not found");
            editTextuserid.setText(u.toString());
        }
        if (sp.contains("pref_pass")) {
            String p = sp.getString("pref_pass", "not found");
            editTextpassword.setText(p.toString());
        }
        if (sp.contains("pref_check")) {
            Boolean c = sp.getBoolean("pref_check", false);
            mcheck.setChecked(c);
        }
    }

    public void insertLoginData()
    {
        username=editTextuserid.getText().toString().trim();
        password=editTextpassword.getText().toString().trim();

        GetData();

        if ((checkBoxrefercode).isChecked())
        {
            textViewrefercode.setVisibility(View.VISIBLE);
            regrefercode = textViewrefercode.getText().toString();
            if (regrefercode != null)
            {
                new getReferData().execute();
                new ReferCodeData().execute();
                new UpdateReferCodeData().execute();
            }

        }

        new LoginData().execute();

    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        googleHelper.onStart();
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        callbackManager.onActivityResult(requestCode, resultCode, data);
////        super.onActivityResult(requestCode, resultCode, data);
////        googleHelper.onActivityResult(requestCode, resultCode, data);
////        facebookHelper.onActivityResult(requestCode, resultCode, data);
////        if (isFbLogin) {
////            isFbLogin = false;
////        }
//    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_menu:
                Intent intent8= new Intent(getApplicationContext(), MainActivity.class);
                intent8.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent8);
                break;

            case R.id.nav_login:
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;


            case R.id.nav_about:
                Intent intent1 = new Intent(getApplicationContext(), AboutActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent1);
                break;

            case R.id.nav_offer:
                Intent intent2 = new Intent(getApplicationContext(), OfferActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent2);
                break;

            case R.id.nav_share:
                Intent intent3 = new Intent(Intent.ACTION_SEND);
                intent3.setType("text/plain");
                String shareBody = "https://drive.google.com/open?id=1zrD8iqWKn5YSqFTTZ4GnNcu5FbLMGH0N";
                String shareSub = "Your Sub Here";
                intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                intent3.putExtra(Intent.EXTRA_SUBJECT, shareSub);
                intent3.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(intent3, "Share Using"));
                break;

            case R.id.nav_wallet:

                Intent intent4 = new Intent(getApplicationContext(), MoneyWalletActivity.class);
                intent4.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent4);
                break;

            case R.id.nav_refer:

                Intent intent5 = new Intent(getApplicationContext(), ReferCodeActivity.class);
                intent5.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent5);
                break;

            case R.id.nav_rateus:

//                AppRate.with(this).setInstallDays(0).setLaunchTimes(2).setRemindInterval(2).monitor();
//                AppRate.showRateDialogIfMeetsConditions(this);

                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.android.crome")));
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                }
                break;

            case R.id.nav_cartlist:

                Intent intent6 = new Intent(getApplicationContext(), CartList2Activity.class);
                intent6.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent6);
                break;

            case R.id.nav_orderhis:

                Intent intent7 = new Intent(getApplicationContext(), OrderHistoryActivity.class);
                intent7.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent7);
                break;

            case R.id.nav_myaccount:

                Intent intent9= new Intent(getApplicationContext(), MyAccountActivity.class);
                intent9.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent9);
                break;

            default:
                fragmentClass = MenuFragment.class;
                break;
        }
        item.setChecked(true);
        // Set action bar title
        setTitle(item.getTitle());
        // Close the navigation drawer
        drawerLayout.closeDrawers();
        return true;
    }
//
//    @Override
//    public void OnFbSignInComplete(GraphResponse graphResponse, String error) {
//
//        if(error==null)
//        {
//            try {
//                JSONObject jsonObject = graphResponse.getJSONObject();
//                String id = jsonObject.getString("id");
//                String profileImg = "http://graph.facebook.com/" + id + "/picture?type=large";
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//    }
//
//    @Override
//    public void OnGSignInSuccess(GoogleSignInAccount googleSignInAccount) {
//
//    }
//
//    @Override
//    public void OnGSignInError(String error) {
//        Log.e(TAG, error);
//    }
//
//    @Override public void onAPICallStarted() {
//        Log.i(TAG, "onAPICallStarted");
//    }

    public void GetData()
    {
        Cursor c = databaseHelpher.GetMobileData(username);

        if (c != null)
        {
            if (c.moveToFirst()) {
                do {

                    username = c.getString(c.getColumnIndex("Email"));
                    password = c.getString(c.getColumnIndex("Password"));
                    mobile = c.getString(c.getColumnIndex("PhoneNo"));
                }
                while (c.moveToNext());
            }
        }
    }


    class LoginData extends AsyncTask<Void,Void,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(LoginActivity.this);
            progress.setMessage("Loading...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setProgress(0);
            progress.show();
        }

        @Override
        protected String doInBackground(Void... voids) {

            shh = new ServiceHandler();
            String url = path + "Registrations/GetLogin";
            Log.d("Url: ", "> " + url);

            try

            {
                List<NameValuePair> params2 = new ArrayList<>();
                params2.add(new BasicNameValuePair("PhoneNo",username));
                params2.add(new BasicNameValuePair("Email", username));
//                params2.add(new BasicNameValuePair("Password", password));
                params2.add(new BasicNameValuePair("ReferCode",refercode));

                String jsonStr = shh.makeServiceCall(url, ServiceHandler.POST, params2);

                if (jsonStr != null) {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    Status= jsonObject.getInt("status");

                } else {
                    Toast.makeText(getBaseContext(), "Data not Found", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {

                e.printStackTrace();

            }

            return null;
        }


        boolean bln = false;
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progress.dismiss();

            if (Status == 1) {

                if (username.equals(emailid))
                {
                    bln = true;
                }
                else if (username.equals(mobile))
                {
                    bln = true;
                }

                if(bln == true)
                {
                    if (mcheck.isChecked()) {
                        Boolean boolIscheck = mcheck.isChecked();
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("pref_name", editTextuserid.getText().toString());
                        editor.putString("pref_pass", editTextpassword.getText().toString());
                        editor.putBoolean("pref_check", boolIscheck);
                        editor.apply();
                    }
                    else {
                        preferences.edit().clear().apply();
                    }

                    Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_LONG).show();
                    databaseHelpher.UpdateRegistrationData(username,1);
                    final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
                    globalVariable.setUsername(username);
                    globalVariable.setloginPassword(password);
                    globalVariable.setMobileNo(mobile);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("User", username);
//                    intent.putExtra("Password", password);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_LONG).show();
                    editTextuserid.setText("");
                    editTextpassword.setText("");
                }
//                    if (username.equals(emailid) && password.equals(pass)) {
//
//                    if (mcheck.isChecked()) {
//                        Boolean boolIscheck = mcheck.isChecked();
//                        SharedPreferences.Editor editor = preferences.edit();
//                        editor.putString("pref_name", editTextuserid.getText().toString());
//                        editor.putString("pref_pass", editTextpassword.getText().toString());
//                        editor.putBoolean("pref_check", boolIscheck);
//                        editor.apply();
//                    }
//                    else {
//                        preferences.edit().clear().apply();
//                    }
//
//                    Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_LONG).show();
//                    final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
//                    globalVariable.setUsername(username);
//                    globalVariable.setloginPassword(password);
//                    globalVariable.setMobileNo(mobile);
//                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                    intent.putExtra("User", username);
//                    intent.putExtra("Password", password);
//                    startActivity(intent);
////                editTextuserid.setText("");
////                editTextpassword.setText("");
//
//                }
//                else if (phoneno.equals(mobile) && password.equals(pass)) {
//                    if (mcheck.isChecked()) {
//                        Boolean boolIscheck = mcheck.isChecked();
//                        SharedPreferences.Editor editor = preferences.edit();
//                        editor.putString("pref_name", editTextuserid.getText().toString());
//                        editor.putString("pref_pass", editTextpassword.getText().toString());
//                        editor.putBoolean("pref_check", boolIscheck);
//                        editor.apply();
//                    } else {
//                        preferences.edit().clear().apply();
//                    }
//
//                    Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_LONG).show();
//                    final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
//                    globalVariable.setUsername(username);
//                    globalVariable.setloginPassword(password);
//                    globalVariable.setMobileNo(mobile);
//                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                    intent.putExtra("User", username);
//                    intent.putExtra("Password", password);
//                    startActivity(intent);
//
////                editTextuserid.setText("");
////                editTextpassword.setText("");
//
//                }

            }

        }

    }

    class ReferCodeData extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... strings) {

            shh = new ServiceHandler();
            String url = path + "ReferalCodes/AddReferalCode";

            try {
                List<NameValuePair> params2 = new ArrayList<>();
                params2.add(new BasicNameValuePair("RefealCode",regrefercode));
                params2.add(new BasicNameValuePair("UserName",username));
                params2.add(new BasicNameValuePair("RefUserName",referuser));
                params2.add(new BasicNameValuePair("RefAmount","15"));
                params2.add(new BasicNameValuePair("Status","1"));

                String Jsonstr = shh.makeServiceCall(url ,ServiceHandler.POST , params2);

                if (Jsonstr != null)
                {
                    JSONObject c1= new JSONObject(Jsonstr);
                }
                else{
                    Toast.makeText(LoginActivity.this, "Data not Found", Toast.LENGTH_SHORT).show();
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

    class UpdateReferCodeData extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            amount=amount+ramount;

        }


        @Override
        protected String doInBackground(String... strings) {

            shh = new ServiceHandler();
            String url = path + "Registrations/UpdateRegRefAmt";

            try {
                List<NameValuePair> params2 = new ArrayList<>();
                params2.add(new BasicNameValuePair("ReferCode",regrefercode));
                params2.add(new BasicNameValuePair("UserName",username));
                params2.add(new BasicNameValuePair("RAmount",String.valueOf(amount)));

                String Jsonstr = shh.makeServiceCall(url ,ServiceHandler.POST , params2);

                if (Jsonstr != null)
                {
                    JSONObject c1= new JSONObject(Jsonstr);
                    Status =c1.getInt("status");
                }
                else{
                    Toast.makeText(LoginActivity.this, "Data not Found", Toast.LENGTH_SHORT).show();
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

    class getReferData extends AsyncTask<Void, Void, String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... params) {
            shh = new ServiceHandler();
            String url = path + "Registrations/getReferCodeData";
            Log.d("Url: ", "> " + url);

            try{
                List<NameValuePair> params2 = new ArrayList<>();
                params2.add(new BasicNameValuePair("ReferCode", regrefercode));
                String jsonStr = shh.makeServiceCall(url, ServiceHandler.POST , params2);

                if (jsonStr != null) {
                    JSONObject c1 = new JSONObject(jsonStr);
                    JSONArray classArray = c1.getJSONArray("sesponse");
                    for (int i = 0; i < classArray.length(); i++) {
                        JSONObject a1 = classArray.getJSONObject(i);
                        referuser = a1.getString("email");
                        refername = a1.getString("custFirstName");
                        referlastname = a1.getString("custLastName");
                        refermobile = a1.getString("phoneNo");
                        amount = a1.getInt("rAmount");
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

    class getRegData extends AsyncTask<Void, Void, String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=new ProgressDialog(LoginActivity.this);
            progress.setMessage("Loading...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setProgress(0);
            progress.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            shh = new ServiceHandler();
            String url = path + "Registrations/GetLogin";
            Log.d("Url: ", "> " + url);

            try{
                List<NameValuePair> params2 = new ArrayList<>();
                params2.add(new BasicNameValuePair("Email",username));
                params2.add(new BasicNameValuePair("PhoneNo",username));
//                params2.add(new BasicNameValuePair("Password",password));

                String jsonStr = shh.makeServiceCall(url, ServiceHandler.POST , params2);

                if (jsonStr != null) {
                    JSONObject jObj = new JSONObject(jsonStr);
                    JSONArray classArray = jObj.getJSONArray("response");
                    for (int i = 0; i < classArray.length(); i++) {
                        JSONObject a1 = classArray.getJSONObject(i);
//                        pass = a1.getString("Password");
                        emailid = a1.getString("email");
                        mobile = a1.getString("phoneNo");
//                        rate = a1.getString("ItemRate");
//                        img = a1.getString("ListImg");
//
//                        ItemPlanet planet1 = new ItemPlanet(itemname,subitem,rate,img);
//                        mPlanetlist1.add(planet1);

                    }
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "You are not Registered user", Toast.LENGTH_LONG).show();
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

        }
    }

}
