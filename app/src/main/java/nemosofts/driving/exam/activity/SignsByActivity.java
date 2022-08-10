package nemosofts.driving.exam.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import lk.nemosofts.androidsdk.ProCompatActivity;
import nemosofts.driving.exam.R;
import nemosofts.driving.exam.SharedPref.Setting;
import nemosofts.driving.exam.Utils.ApplicationUtil;
import nemosofts.driving.exam.Utils.EndlessRecyclerViewScrollListener;
import nemosofts.driving.exam.Utils.Methods;
import nemosofts.driving.exam.Utils.RecyclerItemClickListener;
import nemosofts.driving.exam.adapter.AdapterSigns;
import nemosofts.driving.exam.asyncTask.LoadSigns;
import nemosofts.driving.exam.ifSupported.IsRTL;
import nemosofts.driving.exam.ifSupported.IsScreenshot;
import nemosofts.driving.exam.interfaces.SignsListener;
import nemosofts.driving.exam.item.ItemSigns;

public class SignsByActivity extends ProCompatActivity {

    private Methods methods;
    private RecyclerView rv;
    private AdapterSigns adapterAlbums;
    private ArrayList<ItemSigns> arrayList;
    private ProgressBar progressBar;
    private String id = "";
    private FrameLayout frameLayout;
    private String errr_msg;
    private GridLayoutManager glm_banner;
    private int page = 1;
    private Boolean isOver = false, isScroll = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IsScreenshot.ifSupported(this);
        IsRTL.ifSupported(this);

        Toolbar toolbar = findViewById(R.id.toolbar_signs_by);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        id = getIntent().getStringExtra("cid");

        methods = new Methods(this, (position, type) -> {
            String name =  arrayList.get(position).getSignsName();
            String img =  arrayList.get(position).getSignsImage();
            showDialog(name, img);
        });


        arrayList = new ArrayList<>();

        progressBar = findViewById(R.id.pb_albums);
        frameLayout = findViewById(R.id.fl_empty);

        rv = findViewById(R.id.rv_albums);
        glm_banner = new GridLayoutManager(this, 2);
        glm_banner.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapterAlbums.isHeader(position) ? glm_banner.getSpanCount() : 1;
            }
        });

        rv.setLayoutManager(glm_banner);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setHasFixedSize(true);

        rv.addOnItemTouchListener(new RecyclerItemClickListener(this, (view, position) -> methods.showInter(position, "")));

        rv.addOnScrollListener(new EndlessRecyclerViewScrollListener(glm_banner) {
            @Override
            public void onLoadMore(int p, int totalItemsCount) {
                if (!isOver) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isScroll = true;
                            loadAlbums();
                        }
                    }, 0);
                } else {
                    adapterAlbums.hideHeader();
                }
            }
        });

        loadAlbums();
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_signs_by;
    }

    @Override
    protected boolean setDarkMode() {
        return ApplicationUtil.isDarkMode();
    }

    private void loadAlbums() {
        if (methods.isNetworkAvailable()) {
            LoadSigns loadSigns = new LoadSigns(new SignsListener() {
                @Override
                public void onStart() {
                    if (arrayList.size() == 0) {
                        frameLayout.setVisibility(View.GONE);
                        rv.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onEnd(String success, String verifyStatus, String message, ArrayList<ItemSigns> arrayListAlbums) {
                    if (success.equals("1")) {
                        if (!verifyStatus.equals("-1")) {
                            if (arrayListAlbums.size() == 0) {
                                isOver = true;
                                errr_msg = getString(R.string.no_data_found);
                                try {
                                    adapterAlbums.hideHeader();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                setEmpty();
                            } else {
                                page = page + 1;
                                arrayList.addAll(arrayListAlbums);
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
            }, methods.getAPIRequest(Setting.METHOD_CAT_ID, page, Setting.language_id, id,"","","","","","","","",""));
            loadSigns.execute();
        } else {
            errr_msg = getString(R.string.err_internet_not_conn);
            setEmpty();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setAdapter() {
        if (!isScroll) {
            adapterAlbums = new AdapterSigns(this, arrayList);
            rv.setAdapter(adapterAlbums);
            setEmpty();
        } else {
            adapterAlbums.notifyDataSetChanged();
        }
    }

    @SuppressLint("InflateParams")
    public void setEmpty() {
        if (arrayList.size() > 0) {
            rv.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
        } else {
            rv.setVisibility(View.GONE);
            frameLayout.setVisibility(View.VISIBLE);

            frameLayout.removeAllViews();
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View myView = null;
            if (errr_msg.equals(getString(R.string.no_data_found))) {
                myView = inflater.inflate(R.layout.layout_err_nodata, null);
            } else if (errr_msg.equals(getString(R.string.err_internet_not_conn))) {
                myView = inflater.inflate(R.layout.layout_err_internet, null);
            } else if (errr_msg.equals(getString(R.string.err_server))) {
                myView = inflater.inflate(R.layout.layout_err_server, null);
            }

            assert myView != null;
            TextView textView = myView.findViewById(R.id.tv_empty_msg);
            textView.setText(errr_msg);

            myView.findViewById(R.id.btn_empty_try).setOnClickListener(v -> {
                loadAlbums();
            });

            frameLayout.addView(myView);
        }
    }

    public void showDialog(String message, String img) {
        Dialog dialog_rate = new Dialog(SignsByActivity.this);
        dialog_rate.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_rate.setContentView(R.layout.item_dialog);

        final ImageView iv_close = dialog_rate.findViewById(R.id.close);
        final TextView tv_dialog = dialog_rate.findViewById(R.id.tv_dialog);
        final ImageView iv_dialog = dialog_rate.findViewById(R.id.iv_dialog);

        tv_dialog.setText(message);

        if (ApplicationUtil.isDarkMode()){
            Picasso.get()
                    .load(img)
                    .placeholder(R.drawable.placeholder_night)
                    .into(iv_dialog);
        }else {
            Picasso.get()
                    .load(img)
                    .placeholder(R.drawable.placeholder_light)
                    .into(iv_dialog);
        }

        iv_close.setOnClickListener(view -> dialog_rate.dismiss());

        dialog_rate.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog_rate.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog_rate.show();
        Window window = dialog_rate.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }
}