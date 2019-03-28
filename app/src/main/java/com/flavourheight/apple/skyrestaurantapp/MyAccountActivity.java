package com.flavourheight.apple.skyrestaurantapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyAccountActivity extends AppCompatActivity {

    TextView textViewprofile, textVieworder, textViewlogout, textViewaddress, textViewfavourites;
    ImageView imageViewback;
    ServiceHandler shh;
    DatabaseHelpher databaseHelpher;
    String user, password;
    LinearLayout linearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        databaseHelpher = new DatabaseHelpher(this);

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        user = globalVariable.getUsername();
        password = globalVariable.getloginPassword();

        textVieworder = (TextView)findViewById(R.id.tvmyorder);
        textViewaddress = (TextView)findViewById(R.id.tvmyaddress);
        textViewfavourites = (TextView)findViewById(R.id.tvfavourites);
        textViewlogout = (TextView)findViewById(R.id.tvlogout);
        textViewprofile = (TextView)findViewById(R.id.tvmyprofile);

        linearLayout = (LinearLayout) findViewById(R.id.linearlayoutlogout);

        imageViewback = (ImageView)findViewById(R.id.imgvmyacountback);

        if (user != null && password != null)
        {
            linearLayout.setVisibility(View.VISIBLE);
        }else {
            linearLayout.setVisibility(View.GONE);
        }

        textViewprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyAccountActivity.this, MyProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });

        textVieworder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyAccountActivity.this, MyOrderHistoryActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });

        textViewaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyAccountActivity.this, MyAddressActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });


        textViewfavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyAccountActivity.this, MyFavoritesActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });

        imageViewback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyAccountActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });

        textViewlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Logout();
//                SharedPreferences SM = getSharedPreferences("CurrentUser", MODE_PRIVATE);
//                SharedPreferences.Editor edit = SM.edit();
////                edit.remove("UserName");
////                edit.remove("PassWord");
//                popupMessage();
//                edit.clear();
//                edit.commit();
//                Intent loginPageIntent = new Intent(getApplicationContext(), MainActivity.class);
//                loginPageIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                loginPageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(loginPageIntent);



            }
        });

    }

    public void Logout()
    {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        databaseHelpher.UpdateRegistrationData(user, 0);

    /*int pid = android.os.Process.myPid();=====> use this if you want to kill your activity. But its not a good one to do.
    android.os.Process.killProcess(pid);*/

    }

    public void popupMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MyAccountActivity.this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("Do you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
