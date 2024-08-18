package it.uniroma2.dicii.bd.model.domain;

public enum Role {
    AMMINISTRATORE(1),
    CAPOPROGETTO(2),
    DIPENDENTE(3);


    private final int id;

    private Role(int id) {
        this.id = id;
    }

    public static Role fromInt(int id) {
        for (Role type : values()) {
            if (type.getId() == id) {
                return type;
            }
        }
        return null;
    }

    public int getId() {
        return id;
    }
}
