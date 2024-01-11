package soa.ticketservice.model.enums;

public enum TicketType {
    CHEAP("Дешевый", 1),
    BUDGETARY("Бюджетный", 2),

    USUAL("Обычный", 3),
    VIP("Випка", 4);

    private String ruValue;
    private Integer value;

    TicketType(String ruValue, Integer value) {
        this.ruValue = ruValue;
        this.value = value;
    }

    @Override
    public String toString(){
        return ruValue;
    }

    public int getValue() {
        return value;
    }

    public static TicketType getType(int value) {
        for (TicketType type : values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("No TicketType with value: " + value);
    }
}
