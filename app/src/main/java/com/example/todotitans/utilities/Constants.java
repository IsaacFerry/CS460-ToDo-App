package com.example.todotitans.utilities;

/**
 * This class defines constants used across the application.
 * <p>
 * It provides a centralized location for key names used in Firebase
 * Realtime Database and shared preferences, ensuring consistency and reducing errors.
 * </p>
 */
public class Constants {

    /** The name of the Firebase collection for user data. */
    public static final String KEY_COLLECTION_USERS = "users";

    /** The key for storing or retrieving the user's first name. */
    public static final String KEY_FIRST_NAME = "firstName";

    /** The key for storing or retrieving the user's last name. */
    public static final String KEY_LAST_NAME = "lastName";

    /** The key for storing or retrieving the user's email address. */
    public static final String KEY_EMAIL = "email";

    /** The key for storing or retrieving the user's password. */
    public static final String KEY_PASSWORD = "password";

    /** The key for storing or retrieving the user's unique ID. */
    public static final String KEY_USER_ID = "userid";

    /** The key for indicating whether the user is signed in. */
    public static final String KEY_IS_SIGNED_IN = "isSignedIn";

    /** The name of the shared preferences file for the task manager application. */
    public static final String KEY_PREFERENCE_NAME = "taskManagerPreference";

    /** The key for storing or retrieving the user's security question. */
    public static final String KEY_SECURE_QUESTION = "secureQuestion";
}