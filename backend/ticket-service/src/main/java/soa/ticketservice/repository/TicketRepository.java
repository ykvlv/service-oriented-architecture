package soa.ticketservice.repository;

import soa.ticketservice.model.Ticket;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TicketRepository extends CrudRepository<Ticket, Long>, JpaSpecificationExecutor<Ticket> {
    @Query(value = "select * from ticket where event_id = :id", nativeQuery = true)
    List<Ticket> allTicketsByEventId(
            @Param("id") Long id
    );

    @Modifying
    @Query(value = "delete from ticket t where t.event_id=:id", nativeQuery = true)
    void deleteAllByEventId(
            @Param("id") Long eventId
    );

    @Query(value = "SELECT COUNT(*) as count FROM ticket WHERE type IS NOT NULL GROUP BY type", nativeQuery = true)
    List<Integer> findDistinctTypesWithCount();

    List<Ticket> findAll();
}
