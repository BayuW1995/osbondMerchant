package gmedia.net.id.osbondmerchant;

/**
 * Created by Bayu on 29/12/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

public class SessionManager {
    // Shared Preferences
    SharedPreferences pref,pref2;

    // Editor for Shared preferences
    Editor editor,editor2;

    // Context
    Context _context;

    // Shared pref mode

    int PRIVATE_MODE = 0;
    int CHECK_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "AndroidHivePref";
    private static final String CHECK_NAME = "check";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String IS_CHECK = "IsCheckIn";

    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_UID = "uid";
    public static final String KEY_CABANG = "cabang";

    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";
    public static final String KEY_CHECK = "check";

    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        pref2 = context.getSharedPreferences(CHECK_NAME,CHECK_MODE);
        editor = pref.edit();
        editor2 = pref2.edit();
    }

    /**
     * Create login session
     * */
    public void createLoginSession( String token, String uid, String cabang){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
//        editor.putString(KEY_NAME, name);

        // Storing email in pref
//        editor.putString(KEY_EMAIL, email);
        // Storing token in pref
        editor.putString(KEY_TOKEN, token);
        editor.putString(KEY_UID, uid);
        editor.putString(KEY_CABANG,cabang);

        // commit changes
        editor.commit();
    }


    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status
        if(this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, MainActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }
    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        // user email id
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        // return user
        return user;
    }
    public String getToken(){
        return pref.getString(KEY_TOKEN, "");
    }
    public String getUid(){
        return pref.getString(KEY_UID,"");
    }
    public String getCabang(){
        return pref.getString(KEY_CABANG,"");
    }

    /**
     * Clear session details
     * */

    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, Login.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
    public boolean isCheckIn(){
        return pref2.getBoolean(IS_CHECK,false);
    }
}