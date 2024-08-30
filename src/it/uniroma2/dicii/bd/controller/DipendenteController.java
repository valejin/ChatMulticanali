package it.uniroma2.dicii.bd.controller;

import it.uniroma2.dicii.bd.exception.DAOException;
import it.uniroma2.dicii.bd.model.Canale;
import it.uniroma2.dicii.bd.model.Messaggio;
import it.uniroma2.dicii.bd.model.Progetto;
import it.uniroma2.dicii.bd.model.dao.ConnectionFactory;
import it.uniroma2.dicii.bd.model.dao.InserisciMessaggioDAO;
import it.uniroma2.dicii.bd.model.dao.VisualizzaAppartenenzaCanaliDAO;
import it.uniroma2.dicii.bd.model.dao.capoprogetto.CreaCanaleDAO;
import it.uniroma2.dicii.bd.model.dao.capoprogetto.VisualizzaConversazioneDAO;
import it.uniroma2.dicii.bd.model.domain.Role;
import it.uniroma2.dicii.bd.utils.Printer;
import it.uniroma2.dicii.bd.view.DipendenteView;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.List;

public class DipendenteController implements Controller{

    @Override
    public void start() {

        //we connect to the database with the role CapoProgetto
        try {
            ConnectionFactory.changeRole(Role.DIPENDENTE);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        DipendenteView.stampaTitolo();

        while(true) {
            int choice;
            try {
                choice = DipendenteView.stampaMenu();
            } catch(IOException e) {
                throw new RuntimeException(e);
            }

            switch(choice) {
                case 1 -> inserisciNuovoMessaggio();
                case 2 -> visualizzaConversazione();    // da implementare
                case 3 -> visualizzaAppartenenzaProgetti();
                case 4 -> visualizzaAppartenenzaCanali();
                case 0 -> System.exit(0);
                default -> throw new RuntimeException("Invalid choice");
            }
        }

    }




    /* Metodo per inserire un nuovo messaggio in un canale, chiamato da switch case 1 */
    public void inserisciNuovoMessaggio(){

        // Chiama VIEW per ottenere la scelta di utente su id canale e id progetto
        DipendenteView dipendenteView = new DipendenteView();
        Object[] datiMessaggio = dipendenteView.inserisciMessaggio();

        // Estraggo le informazioni dall'array di oggetti
        String contenutoMessaggio = (String) datiMessaggio[0];
        int idCanaleScelto = (int) datiMessaggio[1];
        int idProgettoScelto = (int) datiMessaggio[2];

        // Ora utilizzo queste informazioni per inserire il messaggio nel database
        InserisciMessaggioDAO.inserisciMessaggio(contenutoMessaggio, idCanaleScelto, idProgettoScelto);

        Printer.printlnViola("\nMessaggio inserito con successo!");

    }


    /* Metodo per recuperare lista di progetti con cf di dipendente da DB, chiamato da VIEW */
    public static List<Progetto> recuperoProgetti(){

        CreaCanaleDAO creaCanaleDAO = new CreaCanaleDAO();
        List<Progetto> progettiTarget = null;


        try {
            progettiTarget = InserisciMessaggioDAO.recuperoListProgetti();
        } catch(SQLException e){
            Printer.errorMessage("Errore recuperoProgetti in Controller");
        }
        return progettiTarget;
    }




    /* Metodo per recuperare la lista di canali con id progetto fornito, chiamato da VIEW */
    public List<Canale> recuperoCanali(int idProgetto){

        List<Canale> canaliTarget = null;

        canaliTarget = InserisciMessaggioDAO.recuperaCanaliByIdProgetto(idProgetto);

        return canaliTarget;
    }




    /* Metodo per visualizzare le conversazioni in un canale scelto da utente, chiamato da switch case 2 */
    public void visualizzaConversazione(){

        // Chiamo VIEW per chiedere all'utente quale conversazione desidera di vedere
        DipendenteView dipendenteView = new DipendenteView();

        Object[] infoCanale = dipendenteView.visualizzaCanaliConversazione();
        List<Messaggio> conversazioni = null;

        // Viene restituito ID progetto e ID canale
        int idCanaleScelto = (int) infoCanale[0];
        int idProgettoScelto = (int) infoCanale[1];
        int tipoCanaleScelto = (int) infoCanale[2];


        // Chiama DAO per recuperare i messaggi del canale scelto, poi li restituisco a VIEW per stampare all'utente
        VisualizzaConversazioneDAO visualizzaConversazioneDAO = new VisualizzaConversazioneDAO();


        try {
            conversazioni = visualizzaConversazioneDAO.recuperoConversazione(idCanaleScelto, idProgettoScelto);
        } catch(DAOException e){
            Printer.errorMessage("Errore Visualizzazione Conversazione Controller");
        }


        if(tipoCanaleScelto==1) {
            dipendenteView.stampaConversazione(conversazioni, idCanaleScelto, idProgettoScelto, tipoCanaleScelto);
        }else{
            dipendenteView.stampaConversazionePrivata(conversazioni,idCanaleScelto,idProgettoScelto);
        }

    }





    /* Metodo per salvare la risposta di utente (in modo pubblico) in DB, chiamato da VIEW */
    public void rispostaPublic(Messaggio messaggioOriginale, String contenutoRisposta){

        try {
            // Nel messaggio originario contiene già idCanale e idProgetto
            // Il messaggio in DB viene identificato da CFMittente, DataInvio e OrarioInvio
            VisualizzaConversazioneDAO.inserisciRisposta(messaggioOriginale, contenutoRisposta, 0);
            Printer.printlnViola("Risposta pubblica inviata con successo.");


        } catch (DAOException e) {
            Printer.errorMessage("Errore durante l'invio della risposta pubblica.");
        }

    }




    /* NUOVO METODO FUNZIONANTE: Metodo per memorizzare la risposta privata in DB */
    public void memorizzaRispostaPrivata(Messaggio messaggioOriginale, String contenutoRisposta){

        try {
            VisualizzaConversazioneDAO visualizzaConversazioneDAO = new VisualizzaConversazioneDAO();
            visualizzaConversazioneDAO.inserisciRispostaPrivata(messaggioOriginale, contenutoRisposta);
        } catch(DAOException e){
            Printer.errorMessage("Errore inserimento risposta privata in DB con nuovo metodo");
            e.printStackTrace();
        }

    }




    /* Metodo per recuperare il messaggio originario di una risposta privata, chiamato da VIEW */
    public Messaggio recuperoMessaggioOriginario(String cfRispondente, Date dataInvioRispondente, Time orarioInvioRispondente){

        Messaggio messaggioOriginale = new Messaggio();  //per contenere il messaggio originale

        VisualizzaConversazioneDAO visualizzaConversazioneDAO = new VisualizzaConversazioneDAO();
        try{
            // qui viene passato: cfRispondente, dataInvioRispondente, orarioInvioRispondente
            messaggioOriginale = visualizzaConversazioneDAO.recuperoMessaggioOriginale(cfRispondente, dataInvioRispondente, orarioInvioRispondente);

        } catch(DAOException e){
            e.printStackTrace();
        }

        return messaggioOriginale;

    }














    /* Metodo per restituire a utente tutti i progetti a cui esso fa parte, chiamato da switch case 3 */
    public void visualizzaAppartenenzaProgetti(){

        DipendenteView dipendenteView = new DipendenteView();
        dipendenteView.listaProgetti();
    }




    /* Metodo per restituire a utente tutti i canali a cui esso fa parte, chiamato da switch case 4 */
    public void visualizzaAppartenenzaCanali(){

        DipendenteView dipendenteView = new DipendenteView();
        dipendenteView.listaCanali();

    }


    /* Metodo per recuperare la lista di canali appartenenti da DB, chiamato da VIEW */
    public List<Canale> recuperoCanaliAppartenenti(){

        List<Canale> canaliTarget = null;
        canaliTarget = VisualizzaAppartenenzaCanaliDAO.recuperaCanali();

        return canaliTarget;
    }



}
