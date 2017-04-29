package com.jaus.albertogiunta.justintrain_oraritreni;

import android.content.Context;

import com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.ENUM_SNACKBAR_ACTIONS;

public interface BaseView {

    /*

    public void onCreate(Bundle savedInstanceState)

    public void onResume()

    public void onDestroy()

    protected void onSaveInstanceState(Bundle outState)

     */

    /**
     * Getter for the view context. (Needed when dealing with shared prefs and such).
     */
    Context getViewContext();

    /**
     * Because everybody might need a snackbar at some point.
     *  @param message The message to display
     * @param intent use it to understand what to do. It also should determine the action string.
     * @param duration
     */
    void showSnackbar(String message, ENUM_SNACKBAR_ACTIONS intent, int duration);
}
