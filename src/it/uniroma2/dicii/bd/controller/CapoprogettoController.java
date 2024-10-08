package it.uniroma2.dicii.bd.controller;

import it.uniroma2.dicii.bd.bean.CanaleBean;
import it.uniroma2.dicii.bd.dao.*;
import it.uniroma2.dicii.bd.dao.capoprogetto.AssegnaProgettoDAO;
import it.uniroma2.dicii.bd.exception.DAOException;
import it.uniroma2.dicii.bd.model.Canale;
import it.uniroma2.dicii.bd.model.Lavoratore;
import it.uniroma2.dicii.bd.model.Messaggio;
import it.uniroma2.dicii.bd.model.Progetto;
import it.uniroma2.dicii.bd.dao.capoprogetto.AssegnaCanaleDAO;
import it.uniroma2.dicii.bd.dao.capoprogetto.CreaCanaleDAO;
import it.uniroma2.dicii.bd.model.domain.Role;
import it.uniroma2.dicii.bd.utils.Printer;
import it.uniroma2.dicii.bd.view.CapoprogettoView;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
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
                case 4 -> visualizzaConversazionePrivata();
                case 5 -> assegnaCanale();
                case 6 -> assegnaProgetto();
                case 7 -> visualizzaAppartenenzaProgetti();
                case 8 -> visualizzaAppartenenzaCanali();
                case 9 -> visualizzaPartecipantiProgetto();
                case 10 -> visualizzaPartecipantiCanali();
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
            Printer.printlnViola("Canale creato con successo!");
        } catch(SQLException e){
            Printer.errorMessage("Errore all'inserimento di nuovo canale da Capo progetto");
            e.printStackTrace();  // Per debug, stampa lo stack trace
            throw new RuntimeException("Errore durante la creazione del canale: " + e.getMessage(), e);
        }
    }




    /* Metodo per recuperare lista di progetti da DB con cf di capoProgetto dato, chiamato da VIEW */
    public static List<Progetto> recuperoProgetti(){

        CreaCanaleDAO creaCanaleDAO = new CreaCanaleDAO();
        List<Progetto> progettiTarget;

        try {
            progettiTarget = creaCanaleDAO.recuperoProgettiByCf();

        } catch(DAOException e){
            Printer.errorMessage("Errore recuperoProgetti in Controller");
            return null;
        }
        return progettiTarget;
    }




    /* Metodo per inserire un nuovo messaggio, chiamato da switch case 2 */
    public void inserisciNuovoMessaggio(){

        // Chiamo View per stampare modulo, e prendere le informazioni
        // Poi inserisco le informazioni in DB tramite DAO

        Object[] datiMessaggio = CapoprogettoView.inserisciMessaggio();

        // Estraggo le informazioni da array di oggetti
        String contenutoMessaggio = (String) datiMessaggio[0];
        int idCanaleScelto = (int) datiMessaggio[1];
        int idProgettoScelto = (int) datiMessaggio[2];

        // Ora utilizzo queste informazioni per inserire il messaggio nel database
        InserisciMessaggioDAO.inserisciMessaggio(contenutoMessaggio, idCanaleScelto, idProgettoScelto);

        Printer.printlnViola("\nMessaggio inserito con successo!");
    }



    /* Metodo per recuperare la lista di canali con id progetto fornito, chiamato da VIEW */
    public List<Canale> recuperoCanali(int idProgetto){

        List<Canale> canaliTarget;

        canaliTarget = InserisciMessaggioDAO.recuperaCanaliByIdProgetto(idProgetto);

        return canaliTarget;
    }



    /* Metodo per recuperare la lista dei canali pubblici a cui fa parte, chiamato da VIEW */
    public List<Canale> recuperoCanaliPubblici(int idProgetto){

        AssegnaCanaleDAO assegnaCanaleDAO = new AssegnaCanaleDAO();
        List<Canale> canaleList;

        try {
            canaleList = assegnaCanaleDAO.recuperoCanaliPubblici(idProgetto);
        } catch(DAOException e) {
            Printer.errorMessage("Non ci sono progetti");
            return null;
        }

        return canaleList;
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
        int tipoCanaleScelto = (int) infoCanale[2];  // indica se il canale scelto è pubblico o privato


        // Chiamo DAO per recuperare i messaggi del canale scelto, poi li restituisco a VIEW per stampare all'utente
        VisualizzaConversazioneDAO visualizzaConversazioneDAO = new VisualizzaConversazioneDAO();
        try {
            conversazioni = visualizzaConversazioneDAO.recuperoConversazione(idCanaleScelto, idProgettoScelto);
        } catch(DAOException e){
            Printer.errorMessage("Errore Visualizzazione Conversazione Controller");
        }

        if(tipoCanaleScelto==1) {
            capoprogettoView.stampaConversazione(conversazioni, idCanaleScelto, idProgettoScelto, tipoCanaleScelto);
        }else{
            capoprogettoView.stampaConversazionePrivata(conversazioni,idCanaleScelto,idProgettoScelto);
        }
    }




    /* Metodo per recuperare il messaggio originario di una risposta privata, chiamato da VIEW */
    public Messaggio recuperoMessaggioOriginario(String cfRispondente, Date dataInvioRispondente, Time orarioInvioRispondente){

        Messaggio messaggioOriginale = new Messaggio();  //per contenere il messaggio originale

        VisualizzaConversazioneDAO visualizzaConversazioneDAO = new VisualizzaConversazioneDAO();
        try{
            // qui viene passato: cfRispondente, dataInvioRispondente, orarioInvioRispondente (chiave primaria)
            messaggioOriginale = visualizzaConversazioneDAO.recuperoMessaggioOriginale(cfRispondente, dataInvioRispondente, orarioInvioRispondente);

        } catch(DAOException e){
            e.printStackTrace();
        }
        return messaggioOriginale;
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
            Printer.printlnViola("Risposta inviata con successo!");
        } catch(DAOException e){
            Printer.errorMessage("Errore inserimento risposta privata in DB con nuovo metodo");
            e.printStackTrace();
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

        try {
            assegnaCanaleDAO.salvaAssegnazioneCanale(lavoratore, idCanaleScelto, idProgettoScelto);
        }catch (DAOException d){
            Printer.errorMessage("Lavoratore scelto già fa parte del canale.");
        }

    }



    /* Metodo per recuperare la lista di lavoratori appartenente al progetto scelto da capo, chiamato da VIEW*/
    public static List<Lavoratore> recuperoLavoratori(int idProgetto){

        AssegnaCanaleDAO assegnaCanaleDAO = new AssegnaCanaleDAO();
        List<Lavoratore> lavoratori;

        try {
            lavoratori = assegnaCanaleDAO.recuperoLavoratoriByIdProgetto(idProgetto);
            // il metodo restituisce una lista di lavoratori
        } catch(DAOException e){
            Printer.errorMessage("Non ci sono lavoratori");
            return null;
        }

        return lavoratori;
    }



    /* Metodo per restituire a utente tutti i progetti a cui esso fa parte, chiamato da switch case 5 */
    public void visualizzaAppartenenzaProgetti(){

        CapoprogettoView capoprogettoView = new CapoprogettoView();
        capoprogettoView.listaProgetti();
    }


    /* Metodo per restituire a utente tutti i canali a cui esso fa parte, chiamato da switch case 6*/
    public void visualizzaAppartenenzaCanali(){

        CapoprogettoView capoprogettoView = new CapoprogettoView();
        capoprogettoView.listaCanali();

    }


    /* Metodo per recuperare la lista di canali appartenenti da DB, chiamato da VIEW */
    public List<Canale> recuperoCanaliAppartenenti(){

        List<Canale> canaliTarget = null;
        canaliTarget = VisualizzaAppartenenzaCanaliDAO.recuperaCanali();

        return canaliTarget;
    }




    /* Metodo per visualizzare i partecipanti di un progetto, chiamato da switch case 7 */
    public void visualizzaPartecipantiProgetto(){

        CapoprogettoView capoprogettoView = new CapoprogettoView();
        capoprogettoView.visualizzaPartecipantiProgetto();

    }



    /* Metodo per visualizzare i partecipanti di un canale scelto, chiamato da switch case 8 */
    public void visualizzaPartecipantiCanali(){

        CapoprogettoView capoprogettoView = new CapoprogettoView();
        capoprogettoView.visualizzaPartecipantiCanali();

    }



    /* Metodo per recuperare la lista dei lavoratori appartenenti a un canale scelto, chiamato da VIEW */
    public List<Lavoratore> recuperoLavoratoriByCanale(int idCanale, int idProgetto){

        List<Lavoratore> lavoratori;
        VisualizzaPartecipantiCanaleDAO visualizzaPartecipantiCanaleDAO = new VisualizzaPartecipantiCanaleDAO();
        lavoratori = visualizzaPartecipantiCanaleDAO.recuperoLavoratoriByCanale(idCanale,idProgetto);

        return lavoratori;
    }




    /* Metodo per assegnare un progetto a un lavoratore, chiamato da switch case 9 */
    public void assegnaProgetto(){

        CapoprogettoView capoprogettoView = new CapoprogettoView();

        Object[] infoLavoratore = capoprogettoView.assegnaProgetto();

        Lavoratore lavoratore = (Lavoratore) infoLavoratore[0];
        int idProgettoScelto = (int) infoLavoratore[1];

        // Chiama DAO per memorizzare in DB
        AssegnaProgettoDAO assegnaProgettoDAO = new AssegnaProgettoDAO();

        try {
            assegnaProgettoDAO.assegnaProgetto(lavoratore, idProgettoScelto);
            Printer.printlnViola("L'assegnazione progetto avvenuta con successo!");
        }catch(DAOException e){
            Printer.errorMessage("Lavoratore già fa parte del progetto.");
        }

    }



    /* Metodo per recuperare tutti i lavoratori, chiamato da VIEW */
    public List<Lavoratore> recuperoTuttiLavoratori(){

        List<Lavoratore> lavoratoreList;
        AssegnaProgettoDAO assegnaProgettoDAO = new AssegnaProgettoDAO();
        lavoratoreList = assegnaProgettoDAO.recuperoTuttiLavoratori();

        return lavoratoreList;
    }



    /* Metodo per visualizzare i canali privati in cui il capo non fa parte, quindi SOLO LETTURA, chiamto da switch case 10 */
    public void visualizzaConversazionePrivata(){

        CapoprogettoView capoprogettoView = new CapoprogettoView();
        Object[] infoCanale = capoprogettoView.visualizzaConversazionePrivata();
        // Viene restituito ID progetto e ID canale
        int idCanaleScelto = (int) infoCanale[0];
        int idProgettoScelto = (int) infoCanale[1];


        List<Messaggio> conversazioni = null;

        // Chiamo DAO per recuperare i messaggi del canale scelto, poi li restituisco a VIEW per stampare all'utente
        VisualizzaConversazioneDAO visualizzaConversazioneDAO = new VisualizzaConversazioneDAO();
        try {
            conversazioni = visualizzaConversazioneDAO.recuperoConversazione(idCanaleScelto, idProgettoScelto);
        } catch(DAOException e){
            Printer.errorMessage("Errore Visualizzazione Conversazione Controller");
        }

        // Chiama VIEW per stampare conversazioni
        capoprogettoView.stampaConversazionePrivataSoloLettura(conversazioni);


    }



    /* Metodo per recuperare i canali in cui capoProgetto non appartiene del progetto, chiamato da VIEW */
    public List<Canale> recuperoCanaliNonAppartiene(int idProgetto){

        List<Canale> canaleList;

        VisualizzaConversazioneDAO visualizzaConversazioneDAO = new VisualizzaConversazioneDAO();
        canaleList = visualizzaConversazioneDAO.recuperoCanaliPrivati(idProgetto);

        return canaleList;
    }












}
