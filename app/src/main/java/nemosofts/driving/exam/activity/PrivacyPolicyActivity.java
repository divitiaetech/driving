package nemosofts.driving.exam.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import androidx.appcompat.widget.Toolbar;

import lk.nemosofts.androidsdk.ProCompatActivity;
import nemosofts.driving.exam.BuildConfig;
import nemosofts.driving.exam.R;
import nemosofts.driving.exam.SharedPref.Setting;
import nemosofts.driving.exam.Utils.ApplicationUtil;
import nemosofts.driving.exam.ifSupported.IsRTL;
import nemosofts.driving.exam.ifSupported.IsScreenshot;


public class PrivacyPolicyActivity extends ProCompatActivity {

    private Toolbar toolbar;
    private ProgressBar progressBar_policy;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IsScreenshot.ifSupported(this);
        IsRTL.ifSupported(this);

        toolbar = this.findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.privacy_policy));
        this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        if (Setting.Dark_Mode) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp);
        } else {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp2);
        }

        progressBar_policy = findViewById(R.id.progressBar_policy);

        WebView privacy_policy = findViewById(R.id.privacy_policy);
        privacy_policy.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                //Make the bar disappear after URL is loaded, and changes string to Loading...
                toolbar.setTitle("Loading...");
                setProgress(progress * 100); //Make the bar disappear after URL is loaded

                // Return the app name after finish loading
                if(progress == 100){
                    toolbar.setTitle(R.string.privacy_policy);
                    progressBar_policy.setVisibility(View.GONE);
                }
            }
        });
        privacy_policy.getSettings().setJavaScriptEnabled(true);
        privacy_policy.loadUrl(BuildConfig.SERVER_URL+"privacy_policy.php");
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_privacy_policy;
    }

    @Override
    protected boolean setDarkMode() {
        return ApplicationUtil.isDarkMode();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}