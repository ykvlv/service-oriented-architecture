package soa.ticketservice.model.enums;

public enum EventType {
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
