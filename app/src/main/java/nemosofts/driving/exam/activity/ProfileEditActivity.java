package nemosofts.driving.exam.activity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import lk.nemosofts.androidsdk.ProCompatActivity;
import nemosofts.driving.exam.R;
import nemosofts.driving.exam.SharedPref.Setting;
import nemosofts.driving.exam.SharedPref.SharedPref;
import nemosofts.driving.exam.Utils.ApplicationUtil;
import nemosofts.driving.exam.Utils.Methods;
import nemosofts.driving.exam.asyncTask.LoadProfileEdit;
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

public class ProfileEditActivity extends ProCompatActivity {

    private EditText editText_name, editText_email, editText_phone, editText_pass, editText_cpass;
    private Toolbar toolbar;
    private Methods methods;
    private SharedPref sharedPref;
    private ProgressDialog progressDialog;
    private LinearLayout ll_pass, ll_cpass;
    private View view_pass, view_cpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IsRTL.ifSupported(this);
        IsScreenshot.ifSupported(this);

        sharedPref = new SharedPref(this);
        methods = new Methods(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        toolbar = findViewById(R.id.toolbar_proedit);
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

        AppCompatButton button_update = findViewById(R.id.button_prof_update);
        ll_pass = findViewById(R.id.ll_prof_edit_pass);
        ll_cpass = findViewById(R.id.ll_prof_edit_cpass);
        view_pass = findViewById(R.id.view_prof_edit_pass);
        view_cpass = findViewById(R.id.view_prof_edit_cpass);
        editText_name = findViewById(R.id.editText_profedit_name);
        editText_email = findViewById(R.id.editText_profedit_email);
        editText_phone = findViewById(R.id.editText_profedit_phone);
        editText_pass = findViewById(R.id.editText_profedit_password);
        editText_cpass = findViewById(R.id.editText_profedit_cpassword);


        if(Setting.itemUser.getLoginType().equals(Setting.LOGIN_TYPE_NORMAL)) {
            ll_pass.setVisibility(View.VISIBLE);
            ll_cpass.setVisibility(View.VISIBLE);
            view_pass.setVisibility(View.VISIBLE);
            view_cpass.setVisibility(View.VISIBLE);
            editText_email.setEnabled(true);
        } else {
            ll_pass.setVisibility(View.GONE);
            ll_cpass.setVisibility(View.GONE);
            view_pass.setVisibility(View.GONE);
            view_cpass.setVisibility(View.GONE);
            editText_email.setEnabled(false);
        }

        LinearLayout ll_adView = findViewById(R.id.ll_adView);
        methods.showBannerAd(ll_adView);

        setProfileVar();

        button_update.setOnClickListener(view -> {
            if (validate()) {
                loadUpdateProfile();
            }
        });
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_profile_edit;
    }

    @Override
    protected boolean setDarkMode() {
        return ApplicationUtil.isDarkMode();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private Boolean validate() {
        editText_name.setError(null);
        editText_email.setError(null);
        editText_cpass.setError(null);
        if (editText_name.getText().toString().trim().isEmpty()) {
            editText_name.setError(getString(R.string.cannot_empty));
            editText_name.requestFocus();
            return false;
        } else if (editText_email.getText().toString().trim().isEmpty()) {
            editText_email.setError(getString(R.string.email_empty));
            editText_email.requestFocus();
            return false;
        } else if (editText_pass.getText().toString().endsWith(" ")) {
            editText_pass.setError(getString(R.string.pass_end_space));
            editText_pass.requestFocus();
            return false;
        } else if (!editText_pass.getText().toString().trim().equals(editText_cpass.getText().toString().trim())) {
            editText_cpass.setError(getString(R.string.pass_nomatch));
            editText_cpass.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    private void updateArray() {
        Setting.itemUser.setName(editText_name.getText().toString());
        Setting.itemUser.setEmail(editText_email.getText().toString());
        Setting.itemUser.setMobile(editText_phone.getText().toString());

        if (!editText_pass.getText().toString().equals("")) {
            sharedPref.setRemeber(false);
        }
    }

    private void loadUpdateProfile() {
        if (methods.isNetworkAvailable()) {
            LoadProfileEdit loadProfileEdit = new LoadProfileEdit(new SuccessListener() {
                @Override
                public void onStart() {
                    progressDialog.show();
                }

                @Override
                public void onEnd(String success, String registerSuccess, String message) {
                    progressDialog.dismiss();
                    if (success.equals("1")) {
                        switch (registerSuccess) {
                            case "1":
                                updateArray();
                                Setting.isUpdate = true;
                                finish();
                                Toast.makeText(ProfileEditActivity.this, message, Toast.LENGTH_SHORT).show();
                                break;
                            case "-1":
                                methods.getVerifyDialog(getString(R.string.error_unauth_access), message);
                                break;
                            default:
                                if (message.contains("Email address already used")) {
                                    editText_email.setError(message);
                                    editText_email.requestFocus();
                                } else {
                                    Toast.makeText(ProfileEditActivity.this, message, Toast.LENGTH_SHORT).show();
                                }
                                break;
                        }
                    } else {
                        Toast.makeText(ProfileEditActivity.this, getString(R.string.server_no_conn), Toast.LENGTH_SHORT).show();
                    }
                }
            }, methods.getAPIRequest(Setting.METHOD_EDIT_PROFILE,0,0,"","",editText_email.getText().toString(),editText_pass.getText().toString(),editText_name.getText().toString(),editText_phone.getText().toString(),"",Setting.itemUser.getId(),"",""));
            loadProfileEdit.execute();
        } else {
            Toast.makeText(ProfileEditActivity.this, getString(R.string.internet_not_connected), Toast.LENGTH_SHORT).show();
        }
    }

    public void setProfileVar() {
        editText_name.setText(Setting.itemUser.getName());
        editText_phone.setText(Setting.itemUser.getMobile());
        editText_email.setText(Setting.itemUser.getEmail());
    }
}