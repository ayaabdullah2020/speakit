package com.speakit.app.presentaion.helpers;

import android.app.Activity;
import android.view.View;

/*
*  Citybookers Project
*  Date picker class
* @author  aya abdullah
* @version 1.0
* @since   10/3/2017
*/
public abstract class DatePicker {
    public static void chooseDate(View view, Activity activity) {
        activity.showDialog(0);
    }

    public static String pad(int c)
    {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

}
