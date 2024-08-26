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
    private boolean isRisposta;  // Indica se questo messaggio è una risposta
    private Messaggio messaggioOriginale;  // Il messaggio originale a cui questo risponde
    private Integer isVisible;  // null di default, 0 per risposta pubblica, 1 per risposta privata

    public Integer getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(Integer isVisible) {
        this.isVisible = isVisible;
    }

    public boolean isRisposta() {
        return isRisposta;
    }

    public void setRisposta(boolean risposta) {
        isRisposta = risposta;
    }

    public Messaggio getMessaggioOriginale() {
        return messaggioOriginale;
    }

    public void setMessaggioOriginale(Messaggio messaggioOriginale) {
        this.messaggioOriginale = messaggioOriginale;
    }

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
