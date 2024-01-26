package soa.bookingservice.model;

import java.io.Serializable;

public enum EventType implements Serializable {
    CONCERT("Концерт"),
    STANDUP("Стендап"),
    OPERA("Опера"),
    THEATRE_PERFORMANCE("Театр");

    private String ruValue;

    EventType (String ruValue) {
        this.ruValue = ruValue;
    }

    @Override
    public String toString(){
        return ruValue;
    }
}
