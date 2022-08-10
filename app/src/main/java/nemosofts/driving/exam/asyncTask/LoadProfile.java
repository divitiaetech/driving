package nemosofts.driving.exam.asyncTask;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import nemosofts.driving.exam.SharedPref.Setting;
import nemosofts.driving.exam.Utils.JSONParser;
import nemosofts.driving.exam.interfaces.SuccessListener;
import nemosofts.driving.exam.item.ItemUser;
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

public class LoadProfile extends AsyncTask<String, String, String> {

    private final RequestBody requestBody;
    private final SuccessListener successListener;
    private String success = "0", message = "";

    public LoadProfile(SuccessListener successListener, RequestBody requestBody) {
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
                String user_id = c.getString("user_id");
                if(user_id != null) {
                    String user_name = c.getString("name");
                    String email = c.getString("email");
                    String phone = c.getString("phone");
                    String loginType = Setting.itemUser.getLoginType();
                    String authID = Setting.itemUser.getAuthID();

                    Setting.itemUser = new ItemUser(user_id, user_name, email, phone, authID, loginType);
                } else {
                    success = "0";
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
        successListener.onEnd(s, success, message);
        super.onPostExecute(s);
    }
}