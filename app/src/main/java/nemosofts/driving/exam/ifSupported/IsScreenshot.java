package nemosofts.driving.exam.ifSupported;

import android.app.Activity;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import nemosofts.driving.exam.SharedPref.Setting;


/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : thivakaran
 * Telegram : @nemosofts
 * Contact : info.nemosofts@gmail.com
 * Skype : skype.nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class IsScreenshot {
    public static void ifSupported(Activity mContext) {
        if (Setting.isScreenshot) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = mContext.getWindow();
                window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
            }
        }
    }
}
