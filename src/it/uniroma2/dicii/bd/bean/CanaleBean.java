package it.uniroma2.dicii.bd.bean;

public class CanaleBean {

    private int idCanale;
    private int idProgetto;
    private String nome;
    private String creatore;
    private String tipo;  //public o private


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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCreatore() {
        return creatore;
    }

    public void setCreatore(String creatore) {
        this.creatore = creatore;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
