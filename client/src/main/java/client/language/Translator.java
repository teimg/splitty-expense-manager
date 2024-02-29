package client.language;

import java.util.ResourceBundle;

public class Translator {

    private String currentLanguage;
    private ResourceBundle resourceBundle;

    public Translator(String language) {
        this.currentLanguage = language;
        this.resourceBundle = ResourceBundle.getBundle("languages." + currentLanguage);
    }

    public String getCurrentLanguage() {
        return currentLanguage;
    }

    public void setCurrentLanguage(String language) {
        this.currentLanguage = language;
        this.resourceBundle = ResourceBundle.getBundle("languages." + this.currentLanguage);
    }

    public String getTranslation(String keyToBeTranslated) {
        return this.resourceBundle.getString(keyToBeTranslated);
    }

}
