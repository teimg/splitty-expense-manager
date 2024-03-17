package client.language;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Translator {

    private String currentLanguage;
    private ResourceBundle resourceBundle;

    public Translator() {
    }

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
        if(keyToBeTranslated.startsWith("Popup.")){
            return getTranslationPopup(keyToBeTranslated);
        }
        return this.resourceBundle.getString(keyToBeTranslated);
    }

    private String getTranslationPopup(String keyToBeTranslated) {

        try{
            return this.resourceBundle.getString(keyToBeTranslated);
        }catch (MissingResourceException e){
            String except = "Translation.error";
            if(!keyToBeTranslated.equals(except)){
                return getTranslationPopup(except);
            }

        }

        // Not supposed to be reached.
        return  "Translation error!!";
    }

}
