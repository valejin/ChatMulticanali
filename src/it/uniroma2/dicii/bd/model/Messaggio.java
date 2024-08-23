package it.uniroma2.dicii.bd.model;

import java.sql.Date;
import java.sql.Time;

public class Messaggio {

    private String cfUtente;
    private String contenuto;
    private Date dataInvio;
    private Time orarioInvio;
    private int idCanale;
    private int idProgetto;
    private String nomeUtente;
    private String cognomeUtente;

    public String getCfUtente() {
        return cfUtente;
    }

    public void setCfUtente(String cfUtente) {
        this.cfUtente = cfUtente;
    }

    public String getContenuto() {
        return contenuto;
    }

    public void setContenuto(String contenuto) {
        this.contenuto = contenuto;
    }

    public Date getDataInvio() {
        return dataInvio;
    }

    public void setDataInvio(Date dataInvio) {
        this.dataInvio = dataInvio;
    }

    public Time getOrarioInvio() {
        return orarioInvio;
    }

    public void setOrarioInvio(Time orarioInvio) {
        this.orarioInvio = orarioInvio;
    }

    public int getIdCanale() {
        return idCanale;
    }

    public void setIdCanale(int idCanale) {
        this.idCanale = idCanale;
    }

    public int getIdProgetto() {
        return idProgetto;
    }

    public void setIdProgetto(int idProgetto) {
        this.idProgetto = idProgetto;
    }

    public String getNomeUtente() {
        return nomeUtente;
    }

    public void setNomeUtente(String nomeUtente) {
        this.nomeUtente = nomeUtente;
    }

    public String getCognomeUtente() {
        return cognomeUtente;
    }

    public void setCognomeUtente(String cognomeUtente) {
        this.cognomeUtente = cognomeUtente;
    }
}
