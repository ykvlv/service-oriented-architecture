package soa.ticketservice.model;


import lombok.NonNull;
import soa.ticketservice.model.enums.EventType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Сущность Event.
 */
@Getter
@Setter
@Entity
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    @SequenceGenerator(name = "sequence", allocationSize = 1, sequenceName = "event_seq")
    private Long id;

    @Column(name = "name")
    @NonNull
    private String name;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "min_age")
    @NonNull
    private Integer minAge;

    @Column(name = "event_type")
    @NonNull
    private EventType eventType;
}
