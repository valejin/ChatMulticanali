package it.uniroma2.dicii.bd.model;

import it.uniroma2.dicii.bd.model.domain.Role;

public class Lavoratore {
    private String cfLavoratore;
    private String nomeLavoratore;
    private String cognomeLavoratore;
    private int ruolo;  //da convertire

    public String getCfLavoratore() {
        return cfLavoratore;
    }

    public void setCfLavoratore(String cfLavoratore) {
        this.cfLavoratore = cfLavoratore;
    }

    public String getNomeLavoratore() {
        return nomeLavoratore;
    }

    public void setNomeLavoratore(String nomeLavoratore) {
        this.nomeLavoratore = nomeLavoratore;
    }

    public String getCognomeLavoratore() {
        return cognomeLavoratore;
    }

    public void setCognomeLavoratore(String cognomeLavoratore) {
        this.cognomeLavoratore = cognomeLavoratore;
    }

    // Metodo per ottenere il ruolo come oggetto Role
    public Role getRuolo() {
        return Role.fromInt(ruolo);  // Converte l'ID numerico in un oggetto Role
    }

    // Metodo per impostare il ruolo usando un oggetto Role
    public void setRuolo(Role ruolo) {
        if (ruolo != null) {
            this.ruolo = ruolo.getId();  // Converte l'oggetto Role in un ID numerico
        } else {
            throw new IllegalArgumentException("Ruolo non può essere null.");
        }
    }

    // Metodo per impostare il ruolo direttamente con l'ID numerico
    public void setRuolo(int ruoloId) {
        Role ruolo = Role.fromInt(ruoloId);
        if (ruolo != null) {
            this.ruolo = ruoloId;
        } else {
            throw new IllegalArgumentException("ID del ruolo non valido: " + ruoloId);
        }
    }



}
