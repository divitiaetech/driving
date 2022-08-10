package nemosofts.driving.exam.SharedPref;

import java.io.Serializable;
import java.util.ArrayList;

import nemosofts.driving.exam.BuildConfig;
import nemosofts.driving.exam.item.ItemQuiz;
import nemosofts.driving.exam.item.ItemUser;


public class Setting implements Serializable {

    private static final long serialVersionUID = 1L;

    public static String api="speed_api.php";
    public static String SERVER_URL = BuildConfig.SERVER_URL + api;
    public static final String TAG_ROOT = "QUIZ_APP";
    public static final String TAG_SUCCESS = "success";
    public static final String TAG_MSG = "MSG";
    public static Boolean Dark_Mode = false;
    public static ArrayList<ItemQuiz> arrayList = new ArrayList<>();
    public static  int okAnswer = 0;
    public static  int totelAnswer = 0;
    public static  int noAnswer = 0;
    public static final String METHOD_CAT = "cat_list";
    public static final String METHOD_LANGUAGE = "language_list";
    public static final String METHOD_CAT_ID = "cat_id_by";
    public static final String METHOD_QUIZ = "quiz";
    public static final String METHOD_APP_DETAILS = "app_details";
    public static final String TAG_CID = "cid";
    public static final String TAG_CAT_NAME = "category_name";
    public static final String TAG_CAT_IMAGE = "category_image";

    public static Boolean  isBannerAd = true, isInterAd = true, isNativeAd = true;
    public static String packageName, bannerAdType = "admob", interstitialAdType = "admob", natveAdType = "admob";
    public static String banner_size = "banner_size";
    public static int adCount = 0;
    public static int interstitialAdShow = 5, nativeAdShow = 6;
    public static String publisherAdID = "";
    public static String bannerAdID = "";
    public static String interstitialAdID = "";
    public static String nativeAdID = "";

    public static String appname = "";
    public static String applogo = "";
    public static String desc = "";
    public static String appversion = "";
    public static String appauthor = "";
    public static String appcontact = "";
    public static String email = "";
    public static String website = "";
    public static String privacy = "";
    public static String developedby = "";

    public static String purchase_code = "";
    public static String apikey = "";

    public static int language_id = 0;

    public static Boolean facebook_login = false;
    public static Boolean google_login= false;
    public static Boolean isRTL = false;
    public static Boolean isScreenshot = false;

    public static Boolean showUpdateDialog = false;
    public static int appVersion = 1;
    public static String version_name="1.0.0";
    public static String description="";
    public static String url="";


    public static final String METHOD_LOGIN = "user_login";
    public static final String METHOD_REGISTER = "user_register";
    public static final String METHOD_FORGOT_PASSWORD = "forgot_pass";
    public static final String METHOD_PROFILE = "user_profile";
    public static final String METHOD_EDIT_PROFILE = "user_profile_update";
    public static final String LOGIN_TYPE_NORMAL = "normal";
    public static final String LOGIN_TYPE_GOOGLE = "google";
    public static final String LOGIN_TYPE_FB = "facebook";

    public static ItemUser itemUser = new ItemUser("","","","","", LOGIN_TYPE_NORMAL);
    public static Boolean isLogged = false;
    public static Boolean isUpdate = false;
}
