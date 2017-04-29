package com.jaus.albertogiunta.justintrain_oraritreni.tutorial;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntro2Fragment;
import com.jaus.albertogiunta.justintrain_oraritreni.R;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.helpers.AnalyticsHelper;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.ACTION_TUTORIAL_DONE;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.ACTION_TUTORIAL_NEXT;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.ACTION_TUTORIAL_SKIP;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.SCREEN_TUTORIAL;

public class IntroActivity extends AppIntro2 {

    AnalyticsHelper analyticsHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        analyticsHelper = AnalyticsHelper.getInstance(this);
        addSlide(AppIntro2Fragment.newInstance("Percorri sempre la stessa tratta?",
                "Aggiungi le tue tratte e i tuoi treni abituali ai preferiti, per averli sempre a portata di mano!", R.drawable.tut1, Color.parseColor("#717C8D")));
        addSlide(AppIntro2Fragment.newInstance("Andata? Ritorno?", "No problem. Trascina la tratta preferita verso dove vuoi andare. Proprio come ti verrebbe naturale fare.", R.drawable.tut2, Color.parseColor("#717C8D")));
        addSlide(AppIntro2Fragment.newInstance("Treno poco affidabile?", "Tienilo sempre sotto controllo tramite la notifica!", R.drawable.tut3, Color.parseColor("#717C8D")));
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        analyticsHelper.logScreenEvent(SCREEN_TUTORIAL, ACTION_TUTORIAL_DONE);
        finish();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        analyticsHelper.logScreenEvent(SCREEN_TUTORIAL, ACTION_TUTORIAL_SKIP);
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        analyticsHelper.logScreenEvent(SCREEN_TUTORIAL, ACTION_TUTORIAL_NEXT);
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
