package com.flavourheights.apple.skyrestaurantapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAccountActivity extends AppCompatActivity {

    TextView textViewprofile, textVieworder, textViewlogout, textViewaddress, textViewfavourites;
    ImageView imageViewback;
    ServiceHandler shh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        textVieworder = (TextView)findViewById(R.id.tvmyorder);
        textViewaddress = (TextView)findViewById(R.id.tvmyaddress);
        textViewfavourites = (TextView)findViewById(R.id.tvfavourites);
        textViewlogout = (TextView)findViewById(R.id.tvlogout);
        textViewprofile = (TextView)findViewById(R.id.tvmyprofile);

        imageViewback = (ImageView)findViewById(R.id.imgvmyacountback);

        textViewprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyAccountActivity.this, MyProfileActivity.class);
                startActivity(intent);
            }
        });

        textVieworder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyAccountActivity.this, OrderHistoryActivity.class);
                startActivity(intent);
            }
        });

        textViewaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyAccountActivity.this, MyAddressActivity.class);
                startActivity(intent);
            }
        });


        textViewfavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyAccountActivity.this, MyFavoritesActivity.class);
                startActivity(intent);
            }
        });

        imageViewback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyAccountActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        textViewlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences SM = getSharedPreferences("CurrentUser", MODE_PRIVATE);
                SharedPreferences.Editor edit = SM.edit();
                edit.remove("UserName");
                edit.remove("PassWord");
                edit.commit();

                popupMessage();

            }
        });

    }

    public void popupMessage()
    {
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
