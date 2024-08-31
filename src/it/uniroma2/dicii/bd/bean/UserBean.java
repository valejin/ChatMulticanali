package it.uniroma2.dicii.bd.bean;

import it.uniroma2.dicii.bd.model.domain.Role;

public class UserBean {
    private String CF;
    private Role ruolo;
    private String username;
    private String nome;
    private String cognome;
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

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

    // Nuovo metodo per ottenere l'ID del ruolo
    public int getRuoloId() {
        if (ruolo != null) {
            return ruolo.getId();
        } else {
            throw new IllegalStateException("Il ruolo non è stato assegnato.");
        }
    }
}
