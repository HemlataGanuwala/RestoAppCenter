package com.flavourheights.apple.skyrestaurantapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

public class EatingPreferenceActivity extends AppCompatActivity {

    RadioButton radioButtonveg, radioButtonvegnonveg;
    TextView textViewok, textViewcancel;
    String eating_preferance;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eating_preference);

        textViewok = (TextView) findViewById(R.id.tvok);
        textViewcancel = (TextView) findViewById(R.id.tvcancel);

        radioButtonveg = (RadioButton) findViewById(R.id.rgbveg);
        radioButtonvegnonveg =(RadioButton) findViewById(R.id.rgbnonveg);



        textViewok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (radioButtonveg.isChecked() == true)
                {
                    eating_preferance = "Veg";

                }else {
                    eating_preferance = "Veg/Non-Veg";
                }

                Intent intent = new Intent(EatingPreferenceActivity.this, MyProfileActivity.class);
                intent.putExtra("EatingPreference", eating_preferance);
                startActivity(intent);
            }
        });

        textViewcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EatingPreferenceActivity.this, MyProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}
