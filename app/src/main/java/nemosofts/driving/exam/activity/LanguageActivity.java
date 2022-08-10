package nemosofts.driving.exam.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Path;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.PathInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import lk.nemosofts.androidsdk.ProCompatActivity;
import nemosofts.driving.exam.R;
import nemosofts.driving.exam.SharedPref.Setting;
import nemosofts.driving.exam.SharedPref.SharedPref;
import nemosofts.driving.exam.Utils.ApplicationUtil;
import nemosofts.driving.exam.Utils.Methods;
import nemosofts.driving.exam.adapter.AdapterLanguage;
import nemosofts.driving.exam.asyncTask.LoadLanguage;
import nemosofts.driving.exam.ifSupported.IsRTL;
import nemosofts.driving.exam.ifSupported.IsScreenshot;
import nemosofts.driving.exam.interfaces.LanguageListener;
import nemosofts.driving.exam.item.ItemLanguage;

public class LanguageActivity extends ProCompatActivity {

    private SharedPref sharedPref;
    private Methods methods;
    private RecyclerView rv;
    private AdapterLanguage adapter;
    private ArrayList<ItemLanguage> arrayList;
    private ProgressBar progressBar;
    private FrameLayout frameLayout;
    private String errr_msg;
    private Button btn_home;
    private AnimatorSet set;
    private Boolean add_btn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IsScreenshot.ifSupported(this);
        IsRTL.ifSupported(this);

        this.sharedPref = new SharedPref(this);
        methods = new Methods(this);
        arrayList = new ArrayList<>();

        progressBar = findViewById(R.id.pb_load);
        frameLayout = findViewById(R.id.fl_empty);

        rv = findViewById(R.id.recycler);
        GridLayoutManager glm = new GridLayoutManager(LanguageActivity.this, 2);
        rv.setLayoutManager(glm);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setHasFixedSize(true);

        getData();

        btn_home = findViewById(R.id.btn_lan);
        isAnimatorButton(btn_home);
        btn_home.setText("No Select");
        btn_home.setBackgroundResource(R.drawable.bg_button_normal);
        add_btn = false;

        btn_home.setOnClickListener(v -> {
            if (add_btn){
                this.sharedPref.setSelectLanguage(false);
                Setting.language_id = this.sharedPref.getLanguage();

                overridePendingTransition(0, 0);
                overridePendingTransition(0, 0);
                startActivity(new Intent(LanguageActivity.this, MainActivity.class));
                finish();
            }
        });

    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_language;
    }

    @Override
    protected boolean setDarkMode() {
        return ApplicationUtil.isDarkMode();
    }

    private void isAnimatorButton(Button view) {
        final float from = 1.0f;
        final float to = 1.3f;

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, View.SCALE_X, from, to);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, View.SCALE_Y,  from, to);
        ObjectAnimator translationZ = ObjectAnimator.ofFloat(view, View.TRANSLATION_Z, from, to);

        AnimatorSet set1 = new AnimatorSet();
        set1.playTogether(scaleX, scaleY, translationZ);
        set1.setDuration(100);
        set1.setInterpolator(new AccelerateInterpolator());

        ObjectAnimator scaleXBack = ObjectAnimator.ofFloat(view, View.SCALE_X, to, from);
        ObjectAnimator scaleYBack = ObjectAnimator.ofFloat(view, View.SCALE_Y, to, from);
        ObjectAnimator translationZBack = ObjectAnimator.ofFloat(view, View.TRANSLATION_Z, to, from);

        Path path = new Path();
        path.moveTo(0.0f, 0.0f);
        path.lineTo(0.5f, 1.3f);
        path.lineTo(0.75f, 0.8f);
        path.lineTo(1.0f, 1.0f);
        PathInterpolator pathInterpolator = new PathInterpolator(path);

        AnimatorSet set2 = new AnimatorSet();
        set2.playTogether(scaleXBack, scaleYBack, translationZBack);
        set2.setDuration(300);
        set2.setInterpolator(pathInterpolator);

        set = new AnimatorSet();
        set.playSequentially(set1, set2);
    }

    private void getButton() {
        if (!add_btn){
            btn_home.setText("Next");
            btn_home.setBackgroundResource(R.drawable.bg_button);
            set.start();
            add_btn = true;
        }
    }

    private void getData() {
        if (methods.isNetworkAvailable()) {
            LoadLanguage loadLanguage = new LoadLanguage(new LanguageListener() {
                @Override
                public void onStart() {
                    frameLayout.setVisibility(View.GONE);
                    rv.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onEnd(String success, String verifyStatus, String message, ArrayList<ItemLanguage> itemLanguages) {
                    if (success.equals("1")) {
                        if (!verifyStatus.equals("-1")) {
                            if (itemLanguages.size() == 0) {
                                errr_msg = getString(R.string.no_data_found);
                                setEmpty();
                            } else {
                                arrayList.addAll(itemLanguages);
                                setAdapter();
                            }
                        } else {
                            methods.getVerifyDialog(getString(R.string.error_unauth_access), message);
                        }
                    } else {
                        errr_msg = getString(R.string.err_server);
                        setEmpty();
                    }
                    progressBar.setVisibility(View.GONE);
                }
            }, methods.getAPIRequest(Setting.METHOD_LANGUAGE, 0, Setting.language_id,"","","","","","","","","",""));
            loadLanguage.execute();
        } else {
            errr_msg = getString(R.string.internet_not_conn);
            setEmpty();
            progressBar.setVisibility(View.GONE);
        }
    }

    private void setAdapter() {
        adapter = new AdapterLanguage(LanguageActivity.this, arrayList, (item, position) -> {
            String id = arrayList.get(position).getLid();
            sharedPref.setLanguage(Integer.parseInt(id),position);
            adapter.select(position);
            getButton();
        });
        rv.setAdapter(adapter);
        if (!sharedPref.getSelectLanguage()){
            adapter.select(sharedPref.getLanguagePosition());
            getButton();
        }
        setEmpty();
    }

    public void setEmpty() {
        if (arrayList.size() > 0) {
            rv.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        } else {
            rv.setVisibility(View.GONE);
            frameLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);

            frameLayout.removeAllViews();
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View myView = null;
            if (errr_msg.equals(getString(R.string.no_data_found))) {
                myView = inflater.inflate(R.layout.layout_err_nodata, null);
            } else if (errr_msg.equals(getString(R.string.internet_not_conn))) {
                myView = inflater.inflate(R.layout.layout_err_internet, null);
            } else if (errr_msg.equals(getString(R.string.err_server))) {
                myView = inflater.inflate(R.layout.layout_err_server, null);
            }else {
                errr_msg = getString(R.string.no_data_found);
                myView = inflater.inflate(R.layout.layout_err_nodata, null);
            }

            TextView textView = myView.findViewById(R.id.tv_empty_msg);
            textView.setText(errr_msg);

            myView.findViewById(R.id.btn_empty_try).setOnClickListener(v -> {
                frameLayout.setVisibility(View.GONE);
                getData();
            });
            frameLayout.addView(myView);
        }
    }
}