package com.jaus.albertogiunta.justintrain_oraritreni.aboutAndSettings;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jaus.albertogiunta.justintrain_oraritreni.BuildConfig;
import com.jaus.albertogiunta.justintrain_oraritreni.R;

import java.util.List;

public class AboutPageUtils {

    public static Boolean isAppInstalled(Context context, String appName) {
        PackageManager pm = context.getPackageManager();
        boolean        installed;
        try {
            pm.getPackageInfo(appName, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }

    public static void showChangelog(Context context) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        View                view        = LayoutInflater.from(context).inflate(R.layout.dialog_changelog, null);
        alertDialog.setView(view)
                .setTitle("Novità versione " + BuildConfig.VERSION_NAME)
                .setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss())
                .setNegativeButton("VOTA SUL PLAY STORE", (dialogInterface, i) -> {
                    try {
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context.getPackageName())));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + context.getPackageName())));
                    }
                })
                .create();

        Button email    = view.findViewById(R.id.btn_email);
        Button twitter  = view.findViewById(R.id.btn_twitter);
        Button telegram = view.findViewById(R.id.btn_telegram);

        email.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"albertogiuntadev@gmail.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback / supporto / suggerimenti");
            context.startActivity(Intent.createChooser(intent, "Invia email..."));
        });

        twitter.setOnClickListener(v -> {
            // Create intent using ACTION_VIEW and a normal Twitter url:
            String tweetUrl = "https://twitter.com/intent/tweet?text=@albigiu";
            Intent intent   = new Intent(Intent.ACTION_VIEW, Uri.parse(tweetUrl));
            // Narrow down to official Twitter app, if available:
            List<ResolveInfo> matches = context.getPackageManager().queryIntentActivities(intent, 0);
            for (ResolveInfo info : matches) {
                if (info.activityInfo.packageName.toLowerCase().startsWith("com.twitter")) {
                    intent.setPackage(info.activityInfo.packageName);
                }
            }
            context.startActivity(intent);
        });

        telegram.setOnClickListener(v -> {
            if (AboutPageUtils.isAppInstalled(context, "org.telegram.messenger")) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://telegram.me/justintrain"));
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "Telegram non sembra essere installato sul tuo dispositivo!", Toast.LENGTH_LONG).show();
            }
        });


        alertDialog.show();
    }

}