package nemosofts.driving.exam.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.ramijemli.percentagechartview.PercentageChartView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import lk.nemosofts.androidsdk.ProCompatActivity;
import nemosofts.driving.exam.R;
import nemosofts.driving.exam.SharedPref.Setting;
import nemosofts.driving.exam.Utils.ApplicationUtil;
import nemosofts.driving.exam.Utils.DBHelper;


public class ResActivity extends ProCompatActivity {

    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DBHelper dbHelper = new DBHelper(this);
        Toolbar toolbar = findViewById(R.id.toolbar_res);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        TextView iv_cont3 = findViewById(R.id.iv_cont3);

        int totalProgress = (int)((double)Setting.okAnswer / Setting.totelAnswer * 100);
        PercentageChartView mChart = findViewById(R.id.mChart);
        mChart.setProgress(totalProgress, true);

        if (totalProgress <= 25){
            iv_cont3.setText("Fail");
        } else if (totalProgress <= 50) {
            iv_cont3.setText("Normal");
        } else if (totalProgress <= 75){
            iv_cont3.setText("Good");
        } else if (totalProgress <= 100){
            iv_cont3.setText("Very Good");
        }

        int totalProgress_no = (int)((double)Setting.noAnswer / Setting.totelAnswer * 100);
        int totalProgress_ok = (int)((double)Setting.okAnswer / Setting.totelAnswer * 100);

        PercentageChartView mChart_no = findViewById(R.id.mChart_no);
        PercentageChartView mChart_ok = findViewById(R.id.mChart_ok);
        mChart_no.setProgress(totalProgress_no, true);
        mChart_ok.setProgress(totalProgress_ok, true);

        @SuppressLint("SimpleDateFormat")
        String dateFormat = new SimpleDateFormat("d MMM yyyy").format(Calendar.getInstance().getTime());
        @SuppressLint("SimpleDateFormat")
        String time = new SimpleDateFormat("hh:mm aaa").format(Calendar.getInstance().getTime());
        String re = Setting.okAnswer+"/"+Setting.totelAnswer;
        dbHelper.addtoResult(dateFormat, time, re);
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_res;
    }

    @Override
    protected boolean setDarkMode() {
        return ApplicationUtil.isDarkMode();
    }
}