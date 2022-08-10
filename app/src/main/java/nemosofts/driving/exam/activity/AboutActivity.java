package nemosofts.driving.exam.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import lk.nemosofts.androidsdk.ProCompatActivity;
import nemosofts.driving.exam.BuildConfig;
import nemosofts.driving.exam.R;
import nemosofts.driving.exam.SharedPref.Setting;
import nemosofts.driving.exam.Utils.ApplicationUtil;


public class AboutActivity extends ProCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Toolbar toolbarAbout = findViewById(R.id.toolbar_ab);
        setSupportActionBar(toolbarAbout);
        setTitle(getResources().getString(R.string.about));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarAbout.setNavigationOnClickListener(view -> onBackPressed());

        if (Setting.Dark_Mode) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp);
        } else {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp2);
        }

        TextView company = findViewById(R.id.company);
        TextView email = findViewById(R.id.email);
        TextView website = findViewById(R.id.website);
        TextView contact = findViewById(R.id.contact);

        company.setText(Setting.appauthor);
        email.setText(Setting.email);
        website.setText(Setting.website);
        contact.setText(Setting.appcontact);


        LinearLayout ll_share = findViewById(R.id.ll_share);
        ll_share.setOnClickListener(v -> {
            final String appName = getPackageName();
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appName)));
        });

        LinearLayout ll_rate = findViewById(R.id.ll_rate);
        ll_rate.setOnClickListener(v -> {
            final String appName = getPackageName();
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appName)));
        });

        TextView version = findViewById(R.id.version);
        version.setText(BuildConfig.VERSION_NAME);
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_about;
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