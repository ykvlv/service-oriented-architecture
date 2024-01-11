package soa.ticketservice.model;


import lombok.NonNull;
import soa.ticketservice.model.enums.TicketType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Сущность Ticket.
 */
@Getter
@Setter
@Entity
@Table(name = "ticket")
public class Ticket {

    /**
     * Идентификатор.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    @SequenceGenerator(name = "sequence", allocationSize = 1, sequenceName = "ticket_seq")
    private Long id;

    /**
     * Название.
     */
    @Column(name = "name")
    @Lob
    @NonNull
    private String name;

    /**
     * Координата X.
     */
    @Column(name = "coordinate_x")
    @NonNull
    private Long coordinateX;

    /**
     * Координата Y.
     */
    @Column(name = "coordinate_y")
    @NonNull
    private Integer coordinateY;

    /**
     * Дата создания.
     */
    @Column(name = "creation_date")
    private LocalDate creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически

    /**
     * Цена.
     */
    @Column(name = "price")
    @NonNull
    private Double price; //Значение поля должно быть больше 0

    /**
     * Скидка.
     */
    @Column(name = "discount")
    @NonNull
    private Double discount; //Поле не может быть null, Значение поля должно быть больше 0, Максимальное значение поля: 100

    /**
     * Тип билета.
     */
    @Column(name = "type")
    private TicketType type; //Поле может быть null

    /**
     * На кокое событие билет.
     */
    @ManyToOne
    @JoinColumn(name="event_id")
    private Event event;  //Поле не может быть null

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinateX=" + coordinateX +
                ", coordinateY=" + coordinateY +
                ", creationDate=" + creationDate +
                ", price=" + price +
                ", discount=" + discount +
                ", type=" + type +
                ", event=" + event +
                '}';
    }
}
