package com.jaus.albertogiunta.justintrain_oraritreni.utils.sharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

public class SharedPreferencesHelper {

    private final static String PREF_FILE = "PREF";

    /**
     * Set a string shared preference
     *
     * @param key - Key to set shared preference
     * @param value - Value for the key
     */
    public static void setSharedPreferenceString(Context context, String key, String value) {
        SharedPreferences        settings = context.getSharedPreferences(PREF_FILE, 0);
        SharedPreferences.Editor editor   = settings.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * Set a integer shared preference
     *
     * @param key - Key to set shared preference
     * @param value - Value for the key
     */
    public static void setSharedPreferenceInt(Context context, String key, int value) {
        SharedPreferences        settings = context.getSharedPreferences(PREF_FILE, 0);
        SharedPreferences.Editor editor   = settings.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    /**
     * Set an object shared preference
     *
     * @param key - Key to set shared preference
     * @param value - Value for the key
     */
    public static void setSharedPreferenceObject(Context context, String key, String value) {
        SharedPreferences        settings = context.getSharedPreferences(PREF_FILE, 0);
        SharedPreferences.Editor editor   = settings.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * Set a Boolean shared preference
     *
     * @param key - Key to set shared preference
     * @param value - Value for the key
     */
    public static void setSharedPreferenceBoolean(Context context, String key, boolean value) {
        SharedPreferences        settings = context.getSharedPreferences(PREF_FILE, 0);
        SharedPreferences.Editor editor   = settings.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    /**
     * Get a string shared preference
     *
     * @param key - Key to look up in shared preferences.
     * @param defValue - Default value to be returned if shared preference isn't found.
     * @return value - String containing value of the shared preference if found.
     */
    public static String getSharedPreferenceString(Context context, String key, String defValue) {
        SharedPreferences settings = context.getSharedPreferences(PREF_FILE, 0);
        return settings.getString(key, defValue);
    }

    /**
     * Get a integer shared preference
     *
     * @param key - Key to look up in shared preferences.
     * @param defValue - Default value to be returned if shared preference isn't found.
     * @return value - String containing value of the shared preference if found.
     */
    public static int getSharedPreferenceInt(Context context, String key, int defValue) {
        SharedPreferences settings = context.getSharedPreferences(PREF_FILE, 0);
        int               value    = settings.getInt(key, defValue);

        return value;
    }

    /**
     * Get an object shared preference
     *
     * @param key - Key to set shared preference
     */
    public static String getSharedPreferenceObject(Context context, String key) {
        SharedPreferences settings = context.getSharedPreferences(PREF_FILE, 0);
        return settings.getString(key, null);
    }

    /**
     * Get a boolean shared preference
     *
     * @param key - Key to look up in shared preferences.
     * @param defValue - Default value to be returned if shared preference isn't found.
     * @return value - String containing value of the shared preference if found.
     */
    public static boolean getSharedPreferenceBoolean(Context context, String key, boolean defValue) {
        SharedPreferences settings = context.getSharedPreferences(PREF_FILE, 0);
        boolean           value    = settings.getBoolean(key, defValue);

        return value;
    }

    /**
     * Remove an object shared preference
     *
     * @param key - Key to set shared preference
     */
    public static void removeSharedPreferenceObject(Context context, String key) {
        SharedPreferences        settings = context.getSharedPreferences(PREF_FILE, 0);
        SharedPreferences.Editor editor   = settings.edit();
        editor.remove(key);
        editor.apply();
    }

    /**
     * Remove all the shared preferences\
     */
    public static void removeAllSharedPreferences(Context context) {
        for (String key : getAll(context).keySet()) {
            removeSharedPreferenceObject(context, key);
        }
    }

    /**
     * Get all the shared preferences as a map of strings and objects (will need to be casted)
     */
    public static Map<String, ?> getAll(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREF_FILE, 0);
        return settings.getAll();
    }

    public static Map<String, ?> getAllMatchingPrefix(Context context, String prefix) {
        SharedPreferences settings = context.getSharedPreferences(PREF_FILE, 0);
        Map<String, ?>    allPrefs = settings.getAll();
        Iterator<String>  iterator = allPrefs.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            if (!Pattern.matches(prefix + ".*", key)) {
                iterator.remove();
            }
        }
        return allPrefs;
    }
}