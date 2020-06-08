package com.speakit.app.presentaion.helpers;

import android.content.Context;
import android.content.SharedPreferences;


public class SharedPref {

    public static SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name

    private static final String PREF_NAME = "pref";
    public void clearUserData(){
        editor.remove("token");
        editor.remove("userName");
        editor.remove("userMob");
        editor.remove("userEmail");
        editor.commit();
    }
    public SharedPref(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public String getString(String key){
        return sharedPreferences.getString(key,"");
    }
    public void putString(String key, String value){
        editor.putString(key, value);
        editor.commit();
    }
    public void putLong(String key, long value){
        editor.putLong(key, value);
        editor.commit();
    }

    public void clear(){
        editor.clear();
        editor.commit();
    }
    public void putFloat(String key, float value){
        editor.putFloat(key, value);
        editor.commit();
    }
    public float getFloat(String key , float defaultVal ){
        return sharedPreferences.getFloat(key,defaultVal);
    }

    public void putInt(String key, int value){
        editor.putInt(key, value);
        editor.commit();
    }


    public int getInt(String key , int defaultVal ){
        return sharedPreferences.getInt(key,defaultVal);
    }

    public long getLong(String key , int defaultVal ){
        return sharedPreferences.getLong(key,defaultVal);
    }
    public void putBoolean(String status, boolean status1) {
        editor.putBoolean(status, status1);
        editor.commit();
    }

    public boolean getBoolean(String key , boolean defaultVal ){
        return sharedPreferences.getBoolean(key,defaultVal);
    }

    public String getLang() {
        return  !getString("lang").equals("") ? getString("lang") : "en";
    }

    // userData    { token ,id ,name ,mobile ,email ,image ,lang }

    public String getToken() {
        if ( getString("token").equals(""))
            return "";

        return   getString("token");
    }

    public void setLang(String value)
    {
        editor.putString("lang", value);
        editor.commit();
    }

    public void setToken(String value)
    {
       editor. putString("token", value);
        editor.commit();

    }

    public String getUserEmail()

    {
        return   getString("userEmail");
    }

    public void setUserEmail(String value)
    {
        editor.putString("userEmail", value);
        editor.commit();
    }

    public  void setUserId (String value){
        editor.putString("userId", value);
        editor.commit();
    }

    public String getUserId(){

        return getString("userId");
    }

    public  void setrealmUserId (int value){
        editor.putInt("realmId", value);
        editor.commit();
    }

    public int getrealmUserId(){

        return getInt("realmId",0);
    }



    public String getUserImage()

    {
        return   getString("userImage");
    }


    public void setUserImage(String value)
    {
        editor.putString("userImage", value);
        editor.commit();
    }

    public String getFirstName()

    {
        return   getString("firstName");
    }

    public void setFirstName(String value)
    {
        editor.putString("firstName", value);
        editor.commit();
    }
    public String getLastName()

    {
        return   getString("lastName");
    }

    public void setLastName(String value)
    {
        editor.putString("lastName", value);
        editor.commit();
    }

    public void setlog_in_out_flag(String value)
    {
        editor.putString("log_in_out", value);
        editor.commit();
    }

    public String getlog_in_out_flag()

    {
        return   getString("log_in_out");
    }

    public String getUserMob()

    {
        return   getString("userMob");
    }

    public void setUserMob(String value)
    {
        editor.putString("userMob", value);
        editor.commit();
    }



}
