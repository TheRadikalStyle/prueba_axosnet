/*
 *
 * Author: David Ochoa Gtz
 * @TheRadikalStyle
 * 2021
 *
 * https://github.com/TheRadikalStyle/android-stuff/blob/master/snippets/Tools.java
 *
 */

package com.davidochoa.helpers;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class Tools {
    private static Tools INSTANCE = null;
    private boolean isEEggAlreadyShow = false;
    private int counter = 0;

    private Tools(){}

    public static Tools getInstance(){
        if(INSTANCE == null){
            INSTANCE = new Tools();
        }
        return(INSTANCE);
    }

    public void releaseInstance(){
        INSTANCE = null;
    }

    public boolean isDeviceOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void SetEasterEgg(Context ctx, TextView versionTXV){
        //Get App Version
        try {
            PackageInfo pInfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
            String version = pInfo.versionName;
            versionTXV.setText("v."+version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        versionTXV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter++;
                if(counter >= 7){
                    if(!isEEggAlreadyShow){
                        Calendar cal = Calendar.getInstance();
                        int hour = cal.get(Calendar.HOUR);
                        int minute = cal.get(Calendar.MINUTE);
                        if(hour == 3 && minute == 0){
                            Toast.makeText(ctx, "Dev by: David Ochoa", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(ctx, "" + ("\ud83d\ude03"), Toast.LENGTH_SHORT).show(); //Show :D emoji
                        }
                        isEEggAlreadyShow = true;
                    }
                    counter = 0;
                }
            }
        });
    }

    public String DateEquality(int year, int month, int day){
        month = month + 1;

        String sMonth = String.valueOf(month);
        if(sMonth.length() == 1)
            sMonth = "0" + sMonth;

        String sDay = String.valueOf(day);
        if(sDay.length() == 1)
            sDay = "0" + sDay;

        return year + "-" + sMonth + "-" + sDay;
    }
}
