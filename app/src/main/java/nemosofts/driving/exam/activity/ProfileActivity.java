package nemosofts.driving.exam.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import lk.nemosofts.androidsdk.ProCompatActivity;
import nemosofts.driving.exam.R;
import nemosofts.driving.exam.SharedPref.Setting;
import nemosofts.driving.exam.Utils.ApplicationUtil;
import nemosofts.driving.exam.Utils.Methods;
import nemosofts.driving.exam.asyncTask.LoadProfile;
import nemosofts.driving.exam.ifSupported.IsRTL;
import nemosofts.driving.exam.ifSupported.IsScreenshot;
import nemosofts.driving.exam.interfaces.SuccessListener;

/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : thivakaran
 * Telegram : @nemosofts
 * Contact : info.nemosofts@gmail.com
 * Skype : skype.nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class ProfileActivity extends ProCompatActivity {

    private Methods methods;
    private Toolbar toolbar;
    private TextView textView_name, textView_email, textView_mobile, textView_notlog;
    private LinearLayout ll_mobile;
    private View view_phone;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IsRTL.ifSupported(this);
        IsScreenshot.ifSupported(this);

        methods = new Methods(this);

        toolbar = findViewById(R.id.toolbar_pro);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_backspace_white);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = 55;
        toolbar.setLayoutParams(params);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        textView_name = findViewById(R.id.tv_prof_fname);
        textView_email = findViewById(R.id.tv_prof_email);
        textView_mobile = findViewById(R.id.tv_prof_mobile);
        textView_notlog = findViewById(R.id.textView_notlog);

        ll_mobile = findViewById(R.id.ll_prof_phone);

        view_phone = findViewById(R.id.view_prof_phone);

        LinearLayout ll_adView = findViewById(R.id.ll_adView);
        methods.showBannerAd(ll_adView);

        if (Setting.itemUser != null && !Setting.itemUser.getId().equals("")) {
            loadUserProfile();
        } else {
            setEmpty(true, getString(R.string.not_log));
        }
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_profile;
    }

    @Override
    protected boolean setDarkMode() {
        return ApplicationUtil.isDarkMode();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_profile, menu);

        if (Setting.itemUser != null && !Setting.itemUser.getId().equals("")) {
            menu.findItem(R.id.item_profile_edit).setVisible(true);
        } else {
            menu.findItem(R.id.item_profile_edit).setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.item_profile_edit:
                if (Setting.itemUser != null && !Setting.itemUser.getId().equals("")) {
                    Intent intent = new Intent(ProfileActivity.this, ProfileEditActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(ProfileActivity.this, getString(R.string.not_log), Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadUserProfile() {
        if (methods.isNetworkAvailable()) {
            LoadProfile loadProfile = new LoadProfile(new SuccessListener() {
                @Override
                public void onStart() {
                    progressDialog.show();
                }

                @Override
                public void onEnd(String success, String registerSuccess, String message) {
                    progressDialog.dismiss();
                    if (success.equals("1")) {
                        if (registerSuccess.equals("1")) {
                            setVariables();
                        } else {
                            setEmpty(false, getString(R.string.invalid_user));
                            methods.logout(ProfileActivity.this);
                        }
                    } else {
                        Toast.makeText(ProfileActivity.this, getString(R.string.server_no_conn), Toast.LENGTH_SHORT).show();
                    }
                }
            }, methods.getAPIRequest(Setting.METHOD_PROFILE,0,0,"","","","","","","",Setting.itemUser.getId(),"",""));
            loadProfile.execute();
        } else {
            Toast.makeText(ProfileActivity.this, getString(R.string.internet_not_connected), Toast.LENGTH_SHORT).show();
        }
    }


    public void setVariables() {
        textView_name.setText(Setting.itemUser.getName());
        textView_mobile.setText(Setting.itemUser.getMobile());
        textView_email.setText(Setting.itemUser.getEmail());

        if (!Setting.itemUser.getMobile().trim().isEmpty()) {
            ll_mobile.setVisibility(View.VISIBLE);
            view_phone.setVisibility(View.VISIBLE);
        }

        textView_notlog.setVisibility(View.GONE);
    }

    public void setEmpty(Boolean flag, String message) {
        if (flag) {
            textView_notlog.setText(message);
            textView_notlog.setVisibility(View.VISIBLE);
        } else {
            textView_notlog.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        if (Setting.isUpdate) {
            Setting.isUpdate = false;
            setVariables();
        }
        super.onResume();
    }
}