package it.uniroma2.dicii.bd.controller;

import it.uniroma2.dicii.bd.bean.CanaleBean;
import it.uniroma2.dicii.bd.exception.DAOException;
import it.uniroma2.dicii.bd.model.Canale;
import it.uniroma2.dicii.bd.model.Lavoratore;
import it.uniroma2.dicii.bd.model.Messaggio;
import it.uniroma2.dicii.bd.model.Progetto;
import it.uniroma2.dicii.bd.model.dao.ConnectionFactory;
import it.uniroma2.dicii.bd.model.dao.capoprogetto.AssegnaCanaleDAO;
import it.uniroma2.dicii.bd.model.dao.capoprogetto.CreaCanaleDAO;
import it.uniroma2.dicii.bd.model.dao.capoprogetto.InserisciMessaggioDAO;
import it.uniroma2.dicii.bd.model.dao.capoprogetto.VisualizzaConversazioneDAO;
import it.uniroma2.dicii.bd.model.domain.Role;
import it.uniroma2.dicii.bd.utils.Printer;
import it.uniroma2.dicii.bd.view.CapoprogettoView;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CapoprogettoController implements Controller{

    @Override
    public void start() {
        //we connect to the database with the role CapoProgetto
        try {
            ConnectionFactory.changeRole(Role.CAPOPROGETTO);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        CapoprogettoView.stampaTitolo();

        while(true) {
            int choice;
            try {
                choice = CapoprogettoView.stampaMenu();
            } catch(IOException e) {
                throw new RuntimeException(e);
            }

            switch(choice) {
                case 1 -> creaCanale();
                case 2 -> inserisciNuovoMessaggio();
                case 3 -> visualizzaConversazione();
                case 4 -> assegnaCanale();
                case 0 -> System.exit(0);
                default -> throw new RuntimeException("Invalid choice");
            }
        }

    }


    /* Metodo per creare il nuovo canale, chiamato da switch case 1 */
    public void creaCanale(){

        // Uso CanaleBean per passare gli input del utente
        CanaleBean canaleBean = CapoprogettoView.creaNuovoCanale();

        // Adesso devo inserire questo canaleBean in DB
        CreaCanaleDAO creaCanaleDAO = new CreaCanaleDAO();


        try{
            creaCanaleDAO.inserireNuovoCanale(canaleBean);
            Printer.println("Canale creato con successo!");
        } catch(SQLException e){
            Printer.errorMessage("Errore all'inserimento di nuovo canale da Capo progetto");
            e.printStackTrace();  // Per debug, stampa lo stack trace
            throw new RuntimeException("Errore durante la creazione del canale: " + e.getMessage(), e);
        }


    }




    /* Metodo per recuperare lista di progetti con cf di capoprogetto dato da DB, chiamato da VIEW */
    public static List<Progetto> recuperoProgetti(){

        CreaCanaleDAO creaCanaleDAO = new CreaCanaleDAO();
        List<Progetto> progettiTarget = null;


        try {
            progettiTarget = creaCanaleDAO.recuperoProgettiByCf();

        } catch(SQLException e){
            Printer.errorMessage("Errore recuperoProgetti in Controller");
        }
        return progettiTarget;
    }




    /* Metodo per inserire un nuovo messaggio, chiamato da switch case 2 */
    public void inserisciNuovoMessaggio(){

        // Chiamo View per stampare modulo, e prendere le informazioni
        // Poi inserisco le informazioni in DB tramite DAO

        Object[] datiMessaggio = CapoprogettoView.inserisciMessaggio();

        // Estraggo le informazioni dall'array di oggetti
        String contenutoMessaggio = (String) datiMessaggio[0];
        int idCanaleScelto = (int) datiMessaggio[1];
        int idProgettoScelto = (int) datiMessaggio[2];

        // Ora utilizzo queste informazioni per inserire il messaggio nel database
        InserisciMessaggioDAO.inserisciMessaggio(contenutoMessaggio, idCanaleScelto, idProgettoScelto);

        Printer.println("\nMessaggio inserito con successo!");
    }



    /* Metodo per recuperare la lista di canali con id progetto fornito, chiamato da VIEW */
    public List<Canale> recuperoCanali(int idProgetto){

        List<Canale> canaliTarget = null;

        canaliTarget = InserisciMessaggioDAO.recuperaCanaliByIdProgetto(idProgetto);

        return canaliTarget;
    }




    /* Metodo per visualizzare le conversazioni in un canale scelto da utente, chiamato da switch case 3 */
    public void visualizzaConversazione(){

        // Chiamo VIEW per chiedere all'utente quale conversazione desidera di vedere
        CapoprogettoView capoprogettoView = new CapoprogettoView();
        Object[] infoCanale = capoprogettoView.visualizzaCanaliAppartenenti();
        List<Messaggio> conversazioni = null;

        // Viene restituito ID progetto e ID canale
        int idCanaleScelto = (int) infoCanale[0];
        int idProgettoScelto = (int) infoCanale[1];
        int tipoCanaleScelto = (int) infoCanale[2];


        // Chiamo DAO per recuperare i messaggi del canale scelto, poi li restituisco a VIEW per stampare all'utente
        VisualizzaConversazioneDAO visualizzaConversazioneDAO = new VisualizzaConversazioneDAO();
        try {
            conversazioni = visualizzaConversazioneDAO.recuperoConversazione(idCanaleScelto, idProgettoScelto);
        } catch(DAOException e){
            Printer.errorMessage("Errore Visualizzazione Conversazione Controller");
        }
        // RICORDA: i messaggi vengono stampati come le pagine

        capoprogettoView.stampaConversazione(conversazioni, idCanaleScelto, idProgettoScelto, tipoCanaleScelto);

    }


    /* Metodo per salvare la risposta di utente (in modo pubblico) in DB, chiamato da VIEW */
    public void rispostaPublic(Messaggio messaggioOriginale, String contenutoRisposta){

        try {
            // Nel messaggio originario contiene già idCanale e idProgetto
            // Il messaggio in DB viene identificato da CFMittente, DataInvio e OrarioInvio
            VisualizzaConversazioneDAO.inserisciRisposta(messaggioOriginale, contenutoRisposta, 0);
            Printer.println("Risposta pubblica inviata con successo.");


        } catch (DAOException e) {
            Printer.errorMessage("Errore durante l'invio della risposta pubblica.");
        }

    }



    /* Metodo per creare un canale di comunicazione privata, chiamata da VIEW */
    public void creaCanalePrivata(CanaleBean canalePrivata){

        CreaCanaleDAO creaCanaleDAO = new CreaCanaleDAO();

        try {
            creaCanaleDAO.inserireNuovoCanale(canalePrivata);
        } catch (SQLException e){
            e.printStackTrace();
        }

    }



    /* NUOVO METODO, DA PROVARE: Metodo per memorizzare la risposta privata in DB */
    public void memorizzaRispostaPrivata(Messaggio messaggioOriginale, String contenutoRisposta){

        try {
            VisualizzaConversazioneDAO visualizzaConversazioneDAO = new VisualizzaConversazioneDAO();
            visualizzaConversazioneDAO.inserisciRispostaPrivata(messaggioOriginale, contenutoRisposta);
        } catch(DAOException e){
            Printer.errorMessage("Errore inserimento risposta privata in DB con nuovo metodo");
            e.printStackTrace();
        }

    }








    /* Metodo per salvare la risposta privata di utente in DB, chiamato da VIEW */
    public void rispostaPrivate(Messaggio messaggioOriginale, String contenutoRisposta){

        try {
            // Nel messaggio originario contiene già idCanale e idProgetto
            // Il messaggio in DB viene identificato da CFrispondente, DataInvio e OrarioInvio
            VisualizzaConversazioneDAO.inserisciRisposta(messaggioOriginale, contenutoRisposta, 1);
            Printer.println("Risposta privata inviata con successo.");


        } catch (DAOException e) {
            Printer.errorMessage("Errore durante l'invio della risposta privata.");
        }

    }




    /* Metodo per assegnare il canale a un lavoratore che appartenente a un progetto, chiamato da switch case 4 */
    public void assegnaCanale(){

        // Chiama VIEW per stampare a schermo i progetti e i canali, restituisce da VIEW il lavoratore scelto
        CapoprogettoView capoprogettoView = new CapoprogettoView();
        Object[] infoLavoratore = capoprogettoView.assegnaCanale();

        // Viene restituito ID progetto e ID canale
        Lavoratore lavoratore = (Lavoratore) infoLavoratore[0];
        int idCanaleScelto = (int) infoLavoratore[1];
        int idProgettoScelto = (int) infoLavoratore[2];


        // Chiama DAO per memorizzare tale assegnazione
        AssegnaCanaleDAO assegnaCanaleDAO = new AssegnaCanaleDAO();
        assegnaCanaleDAO.salvaAssegnazioneCanale(lavoratore, idCanaleScelto, idProgettoScelto);


        Printer.printlnViola("Assegnazione avvenuta con successo!");


    }



    /* Metodo per recuperare la lista di lavoratori appartenente al progetto scelto da capo, chiamato da VIEW*/
    public List<Lavoratore> recuperoLavoratori(int idProgetto){

        AssegnaCanaleDAO assegnaCanaleDAO = new AssegnaCanaleDAO();
        List<Lavoratore> lavoratori = new ArrayList<>();

        lavoratori = assegnaCanaleDAO.recuperoLavoratoriByIdProgetto(idProgetto);
        // il metodo restituisce una lista di lavoratori

        return lavoratori;

    }


}
