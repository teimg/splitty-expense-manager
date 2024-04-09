package client.utils;

import commons.Event;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class RecentEventTracker {

    private static final Path location = Paths.get(
            "client", "src", "main", "resources", "client", "config", "recent.ser");

    private ObservableList<JoinableEvent> joinableEvents;

    /**
     * Should be created in singleton scope with DI.
     */
    @SuppressWarnings("unchecked")
    public RecentEventTracker() {
        if (Files.isRegularFile(location)) {
            try (FileInputStream in = new FileInputStream(location.toString())) {
                ObjectInputStream oin = new ObjectInputStream(in);
                ArrayList<JoinableEvent> events = (ArrayList<JoinableEvent>) oin.readObject();
                joinableEvents = FXCollections.observableList(events);
                return;
            } catch (Exception e) {
                System.err.println("Cannot deserialize recent events");
            }
        }
        joinableEvents = FXCollections.observableArrayList();
    }

    public void registerEvent(Event event) {
        joinableEvents.stream()
                .filter(joinableEvent -> joinableEvent.inviteCode() != null
                        && joinableEvent.inviteCode().equals(event.getInviteCode()))
                .findFirst()
                .ifPresent(joinableEvent -> joinableEvents.remove(joinableEvent));
        joinableEvents.addFirst(JoinableEvent.fromEvent(event));
    }

    public void deleteEvent(JoinableEvent event) {
        joinableEvents.remove(event);
    }

    public ObservableList<JoinableEvent> getEvents() {
        return joinableEvents;
    }

    public void persistEvents() {
        try (FileOutputStream out = new FileOutputStream(location.toString())) {
            ObjectOutputStream oout = new ObjectOutputStream(out);
            oout.writeObject(new ArrayList<>(joinableEvents));
            oout.flush();
        } catch (IOException e) {
            System.err.println("Cannot serialize recent events");
        }
    }
}
