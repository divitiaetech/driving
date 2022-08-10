package nemosofts.driving.exam.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.Task;

import lk.nemosofts.androidsdk.ProCompatActivity;
import nemosofts.driving.exam.R;
import nemosofts.driving.exam.SharedPref.Setting;
import nemosofts.driving.exam.SharedPref.SharedPref;
import nemosofts.driving.exam.Utils.ApplicationUtil;
import nemosofts.driving.exam.Utils.Methods;
import nemosofts.driving.exam.ifSupported.IsRTL;
import nemosofts.driving.exam.ifSupported.IsScreenshot;

public class MainActivity extends ProCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Methods methods;
    private MenuItem menu_login, menu_prof;
    private ReviewManager manager;
    private ReviewInfo reviewInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPref sharedPref = new SharedPref(this);
        Setting.language_id = sharedPref.getLanguage();
        Setting.Dark_Mode = sharedPref.getNightMode();
        super.onCreate(savedInstanceState);

        IsScreenshot.ifSupported(this);
        IsRTL.ifSupported(this);

        methods = new Methods(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setToolbarNavigationClickListener(view -> drawer.openDrawer(GravityCompat.START));

        if (Setting.Dark_Mode){
            toggle.setHomeAsUpIndicator(R.drawable.ic_menu2);
        }else {
            toggle.setHomeAsUpIndicator(R.drawable.ic_menu1);
        }

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(false);

        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();
        menu_login = menu.findItem(R.id.nav_login);
        menu_prof = menu.findItem(R.id.nav_profile);

        LinearLayout ll_exam = findViewById(R.id.ll_exam);
        ll_exam.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, QuizDetails.class);
            startActivity(intent);
        });

        LinearLayout ll_lane = findViewById(R.id.ll_lane);
        ll_lane.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SignsActivity.class);
            startActivity(intent);
        });

        LinearLayout AdView= findViewById(R.id.adView);
        methods.showBannerAd(AdView);

        Button btn_result = findViewById(R.id.btn_result);
        btn_result.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, OldResultActivity.class);
            startActivity(intent);
        });

        manager = ReviewManagerFactory.create(this);
        Task<ReviewInfo> request = manager.requestReviewFlow();
        request.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                reviewInfo = task.getResult();
            }
        });

        changeLoginName();

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void changeLoginName() {
        if (menu_login != null) {
            if (Setting.isLogged) {
                menu_prof.setVisible(true);
                menu_login.setTitle(getResources().getString(R.string.logout));
                menu_login.setIcon(getResources().getDrawable(R.drawable.ic_logout));
            } else {
                menu_prof.setVisible(false);
                menu_login.setTitle(getResources().getString(R.string.login));
                menu_login.setIcon(getResources().getDrawable(R.drawable.ic_login));
            }
        }
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_main;
    }

    @Override
    protected boolean setDarkMode() {
        return ApplicationUtil.isDarkMode();
    }


    @SuppressLint("NonConstantResourceId")
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case R.id.nav_road_signs:
                    Intent intent = new Intent(MainActivity.this, SignsActivity.class);
                    startActivity(intent);
                break;

            case R.id.nav_translation:
                overridePendingTransition(0, 0);
                overridePendingTransition(0, 0);
                startActivity(new Intent(MainActivity.this, LanguageActivity.class));
                finish();
                break;
            case R.id.nav_profile:
                ActivityCompat.startActivity(MainActivity.this, new Intent(MainActivity.this, ProfileActivity.class), null);
                break;

            case R.id.nav_set:
                overridePendingTransition(0, 0);
                overridePendingTransition(0, 0);
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                finish();
                break;
            case R.id.nav_login:
                methods.clickLogin();
                break;
            default :
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else if (reviewInfo != null){
            Task<Void> flow = manager.launchReviewFlow(MainActivity.this, reviewInfo);
            flow.addOnCompleteListener(task1 -> exitDialog());
        }else {
            exitDialog();
        }
    }

    private void exitDialog() {
        AlertDialog.Builder alert;
        if (Setting.Dark_Mode){
            alert = new AlertDialog.Builder(MainActivity.this, R.style.ThemeDialog2);
        }else {
            alert = new AlertDialog.Builder(MainActivity.this, R.style.ThemeDialog);
        }

        alert.setTitle(getString(R.string.exit));
        alert.setMessage(getString(R.string.sure_exit));
        alert.setPositiveButton(getString(R.string.exit), (dialogInterface, i) -> finish());
        alert.setNegativeButton(getString(R.string.cancel), (dialogInterface, i) -> {

        });
        alert.show();
    }

    @Override
    protected void onResume() {
        changeLoginName();
        super.onResume();
    }

}