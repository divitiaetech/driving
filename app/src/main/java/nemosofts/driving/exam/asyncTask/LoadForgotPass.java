package nemosofts.driving.exam.asyncTask;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import nemosofts.driving.exam.SharedPref.Setting;
import nemosofts.driving.exam.Utils.JSONParser;
import nemosofts.driving.exam.interfaces.SuccessListener;
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

public class LoadForgotPass extends AsyncTask<String, String, String> {

    private final RequestBody requestBody;
    private final SuccessListener successListener;
    private String success = "0", message = "";

    public LoadForgotPass(SuccessListener successListener, RequestBody requestBody) {
        this.successListener = successListener;
        this.requestBody = requestBody;
    }

    @Override
    protected void onPreExecute() {
        successListener.onStart();
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
            }
            return "1";
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }

    @Override
    protected void onPostExecute(String s) {
        successListener.onEnd(s, success, message);
        super.onPostExecute(s);
    }
}