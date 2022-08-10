package nemosofts.driving.exam.Utils;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAdListener;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ironsource.mediationsdk.ISBannerSize;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.IronSourceBannerLayout;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.sdk.InterstitialListener;
import com.startapp.sdk.ads.banner.Banner;
import com.startapp.sdk.adsbase.Ad;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;
import com.startapp.sdk.adsbase.adlisteners.AdDisplayListener;
import com.unity3d.ads.IUnityAdsListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.services.banners.BannerErrorInfo;
import com.unity3d.services.banners.BannerView;
import com.unity3d.services.banners.UnityBannerSize;
import com.yakivmospan.scytale.Crypto;
import com.yakivmospan.scytale.Options;
import com.yakivmospan.scytale.Store;

import org.jetbrains.annotations.NotNull;

import javax.crypto.SecretKey;

import es.dmoral.toasty.Toasty;
import nemosofts.driving.exam.BuildConfig;
import nemosofts.driving.exam.R;
import nemosofts.driving.exam.SharedPref.Setting;
import nemosofts.driving.exam.SharedPref.SharedPref;
import nemosofts.driving.exam.activity.LoginActivity;
import nemosofts.driving.exam.constant.Constant;
import nemosofts.driving.exam.interfaces.InterAdListener;
import nemosofts.driving.exam.item.ItemUser;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class Methods {

    private final Context context;
    private SecretKey key;
    private InterAdListener interAdListener;
    private StartAppAd startAppAd;


    @SuppressLint("CommitPrefEdits")
    public Methods(Context context) {
        this.context = context;
    }

    public Methods(Context context, InterAdListener interAdListener) {
        this.context = context;
        this.interAdListener = interAdListener;
        loadAds();
    }



    public Methods(Context context, Boolean flag) {
        this.context = context;
        Store store = new Store(context);
        if (!store.hasKey(BuildConfig.ENC_KEY)) {
            key = store.generateSymmetricKey(BuildConfig.ENC_KEY, null);
        } else {
            key = store.getSymmetricKey(BuildConfig.ENC_KEY, null);
        }
    }

    private void loadAds() {
        switch (Setting.interstitialAdType) {
            case "startApp":
                StartAppSDK.init(context, Setting.interstitialAdID, false);
                startAppAd = new StartAppAd(context);
                startAppAd.loadAd();
                break;
            case "unity":
                UnityAds.initialize(context, Setting.interstitialAdID, Constant.testMode);
                UnityAds.load("interstitial");
                break;
            case "iron":
                IronSource.init((Activity) context, Setting.interstitialAdID, IronSource.AD_UNIT.INTERSTITIAL);
                IronSource.loadInterstitial();
                break;
            default:
                break;
        }
    }


    public void showInter(final int pos, final String type) {
        if (Setting.isInterAd) {
            Setting.adCount = Setting.adCount + 1;
            if (Setting.adCount % Setting.interstitialAdShow == 0) {
                switch (Setting.interstitialAdType) {
                    case "admob":
                        final AdManagerAdmobInter adManagerAdmobInter = new AdManagerAdmobInter(context);
                        if(adManagerAdmobInter.getAd() != null) {
                            adManagerAdmobInter.getAd().setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    AdManagerAdmobInter.setAd(null);
                                    adManagerAdmobInter.createAd();
                                    interAdListener.onClick(pos, type);
                                    super.onAdDismissedFullScreenContent();
                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(@NonNull @NotNull com.google.android.gms.ads.AdError adError) {
                                    AdManagerAdmobInter.setAd(null);
                                    adManagerAdmobInter.createAd();
                                    interAdListener.onClick(pos, type);
                                    super.onAdFailedToShowFullScreenContent(adError);
                                }
                            });
                            adManagerAdmobInter.getAd().show((Activity) context);
                        } else {
                            AdManagerAdmobInter.setAd(null);
                            adManagerAdmobInter.createAd();
                            interAdListener.onClick(pos, type);
                        }
                        break;
                    case "facebook":
                        final AdManagerFB adManagerFB = new AdManagerFB(context);
                        if(adManagerFB.getAd() != null && adManagerFB.getAd().isAdLoaded()) {
                            adManagerFB.getAd().loadAd(adManagerFB.getAd().buildLoadAdConfig()
                                    .withAdListener(new InterstitialAdListener() {
                                        @Override
                                        public void onInterstitialDisplayed(com.facebook.ads.Ad ad) {

                                        }

                                        @Override
                                        public void onInterstitialDismissed(com.facebook.ads.Ad ad) {
                                            AdManagerFB.setAd(null);
                                            adManagerFB.createAd();
                                            interAdListener.onClick(pos, type);
                                        }

                                        @Override
                                        public void onError(com.facebook.ads.Ad ad, AdError adError) {

                                        }

                                        @Override
                                        public void onAdLoaded(com.facebook.ads.Ad ad) {

                                        }

                                        @Override
                                        public void onAdClicked(com.facebook.ads.Ad ad) {

                                        }

                                        @Override
                                        public void onLoggingImpression(com.facebook.ads.Ad ad) {

                                        }
                                    })
                                    .build());
                            adManagerFB.getAd().show();
                        } else {
                            AdManagerFB.setAd(null);
                            adManagerFB.createAd();
                            interAdListener.onClick(pos, type);
                        }
                        break;
                    case "startApp":
                        startAppAd.showAd(new AdDisplayListener() {
                            @Override
                            public void adHidden(Ad ad) {
                                interAdListener.onClick(pos, type);
                            }

                            @Override
                            public void adDisplayed(Ad ad) {

                            }

                            @Override
                            public void adClicked(Ad ad) {

                            }

                            @Override
                            public void adNotDisplayed(Ad ad) {
                                interAdListener.onClick(pos, type);
                            }
                        });
                        loadAds();
                        break;
                    case "unity":
                        IUnityAdsListener unityAdsListener = new IUnityAdsListener() {
                            @Override
                            public void onUnityAdsReady(String s) {

                            }

                            @Override
                            public void onUnityAdsStart(String s) {

                            }

                            @Override
                            public void onUnityAdsFinish(String s, UnityAds.FinishState finishState) {
                                interAdListener.onClick(pos, type);
                            }

                            @Override
                            public void onUnityAdsError(UnityAds.UnityAdsError unityAdsError, String s) {
                                interAdListener.onClick(pos, type);
                            }
                        };
                        UnityAds.setListener(unityAdsListener);
                        if (UnityAds.isReady("interstitial")) {
                            UnityAds.show((Activity) context, "interstitial");
                        } else {
                            interAdListener.onClick(pos, type);
                        }
                        loadAds();
                        break;
                    case "iron":
                        IronSource.setInterstitialListener(new InterstitialListener() {
                            @Override
                            public void onInterstitialAdReady() {

                            }

                            @Override
                            public void onInterstitialAdLoadFailed(IronSourceError error) {

                            }

                            @Override
                            public void onInterstitialAdOpened() {
                            }

                            @Override
                            public void onInterstitialAdClosed() {
                                interAdListener.onClick(pos, type);
                            }

                            @Override
                            public void onInterstitialAdShowFailed(IronSourceError error) {
                                interAdListener.onClick(pos, type);
                            }

                            @Override
                            public void onInterstitialAdClicked() {
                            }

                            @Override
                            public void onInterstitialAdShowSucceeded() {
                            }
                        });
                        if (IronSource.isInterstitialReady()) {
                            //show the interstitial
                            IronSource.showInterstitial();
                        }
                        loadAds();
                        break;
                    default:
                        interAdListener.onClick(pos, type);
                        break;
                }
            } else {
                interAdListener.onClick(pos, type);
            }
        } else {
            interAdListener.onClick(pos, type);
        }
    }

    private AdView showPersonalizedAds(LinearLayout linearLayout) {
        if (Setting.isBannerAd) {
            AdView adView = new AdView(context);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.setAdUnitId(Setting.bannerAdID);
            if (Setting.banner_size.equals("SMART_BANNER")){
                adView.setAdSize(AdSize.SMART_BANNER);
            }else {
                adView.setAdSize(AdSize.BANNER);
            }
            linearLayout.addView(adView);
            adView.loadAd(adRequest);
            return adView;
        } else {
            return null;
        }
    }

    private AdView showNonPersonalizedAds(LinearLayout linearLayout) {
        Bundle extras = new Bundle();
        extras.putString("npa", "1");
        if (Setting.isBannerAd) {
            AdView adView = new AdView(context);
            AdRequest adRequest = new AdRequest.Builder()
                    .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                    .build();
            adView.setAdUnitId(Setting.bannerAdID);
            if (Setting.banner_size.equals("SMART_BANNER")){
                adView.setAdSize(AdSize.SMART_BANNER);
            }else {
                adView.setAdSize(AdSize.BANNER);
            }
            linearLayout.addView(adView);
            adView.loadAd(adRequest);
            return adView;
        } else {
            return null;
        }
    }

    public AdView showBannerAd(LinearLayout linearLayout) {
        if (isNetworkAvailable()) {
            if (Setting.isBannerAd){
                switch (Setting.bannerAdType) {
                    case "admob":
                        if (ConsentInformation.getInstance(context).getConsentStatus() == ConsentStatus.NON_PERSONALIZED) {
                            return showNonPersonalizedAds(linearLayout);
                        } else {
                            return showPersonalizedAds(linearLayout);
                        }
                    case "facebook":
                        com.facebook.ads.AdView adView;
                        if (Setting.banner_size.equals("BANNER_HEIGHT_90")) {
                            adView = new com.facebook.ads.AdView(context, Setting.bannerAdID, com.facebook.ads.AdSize.BANNER_HEIGHT_90);
                        } else {
                            adView = new com.facebook.ads.AdView(context, Setting.bannerAdID, com.facebook.ads.AdSize.BANNER_HEIGHT_50);
                        }
                        adView.loadAd();
                        linearLayout.addView(adView);
                        return null;
                    case "startApp":
                        StartAppSDK.init(context, Setting.bannerAdID, false);
                        Banner startAppBanner = new Banner(context);
                        linearLayout.addView(startAppBanner);
                        return null;
                    case "unity":
                        UnityAds.initialize(context, Setting.bannerAdID, Constant.testMode);
                        BannerView bannerView = new BannerView((Activity) context, "banner", new UnityBannerSize(320, 50));
                        bannerView.setListener(new BannerView.Listener() {
                            @Override
                            public void onBannerLoaded(BannerView bannerAdView) {
                                super.onBannerLoaded(bannerAdView);
                            }

                            @Override
                            public void onBannerFailedToLoad(BannerView bannerAdView, BannerErrorInfo errorInfo) {
                                super.onBannerFailedToLoad(bannerAdView, errorInfo);
                            }

                            @Override
                            public void onBannerClick(BannerView bannerAdView) {
                                super.onBannerClick(bannerAdView);
                            }

                            @Override
                            public void onBannerLeftApplication(BannerView bannerAdView) {
                                super.onBannerLeftApplication(bannerAdView);
                            }
                        });
                        bannerView.load();
                        bannerView.setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
                        linearLayout.addView(bannerView);
                        return null;

                    case "iron":
                        IronSource.init((Activity) context, Setting.bannerAdID, IronSource.AD_UNIT.BANNER);
                        ISBannerSize size;
                        if (Setting.banner_size.equals("BANNER_HEIGHT_90")) {
                            size = ISBannerSize.LARGE;
                        } else {
                            size = ISBannerSize.BANNER;
                        }
                        IronSourceBannerLayout banner  = IronSource.createBanner((Activity) context, size);
                        linearLayout.addView(banner);
                        banner.setBannerListener(new com.ironsource.mediationsdk.sdk.BannerListener() {
                            @Override
                            public void onBannerAdLoaded() {

                            }
                            @Override
                            public void onBannerAdLoadFailed(IronSourceError error) {
                                linearLayout.setVisibility(View.GONE);
                            }
                            @Override
                            public void onBannerAdClicked() {

                            }
                            @Override
                            public void onBannerAdScreenPresented() {

                            }
                            @Override
                            public void onBannerAdScreenDismissed() {

                            }
                            @Override
                            public void onBannerAdLeftApplication() {

                            }
                        });
                        IronSource.loadBanner(banner);
                        return null;
                    default:
                        return null;
                }
            }else {
                return null;
            }
        } else {
            return null;
        }
    }

    public String encrypt(String value) {
        try {
            Crypto crypto = new Crypto(Options.TRANSFORMATION_SYMMETRIC);
            return crypto.encrypt(value, key);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String decrypt(String value) {
        try {
            Crypto crypto = new Crypto(Options.TRANSFORMATION_SYMMETRIC);
            return crypto.decrypt(value, key);
        } catch (Exception e) {
            Crypto crypto = new Crypto(Options.TRANSFORMATION_SYMMETRIC);
            return crypto.encrypt("null", key);
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void showSnackBar(String message, boolean type) {
        if (type){
            Toasty.success(context, message, Toast.LENGTH_SHORT, true).show();
        }else {
            Toasty.error(context, message, Toast.LENGTH_SHORT, true).show();
        }
    }

    public RequestBody getAPIRequest(String method, int page, int itemID, String catID, String loginType, String email, String password, String name, String phone, String auth_id, String userID, String comment, String reportMessage) {
        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API());
        jsObj.addProperty("method_name", method);
        jsObj.addProperty("package_name", context.getPackageName());

        switch (method) {
            case Setting.METHOD_LOGIN:
                jsObj.addProperty("email", email);
                jsObj.addProperty("password", password);
                jsObj.addProperty("auth_id", auth_id);
                jsObj.addProperty("type", loginType);
                break;

            case Setting.METHOD_REGISTER:
                jsObj.addProperty("name", name);
                jsObj.addProperty("email", email);
                jsObj.addProperty("password", password);
                jsObj.addProperty("phone", phone);
                jsObj.addProperty("auth_id", auth_id);
                jsObj.addProperty("type", loginType);
                break;
            case Setting.METHOD_FORGOT_PASSWORD:
                jsObj.addProperty("user_email", email);
                break;

            case Setting.METHOD_PROFILE:
                jsObj.addProperty("user_id", userID);
                break;

            case Setting.METHOD_EDIT_PROFILE:
                jsObj.addProperty("user_id", userID);
                jsObj.addProperty("name", name);
                jsObj.addProperty("email", email);
                jsObj.addProperty("password", password);
                jsObj.addProperty("phone", phone);
                break;


            case Setting.METHOD_CAT:
                jsObj.addProperty("page", String.valueOf(page));
                break;

            case Setting.METHOD_CAT_ID:
                jsObj.addProperty("lan_id", String.valueOf(itemID));
                jsObj.addProperty("cat_id", catID);
                jsObj.addProperty("page", String.valueOf(page));
                break;

            case Setting.METHOD_QUIZ:
                jsObj.addProperty("lan_id", String.valueOf(itemID));
                break;
        }

        return new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("data", API.toBase64(jsObj.toString()))
                .build();
    }


    public void getVerifyDialog(String title, String message) {
        AlertDialog.Builder alertDialog;
        if (ApplicationUtil.isDarkMode()){
            alertDialog = new AlertDialog.Builder(context, R.style.ThemeDialog2);
        }else {
            alertDialog = new AlertDialog.Builder(context, R.style.ThemeDialog);
        }
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("OK", (dialog, which) -> {
        });
        alertDialog.show();
    }

    public void clickLogin() {
        if (Setting.isLogged) {
            logout((Activity) context);
            Toast.makeText(context, context.getString(R.string.logout_success), Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(context, LoginActivity.class);
            intent.putExtra("from", "app");
            context.startActivity(intent);
        }
    }

    public void changeAutoLogin(Boolean isAutoLogin) {
        SharedPref sharePref = new SharedPref(context);
        sharePref.setIsAutoLogin(isAutoLogin);
    }

    public void logout(Activity activity) {
        changeAutoLogin(false);
        Setting.isLogged = false;
        Setting.itemUser = new ItemUser("", "", "", "", "", Setting.LOGIN_TYPE_NORMAL);
        Intent intent1 = new Intent(context, LoginActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent1.putExtra("from", "");
        context.startActivity(intent1);
        activity.finish();
    }
}
