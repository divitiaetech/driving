package nemosofts.driving.exam.item;

public class ItemCat {

    private final String cid;
    private final String category_name;
    private final String category_image;

    public ItemCat(String cid, String category_name, String category_image) {
        this.cid = cid;
        this.category_name = category_name;
        this.category_image = category_image;
    }

    public String getCid() {
        return cid;
    }

    public String getCategory_name() {
        return category_name;
    }

    public String getCategory_image() {
        return category_image;
    }
}
