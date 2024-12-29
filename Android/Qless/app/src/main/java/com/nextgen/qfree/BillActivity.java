package com.nextgen.qfree;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import java.util.Random;

public class BillActivity extends AppCompatActivity {

    TextView amountTV;
    TextView billIdTV;


    String amount,account;

    private ImageView backImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        backImageView = (ImageView) findViewById(R.id.BackImageButton);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BillActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });



        amount = getIntent().getStringExtra("total");
        account = getIntent().getStringExtra("acc");

        amountTV = findViewById(R.id.amountPaidTextView);
        billIdTV = findViewById(R.id.billIdTextView);

        Random rnd = new Random();
        int number = rnd.nextInt(999999);


        amountTV.setText(amount);
        billIdTV.setText(String.valueOf(number));


    }


}