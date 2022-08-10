package nemosofts.driving.exam.activity;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import lk.nemosofts.androidsdk.ProCompatActivity;
import nemosofts.driving.exam.R;
import nemosofts.driving.exam.Utils.ApplicationUtil;
import nemosofts.driving.exam.Utils.DBHelper;
import nemosofts.driving.exam.adapter.AdapterResult;
import nemosofts.driving.exam.ifSupported.IsRTL;
import nemosofts.driving.exam.ifSupported.IsScreenshot;
import nemosofts.driving.exam.item.ItemResult;

public class OldResultActivity extends ProCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IsScreenshot.ifSupported(this);
        IsRTL.ifSupported(this);

        Toolbar toolbar = findViewById(R.id.toolbar_result);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        ArrayList<ItemResult> arrayList = new ArrayList<>();

        RecyclerView rv = findViewById(R.id.rv_result);
        LinearLayoutManager llm_top_songs = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(llm_top_songs);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setHasFixedSize(true);

        try {
            DBHelper dbHelper = new DBHelper(this);
            arrayList.addAll(dbHelper.getResult(DBHelper.TABLE_RESULT));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (arrayList.size() != 0) {
            AdapterResult adapterResult = new AdapterResult(arrayList);
            rv.setAdapter(adapterResult);

        }
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_old_result;
    }

    @Override
    protected boolean setDarkMode() {
        return ApplicationUtil.isDarkMode();
    }
}