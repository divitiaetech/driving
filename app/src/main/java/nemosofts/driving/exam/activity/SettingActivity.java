package nemosofts.driving.exam.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.suke.widget.SwitchButton;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.text.DecimalFormat;

import lk.nemosofts.androidsdk.ProCompatActivity;
import nemosofts.driving.exam.R;
import nemosofts.driving.exam.SharedPref.Setting;
import nemosofts.driving.exam.SharedPref.SharedPref;
import nemosofts.driving.exam.Utils.Methods;


public class SettingActivity extends ProCompatActivity {

    private Methods methods;
    private SharedPref sharedPref;
    private ProgressDialog progressDialog;
    private TextView tv_cachesize;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = new SharedPref(this);
        Setting.language_id = sharedPref.getLanguage();
        Setting.Dark_Mode = sharedPref.getNightMode();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        if (Setting.Dark_Mode){
            progressDialog = new ProgressDialog(SettingActivity.this, R.style.ThemeDialog2);
        }else {
            progressDialog = new ProgressDialog(SettingActivity.this, R.style.ThemeDialog);
        }
        progressDialog.setMessage(getString(R.string.clearing_cache));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        methods = new Methods(this);

        Toolbar toolbar = findViewById(R.id.toolbar_setting);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getResources().getString(R.string.settings));

        this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayout ll_clearcache = findViewById(R.id.ll_cache);
        tv_cachesize = findViewById(R.id.tv_cachesize);

        LinearLayout ll_about = findViewById(R.id.ll_about);
        ll_about.setOnClickListener(v -> {
            Intent intent = new Intent(SettingActivity.this,AboutActivity.class);
            startActivity(intent);
        });

        LinearLayout ll_language= findViewById(R.id.ll_language);
        ll_language.setOnClickListener(v -> {
            overridePendingTransition(0, 0);
            overridePendingTransition(0, 0);
            startActivity(new Intent(SettingActivity.this, LanguageActivity.class));
            finish();
        });


        LinearLayout ll_privacy = findViewById(R.id.ll_privacy);
        ll_privacy.setOnClickListener(v -> openOpnsDialog());

        ll_clearcache.setOnClickListener(new View.OnClickListener() {
            @Override
            @SuppressLint("StaticFieldLeak")
            public void onClick(View v) {
                new AsyncTask<String, String, String>() {
                    @Override
                    protected void onPreExecute() {
                        progressDialog.show();
                        super.onPreExecute();
                    }

                    @Override
                    protected String doInBackground(String... strings) {
                        try {
                            FileUtils.deleteQuietly(getCacheDir());
                            FileUtils.deleteQuietly(getExternalCacheDir());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        progressDialog.dismiss();
                        methods.showSnackBar(getString(R.string.cache_cleared),true);
                        tv_cachesize.setText("0 MB");
                        super.onPostExecute(s);
                    }
                }.execute();

            }
        });

        initializeCache();

        SwitchButton switch_dark = findViewById(R.id.switch_dark);
        if (sharedPref.getNightMode()) {
            switch_dark.setChecked(true);
        } else {
            switch_dark.setChecked(false);
        }
        switch_dark.setOnCheckedChangeListener((view, isChecked) -> {
            sharedPref.setNightMode(isChecked);
            Apps_recreate();
        });
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_setting;
    }

    @Override
    protected boolean setDarkMode() {
        SharedPref shared = new SharedPref(this);
        return shared.getNightMode();
    }

    public void openOpnsDialog() {
        startActivity(new Intent(SettingActivity.this, PrivacyPolicyActivity.class));
    }

    private void Apps_recreate() {
        recreate();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            overridePendingTransition(0, 0);
            overridePendingTransition(0, 0);
            startActivity(new Intent(SettingActivity.this, MainActivity.class));
            finish();
        } else {
            return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        overridePendingTransition(0, 0);
        overridePendingTransition(0, 0);
        startActivity(new Intent(SettingActivity.this, MainActivity.class));
        finish();
    }

    private void initializeCache() {
        long size = 0;
        size += getDirSize(this.getCacheDir());
        size += getDirSize(this.getExternalCacheDir());
        tv_cachesize.setText(readableFileSize(size));
    }

    public long getDirSize(File dir) {
        long size = 0;
        for (File file : dir.listFiles()) {
            if (file != null && file.isDirectory()) {
                size += getDirSize(file);
            } else if (file != null && file.isFile()) {
                size += file.length();
            }
        }
        return size;
    }

    public static String readableFileSize(long size) {
        if (size <= 0) return "0 Bytes";
        final String[] units = new String[]{"Bytes", "kB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}