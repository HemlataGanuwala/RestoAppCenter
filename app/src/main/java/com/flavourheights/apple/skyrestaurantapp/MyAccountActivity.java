package com.flavourheights.apple.skyrestaurantapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAccountActivity extends AppCompatActivity {

    TextView textViewprofile, textVieworder, textViewlogout, textViewaddress, textViewfavourites;
    ImageView imageViewback;

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

            }
        });


    }
}
