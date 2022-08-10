package nemosofts.driving.exam.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import lk.nemosofts.androidsdk.ProCompatActivity;
import nemosofts.driving.exam.R;
import nemosofts.driving.exam.SharedPref.Setting;
import nemosofts.driving.exam.Utils.ApplicationUtil;
import nemosofts.driving.exam.ifSupported.IsRTL;
import nemosofts.driving.exam.ifSupported.IsScreenshot;


public class UpdateActivity extends ProCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IsScreenshot.ifSupported(this);
        IsRTL.ifSupported(this);

        Button appUpdate = findViewById(R.id.btn_appUpdate);
        appUpdate.setOnClickListener(v -> {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Setting.url)));
            finish();
        });

        ImageView close = findViewById(R.id.iv_close);
        close.setOnClickListener(v -> finish());

        TextView description = findViewById(R.id.iv_noteDescription);
        description.setText(Setting.description);
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_update;
    }

    @Override
    protected boolean setDarkMode() {
        return ApplicationUtil.isDarkMode();
    }
}