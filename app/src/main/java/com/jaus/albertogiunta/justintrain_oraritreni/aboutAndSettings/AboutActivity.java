package com.jaus.albertogiunta.justintrain_oraritreni.aboutAndSettings;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jaus.albertogiunta.justintrain_oraritreni.BuildConfig;
import com.jaus.albertogiunta.justintrain_oraritreni.R;
import com.jaus.albertogiunta.justintrain_oraritreni.tutorial.IntroActivity;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AboutActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        String text = "Ciao, sono Alberto, lo sviluppatore di JustInTrain - Orari Trenitalia." +
                "\nProprio come te, anche io sono un pendolare, e ho sviluppato quest'app con l'obiettivo di rendere a tutti noi la vita un po' più semplice. Ho cercato principalmente di ridurre al minimo le perdite di tempo dovute a interfacce poco ottimizzate o che non tenessero in considerazione le nostre necessità." +
                "\nPer questo ho speso una quantità considerevole di tempo nella realizzazione di ciò che vedi, oltre a un server che fa funzionare il tutto, che non vedi ma che ha un costo vivo mensile." +
                "\nTi chiedo quindi, dato l'impegno e la dedizione che ho versato in questo progetto, di aiutarmi nella sua manutenzione e sviluppo se trovi l'app utile, o se reputi che se lo meriti." +
                "\nCosa puoi fare? Anche un piccolo gesto come una buona recensione o il passaparola fanno una grande differenza :)";

        View           view1;
        LinearLayout   linearLayout;
        LayoutInflater inflater = LayoutInflater.from(this);
        view1 = inflater.inflate(R.layout.activity_about_settings, null);
        linearLayout = (LinearLayout) view1.findViewById(R.id.ll_container);

        View aboutPage = new AboutPageBuilder(this)

                .addItem(new ItemBuilder(this, linearLayout).addItemGroupHeader("Feedback").build())
                .addItem(new ItemBuilder(this, linearLayout)
                        .addItemWithTitleSubtitle("L'app ti piace e ti è stata utile?", "Votala sul Google Play Store!")
                        .addOnClickListener(view -> {
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                            }
                        }).build())
                .addSeparator() //-----------------------------------------//
                .addItem(new ItemBuilder(this, linearLayout)
                        .addItemWithTitleSubtitle("Feedback, supporto e suggerimenti", "albertogiuntadev@gmail.com")
                        .addOnClickListener(view -> {
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("message/rfc822");
                            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"albertogiuntadev@gmail.com"});
                            intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback / supporto / suggerimenti");
                            startActivity(Intent.createChooser(intent, "Invia email..."));
                        }).build())
                .addSeparator() //-----------------------------------------//
                .addItem(new ItemBuilder(this, linearLayout)
                        .addItemWithTitleSubtitle("Consiglia l'app ad un amico!", "GO #TeamJustInTrainers")
                        .addOnClickListener(view -> {
                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_TEXT,
                                    "Dai un'occhiata a quest'app pensata appositamente per i pendolari Trenitalia!\nhttps://play.google.com/store/apps/details?id=com.jaus.albertogiunta.justintrain_oraritreni");
                            sendIntent.setType("text/plain");
                            startActivity(Intent.createChooser(sendIntent, "Consiglia l'app via..."));
                        })
                        .build())
                .addSeparator() //-----------------------------------------//
                .addItem(new ItemBuilder(this, linearLayout)
                        .addItemWithTitleSubtitle("Iscriviti al canale Telegram!", "Per anticipazioni e per votare nuove feature")
                        .addOnClickListener(view -> {
                            if (AboutPageUtils.isAppInstalled(this, "org.telegram.messenger")) {
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_VIEW);
                                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                                intent.setData(Uri.parse("https://telegram.me/justintrain"));
                                startActivity(intent);
                            } else {
                                Toast.makeText(this, "Telegram non sembra essere installato sul tuo dispositivo!", Toast.LENGTH_LONG).show();
                            }
                        })
                        .build())

                .addItem(new ItemBuilder(this, linearLayout).addItemGroupHeader("Info sull'app").build())
                .addItem(new ItemBuilder(this, linearLayout)
                        .addItemWithTitleSubtitle("Changelog", "Versione " + BuildConfig.VERSION_NAME + " (" + BuildConfig.VERSION_CODE + ")")
                        .addOnClickListener(view -> {
                            handler.sendEmptyMessage(1);
                        })
                        .build())
                .addSeparator() //-----------------------------------------//
                .addItem(new ItemBuilder(this, linearLayout)
                        .addItemWithTitleSubtitle("Tutorial", "Clicca per visualizzare il Tutorial")
                        .addOnClickListener(view -> {
                            Intent i = new Intent(AboutActivity.this, IntroActivity.class);
                            startActivity(i);
                        })
                        .build())
                .addSeparator() //-----------------------------------------//
                .addItem(new ItemBuilder(this, linearLayout)
                        .addItemWithTitle("Licenze")
                        .addOnClickListener(v -> {
                            Intent myIntent = new Intent(AboutActivity.this, LicencesActivity.class);
                            AboutActivity.this.startActivity(myIntent);
                        })
                        .build())

                .addItem(new ItemBuilder(this, linearLayout).addItemGroupHeader("Autore").build())
                .addItem(new ItemBuilder(this, linearLayout)
                        .addItemWithIconTitleSubtitle(R.drawable.about_icon_link, "Sito personale", "www.albertogiunta.it")
                        .addOnClickListener(view -> {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.addCategory(Intent.CATEGORY_BROWSABLE);
                            intent.setData(Uri.parse("http://www.albertogiunta.it"));
                            startActivity(intent);
                        }).build())
                .addSeparator() //-----------------------------------------//
                .addItem(new ItemBuilder(this, linearLayout)
                        .addItemWithIconTitleSubtitle(R.drawable.about_icon_email, "Email", "albertogiuntadev@gmail.com")
                        .addOnClickListener(view -> {
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("message/rfc822");
                            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"albertogiuntadev@gmail.com"});
                            intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback / supporto / suggerimenti");
                            startActivity(Intent.createChooser(intent, "Invia email..."));
                        }).build())
                .addSeparator() //-----------------------------------------//
                .addItem(new ItemBuilder(this, linearLayout)
                        .addItemWithIconTitleSubtitle(R.drawable.about_icon_twitter, "Twitter", "albigiu")
                        .addOnClickListener(view -> {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.addCategory(Intent.CATEGORY_BROWSABLE);
                            if (AboutPageUtils.isAppInstalled(this, "com.twitter.android")) {
                                intent.setPackage("com.twitter.android");
                                intent.setData(Uri.parse(String.format("twitter://user?screen_name=%s", "albigiu")));
                            } else {
                                intent.setData(Uri.parse(String.format("https://twitter.com/intent/user?screen_name=%s", "albigiu")));
                            }
                            startActivity(intent);
                        })
                        .build())
                .addSeparator() //-----------------------------------------//
                .addItem(new ItemBuilder(this, linearLayout)
                        .addItemWithIconTitleSubtitle(R.drawable.about_icon_instagram, "Instagram", "illbegiu")
                        .addOnClickListener(view -> {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.addCategory(Intent.CATEGORY_BROWSABLE);
                            intent.setData(Uri.parse(String.format("https://www.instagram.com/%s", "illbegiu")));
                            startActivity(intent);
                        })
                        .build())
                .addSeparator() //-----------------------------------------//
                .addItem(new ItemBuilder(this, linearLayout)
                        .addItemWithIconTitleSubtitle(R.drawable.about_icon_github, "Github", "albertogiunta")
                        .addOnClickListener(view -> {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.addCategory(Intent.CATEGORY_BROWSABLE);
                            intent.setData(Uri.parse(String.format("https://github.com/%s", "albertogiunta")));
                            startActivity(intent);
                        })
                        .build())

                .addItem(new ItemBuilder(this, linearLayout).addItemLongText(text).build())

                .build();

        setContentView(aboutPage);

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    Handler handler = new Handler(message -> {
        AboutPageUtils.showChangelog(this);
        return false;
    });
}
