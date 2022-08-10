package nemosofts.driving.exam.asyncTask;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import nemosofts.driving.exam.Utils.JSONParser;
import nemosofts.driving.exam.Utils.Methods;
import nemosofts.driving.exam.SharedPref.Setting;
import nemosofts.driving.exam.interfaces.AboutListener;

public class LoadAbout extends AsyncTask<String, String, String> {

    private final Methods methods;
    private final AboutListener aboutListener;
    private String message = "", verifyStatus = "0";

    public LoadAbout(Context context, AboutListener aboutListener) {
        this.aboutListener = aboutListener;
        methods = new Methods(context);
    }

    @Override
    protected void onPreExecute() {
        aboutListener.onStart();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            String json = JSONParser.okhttpPost(Setting.SERVER_URL, methods.getAPIRequest(Setting.METHOD_APP_DETAILS, 0, 0,"","","","","","","","","",""));
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.has(Setting.TAG_ROOT)) {
                JSONArray jsonArray = jsonObject.getJSONArray(Setting.TAG_ROOT);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject c = jsonArray.getJSONObject(i);

                    if (!c.has(Setting.TAG_SUCCESS)) {
                        Setting.packageName = c.getString("package_name");

                        Setting.appname = c.getString("app_name");
                        Setting.applogo = c.getString("app_logo");
                        Setting.desc = c.getString("app_description");
                        Setting.appversion = c.getString("app_version");
                        Setting.appauthor = c.getString("app_author");
                        Setting.appcontact = c.getString("app_contact");
                        Setting.email = c.getString("app_email");
                        Setting.website = c.getString("app_website");
                        Setting.privacy = c.getString("app_privacy_policy");
                        Setting.developedby = c.getString("app_developed_by");

                        Setting.publisherAdID = c.getString("publisher_id");
                        Setting.isBannerAd = Boolean.parseBoolean(c.getString("banner_ad"));
                        Setting.isInterAd = Boolean.parseBoolean(c.getString("interstital_ad"));
                        Setting.isNativeAd = Boolean.parseBoolean(c.getString("native_ad"));
                        Setting.bannerAdType = c.getString("banner_ad_type");
                        Setting.interstitialAdType = c.getString("interstital_ad_type");
                        Setting.natveAdType = c.getString("native_ad_type");
                        Setting.bannerAdID = c.getString("banner_ad_id");
                        Setting.interstitialAdID = c.getString("interstital_ad_id");
                        Setting.nativeAdID = c.getString("native_ad_id");
                        Setting.interstitialAdShow = Integer.parseInt(c.getString("interstital_ad_click"));
                        Setting.nativeAdShow = Integer.parseInt(c.getString("native_position"));
                        Setting.banner_size = c.getString("banner_size");

                        Setting.showUpdateDialog = Boolean.parseBoolean(c.getString("isUpdate"));
                        if(!c.getString("version").equals("")) {
                            Setting.appVersion = Integer.parseInt(c.getString("version"));
                        }
                        Setting.version_name = c.getString("version_name");
                        Setting.description = c.getString("description");
                        Setting.url = c.getString("url");

                        Setting.isRTL = Boolean.parseBoolean(c.getString("isRTL"));
                        Setting.isScreenshot = Boolean.parseBoolean(c.getString("isScreenshot"));
                        Setting.facebook_login = Boolean.parseBoolean(c.getString("facebook_login"));
                        Setting.google_login = Boolean.parseBoolean(c.getString("google_login"));

                        Setting.purchase_code = c.getString("envato_purchase_code");
                        Setting.apikey = c.getString("app_api_key");

                    } else {
                        verifyStatus = c.getString(Setting.TAG_SUCCESS);
                        message = c.getString(Setting.TAG_MSG);
                    }
                }
            }
            return "1";
        } catch (Exception ee) {
            ee.printStackTrace();
            return "0";
        }
    }

    @Override
    protected void onPostExecute(String s) {
        aboutListener.onEnd(s, verifyStatus, message);
        super.onPostExecute(s);
    }
}