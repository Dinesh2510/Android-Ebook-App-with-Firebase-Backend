package com.pixeldev.firepdf.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefs {
    public static final String SAVED_PDF_PAGES = "fire_pdf_prefs";

    public static boolean setPreferenceOfPDF(Context context, String Preference_NAME, String key, int value) {
        SharedPreferences.Editor edit = context.getSharedPreferences(Preference_NAME, 0).edit();
        edit.putInt(key, value);
        return edit.commit();
    }

    public static int getPreferenceOfPDF(Context context, String Preference_NAME, String key, int value) {
        return context.getSharedPreferences(Preference_NAME, 0).getInt(key, value);
    }
}
