package client.utils.communicators.interfaces;


import commons.Tag;

public interface ITagCommunicator {

    Tag createTag(Tag tag);

    Tag getTag(long id);

    Tag deleteTag(long id);

    Tag updateTag(Tag tag);

    Tag saveOrUpdateTag(Tag tag);

}
