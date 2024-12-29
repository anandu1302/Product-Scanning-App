package com.nextgen.qfree;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

public class GlobalPreference {
    SharedPreferences sharedPreference;
    SharedPreferences.Editor editor;


    private Context context;

    public GlobalPreference(Context context){
        sharedPreference = context.getSharedPreferences("sample",MODE_PRIVATE);
        editor = sharedPreference.edit();

    }

    public void saveIp(String ipaddress){
        editor.putString("ip",ipaddress);
        editor.apply();
    }

    public String getIp(){ return sharedPreference.getString("ip","");}


    public void saveID(String id){
        editor.putString("id",id);
        editor.apply();
    }

    public String getID(){ return sharedPreference.getString("id","");}

    public void saveName(String name){
        editor.putString("name",name);
        editor.apply();
    }

    public String getName(){ return sharedPreference.getString("name","");}

    public void saveEmail(String email){
        editor.putString("email",email);
        editor.apply();
    }

    public String getEmail(){ return sharedPreference.getString("email","");}
}
