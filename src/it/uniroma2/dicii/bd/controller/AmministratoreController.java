package it.uniroma2.dicii.bd.controller;

import it.uniroma2.dicii.bd.bean.ProgettoBean;
import it.uniroma2.dicii.bd.bean.UserBean;
import it.uniroma2.dicii.bd.exception.DAOException;
import it.uniroma2.dicii.bd.model.CapoProgetto;
import it.uniroma2.dicii.bd.model.Progetto;
import it.uniroma2.dicii.bd.dao.ConnectionFactory;
import it.uniroma2.dicii.bd.dao.amministratore.AssegnaCapoProgettoDAO;
import it.uniroma2.dicii.bd.dao.amministratore.InserisciLavoratoreDAO;
import it.uniroma2.dicii.bd.dao.amministratore.InserisciProgettoDAO;
import it.uniroma2.dicii.bd.dao.amministratore.PrintListProgettoCapoDAO;
import it.uniroma2.dicii.bd.model.domain.Role;
import it.uniroma2.dicii.bd.utils.Printer;
import it.uniroma2.dicii.bd.view.AmministratoreView;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
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
                case 2 -> assegnaCapoprogetto();
                case 3 -> stampaListaConCapo();
                case 4 -> stampaListaSenzaCapo();   //progetti senza capo
                case 5 -> inserisciLavoratore();
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

            Printer.printlnViola("\nProgetto inserito con successo!");

        } catch(SQLException e){
            Printer.errorMessage("Errore durante l'inserimento del progetto: " + e.getLocalizedMessage());
        }

    }



/*------------ Metodo per assegnare un capo progetto a un progetto esistente */
    public void assegnaCapoprogetto() {

        Object[] risultato;
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
        }else{
            return;
        }

        String cfCapo = capoProgetto.getCf();

        // Devo inserire cf di capoProgetto in colonna CapoProgetto di Progetto
        try {
            AssegnaCapoProgettoDAO assegnaCapoProgettoDAO = new AssegnaCapoProgettoDAO();
            assegnaCapoProgettoDAO.assegnaCapoProgetto(idProgetto,cfCapo);
        } catch (SQLException e){
            Printer.errorMessage("Errore durante l'assegnamento del capo progetto");
        }

        Printer.printlnViola("Il capo progetto: " + capoProgetto.getNome() + " " + capoProgetto.getCognome() +
                " è stato assegnato al progetto di ID: " + idProgetto);


    }



/*------------ Metodo per stampare la lista di progetto senza capo  */
    public List<Progetto> stampaLista(){

        //deve stampare una lista di progetto
        //deve recuperare la lista da DB per poter visualizzare

        AssegnaCapoProgettoDAO assegnaCapoProgettoDAO = new AssegnaCapoProgettoDAO();
        List<Progetto> progettiSenzaCapo = new ArrayList<>();
        try {
            progettiSenzaCapo = assegnaCapoProgettoDAO.listaProgettiSenzaCapo();

        } catch (DAOException e) {
            Printer.errorMessage("Non ci sono progetti");
        }

        return progettiSenzaCapo;

    }




/*------------ Metodo per stampare la lista di capi progetto candidati */
    public List<CapoProgetto> stampaCandidatiCapo(){
        AssegnaCapoProgettoDAO assegnaCapoProgettoDAO = new AssegnaCapoProgettoDAO();
        List<CapoProgetto> listaCandidati = new ArrayList<>();

        try{
            listaCandidati = assegnaCapoProgettoDAO.listaCandidatiCapo();

        } catch (SQLException e) {
            Printer.errorMessage("Non ci sono capi progetti");
        }

        return listaCandidati;
    }



/*------------ Metodo per restituire la lista di progetti con i relativi capi progetto */
    public List<Progetto> listaConCapo(){
        List<Progetto> progettiConCapo = new ArrayList<>();

        try {
            progettiConCapo = PrintListProgettoCapoDAO.listaProgettiConCapo();
        } catch(DAOException e){
            Printer.errorMessage("Non ci sono capi progetto");
        }

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



/*------------ Metodo per inserire lavoratore, chiamato da switch case 5 */
    public void inserisciLavoratore(){

        AmministratoreView amministratoreView = new AmministratoreView();
        UserBean userBean = amministratoreView.nuovoUtente();

        // Il valore di ritorno verrà salvato in DB: tabella lavoratore
        InserisciLavoratoreDAO inserisciLavoratoreDAO = new InserisciLavoratoreDAO();
        inserisciLavoratoreDAO.inserisciLavoratore(userBean);

        Printer.printlnViola("Lavoratore inserito con successo!");

    }


}
