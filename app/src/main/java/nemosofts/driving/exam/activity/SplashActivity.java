package nemosofts.driving.exam.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;

import com.facebook.AccessToken;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.startapp.sdk.adsbase.StartAppAd;

import lk.nemosofts.androidsdk.Nemosofts;
import lk.nemosofts.androidsdk.ProSplashActivity;
import lk.nemosofts.androidsdk.asyncTask.LoadTask;
import lk.nemosofts.androidsdk.interfaces.ProductListener;
import nemosofts.driving.exam.BuildConfig;
import nemosofts.driving.exam.R;
import nemosofts.driving.exam.SharedPref.Setting;
import nemosofts.driving.exam.SharedPref.SharedPref;
import nemosofts.driving.exam.Utils.ApplicationUtil;
import nemosofts.driving.exam.Utils.Methods;
import nemosofts.driving.exam.asyncTask.LoadAbout;
import nemosofts.driving.exam.asyncTask.LoadLogin;
import nemosofts.driving.exam.interfaces.AboutListener;
import nemosofts.driving.exam.interfaces.LoginListener;
import nemosofts.driving.exam.item.ItemUser;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends ProSplashActivity {

    private Nemosofts nemosofts;
    private Methods methods;
    private SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        nemosofts = new Nemosofts(this);
        sharedPref = new SharedPref(this);
        Setting.language_id = sharedPref.getLanguage();
        Setting.Dark_Mode = sharedPref.getNightMode();
        super.onCreate(savedInstanceState);

        StartAppAd.disableSplash();

        methods = new Methods(this);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.smalltobig);
        CardView logo = findViewById(R.id.logo);
        logo.startAnimation(animation);
        TextView logo_text = findViewById(R.id.logo_text);
        logo_text.startAnimation(animation);

        loadAboutData();
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_splash;
    }

    @Override
    protected boolean setDarkMode() {
        SharedPref shared = new SharedPref(this);
        return shared.getNightMode();
    }


    public void loadAboutData() {
        if (methods.isNetworkAvailable()) {
            LoadAbout loadAbout = new LoadAbout(SplashActivity.this, new AboutListener() {
                @Override
                public void onStart() {
                }

                @Override
                public void onEnd(String success, String verifyStatus, String message) {
                    if (success.equals("1")) {
                        if (!verifyStatus.equals("-1")) {
                            setSaveData();
                        }
                        else {
                            errorDialog(getString(R.string.error_unauth_access), message);
                        }
                    } else {
                        errorDialog(getString(R.string.server_error), getString(R.string.err_server));
                    }
                }
            });
            loadAbout.execute();
        } else {
            errorDialog(getString(R.string.err_internet_not_conn), getString(R.string.error_connect_net_tryagain));
        }
    }

    private void setSaveData() {
        if (nemosofts.IsFirst()) {
            LoadTask loadTask = new LoadTask(this, new ProductListener(){
                @Override
                public void onStart() {

                }
                @Override
                public void onEnd(String success, String verifyStatus, String message) {
                    if (success.equals("1")) {
                        if (!verifyStatus.equals("-1")) {
                            loadSettings();
                        } else {
                            errorDialog(getString(R.string.error_unauth_access), message);
                        }
                    }else {
                        errorDialog(getString(R.string.server_error), getString(R.string.server_error));
                    }
                }
            },Setting.apikey);
            loadTask.execute();
        } else {
            if (Setting.apikey.equals(nemosofts.getApiKey())){
                loadSettings();
            }else {
                nemosofts.setFirst();
                loadAboutData();
                methods.showSnackBar("Please wait a minute",true);
            }
        }
    }

    public void loadSettings() {
        if (sharedPref.getIsFirst()) {
            openLoginActivity();
        } else {
            if (!sharedPref.getIsAutoLogin()) {
                new Handler().postDelayed(this::openMainActivity, 2000);
            } else {
                if (methods.isNetworkAvailable()) {
                    if (sharedPref.getLoginType().equals(Setting.LOGIN_TYPE_FB)) {
                        if (AccessToken.getCurrentAccessToken() != null) {
                            loadLogin(Setting.LOGIN_TYPE_FB, sharedPref.getAuthID());
                        } else {
                            sharedPref.setIsAutoLogin(false);
                            openMainActivity();
                        }
                    } else if (sharedPref.getLoginType().equals(Setting.LOGIN_TYPE_GOOGLE)) {
                        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                        if (currentUser != null) {
                            loadLogin(Setting.LOGIN_TYPE_GOOGLE, sharedPref.getAuthID());
                        } else {
                            sharedPref.setIsAutoLogin(false);
                            openMainActivity();
                        }
                    } else {
                        loadLogin(Setting.LOGIN_TYPE_NORMAL, "");
                    }
                } else {
                    sharedPref.getUserDetails();
                    openMainActivity();
                }
            }
        }
    }

    private void openMainActivity() {
        if (Setting.apikey.equals(nemosofts.getApiKey())){
            Intent intent;
            if(Setting.showUpdateDialog && BuildConfig.VERSION_CODE != Setting.appVersion){
                intent = new Intent(SplashActivity.this, UpdateActivity.class);
            } else {
                if (sharedPref.getSelectLanguage()){
                    intent = new Intent(SplashActivity.this, LanguageActivity.class);
                }else {
                    intent = new Intent(SplashActivity.this,MainActivity.class);
                }
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }else {
            nemosofts.setFirst();
            loadAboutData();
            methods.showSnackBar("Please wait a minute",true);
        }
    }

    private void openLoginActivity() {
        Intent intent;
        if(Setting.showUpdateDialog && BuildConfig.VERSION_CODE != Setting.appVersion){
            intent = new Intent(SplashActivity.this, UpdateActivity.class);
            startActivity(intent);
            finish();
        }else {
            if (sharedPref.getIsFirst()) {
                sharedPref.setIsFirst(false);
                intent = new Intent(SplashActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("from", "");
                startActivity(intent);
                finish();
            } else {
                openMainActivity();
            }
        }
    }


    private void errorDialog(String title, String message) {
        final AlertDialog.Builder  alertDialog ;

        if (ApplicationUtil.isDarkMode()){
            alertDialog = new AlertDialog.Builder(SplashActivity.this, R.style.ThemeDialog2);
        }else {
            alertDialog = new AlertDialog.Builder(SplashActivity.this, R.style.ThemeDialog);
        }

        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);

        if (title.equals(getString(R.string.err_internet_not_conn)) || title.equals(getString(R.string.server_error))) {
            alertDialog.setNegativeButton(getString(R.string.try_again), (dialog, which) -> loadAboutData());
        }

        alertDialog.setPositiveButton(getString(R.string.exit), (dialog, which) -> finish());
        alertDialog.show();
    }

    private void loadLogin(final String loginType, final String authID) {
        if (methods.isNetworkAvailable()) {
            LoadLogin loadLogin = new LoadLogin(new LoginListener() {
                @Override
                public void onStart() {

                }

                @Override
                public void onEnd(String success, String loginSuccess, String message, String user_id, String user_name) {
                    if (success.equals("1")) {
                        if (loginSuccess.equals("1")) {
                            Setting.itemUser = new ItemUser(user_id, user_name, sharedPref.getEmail(), "",authID, loginType);
                            Setting.isLogged = true;
                        }
                    }
                    openMainActivity();
                }
            }, methods.getAPIRequest(Setting.METHOD_LOGIN, 0, 0, "", loginType, sharedPref.getEmail(), sharedPref.getPassword(), "", "", authID, "","",""));
            loadLogin.execute();
        } else {
            Toast.makeText(SplashActivity.this, getString(R.string.internet_not_connected), Toast.LENGTH_SHORT).show();
        }
    }

}