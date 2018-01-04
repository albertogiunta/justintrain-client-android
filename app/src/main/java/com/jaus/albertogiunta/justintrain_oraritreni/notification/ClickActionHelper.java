package com.jaus.albertogiunta.justintrain_oraritreni.notification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import trikita.log.Log;

public class ClickActionHelper {
    public static void startActivity(String className, Bundle extras, Context context){
        Class cls;
        if (className != null && extras != null && context != null) {
            try {
                cls = Class.forName(className);
                Intent i = new Intent(context, cls);
                i.putExtras(extras);
                context.startActivity(i);
            } catch (ClassNotFoundException | NullPointerException e) {
                Log.d("startActivity: class not found or something was null");
                //means you made a wrong input in firebase console
            }
        }
    }
}