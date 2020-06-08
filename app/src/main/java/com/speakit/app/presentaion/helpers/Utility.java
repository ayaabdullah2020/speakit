package com.speakit.app.presentaion.helpers;
import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public abstract class Utility {

    public static int version_number = 0;
    public static boolean isValidPassword(String passwordEd) {
        String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[d$@$!%*?&#])[a-zA-Z\\dd$@$!%*?&#]{6,}$";
        //"^(?=.*[A-Z])(?=.*[@_.]).*$";
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(passwordEd);
        if (!passwordEd.matches(".*\\d.*") || !matcher.matches()) {
            return true;
        }
        return false;
    }


    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }


    public static void showToast(String message, Activity activity) {
        if (activity != null)
            Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
    }

    public static void showToast(String message, Context context) {
        if (context != null)
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }


    public static void downloadImage(Activity activity, String imageUrl, int placeHolder, ImageView imageView) {

        if (imageUrl != "" && imageUrl != null) {
            try {
                Picasso.with(activity).invalidate(imageUrl);
                Picasso.with(activity).load(imageUrl)

                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .resize(300, 200)
                        .placeholder(placeHolder)
                        .into(imageView);
            } catch (Exception e) {
                imageView.setImageResource(placeHolder);
            }
        } else
            imageView.setImageResource(placeHolder);

    }

}