package com.speakit.app.presentaion.helpers;

import android.app.Activity;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.provider.Settings;


/**
 * Created by Aya Abdullah on 4/25/2017 AD.
 */

public abstract class IntentClass {
    // go to another activity

    public static void goToActivity(Activity currentActivity, Class targetClass, Bundle value) {

    }
    public static void goToActivityAndClear(Activity currentActivity, Class targetClass, Bundle value) {

        Intent intent = new Intent(currentActivity, targetClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("data", value);
        currentActivity.startActivity(intent);
    }
     // to open dial phone number

    public static void goTodialPhoneNumber(Activity currentActivity, String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(currentActivity.getPackageManager()) != null) {
            currentActivity.startActivity(intent);
        }
    }

    public static void goToActivitystring(Activity currentActivity, Class targetClass, String value) {
        Intent intent = new Intent(currentActivity, targetClass);
        intent.putExtra("data", value);
        currentActivity.startActivity(intent);
    }



    public static void goToActivityint(Activity currentActivity, Class targetClass, int value) {

        Intent intent = new Intent(currentActivity, targetClass);
        intent.putExtra("data", value);
        currentActivity.startActivity(intent);
    }
    //go to fb ,twitter ,google plus ....etc

    public static void goToLink(Activity activity, String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        activity.startActivity(intent);


    }


    // go to other app with data
    public static void goSharedata(Activity activity, String text, String sendTo) {

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        activity.startActivity(Intent.createChooser(sendIntent, sendTo));
    }
    public static void goToSMS(Activity activity, String smsNumber, String smsText) {

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + smsNumber));
        intent.putExtra("sms_body", smsText);
        activity.startActivity(intent);
    }


    // go to whatsapp
    public static void goTowhatsApp(Activity activity, String smsNumber, String smsText) {

        Uri uri = Uri.parse("smsto:" + smsNumber);
        Intent i = new Intent(Intent.ACTION_SENDTO, uri);
        i.putExtra("sms_body", smsText);
        i.setPackage("com.whatsapp");
        activity.startActivity(i);
    }

    // to open wifi settings and can change any action setting
    public static void goToOpenWifiSettings(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        }
    }

    // to navigate
    public static void goToNavigate(Activity activity, Double lat, Double lng) {

        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?saddr=20.344,34.34&daddr=" + lat + "," + lng));
        activity.startActivity(intent);
    }


    //to open bluetooth
    public static void goToBlutooth(Activity activity) {

        final Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.bluetooth.BluetoothSettings");
        intent.setComponent(cn);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);

    }


    // to create event on mob
    public static void goToAddEvent(Activity activity, String title, String location, long begin, long end) {
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.Events.TITLE, title)
                .putExtra(CalendarContract.Events.EVENT_LOCATION, location)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, begin)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, end);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        }
    }


    //add new contact

    public static void goToInsertContact(Activity activity, String name, String email) {
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
        intent.putExtra(ContactsContract.Intents.Insert.EMAIL, email);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        }
    }


    // send email to more than one with attachment

    public static  void goTocomposeEmail(Activity activity, String[] addresses, String subject, Uri attachment) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_STREAM, attachment);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        }
    }
    //send email to one

    public static  void goToEmail(Activity activity, String addresse, String subject) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_EMAIL, addresse);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        }
    }

    // create note

    //search any thing in any app on your mob

    public static void goToSearchWeb(Activity activity, String query) {
        Intent intent = new Intent(Intent.ACTION_SEARCH);
        intent.putExtra(SearchManager.QUERY, query);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        }
    }


}