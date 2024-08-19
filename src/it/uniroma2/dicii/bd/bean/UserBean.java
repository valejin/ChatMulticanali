package it.uniroma2.dicii.bd.bean;

import it.uniroma2.dicii.bd.model.domain.Role;

public class UserBean {
    private String CF;
    private Role ruolo;
    private String username;

    public String getCF() {
        return CF;
    }

    public void setCF(String CF) {
        this.CF = CF;
    }

    public Role getRuolo() {
        return ruolo;
    }

    public void setRuolo(Role ruolo) {
        this.ruolo = ruolo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
