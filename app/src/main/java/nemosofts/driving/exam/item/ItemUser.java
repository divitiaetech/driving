package nemosofts.driving.exam.item;

import java.io.Serializable;

/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : thivakaran
 * Telegram : @nemosofts
 * Contact : info.nemosofts@gmail.com
 * Skype : skype.nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class ItemUser implements Serializable {

    private String id;
    private String name;
    private String email;
    private String mobile;
    private final String authID;
    private final String loginType;

    public ItemUser(String id, String name, String email, String mobile, String authID, String loginType) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.authID = authID;
        this.loginType = loginType;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLoginType() {
        return loginType;
    }

    public String getAuthID() {
        return authID;
    }
}