package com.example.todotitans.utilities;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * A utility class to manage shared preferences for storing and retrieving application settings.
 * <p>
 * This class provides methods to store boolean and string values in the shared preferences
 * using a centralized preference name defined in {@link Constants}.
 * </p>
 */
public class PreferenceManager {

    /** The SharedPreferences instance used for managing application preferences. */
    private final SharedPreferences sharedPreferences;

    /**
     * Constructs a new {@code PreferenceManager} instance.
     *
     * @param context The {@link Context} used to initialize the shared preferences.
     *                It should typically be the application or activity context.
     */
    public PreferenceManager(Context context) {
        sharedPreferences = context.getSharedPreferences(Constants.KEY_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Stores a boolean value in the shared preferences.
     *
     * @param key   The key under which the value will be stored.
     * @param value The boolean value to store.
     */
    public void putBoolean(String key, Boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply(); // Asynchronous save for performance
    }

    /**
     * Stores a string value in the shared preferences.
     *
     * @param key   The key under which the value will be stored.
     * @param value The string value to store.
     */
    public void putString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply(); // Asynchronous save for performance
    }
}

