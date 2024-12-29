package com.nextgen.qfree;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PinActivity extends AppCompatActivity {

    WebView webView;

    String amount,account;

    private GlobalPreference globalPreference;
    private String ip,uid;
    String acc="",am="",pin="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);

        globalPreference = new GlobalPreference(this);
        ip = globalPreference.getIp();
        uid = globalPreference.getID();

        amount = getIntent().getStringExtra("total");
        account = getIntent().getStringExtra("acc");
        webView = new WebView(this);

        webView.getSettings().setUseWideViewPort(false);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(false);
        webView.addJavascriptInterface(new WebAppInterface(this), "android");
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        webView.loadUrl("http://" + ip + "/Qless/api/shuffle.php");
        setContentView(webView);
    }

    public  void alert(String t){

        pin=t;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PinActivity.this);


        alertDialogBuilder.setTitle("Confirm")
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                CheckPin(pin,account,amount,uid);
                                dialog.cancel();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();

                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();


    }

    //////////////////////////////////////////Check Pin//////////////////////////////////////////////

    private void CheckPin(String pin, String account, String amount, String uid) {
        String UrlData = "?pin=" + pin+"&acc=" + account+"&am=" + amount+"&uid=" + uid;

        // Toast.makeText(getApplicationContext(), "details"+UrlData, Toast.LENGTH_LONG).show();

        class Details extends AsyncTask<String, Void, String> {


            ProgressDialog progressDialog;


            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(PinActivity.this, "Please wait", null, true, true);


            }

            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();
//                Log.d("result",s);
                //Toast.makeText(PinActivity.this,""+s, Toast.LENGTH_LONG).show();
                if(s!=null && s.equalsIgnoreCase("failed")){
                    Toast.makeText(PinActivity.this,"Your Account Does not have Enough Balance", Toast.LENGTH_LONG).show();
                }
                else if(s!=null && s.equalsIgnoreCase("accerror")){
                    Toast.makeText(PinActivity.this,"Account Doesnot exist", Toast.LENGTH_LONG).show();
                }
                else if(s!=null && s.equalsIgnoreCase("pin")){
                    Toast.makeText(PinActivity.this,"Incorrect Pin", Toast.LENGTH_LONG).show();
                }
                else if(s!=null && s.equalsIgnoreCase("success")){
                    Toast.makeText(PinActivity.this,"Payment Success", Toast.LENGTH_LONG).show();
                    Intent intent =new Intent(PinActivity.this,BillActivity.class);
                    intent.putExtra("total",amount);
                    intent.putExtra("acc",account);
                    startActivity(intent);
                }

            }

            @Override
            protected String doInBackground(String... params) {

                String s = params[0];
                Log.d("result", params[0]);
                BufferedReader bf = null;

                try {
                    URL url = new URL("http://"+ip+"/Qless/api/pay.php" + s);

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    bf = new BufferedReader(new InputStreamReader(connection.getInputStream()));


                    String result = bf.readLine();
                    return result;


                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }


            }

        }


        Details detail = new Details();
        detail.execute(UrlData);
    }


    public class WebAppInterface {
        Context mContext;

        /** Instantiate the interface and set the context */
        WebAppInterface(Context c) {
            mContext = c;
        }

        /** Show a toast from the web page */
        @JavascriptInterface
        public void showToast(String toast) {
            //  Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
            alert(toast);
        }
    }

}