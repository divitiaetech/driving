package nemosofts.driving.exam.asyncTask;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import nemosofts.driving.exam.Utils.JSONParser;
import nemosofts.driving.exam.SharedPref.Setting;
import nemosofts.driving.exam.interfaces.SignsListener;
import nemosofts.driving.exam.item.ItemSigns;
import okhttp3.RequestBody;

public class LoadSigns extends AsyncTask<String, String, String> {

    private final RequestBody requestBody;
    private final SignsListener categoryListener;
    private final ArrayList<ItemSigns> arrayList = new ArrayList<>();
    private String verifyStatus = "0", message = "";

    public LoadSigns(SignsListener categoryListener, RequestBody requestBody) {
        this.categoryListener = categoryListener;
        this.requestBody = requestBody;
    }

    @Override
    protected void onPreExecute() {
        categoryListener.onStart();
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
                    String id = objJson.getString("sid");
                    String name = objJson.getString("signs_name");
                    String image = objJson.getString("signs_image");

                    ItemSigns objItem = new ItemSigns(id, name, image);
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
        categoryListener.onEnd(s, verifyStatus, message, arrayList);
        super.onPostExecute(s);
    }
}