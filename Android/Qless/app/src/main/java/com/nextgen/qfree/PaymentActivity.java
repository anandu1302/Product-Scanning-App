package com.nextgen.qfree;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PaymentActivity extends AppCompatActivity {

    EditText accountNoET;
    EditText amountET;
    TextView amountTV;
    Button payBT;

    private GlobalPreference globalPreference;
    private String ip,uid;
    private String amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        globalPreference = new GlobalPreference(this);
        ip = globalPreference.getIp();

        accountNoET = findViewById(R.id.accountnoEditText);
        amountTV = findViewById(R.id.amountTextView);
        payBT = findViewById(R.id.payButton);

        amount = getIntent().getStringExtra("total");
        amountTV.setText(amount);

       payBT.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               
               alertDialog();


           }
       });


    }

    private void alertDialog() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Confirm Payment");
        alertDialogBuilder.setMessage("Are you Sure to Make Payment?");
        alertDialogBuilder.setIcon(R.drawable.check);
        alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //on Success this action takes place

                String acc = accountNoET.getText().toString();
                String amnt = amountTV.getText().toString();
                Intent intent = new Intent(PaymentActivity.this,PinActivity.class);
                intent.putExtra("total",amnt);
                intent.putExtra("acc",acc);
                startActivity(intent);

            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
}