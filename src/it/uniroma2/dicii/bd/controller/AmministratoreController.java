package it.uniroma2.dicii.bd.controller;

import it.uniroma2.dicii.bd.bean.ProgettoBean;
import it.uniroma2.dicii.bd.model.CapoProgetto;
import it.uniroma2.dicii.bd.model.Progetto;
import it.uniroma2.dicii.bd.model.dao.ConnectionFactory;
import it.uniroma2.dicii.bd.model.dao.amministratore.AssegnaCapoProgettoDAO;
import it.uniroma2.dicii.bd.model.dao.amministratore.InserisciProgettoDAO;
import it.uniroma2.dicii.bd.model.dao.amministratore.PrintListProgettoCapoDAO;
import it.uniroma2.dicii.bd.model.domain.Role;
import it.uniroma2.dicii.bd.utils.Printer;
import it.uniroma2.dicii.bd.view.AmministratoreView;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AmministratoreController implements Controller {

    @Override
    public void start() {
        //we connect to the database with the role AMMINISTRATORE
        try {
            ConnectionFactory.changeRole(Role.AMMINISTRATORE);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        AmministratoreView.stampaTitolo();

        while(true) {
            int choice;
            try {
                choice = AmministratoreView.stampaMenu();
            } catch(IOException e) {
                throw new RuntimeException(e);
            }

            switch(choice) {
                case 1 -> inserisciProgetto();
                case 2 -> {
                    try {
                        assegnaCapoprogetto();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                case 3 -> stampaListaConCapo();
                case 4 -> stampaListaSenzaCapo();        //progetti senza capo
                case 0 -> System.exit(0);
                default -> throw new RuntimeException("Invalid choice");
            }
        }


    }


/*------------ Metodo per inserire un nuovo progetto */
    public void inserisciProgetto(){
        //quando inserisco uso bean per passare i dati

        ProgettoBean progettoNew;

        try{
            progettoNew = AmministratoreView.inserisciProgetto();
            new InserisciProgettoDAO().execute(progettoNew);

            Printer.println("\nProgetto inserito con successo!");

        } catch(SQLException e){
            Printer.errorMessage("Errore durante l'inserimento del progetto: " + e.getLocalizedMessage());
        }

    }



/*------------ Metodo per assegnare un capo progetto a un progetto esistente */
    public void assegnaCapoprogetto() throws SQLException {

        Object[] risultato = null;
        try {
            risultato = AmministratoreView.assegnaCapoProgetto();
        } catch (SQLException e) {
            // Gestisci l'eccezione generata dal metodo assegnaCapoProgetto
            Printer.errorMessage("Errore durante l'assegnazione del capo progetto: " + e.getMessage());
            return;  // Esci dal metodo se c'è stato un errore
        }

        int idProgetto = -1;
        CapoProgetto capoProgetto = null;

        if (risultato != null) {
            idProgetto = (int) risultato[0];
            capoProgetto = (CapoProgetto) risultato[1];
        }

        String cfCapo = capoProgetto.getCf();

        // Devo inserire cf di capoProgetto in colonna CapoProgetto di Progetto
        try {
            AssegnaCapoProgettoDAO assegnaCapoProgettoDAO = new AssegnaCapoProgettoDAO();
            assegnaCapoProgettoDAO.assegnaCapoProgetto(idProgetto,cfCapo);
        } catch (SQLException e){
            Printer.errorMessage("Errore durante l'assegnamento del capo progetto");
        }

        Printer.println("\nIl capo progetto: " + capoProgetto.getNome() + " " + capoProgetto.getCognome() +
                " è stato assegnato al progetto di ID: " + idProgetto + ".");


    }



/*------------ Metodo per stampare la lista di progetto senza capo  */
    public List<Progetto> stampaLista(){

        //deve stampare una lista di progetto
        //deve recuperare la lista da DB per poter visualizzare

        AssegnaCapoProgettoDAO assegnaCapoProgettoDAO = new AssegnaCapoProgettoDAO();
        List<Progetto> progettiSenzaCapo;
        try {
            progettiSenzaCapo = assegnaCapoProgettoDAO.listaProgettiSenzaCapo();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return progettiSenzaCapo;

    }




/*------------ Metodo per stampare la lista di candidati per poter essere capi progetti */
    public List<CapoProgetto> stampaCandidatiCapo(){
        AssegnaCapoProgettoDAO assegnaCapoProgettoDAO = new AssegnaCapoProgettoDAO();
        List<CapoProgetto> listaCandidati;

        try{
            listaCandidati = assegnaCapoProgettoDAO.listaCandidatiCapo();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return listaCandidati;
    }



/*------------ Metodo per restituire la lista di progetti con i relativi capi progetto */
    public List<Progetto> ListaConCapo(){
        List<Progetto> progettiConCapo;

        progettiConCapo = PrintListProgettoCapoDAO.listaProgettiConCapo();

        return progettiConCapo;
    }


/*------------ Metodo per stampare la lista di progetti con i relativi capi progetto */
    public void stampaListaConCapo(){
        AmministratoreView amministratoreView = new AmministratoreView();
        amministratoreView.listaConCapo();

    }



/*------------ Metodo per stampare la lista di progetti senza capo progetto */
    public void stampaListaSenzaCapo(){
        AmministratoreView amministratoreView = new AmministratoreView();
        amministratoreView.listaSenzaCapo();
    }







}
