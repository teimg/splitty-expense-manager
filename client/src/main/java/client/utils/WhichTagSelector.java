package client.utils;

import commons.Tag;

import java.util.ArrayList;
import java.util.List;

public class WhichTagSelector {

    private List<Tag> tags;
    private List<String> optionalTags;

    public WhichTagSelector(List<Tag> tag) {
        this.tags = tag;
        optionalTags = new ArrayList<>();
    }

    public List<String> query(String query){
        if(query == null || query.isEmpty()){
            return tags.stream().map(Tag::getName).toList();
        }

        optionalTags = new ArrayList<>();
        query = query.toLowerCase();

        for (Tag tag: tags){
            String name = tag.getName().toLowerCase();

            if(name.startsWith(query)){
                optionalTags.add(name.substring(0, 1).toUpperCase() + name.substring(1));
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

}
