package client.utils.communicators.implementations;

import client.utils.ClientConfiguration;
import client.utils.communicators.ClientSupplier;
import client.utils.communicators.interfaces.ITagCommunicator;
import com.google.inject.Inject;
import commons.*;
import jakarta.ws.rs.client.Entity;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class TagCommunicator implements ITagCommunicator {

    private final String origin;

    private final ClientSupplier client;

    @Inject
    public TagCommunicator(ClientConfiguration config, ClientSupplier client) {
        this.origin = config.getServer();
        this.client = client;
    }

    @Override
    public Tag createTag(Tag tag) {
        return client.getClient()
                .target(origin).path("api/tags")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(tag, APPLICATION_JSON), Tag.class);
    }

    @Override
    public Tag getTag(long id) {
        return client.getClient()
                .target(origin).path("api/tags/{id}")
                .resolveTemplate("id", id)
                .request(APPLICATION_JSON).accept(APPLICATION_JSON)
                .get(Tag.class);
    }

    @Override
    public Tag deleteTag(long id) {
        return client.getClient()
                .target(origin).path("api/tags/{id}")
                .resolveTemplate("id", id)
                .request(APPLICATION_JSON).accept(APPLICATION_JSON)
                .delete(Tag.class);
    }

    @Override
    public Tag updateTag(Tag tag) {
        return client.getClient()
                .target(origin).path("api/tags/{id}")
                .resolveTemplate("id", tag.getId())
                .request(APPLICATION_JSON).accept(APPLICATION_JSON)
                .put(Entity.entity(tag, APPLICATION_JSON), Tag.class);
    }

    @Override
    public Tag saveOrUpdateTag(Tag tag) {
        return client.getClient()
                .target(origin).path("api/tags/saveOrUpdate")
                .request(APPLICATION_JSON).accept(APPLICATION_JSON)
                .post(Entity.entity(tag, APPLICATION_JSON), Tag.class);
    }

}
