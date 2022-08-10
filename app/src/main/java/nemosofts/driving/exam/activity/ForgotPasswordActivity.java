package nemosofts.driving.exam.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import lk.nemosofts.androidsdk.ProCompatActivityNormal;
import nemosofts.driving.exam.R;
import nemosofts.driving.exam.SharedPref.Setting;
import nemosofts.driving.exam.Utils.ApplicationUtil;
import nemosofts.driving.exam.Utils.Methods;
import nemosofts.driving.exam.asyncTask.LoadForgotPass;
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

public class ForgotPasswordActivity extends ProCompatActivityNormal {

    private Methods methods;
    private EditText editText_email;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = findViewById(R.id.toolbar_forgostpass);

        methods = new Methods(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");

        IsScreenshot.ifSupported(this);
        IsRTL.ifSupported(this);

        progressDialog = new ProgressDialog(ForgotPasswordActivity.this);
        progressDialog.setMessage(getString(R.string.loading));

        Button button_send = findViewById(R.id.button_forgot_send);
        editText_email = findViewById(R.id.et_forgot_email);

        button_send.setOnClickListener(v -> {
            if (methods.isNetworkAvailable()) {
                if (!editText_email.getText().toString().trim().isEmpty()) {
                    loadForgotPass();
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, getString(R.string.enter_email), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ForgotPasswordActivity.this, getString(R.string.internet_not_connected), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_forgot_password;
    }

    @Override
    protected boolean setDarkMode() {
        return ApplicationUtil.isDarkMode();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            onBackPressed();
        } else {
            return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }

    private void loadForgotPass() {
        LoadForgotPass loadForgotPass = new LoadForgotPass(new SuccessListener() {
            @Override
            public void onStart() {
                progressDialog.show();
            }

            @Override
            public void onEnd(String success, String registerSuccess, String message) {
                progressDialog.dismiss();
                if (success.equals("1")) {
                    Toast.makeText(ForgotPasswordActivity.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                }
            }
        }, methods.getAPIRequest(Setting.METHOD_FORGOT_PASSWORD, 0, 0, "", "", editText_email.getText().toString(), "", "", "", "", "","",""));
        loadForgotPass.execute();
    }
}