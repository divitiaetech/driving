package nemosofts.driving.exam.ifSupported;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.view.View;

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

public class IsRTL {
    @SuppressLint("ObsoleteSdkInt")
    public static void ifSupported(Activity mContext) {
        if (Setting.isRTL) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                mContext.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
        }
    }
}
