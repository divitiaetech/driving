package nemosofts.driving.exam.SharedPref;

import android.content.Context;
import android.content.SharedPreferences;

import nemosofts.driving.exam.Utils.Methods;
import nemosofts.driving.exam.item.ItemUser;


public class SharedPref {
    private final Methods methods;
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    private static final String TAG_UID = "uid";
    private static final String TAG_USERNAME = "name";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_MOBILE = "mobile";
    private static final String TAG_REMEMBER = "rem";
    private static final String TAG_PASSWORD = "pass";
    private static final String SHARED_PREF_AUTOLOGIN = "autologin";
    private static final String TAG_LOGIN_TYPE = "loginType";
    private static final String TAG_AUTH_ID = "auth_id";
    private static final String TAG_FIRST_OPEN = "first_open";

    public SharedPref(Context context) {
        methods = new Methods(context, false);
        sharedPreferences = context.getSharedPreferences("setting", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setIsFirst(Boolean flag) {
        editor.putBoolean(TAG_FIRST_OPEN, flag);
        editor.apply();
    }

    public Boolean getIsFirst() {
        return sharedPreferences.getBoolean(TAG_FIRST_OPEN, true);
    }

    public void setLoginDetails(ItemUser itemUser, Boolean isRemember, String password, String loginType) {
        editor.putBoolean(TAG_REMEMBER, isRemember);
        editor.putString(TAG_UID, methods.encrypt(itemUser.getId()));
        editor.putString(TAG_USERNAME, methods.encrypt(itemUser.getName()));
        editor.putString(TAG_MOBILE, methods.encrypt(itemUser.getMobile()));
        editor.putString(TAG_EMAIL, methods.encrypt(itemUser.getEmail()));
        editor.putBoolean(TAG_REMEMBER, isRemember);
        editor.putString(TAG_PASSWORD, methods.encrypt(password));
        editor.putString(TAG_LOGIN_TYPE, methods.encrypt(loginType));
        editor.putString(TAG_AUTH_ID, methods.encrypt(itemUser.getAuthID()));
        editor.apply();
    }
    public void setRemeber(Boolean isRemember) {
        editor.putBoolean(TAG_REMEMBER, isRemember);
        editor.putString(TAG_PASSWORD, "");
        editor.apply();
    }

    public Boolean getIsRemember() {
        return sharedPreferences.getBoolean(TAG_REMEMBER, false);
    }

    public void getUserDetails() { Setting.itemUser = new ItemUser(methods.decrypt(sharedPreferences.getString(TAG_UID,"")), methods.decrypt(sharedPreferences.getString(TAG_USERNAME,"")), methods.decrypt(sharedPreferences.getString(TAG_EMAIL,"")), methods.decrypt(sharedPreferences.getString(TAG_MOBILE,"")), methods.decrypt(sharedPreferences.getString(TAG_AUTH_ID,"")), methods.decrypt(sharedPreferences.getString(TAG_LOGIN_TYPE,""))); }

    public String getEmail() {
        return methods.decrypt(sharedPreferences.getString(TAG_EMAIL,""));
    }

    public String getPassword() { return methods.decrypt(sharedPreferences.getString(TAG_PASSWORD,"")); }

    public String getLoginType() { return methods.decrypt(sharedPreferences.getString(TAG_LOGIN_TYPE,"")); }

    public String getAuthID() { return methods.decrypt(sharedPreferences.getString(TAG_AUTH_ID,"")); }

    public Boolean isRemember() {
        return sharedPreferences.getBoolean(TAG_REMEMBER, false);
    }

    public Boolean getIsAutoLogin() { return sharedPreferences.getBoolean(SHARED_PREF_AUTOLOGIN, false); }
    public void setIsAutoLogin(Boolean isAutoLogin) {
        editor.putBoolean(SHARED_PREF_AUTOLOGIN, isAutoLogin);
        editor.apply();
    }


    public Boolean getNightMode() {
        return sharedPreferences.getBoolean("night_mode", false);
    }

    public void setNightMode(Boolean state) {
        editor.putBoolean("night_mode", state);
        editor.apply();
    }


    public Boolean getSelectLanguage() {
        return sharedPreferences.getBoolean("select_language", true);
    }

    public void setSelectLanguage(Boolean state) {
        editor.putBoolean("select_language", state);
        editor.apply();
    }


    public void setLanguage(int id, int position) {
        Setting.language_id = id;
        editor.putInt("language_id", id);
        editor.putInt("language_position", position);
        editor.apply();
    }

    public int getLanguage() {
       return sharedPreferences.getInt("language_id", 0);
    }

    public int getLanguagePosition() {
        return sharedPreferences.getInt("language_position", 0);
    }
}
