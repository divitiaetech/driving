package nemosofts.driving.exam.item;

public class ItemLanguage {

    private final String lid;
    private final String language_name;

    public ItemLanguage(String lid, String language_name) {
        this.lid = lid;
        this.language_name = language_name;
    }

    public String getLid() {
        return lid;
    }

    public String getLanguageName() {
        return language_name;
    }

}
