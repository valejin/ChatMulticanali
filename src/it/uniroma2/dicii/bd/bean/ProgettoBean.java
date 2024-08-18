package it.uniroma2.dicii.bd.bean;

public class ProgettoBean {

    private int idProgetto;
    private String nome;
    private String dataInizio;
    private String dataScadenza;
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

    public String getDataInizio() {
        return dataInizio;
    }

    public void setDataInizio(String dataInizio) {
        this.dataInizio = dataInizio;
    }

    public String getDataScadenza() {
        return dataScadenza;
    }

    public void setDataScadenza(String dataScadenza) {
        this.dataScadenza = dataScadenza;
    }

    public String getCFcapo() {
        return CFcapo;
    }

    public void setCFcapo(String CFcapo) {
        this.CFcapo = CFcapo;
    }
}
