package it.uniroma2.dicii.bd.model;

import it.uniroma2.dicii.bd.bean.CanaleBean;

import java.sql.Date;

public class Canale {

    private int idCanale;
    private int idProgetto;
    private String nome;
    private Date data;
    private String cfCreatore;
    private Canale.Tipo tipo;


    public enum Tipo {
        PUBLIC(1),
        PRIVATE(2);

        private final int id;

        Tipo(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static Canale.Tipo fromId(int id) {
            for (Canale.Tipo tipo : Canale.Tipo.values()) {
                if (tipo.getId() == id) {
                    return tipo;
                }
            }
            throw new IllegalArgumentException("ID non valido: " + id);
        }
    }


    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCfCreatore() {
        return cfCreatore;
    }

    public void setCfCreatore(String cfCreatore) {
        this.cfCreatore = cfCreatore;
    }


    // Gestione del campo `tipo` come enum
    public Canale.Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Canale.Tipo tipo) {
        this.tipo = tipo;
    }

    public void setTipoById(int id) {
        this.tipo = Canale.Tipo.fromId(id);
    }

    public int getTipoId() {
        return tipo.getId();
    }


}
