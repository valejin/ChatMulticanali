package it.uniroma2.dicii.bd.view;

import it.uniroma2.dicii.bd.bean.CanaleBean;
import it.uniroma2.dicii.bd.controller.CapoprogettoController;
import it.uniroma2.dicii.bd.model.Canale;
import it.uniroma2.dicii.bd.model.Lavoratore;
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

        Printer.printlnBlu("\n------- SCEGLI TRA OPERAZIONI ------");
        Printer.println("1) Crea nuovo canale");
        Printer.println("2) Inserisci messaggio");
        Printer.println("3) Visualizza conversazione");
        Printer.println("4) Assegna canale");
        Printer.println("0) Quit");


        Scanner input = new Scanner(System.in);
        int choice;
        while (true) {
            Printer.print("Please enter your choice: ");
            choice = input.nextInt();
            if (choice >= 0 && choice <= 4) {  //da cambiare il valore di limite
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

        //Printer.print("ID canale: ");
        //int idCanale = input.nextInt();
        //input.nextLine();  // Consuma la newline

        Printer.print("Nome canale: ");
        String nomeCanale = input.next();
        input.nextLine();  // Consuma la newline

        // Prendo la data odierna come giorno di creazione canale
        LocalDate currentDate = LocalDate.now();
        Date giornoCreazione = Date.valueOf(currentDate);

        UserSession session = UserSession.getInstance();
        String cfCapo = session.getCf();

        // Compilo info canale
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





    /* Metodo per visualizzare i messaggi di un canale scelto da utente */
    public Object[] visualizzaCanaliAppartenenti(){

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
        int tipoCanaleScelto = 0;



        // Trova il tipo di canale selezionato
        for (Canale canale : canaleList) {
            if (canale.getIdCanale() == idCanaleScelto) {
                tipoCanaleScelto = canale.getTipoId();
                break;
            }
        }


        // Restituisce un array contenente l'ID del canale, l'ID del progetto e il tipo di canale
        return new Object[] {idCanaleScelto, idProgettoScelto, tipoCanaleScelto};    //qui bisogna passare anche il tipo di canale

    }



    /* RISPOSTA PRIVATA DA RIVEDERE: Metodo per stampare le pagine di messaggi, chiamato da controller */
    public void stampaConversazione(List<Messaggio> conversazione, int idCanale, int idProgetto, int tipoCanale) {
        /* I messaggi sono organizzati in pagine e i capi progetto e dipendenti possono visualizzare,
        una per una, le pagine della conversazione.*/

        Scanner scanner = new Scanner(System.in);
        int messaggiPerPagina = 5;  // Numero di messaggi per pagina
        List<Messaggio> messaggiDaVisualizzare = new ArrayList<>();
        UserSession session = UserSession.getInstance();


        // Filtra i messaggi in base al tipo di canale
        if (tipoCanale == 1) {  // Canale pubblico
            for (Messaggio messaggio : conversazione) {
                if (messaggio.getIsVisible() == null || messaggio.getIsVisible() == 0) {
                    messaggiDaVisualizzare.add(messaggio);
                }
            }
        } else if (tipoCanale == 2) {  // Canale privato
            messaggiDaVisualizzare.addAll(conversazione);
        }




        // Filtra i messaggi per includere solo quelli pubblici
        /*for (Messaggio messaggio : conversazione) {
            if (messaggio.getIsVisible() == null || messaggio.getIsVisible() == 0) {
                messaggiDaVisualizzare.add(messaggio);
            }
        }

         */


        int numeroPagine = (int) Math.ceil((double) messaggiDaVisualizzare.size() / messaggiPerPagina);
        int paginaCorrente = 0;

        while (true) {
            // Calcola l'indice di inizio e fine per la pagina corrente
            int start = paginaCorrente * messaggiPerPagina;
            int end = Math.min(start + messaggiPerPagina, messaggiDaVisualizzare.size());

            Printer.printlnBlu("\n***** DETTAGLI CONVERSAZIONE - PAGINA " + (paginaCorrente + 1) + " di " + numeroPagine + " *****");

            // Stampa i messaggi per la pagina corrente con numerazione
            for (int i = start; i < end; i++) {
                Messaggio messaggio = messaggiDaVisualizzare.get(i);
                int numeroMessaggio = i + 1;  // Numero univoco del messaggio

                if (messaggio.isRisposta()) {
                    // Cerca l'indice del messaggio originale
                    Messaggio messaggioOriginale = messaggio.getMessaggioOriginale();
                    int numeroMessaggioOriginale = -1;

                    for (int j = 0; j < messaggiDaVisualizzare.size(); j++) {
                        Messaggio msg = messaggiDaVisualizzare.get(j);
                        // Confronta gli attributi unici per identificare il messaggio originale
                        if (msg.getCfUtente().equals(messaggioOriginale.getCfUtente())
                                && msg.getDataInvio().equals(messaggioOriginale.getDataInvio())
                                && msg.getOrarioInvio().equals(messaggioOriginale.getOrarioInvio())) {
                            numeroMessaggioOriginale = j + 1; // Indice del messaggio originale (inizia da 1)
                            break;
                        }
                    }

                    if (numeroMessaggioOriginale != -1) {
                        Printer.printlnViola("Risposta al Messaggio #" + numeroMessaggioOriginale);
                    } else {
                        Printer.println("Risposta a un messaggio non trovato.");
                    }
                }

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
                Printer.print("\nInserisci il numero del messaggio a cui vuoi rispondere: ");

                int numeroMessaggio = scanner.nextInt();
                scanner.nextLine();
                Messaggio messaggioOriginale = messaggiDaVisualizzare.get(numeroMessaggio - 1);
                messaggioOriginale.setIdCanale(idCanale);
                messaggioOriginale.setIdProgetto(idProgetto);

                Printer.println("\nVuoi rispondere in [1] Pubblico o [2] Privato?");
                int sceltaRisposta = scanner.nextInt();
                scanner.nextLine();

                if (sceltaRisposta == 1) {
                    // Risposta pubblica
                    Printer.print("Inserisci la risposta: ");
                    String contenutoRisposta = scanner.nextLine();

                    // Chiama il metodo di controller per memorizzare la risposta in DB
                    CapoprogettoController capoprogettoController = new CapoprogettoController();
                    capoprogettoController.rispostaPublic(messaggioOriginale, contenutoRisposta);

                } else if (sceltaRisposta == 2) {
                    // Risposta privata
                    Printer.print("Inserisci il tuo messaggio privato: ");
                    String contenutoRispostaPrivata = scanner.nextLine();


                    /*
                    // Imposto le info per creare un canale privata
                    CanaleBean canalePrivata = new CanaleBean();
                    canalePrivata.setIdProgetto(idProgetto);
                    canalePrivata.setTipoById(2);  //private
                    canalePrivata.setCfCreatore(session.getCf());  //cf utente rispondente

                    // Prendo la data odierna come giorno di creazione canale
                    LocalDate currentDate = LocalDate.now();
                    Date giornoCreazione = Date.valueOf(currentDate);

                    canalePrivata.setData(giornoCreazione);  //giorni odierno



                    // Chiama il metodo di controller per creare un nuovo canale privato e memorizzare la risposta in DB
                    CapoprogettoController capoprogettoController = new CapoprogettoController();
                    capoprogettoController.creaCanalePrivata(canalePrivata);  //creato nuovo canale privato

                    // Memorizzo la risposta privata in DB
                    capoprogettoController.rispostaPrivate(messaggioOriginale, contenutoRispostaPrivata);


                     */


                    // NUOVO METODO: Chiamo controller per memorizzare il messaggio di risposta privata
                    CapoprogettoController capoprogettoController = new CapoprogettoController();
                    capoprogettoController.memorizzaRispostaPrivata(messaggioOriginale, contenutoRispostaPrivata);



                }

            } else if (scelta == 0) {
                break;  // Esci dalla visualizzazione
            } else {
                Printer.errorMessage("Scelta non valida, riprova.");
            }
        }

    }



    /* Metodo per assegnare un canale a un lavoratore appartenente al progetto */
    public Object[] assegnaCanale(){

        Scanner input = new Scanner(System.in);

        Printer.printlnBlu("\n***** ASSEGNAZIONE CANALE *****");

        CapoprogettoController capoprogettoController = new CapoprogettoController();

        List<Progetto> progettiCoordinati = capoprogettoController.recuperoProgetti();

        Printer.println("\nLista di progetti da te coordinati: ");

        // Itera e stampa dei dettagli dei progetti con capoprogetto loggato
        for (Progetto progetto : progettiCoordinati) {
            Printer.println("ID Progetto: " + progetto.getId());
            Printer.println("Nome Progetto: " + progetto.getNome());
            Printer.println("Data Inizio: " + progetto.getDataInzio());
            Printer.println("Data Scadenza: " + progetto.getDataScadenza());
            Printer.println("-------------------------------");
        }

        // Chiedi all'utente di scegliere l'ID del progetto
        Printer.print("\nInserisci l'ID del progetto a cui vuoi coordinare i canali: ");
        int idProgettoScelto = input.nextInt();
        input.nextLine();  // Consuma la newline

        // Chiama il metodo di controller che restituisce tutti i canali di tale progetto
        Printer.printlnBlu("\nLista di canali disponibili per il progetto scelto: ");
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

        Printer.print("\nInserisci l'ID del canale che vuoi assegnare: ");
        int idCanaleScelto = input.nextInt();
        input.nextLine();  // Consuma la newline

        // Lista dei lavoratori che appartiene al progetto scelto
        Printer.println("\nLista dei lavoratori appartenenti al progetto scelto: ");
        List<Lavoratore> lavoratoreList = capoprogettoController.recuperoLavoratori(idProgettoScelto);


        int index = 1;
        // Stampo la lista di lavoratori
        for (Lavoratore lavoratore : lavoratoreList) {
            Printer.println(index + ". CF lavoratore: " + lavoratore.getCfLavoratore());
            Printer.println("   Nome lavoratore: " + lavoratore.getNomeLavoratore());
            Printer.println("   Cognome lavoratore: " + lavoratore.getCognomeLavoratore());
            Printer.println("   Ruolo: " + lavoratore.getRuolo());
            Printer.println("-------------------------------");
            index++;
        }

        // Chiedi all'utente di scegliere il lavoratore per numero
        Printer.print("\nInserisci il numero del lavoratore che vuoi assegnare al canale: ");
        int lavoratoreSceltoIndex = input.nextInt();
        input.nextLine();  // Consuma la newline

        Lavoratore lavoratoreScelto = new Lavoratore();

        // Recupera il lavoratore selezionato dalla lista usando l'indice
        if (lavoratoreSceltoIndex > 0 && lavoratoreSceltoIndex <= lavoratoreList.size()) {
            lavoratoreScelto = lavoratoreList.get(lavoratoreSceltoIndex - 1);

        }else {
            Printer.println("Numero del lavoratore non valido. Operazione annullata.");
        }

        return new Object[] {lavoratoreScelto, idCanaleScelto, idProgettoScelto};
    }





}
