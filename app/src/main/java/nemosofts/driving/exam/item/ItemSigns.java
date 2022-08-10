package nemosofts.driving.exam.item;

public class ItemSigns {

    private final String sid;
    private final String signs_name;
    private final String signs_image;

    public ItemSigns(String sid, String signs_name, String signs_image) {
        this.sid = sid;
        this.signs_name = signs_name;
        this.signs_image = signs_image;
    }

    public String getSid() {
        return sid;
    }


    public String getSignsName() {
        return signs_name;
    }


    public String getSignsImage() {
        return signs_image;
    }

}
