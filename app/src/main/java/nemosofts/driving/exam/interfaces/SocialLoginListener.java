package nemosofts.driving.exam.interfaces;

/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : thivakaran
 * Telegram : @nemosofts
 * Contact : info.nemosofts@gmail.com
 * Skype : skype.nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public interface SocialLoginListener {
    void onStart();
    void onEnd(String success, String registerSuccess, String message, String user_id, String user_name, String email, String auth_id);
}