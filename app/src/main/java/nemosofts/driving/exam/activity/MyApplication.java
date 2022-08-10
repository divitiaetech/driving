package nemosofts.driving.exam.activity;

import android.content.Context;
import android.os.StrictMode;

import androidx.multidex.MultiDex;

import com.facebook.FacebookSdk;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.ironsource.mediationsdk.IronSource;
import com.onesignal.OneSignal;

import lk.nemosofts.androidsdk.BaseApplication;
import nemosofts.driving.exam.BuildConfig;
import nemosofts.driving.exam.R;
import nemosofts.driving.exam.SharedPref.Setting;
import nemosofts.driving.exam.Utils.DBHelper;


public class MyApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseAnalytics.getInstance(getApplicationContext());

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        DBHelper dbHelper = new DBHelper(getApplicationContext());
        dbHelper.onCreate(dbHelper.getWritableDatabase());

        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(getApplicationContext());
        OneSignal.setAppId(getApplicationContext().getString(R.string.ONESIGNAL_APP_ID));

        IronSource.setMetaData("Facebook_IS_CacheFlag","IMAGE");
        FacebookSdk.sdkInitialize(getApplicationContext());
        AudienceNetworkAds.initialize(this);
        MobileAds.initialize(getApplicationContext());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public String setEnvatoPurchaseCode() {
        //  A purchase code is a license identifier in the form of a unique numerical code.
        //  On Envato Market, the purchase code is issued with an item upon purchase and is included in the item's License Certificate
        return Setting.purchase_code;
    }

    @Override
    public String setProductID() {
        // The ID is numeric and you can see it in an item’s URL
        return "33907205";
    }

    @Override
    public String setApplicationID() {
        // Every Android app has a unique application ID that looks like a Java package name, such as com. example. myapp.
        return BuildConfig.APPLICATION_ID;
    }

    @Override
    public String setApiKey() {
        // ApiKey
        return Setting.apikey;
    }

    @Override
    public String setServerUrl() {
        // ApiUrl
        return BuildConfig.SERVER_URL;
    }

    @Override
    public String setSharedPreferences() {
        // SharedPreferences Name
        return "setting_lic";
    }

}