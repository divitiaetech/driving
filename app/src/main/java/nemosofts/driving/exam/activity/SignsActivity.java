package nemosofts.driving.exam.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import lk.nemosofts.androidsdk.ProCompatActivity;
import nemosofts.driving.exam.R;
import nemosofts.driving.exam.SharedPref.Setting;
import nemosofts.driving.exam.Utils.ApplicationUtil;
import nemosofts.driving.exam.Utils.EndlessRecyclerViewScrollListener;
import nemosofts.driving.exam.Utils.Methods;
import nemosofts.driving.exam.adapter.AdapterCategory;
import nemosofts.driving.exam.asyncTask.LoadCat;
import nemosofts.driving.exam.ifSupported.IsRTL;
import nemosofts.driving.exam.ifSupported.IsScreenshot;
import nemosofts.driving.exam.interfaces.CategoryListener;
import nemosofts.driving.exam.item.ItemCat;

public class SignsActivity extends ProCompatActivity {

    private Methods methods;
    private RecyclerView rv;
    private AdapterCategory adapter;
    private ArrayList<ItemCat> arrayList;
    private ProgressBar progressBar;
    private FrameLayout frameLayout;
    private String errr_msg;
    private GridLayoutManager glm;
    private int page = 1;
    private Boolean isOver = false, isScroll = false;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IsScreenshot.ifSupported(this);
        IsRTL.ifSupported(this);

        methods = new Methods(this);
        arrayList = new ArrayList<>();

        Toolbar toolbar = findViewById(R.id.toolbar_signs);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        methods = new Methods(this, (position, type) -> {
            Intent intent = new Intent(SignsActivity.this, SignsByActivity.class);
            intent.putExtra("name", arrayList.get(position).getCategory_name());
            intent.putExtra("cid", arrayList.get(position).getCid());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ActivityCompat.startActivity(SignsActivity.this, intent, null);
        });

        progressBar = findViewById(R.id.pb_load);
        frameLayout = findViewById(R.id.fl_empty);

        rv = findViewById(R.id.recycler);
        glm = new GridLayoutManager(SignsActivity.this, 1);
        glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapter.isHeader(position) ? glm.getSpanCount() : 1;
            }
        });
        rv.setLayoutManager(glm);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setHasFixedSize(true);

        rv.addOnScrollListener(new EndlessRecyclerViewScrollListener(glm) {
            @Override
            public void onLoadMore(int p, int totalItemsCount) {
                if (!isOver) {
                    new Handler().postDelayed(() -> {
                        isScroll = true;
                        getData();
                    }, 0);
                } else {
                    try {
                        adapter.hideHeader();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        fab = findViewById(R.id.fab);
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstVisibleItem = glm.findFirstVisibleItemPosition();

                if (firstVisibleItem > 6) {
                    fab.show();
                } else {
                    fab.hide();
                }
            }
        });

        fab.setOnClickListener(v -> rv.smoothScrollToPosition(0));
        getData();

        LinearLayout AdView_radio = findViewById(R.id.adView);
        methods.showBannerAd(AdView_radio);

    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_signs;
    }

    @Override
    protected boolean setDarkMode() {
        return ApplicationUtil.isDarkMode();
    }

    private void getData() {
        if (methods.isNetworkAvailable()) {
            LoadCat loadCat = new LoadCat(new CategoryListener() {
                @Override
                public void onStart() {
                    if (arrayList.size() == 0) {
                        frameLayout.setVisibility(View.GONE);
                        rv.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onEnd(String success, String verifyStatus, String message, ArrayList<ItemCat> itemCats) {
                    if (success.equals("1")) {
                        if (!verifyStatus.equals("-1")) {
                            if (itemCats.size() == 0) {
                                isOver = true;
                                try {
                                    adapter.hideHeader();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                errr_msg = getString(R.string.no_data_found);
                                setEmpty();
                            } else {
                                page = page + 1;
                                arrayList.addAll(itemCats);
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
            }, methods.getAPIRequest(Setting.METHOD_CAT, page,  Setting.language_id, "","","","","","","","","",""));
            loadCat.execute();
        } else {
            errr_msg = getString(R.string.internet_not_conn);
            setEmpty();
            progressBar.setVisibility(View.GONE);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setAdapter() {
        if (!isScroll) {
            adapter = new AdapterCategory(arrayList, (itemCat, position) -> methods.showInter(position,""));
            rv.setAdapter(adapter);
            setEmpty();
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @SuppressLint("InflateParams")
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

            View myView;
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