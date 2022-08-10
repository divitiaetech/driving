package nemosofts.driving.exam.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import lk.nemosofts.androidsdk.ProCompatActivity;
import nemosofts.driving.exam.R;
import nemosofts.driving.exam.SharedPref.Setting;
import nemosofts.driving.exam.Utils.ApplicationUtil;
import nemosofts.driving.exam.Utils.Methods;
import nemosofts.driving.exam.adapter.AdapterQuiz;
import nemosofts.driving.exam.asyncTask.LoadQuiz;
import nemosofts.driving.exam.ifSupported.IsRTL;
import nemosofts.driving.exam.ifSupported.IsScreenshot;
import nemosofts.driving.exam.interfaces.QuizListener;
import nemosofts.driving.exam.item.ItemQuiz;

public class QuizDetails extends ProCompatActivity {

    private Methods methods;
    private ArrayList<ItemQuiz> arrayList;
    private AdapterQuiz adapter;
    private RecyclerView recyclerView;
    private TextView tv_details_Answer, iv_cont, tv_details_Answer_A, tv_details_Answer_B, tv_details_Answer_C, tv_details_Answer_D;
    private ImageView iv_image;
    private RadioButton rb_1, rb_2, rb_3, rb_4;
    private ProgressBar progressBar, loading;
    private RelativeLayout rl_details_bottom;
    private RelativeLayout rl_bt_1;
    private RelativeLayout rl_bt_2;
    private RelativeLayout rl_bt_3;
    private RelativeLayout rl_bt_4;
    private ProgressBar pb_details;
    private  NestedScrollView scrollView;
    private int selectedPosition = 0;
    private String myPosition = "";
    private Boolean isFinish = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IsScreenshot.ifSupported(this);
        IsRTL.ifSupported(this);

        methods = new Methods(this);
        methods = new Methods(this, (position, type) -> { });

        scrollView = findViewById(R.id.scrollView);
        scrollView.setVisibility(View.GONE);
        pb_details = findViewById(R.id.pb_details);
        TextView tv_details_su_title = findViewById(R.id.tv_details_su_title);
        String cat_name = "";
        tv_details_su_title.setText(cat_name);

        Setting.okAnswer = 0;
        Setting.noAnswer = 0;
        Setting.totelAnswer = 0;

        arrayList = new ArrayList<>();

        recyclerView = findViewById(R.id.rv_filter);
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(llm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        tv_details_Answer = findViewById(R.id.tv_details_Answer);
        iv_cont = findViewById(R.id.iv_cont);
        iv_image= findViewById(R.id.iv_image);
        rb_1 = findViewById(R.id.rb_1);
        rb_2 = findViewById(R.id.rb_2);
        rb_3 = findViewById(R.id.rb_3);
        rb_4 = findViewById(R.id.rb_4);
        tv_details_Answer_A = findViewById(R.id.tv_details_Answer_A);
        tv_details_Answer_B = findViewById(R.id.tv_details_Answer_B);
        tv_details_Answer_C = findViewById(R.id.tv_details_Answer_C);
        tv_details_Answer_D = findViewById(R.id.tv_details_Answer_D);
        progressBar = findViewById(R.id.pb_load);
        loading = findViewById(R.id.loading);

        rl_details_bottom = findViewById(R.id.rl_details_bottom);
        rl_details_bottom.setBackgroundResource(R.drawable.bg_answer3);
        RelativeLayout rl_onBack = findViewById(R.id.rl_onBack);
        rl_onBack.setOnClickListener(v -> {
            onBackPressed();
        });

        rl_bt_1 = findViewById(R.id.rl_bt_1);
        rl_bt_2 = findViewById(R.id.rl_bt_2);
        rl_bt_3 = findViewById(R.id.rl_bt_3);
        rl_bt_4 = findViewById(R.id.rl_bt_4);

        getData();
        rl_details_bottom.setOnClickListener(v -> {
            int cont5 = selectedPosition + 1;
            int cont6 = arrayList.size() + 1;
            if (isFinish && cont5 == arrayList.size()){
                Setting.arrayList.addAll(arrayList);
                Intent intent = new Intent(QuizDetails.this,ResActivity.class);
                startActivity(intent);
                finish();
            }else {
                if (!myPosition.equals("")){
                    if (!arrayList.get(selectedPosition).isEnabled()){
                        if (cont5 != cont6){
                            String myAnswer = arrayList.get(selectedPosition).getCorrectAnswer();
                            if (myAnswer.equals(myPosition)){
                                arrayList.get(selectedPosition).setAnswerDraw(2);
                                arrayList.get(selectedPosition).setEnabled(true);
                                Setting.okAnswer = Setting.okAnswer + 1;
                            }else {
                                arrayList.get(selectedPosition).setAnswerDraw(3);
                                arrayList.get(selectedPosition).setEnabled(true);
                                Setting.noAnswer = Setting.noAnswer + 1;
                            }
                            arrayList.get(selectedPosition).setMyAnswer(myPosition);

                            if (arrayList.size() != cont5){
                                selectedPosition = selectedPosition + 1;
                                recyclerView.scrollToPosition(selectedPosition);
                                progressBar.setMax(arrayList.size());
                                progressBar.setProgress(cont5);
                            }
                            adapter.select(selectedPosition);
                            loadData();
                            if (cont5 == arrayList.size()){
                                progressBar.setProgress(0);
                                rl_details_bottom.setBackgroundResource(R.drawable.bg_answer2);
                                isFinish = true;
                            }
                            myPosition = "";
                        }
                    }
                }
            }
        });

        rl_bt_1.setOnClickListener(v -> {
            if (!arrayList.get(selectedPosition).isEnabled()){
                myPosition = "A";
                rb_1.setChecked(true);
                rb_2.setChecked(false);
                rb_3.setChecked(false);
                rb_4.setChecked(false);
            }
        });
        rl_bt_2.setOnClickListener(v -> {
            if (!arrayList.get(selectedPosition).isEnabled()){
                myPosition = "B";
                rb_1.setChecked(false);
                rb_2.setChecked(true);
                rb_3.setChecked(false);
                rb_4.setChecked(false);
            }
        });
        rl_bt_3.setOnClickListener(v -> {
            if (!arrayList.get(selectedPosition).isEnabled()){
                myPosition = "C";
                rb_1.setChecked(false);
                rb_2.setChecked(false);
                rb_3.setChecked(true);
                rb_4.setChecked(false);
            }
        });
        rl_bt_4.setOnClickListener(v -> {
            if (!arrayList.get(selectedPosition).isEnabled()){
                myPosition = "D";
                rb_1.setChecked(false);
                rb_2.setChecked(false);
                rb_3.setChecked(false);
                rb_4.setChecked(true);
            }
        });
        rb_1.setOnClickListener(view -> {
            myPosition = "A";
            rb_1.setChecked(true);
            rb_2.setChecked(false);
            rb_3.setChecked(false);
            rb_4.setChecked(false);
        });
        rb_2.setOnClickListener(view -> {
            myPosition = "B";
            rb_1.setChecked(false);
            rb_2.setChecked(true);
            rb_3.setChecked(false);
            rb_4.setChecked(false);
        });
        rb_3.setOnClickListener(view -> {
            myPosition = "C";
            rb_1.setChecked(false);
            rb_2.setChecked(false);
            rb_3.setChecked(true);
            rb_4.setChecked(false);
        });
        rb_4.setOnClickListener(view -> {
            myPosition = "D";
            rb_1.setChecked(false);
            rb_2.setChecked(false);
            rb_3.setChecked(false);
            rb_4.setChecked(true);
        });

        LinearLayout AdView_radio = findViewById(R.id.adView);
        methods.showBannerAd(AdView_radio);
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_quiz_details;
    }

    @Override
    protected boolean setDarkMode() {
        return ApplicationUtil.isDarkMode();
    }

    private void getData() {
        if (methods.isNetworkAvailable()) {
            LoadQuiz loadQuiz = new LoadQuiz(new QuizListener() {
                @Override
                public void onStart() {
                    pb_details.setVisibility(View.VISIBLE);
                }

                @Override
                public void onEnd(String success, String message, ArrayList<ItemQuiz> arrayList_img, ArrayList<ItemQuiz> arrayList_no_img) {
                    if (success.equals("1")) {
                        if (arrayList_img.size() == 0) {
                            pb_details.setVisibility(View.GONE);
                        } else {
                            arrayList.addAll(arrayList_img);
                            arrayList.addAll(arrayList_no_img);
                            pb_details.setVisibility(View.GONE);
                            scrollView.setVisibility(View.VISIBLE);
                            rl_details_bottom.setBackgroundResource(R.drawable.bg_answer);
                            SetAdapter();
                        }
                    } else {
                        pb_details.setVisibility(View.GONE);
                    }
                }

            }, methods.getAPIRequest(Setting.METHOD_QUIZ, 0, Setting.language_id, "","","","","","","","","",""));
            loadQuiz.execute();
        } else {
            pb_details.setVisibility(View.GONE);
        }


    }

    private void SetAdapter() {
        adapter = new AdapterQuiz(this, arrayList);
        recyclerView.setAdapter(adapter);
        adapter.select(0);
        Setting.totelAnswer = arrayList.size();

        adapter.setOnItemClickListener(position -> {
            selectedPosition = position;
            adapter.select(position);
            loadData();
        });
        loadData();
    }

    @SuppressLint("SetTextI18n")
    private void loadData() {
        methods.showInter(1,"");
        tv_details_Answer.setText(arrayList.get(selectedPosition).getAnswer());
        int cont = selectedPosition + 1;
        int cont2 = arrayList.size();
        iv_cont.setText(cont +" / "+ cont2);

        if (!arrayList.get(selectedPosition).getImage().equals("")){
            iv_image.setVisibility(View.VISIBLE);
            loading.setVisibility(View.VISIBLE);
            if (Setting.Dark_Mode){
                Picasso.get()
                        .load(arrayList.get(selectedPosition).getImage())
                        .placeholder(R.drawable.placeholder_night)
                        .into(iv_image, new Callback() {
                            @Override
                            public void onSuccess() {
                                loading.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError(Exception e) {
                                loading.setVisibility(View.GONE);
                            }
                        });
            }else {
                Picasso.get()
                        .load(arrayList.get(selectedPosition).getImage())
                        .placeholder(R.drawable.placeholder_light)
                        .into(iv_image, new Callback() {
                            @Override
                            public void onSuccess() {
                                loading.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError(Exception e) {
                                loading.setVisibility(View.GONE);
                            }
                        });
            }

        }else {
            iv_image.setVisibility(View.GONE);
            loading.setVisibility(View.GONE);
        }

        tv_details_Answer_A.setText(arrayList.get(selectedPosition).getAnswerA());
        tv_details_Answer_B.setText(arrayList.get(selectedPosition).getAnswerB());
        tv_details_Answer_C.setText(arrayList.get(selectedPosition).getAnswerC());
        tv_details_Answer_D.setText(arrayList.get(selectedPosition).getAnswerD());

        rb_1.setChecked(false);
        rb_2.setChecked(false);
        rb_3.setChecked(false);
        rb_4.setChecked(false);

        if (arrayList.get(selectedPosition).isEnabled()){
            rb_1.setEnabled(false);
            rb_2.setEnabled(false);
            rb_3.setEnabled(false);
            rb_4.setEnabled(false);

            switch (arrayList.get(selectedPosition).isMyAnswer()) {
                case "A":
                    rl_bt_1.setBackgroundResource(R.drawable.bg_an_error);
                    rl_bt_2.setBackgroundResource(R.drawable.bg_an);
                    rl_bt_3.setBackgroundResource(R.drawable.bg_an);
                    rl_bt_4.setBackgroundResource(R.drawable.bg_an);
                    break;
                case "B":
                    rl_bt_1.setBackgroundResource(R.drawable.bg_an);
                    rl_bt_2.setBackgroundResource(R.drawable.bg_an_error);
                    rl_bt_3.setBackgroundResource(R.drawable.bg_an);
                    rl_bt_4.setBackgroundResource(R.drawable.bg_an);
                    break;
                case "C":
                    rl_bt_1.setBackgroundResource(R.drawable.bg_an);
                    rl_bt_2.setBackgroundResource(R.drawable.bg_an);
                    rl_bt_3.setBackgroundResource(R.drawable.bg_an_error);
                    rl_bt_4.setBackgroundResource(R.drawable.bg_an);
                    break;
                case "D":
                    rl_bt_1.setBackgroundResource(R.drawable.bg_an);
                    rl_bt_2.setBackgroundResource(R.drawable.bg_an);
                    rl_bt_3.setBackgroundResource(R.drawable.bg_an);
                    rl_bt_4.setBackgroundResource(R.drawable.bg_an_error);
                    break;
            }

            if (arrayList.get(selectedPosition).getCorrectAnswer().equals("A")){
                rl_bt_1.setBackgroundResource(R.drawable.bg_an_ok);
            }
            if (arrayList.get(selectedPosition).getCorrectAnswer().equals("B")){
                rl_bt_2.setBackgroundResource(R.drawable.bg_an_ok);
            }
            if (arrayList.get(selectedPosition).getCorrectAnswer().equals("C")){
                rl_bt_3.setBackgroundResource(R.drawable.bg_an_ok);
            }
            if (arrayList.get(selectedPosition).getCorrectAnswer().equals("D")){
                rl_bt_4.setBackgroundResource(R.drawable.bg_an_ok);
            }
        }else {
            rl_bt_1.setBackgroundResource(R.drawable.bg_an);
            rl_bt_2.setBackgroundResource(R.drawable.bg_an);
            rl_bt_3.setBackgroundResource(R.drawable.bg_an);
            rl_bt_4.setBackgroundResource(R.drawable.bg_an);

            rb_1.setEnabled(true);
            rb_2.setEnabled(true);
            rb_3.setEnabled(true);
            rb_4.setEnabled(true);
        }

        if (arrayList.get(selectedPosition).isMyAnswer().equals("A")){
            rb_1.setChecked(true);
            rb_2.setChecked(false);
            rb_3.setChecked(false);
            rb_4.setChecked(false);
        }else if (arrayList.get(selectedPosition).isMyAnswer().equals("B")){
            rb_1.setChecked(false);
            rb_2.setChecked(true);
            rb_3.setChecked(false);
            rb_4.setChecked(false);
        }else if (arrayList.get(selectedPosition).isMyAnswer().equals("C")){
            rb_1.setChecked(false);
            rb_2.setChecked(false);
            rb_3.setChecked(true);
            rb_4.setChecked(false);
        }else if (arrayList.get(selectedPosition).isMyAnswer().equals("D")){
            rb_1.setChecked(false);
            rb_2.setChecked(false);
            rb_3.setChecked(false);
            rb_4.setChecked(true);
        }

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}