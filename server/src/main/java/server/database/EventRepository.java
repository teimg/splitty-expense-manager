package server.database;

import commons.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findByInviteCode(String inviteCode);
    boolean existsByInviteCode(String inviteCode);
}
