package it.uniroma2.dicii.bd.model;

import java.sql.Date;

public class Progetto {

    private int id;
    private String nome;
    private Date dataInzio;
    private Date dataScadenza;
    private String capoProgetto;   //restituisce CF del capo progetto, in controller devo trasformarlo in nome, cognome

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getDataInzio() {
        return dataInzio;
    }

    public void setDataInzio(Date dataInzio) {
        this.dataInzio = dataInzio;
    }

    public Date getDataScadenza() {
        return dataScadenza;
    }

    public void setDataScadenza(Date dataScadenza) {
        this.dataScadenza = dataScadenza;
    }

    public String getCapoProgetto() {
        return capoProgetto;
    }

    public void setCapoProgetto(String capoProgetto) {
        this.capoProgetto = capoProgetto;
    }
}
