package nemosofts.driving.exam.asyncTask;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import nemosofts.driving.exam.Utils.JSONParser;
import nemosofts.driving.exam.SharedPref.Setting;
import nemosofts.driving.exam.interfaces.LanguageListener;
import nemosofts.driving.exam.item.ItemLanguage;
import okhttp3.RequestBody;

public class LoadLanguage extends AsyncTask<String, String, String> {

    private final RequestBody requestBody;
    private final LanguageListener listener;
    private final ArrayList<ItemLanguage> arrayList = new ArrayList<>();
    private String verifyStatus = "0", message = "";

    public LoadLanguage(LanguageListener listener, RequestBody requestBody) {
        this.listener = listener;
        this.requestBody = requestBody;
    }

    @Override
    protected void onPreExecute() {
        listener.onStart();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            String json = JSONParser.okhttpPost(Setting.SERVER_URL, requestBody);
            JSONObject mainJson = new JSONObject(json);
            JSONArray jsonArray = mainJson.getJSONArray(Setting.TAG_ROOT);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject objJson = jsonArray.getJSONObject(i);

                if (!objJson.has(Setting.TAG_SUCCESS)) {
                    String id = objJson.getString("lid");
                    String name = objJson.getString("language_name");

                    ItemLanguage objItem = new ItemLanguage(id, name);
                    arrayList.add(objItem);
                } else {
                    verifyStatus = objJson.getString("success");
                    message = objJson.getString("msg");
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
        listener.onEnd(s, verifyStatus, message, arrayList);
        super.onPostExecute(s);
    }
}