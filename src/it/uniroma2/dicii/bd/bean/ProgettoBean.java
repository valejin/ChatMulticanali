package it.uniroma2.dicii.bd.bean;

import java.sql.Date;

public class ProgettoBean {

    private int idProgetto;
    private String nome;
    private java.sql.Date dataInizio;
    private java.sql.Date dataScadenza;
    private String CFcapo;

    public int getIdProgetto() {
        return idProgetto;
    }

    public void setIdProgetto(int idProgetto) {
        this.idProgetto = idProgetto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public java.sql.Date getDataInizio() {
        return dataInizio;
    }

    public void setDataInizio(java.sql.Date dataInizio) {
        this.dataInizio = dataInizio;
    }

    public java.sql.Date getDataScadenza() {
        return dataScadenza;
    }

    public void setDataScadenza(Date dataScadenza) {
        this.dataScadenza = dataScadenza;
    }

    public String getCFcapo() {
        return CFcapo;
    }

    public void setCFcapo(String CFcapo) {
        this.CFcapo = CFcapo;
    }
}
