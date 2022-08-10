package nemosofts.driving.exam.asyncTask;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import nemosofts.driving.exam.SharedPref.Setting;
import nemosofts.driving.exam.Utils.JSONParser;
import nemosofts.driving.exam.interfaces.SocialLoginListener;
import okhttp3.RequestBody;

/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : thivakaran
 * Telegram : @nemosofts
 * Contact : info.nemosofts@gmail.com
 * Skype : skype.nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class LoadRegister extends AsyncTask<String, String, String> {

    private final RequestBody requestBody;
    private final SocialLoginListener socialLoginListener;
    private String success = "0", message = "", user_id = "", user_name = "", email = "", auth_id = "";

    public LoadRegister(SocialLoginListener socialLoginListener, RequestBody requestBody) {
        this.socialLoginListener = socialLoginListener;
        this.requestBody = requestBody;
    }

    @Override
    protected void onPreExecute() {
        socialLoginListener.onStart();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            String json = JSONParser.okhttpPost(Setting.SERVER_URL, requestBody);
            JSONObject mainJson = new JSONObject(json);
            JSONArray jsonArray = mainJson.getJSONArray(Setting.TAG_ROOT);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);

                success = c.getString(Setting.TAG_SUCCESS);
                message = c.getString(Setting.TAG_MSG);
                if(c.has("user_id")) {
                    user_id = c.getString("user_id");
                    user_name = c.getString("name");

                    auth_id = c.getString("auth_id");
                    email = c.getString("email");
                }
            }
            return "1";
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }

    @Override
    protected void onPostExecute(String s) {
        socialLoginListener.onEnd(s, success, message, user_id, user_name, email, auth_id);
        super.onPostExecute(s);
    }
}