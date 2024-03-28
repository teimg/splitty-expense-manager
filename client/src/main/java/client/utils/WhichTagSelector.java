package client.utils;

import commons.Tag;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.List;

public class WhichTagSelector extends StringConverter<Tag> {

    private List<Tag> tags;
    private List<Tag> optionalTags;

    public WhichTagSelector(List<Tag> tag) {
        this.tags = tag;
        optionalTags = new ArrayList<>();
    }

    public List<Tag> query(String query){
        if(query == null || query.isEmpty()){
            return new ArrayList<>(tags);
        }

        optionalTags = new ArrayList<>();
        query = query.toLowerCase();

        for (Tag tag: tags){
            String name = tag.getName().toLowerCase();

            if(name.startsWith(query)){
                optionalTags.add(tag);
            }
        }

        return optionalTags;
    }

    public Tag getCurrentTag(String name) {
        if(name == null){
            return  null;
        }

        for (Tag tag : tags){
            if (tag.getName().equals(name)){
                return tag;
            }
        }

        return  null;
    }

    @Override
    public String toString(Tag object) {
        if(object == null) return null;
        return object.getName();
    }

    @Override
    public Tag fromString(String string) {
        if(string == null || string.isEmpty()) return null;
        return getCurrentTag(string);
    }
}
