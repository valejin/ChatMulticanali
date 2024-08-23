package it.uniroma2.dicii.bd.view;

import it.uniroma2.dicii.bd.bean.CanaleBean;
import it.uniroma2.dicii.bd.controller.CapoprogettoController;
import it.uniroma2.dicii.bd.model.Canale;
import it.uniroma2.dicii.bd.model.Messaggio;
import it.uniroma2.dicii.bd.model.Progetto;
import it.uniroma2.dicii.bd.utils.Printer;
import it.uniroma2.dicii.bd.utils.UserSession;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CapoprogettoView {

    public static void stampaTitolo(){
        Printer.println("\n************************************");
        Printer.printlnBlu("*    Benvenuto a ChatMulticanale   *");
        Printer.println("************************************");
    }



    public static int stampaMenu() throws IOException {

        Printer.printlnBlu("\n------- SCEGLI TRA OPERAZIONI ------\n");
        Printer.println("1) Crea nuovo canale");
        Printer.println("2) Inserisci messaggio");
        Printer.println("3) Visualizza conversazione");
        Printer.println("0) Quit");


        Scanner input = new Scanner(System.in);
        int choice;
        while (true) {
            Printer.print("Please enter your choice: ");
            choice = input.nextInt();
            if (choice >= 0 && choice <= 3) {  //da cambiare il valore di limite
                break;
            }
            Printer.println("Invalid option");
        }

        return choice;
    }



    public static CanaleBean creaNuovoCanale(){

        Scanner input = new Scanner(System.in);

        CanaleBean canaleBean = new CanaleBean();
        Printer.printlnBlu("\n***** CREA NUOVO CANALE *****");

        //devo prima scegliere il progetto, poi devo creare il nuovo canale
        //recupero da DAO una lista di progetti a cui sono CapoProgetto

        List<Progetto> progettiCoordinati = CapoprogettoController.recuperoProgetti();

        Printer.println("Lista di progetti da te coordinati: ");

        // Itera e stampa dei dettagli dei progetti con capoprogetto loggato
        for (Progetto progetto : progettiCoordinati) {
            Printer.println("ID Progetto: " + progetto.getId());
            Printer.println("Nome Progetto: " + progetto.getNome());
            Printer.println("Data Inizio: " + progetto.getDataInzio());
            Printer.println("Data Scadenza: " + progetto.getDataScadenza());
            Printer.println("-------------------------------");
        }

        // Chiedi all'utente di scegliere l'ID del progetto
        Printer.print("\nInserisci l'ID del progetto in cui vuoi creare un nuovo canale: ");
        int idProgettoScelto = input.nextInt();
        input.nextLine();  // Consuma la newline

        // Chiedi all'utente di compilare le informazioni per creare un nuovo canale
        Printer.printlnBlu("\n------ COMPILA MODULO -----\n");

        Printer.print("ID canale: ");
        int idCanale = input.nextInt();
        input.nextLine();  // Consuma la newline

        Printer.print("Nome canale: ");
        String nomeCanale = input.next();
        input.nextLine();  // Consuma la newline

        // Prendo la data odierna come giorno di creazione canale
        LocalDate currentDate = LocalDate.now();
        Date giornoCreazione = Date.valueOf(currentDate);

        UserSession session = UserSession.getInstance();
        String cfCapo = session.getCf();

        // Compilo info canale
        canaleBean.setIdCanale(idCanale);
        canaleBean.setIdProgetto(idProgettoScelto);
        canaleBean.setNome(nomeCanale);
        canaleBean.setCfCreatore(cfCapo);
        canaleBean.setData(giornoCreazione);
        canaleBean.setTipoById(1);  //publico

        return canaleBean;
    }


    /* Metodo per inserire un nuovo messaggio nel canale */
    public static Object[] inserisciMessaggio(){

        Scanner input = new Scanner(System.in);

        Printer.printlnBlu("\n***** INSERISCI UN NUOVO MESSAGGIO *****");

        // L'utente prima devo scegliere il progetto a cui fa parte
        // Poi deve scegliere il canale a cui fa parte per inviare il nuovo messaggio

        Printer.printlnBlu("\nLista di progetti a cui fa parte: ");

        // Stampo lista di progetti a cui fa parte
        List<Progetto> progettiCandidati = new ArrayList<>();

        CapoprogettoController capoprogettoController = new CapoprogettoController();

        // Chiama il metodo di controller per restituire i progetti
        progettiCandidati = capoprogettoController.recuperoProgetti();

        // Itera e stampa dei dettagli dei progetti con capoprogetto loggato
        for (Progetto progetto : progettiCandidati) {
            Printer.println("ID Progetto: " + progetto.getId());
            Printer.println("Nome Progetto: " + progetto.getNome());
            Printer.println("Data Inizio: " + progetto.getDataInzio());
            Printer.println("Data Scadenza: " + progetto.getDataScadenza());
            Printer.println("-------------------------------");
        }

        Printer.print("\nInserisci l'ID del progetto in cui vuoi inviare un messaggio: ");
        int idProgettoScelto = input.nextInt();
        input.nextLine();  // Consuma la newline


        // Chiama il metodo di controller che restituisce tutti i canali di tale progetto
        Printer.printlnBlu("\nLista di canali disponibili a cui fa parte: ");
        List<Canale> canaleList = capoprogettoController.recuperoCanali(idProgettoScelto);

        // Itera e stampa i canali che appartengono al id progetto scelto
        for (Canale canale : canaleList) {
            Printer.println("ID Canale: " + canale.getIdCanale());
            Printer.println("Nome Canale: " + canale.getNome());
            Printer.println("CF creatore: " + canale.getCfCreatore());
            Printer.println("Data creazione: " + canale.getData());
            Printer.println("Tipo di canale: " + canale.getTipo());
            Printer.println("-------------------------------");
        }


        Printer.print("\nInserisci l'ID del canale in cui vuoi inviare un messaggio: ");
        int idCanaleScelto = input.nextInt();
        input.nextLine();  // Consuma la newline

        // Raccolgo il contenuto del messaggio
        Printer.println("\nInserisci il messaggio (Permere INVIO per confermare l'invio di messaggio): ");
        String contenutoMessaggio = input.nextLine();


        // Restituisco un array di oggetti con il contenuto del messaggio, ID del canale e ID del progetto
        return new Object[] {contenutoMessaggio, idCanaleScelto, idProgettoScelto};

    }





    /* Metodo per visualizzare i messaggi di un canale sclto da utente */
    public Object[] visualizzaConversazione(){

        Scanner input = new Scanner(System.in);

        Printer.printlnBlu("\n***** VISUALIZZA CONVERSAZIONE *****");

        // L'utente devo scegliere ID progetto a cui fa parte, poi ID canale a cui fa parte
        Printer.printlnBlu("\nLista di progetti a cui fa parte: ");

        // Stampo lista di progetti a cui fa parte
        List<Progetto> progettiCandidati = new ArrayList<>();

        CapoprogettoController capoprogettoController = new CapoprogettoController();

        // Chiama il metodo di controller per restituire i progetti
        progettiCandidati = capoprogettoController.recuperoProgetti();

        // Itera e stampa dei dettagli dei progetti con capoprogetto loggato
        for (Progetto progetto : progettiCandidati) {
            Printer.println("ID Progetto: " + progetto.getId());
            Printer.println("Nome Progetto: " + progetto.getNome());
            Printer.println("Data Inizio: " + progetto.getDataInzio());
            Printer.println("Data Scadenza: " + progetto.getDataScadenza());
            Printer.println("-------------------------------");
        }

        Printer.print("\nInserisci l'ID del progetto in cui vuoi inviare un messaggio: ");
        int idProgettoScelto = input.nextInt();
        input.nextLine();  // Consuma la newline


        // Chiama il metodo di controller che restituisce tutti i canali di tale progetto
        Printer.printlnBlu("\nLista di canali disponibili a cui fa parte: ");
        List<Canale> canaleList = capoprogettoController.recuperoCanali(idProgettoScelto);

        // Itera e stampa i canali che appartengono al id progetto scelto
        for (Canale canale : canaleList) {
            Printer.println("ID Canale: " + canale.getIdCanale());
            Printer.println("Nome Canale: " + canale.getNome());
            Printer.println("CF creatore: " + canale.getCfCreatore());
            Printer.println("Data creazione: " + canale.getData());
            Printer.println("Tipo di canale: " + canale.getTipo());
            Printer.println("-------------------------------");
        }


        Printer.print("\nInserisci l'ID del canale per visualizzare la conversazione: ");
        int idCanaleScelto = input.nextInt();
        input.nextLine();  // Consuma la newline

        return new Object[] {idCanaleScelto, idProgettoScelto};

    }



    /* Metodo per stampare le pagine di messaggi, chiamato da controller */
    public void stampaConversazione(List<Messaggio> conversazione){
        /* I messaggi sono organizzati in pagine e i capi progetto e dipendenti possono visualizzare,
        una per una, le pagine della conversazione.*/

        Scanner scanner = new Scanner(System.in);
        int messaggiPerPagina = 5;  // Numero di messaggi per pagina
        int numeroPagine = (int) Math.ceil((double) conversazione.size() / messaggiPerPagina);
        int paginaCorrente = 0;

        while (true) {
            // Calcola l'indice di inizio e fine per la pagina corrente
            int start = paginaCorrente * messaggiPerPagina;
            int end = Math.min(start + messaggiPerPagina, conversazione.size());

            Printer.printlnBlu("\n***** DETTAGLI CONVERSAZIONE - PAGINA " + (paginaCorrente + 1) + " di " + numeroPagine + " *****");

            // Stampa i messaggi per la pagina corrente con numerazione
            for (int i = start; i < end; i++) {
                Messaggio messaggio = conversazione.get(i);
                int numeroMessaggio = i + 1;  // Numero univoco del messaggio
                Printer.println("Messaggio #" + numeroMessaggio);
                Printer.print("Nome: " + messaggio.getNomeUtente() + "   ");
                Printer.println("Cognome: " + messaggio.getCognomeUtente());
                Printer.print("Data Invio: " + messaggio.getDataInvio() + "   ");
                Printer.println("Orario Invio: " + messaggio.getOrarioInvio());
                Printer.println("Contenuto: " + messaggio.getContenuto());
                Printer.println("-------------------------------");
            }

            // Chiedi all'utente cosa fare
            Printer.println("\n[1] Pagina precedente | [2] Pagina successiva | [3] Rispondi | [0] Esci");
            int scelta = scanner.nextInt();

            if (scelta == 1 && paginaCorrente > 0) {
                paginaCorrente--;  // Vai alla pagina precedente
            } else if (scelta == 2 && paginaCorrente < numeroPagine - 1) {
                paginaCorrente++;  // Vai alla pagina successiva
            } else if (scelta == 3) {
                Printer.print("Inserisci il numero del messaggio a cui vuoi rispondere: ");
                int numeroMessaggio = scanner.nextInt();
                //rispondiMessaggio(conversazione, numeroMessaggio);  // Chiama il metodo per rispondere al messaggio
            } else if (scelta == 0) {
                break;  // Esci dalla visualizzazione
            } else {
                Printer.errorMessage("Scelta non valida, riprova.");
            }
        }


    }







}
