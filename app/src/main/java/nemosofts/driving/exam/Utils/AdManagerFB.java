package nemosofts.driving.exam.Utils;

import android.content.Context;

import com.facebook.ads.InterstitialAd;

import nemosofts.driving.exam.SharedPref.Setting;

public class AdManagerFB {
    static InterstitialAd interAd;
    private final Context ctx;

    public AdManagerFB(Context ctx) {
        this.ctx = ctx;
    }

    public void createAd() {
        interAd = new InterstitialAd(ctx, Setting.interstitialAdID);
        interAd.loadAd();
    }

    public InterstitialAd getAd() {
        return interAd;
    }

    public static void setAd(InterstitialAd interstitialAd) {
        interAd = interstitialAd;
    }
}