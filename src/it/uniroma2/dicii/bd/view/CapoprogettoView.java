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
import java.sql.Time;
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
        Printer.println("3) Visualizza conversazioni appartenenti");
        Printer.println("4) Assegna canale");
        Printer.println("5) Visualizza partecipazione progetti");
        Printer.println("6) Visualizza appartenenza canali");
        Printer.println("7) Visualizza partecipanti del progetto");
        Printer.println("8) Visualizza partecipanti del canale");
        Printer.println("9) Assegna progetto");
        Printer.println("10) Visualizza conversazioni privati (solo in lettura)");

        Printer.println("0) Quit");


        Scanner input = new Scanner(System.in);
        int choice;
        while (true) {
            Printer.print("Please enter your choice: ");

            // Controlla se l'input è un intero
            if (input.hasNextInt()) {
                choice = input.nextInt();

                // Verifica se la scelta è nell'intervallo corretto
                if (choice >= 0 && choice <= 10) {
                    break;
                } else {
                    Printer.errorMessage("Invalid option. Please enter a number between 0 and 10.");
                }
            } else {
                // Se l'input non è un intero, mostra un errore
                Printer.errorMessage("Invalid input. Please enter an integer.");
                input.next(); // Consuma l'input non valido
            }
        }

        return choice;
    }




    private static void stampaListaProgetti(List<Progetto> progetti) {

        for (Progetto progetto : progetti) {
            Printer.println("ID Progetto: " + progetto.getId());
            Printer.println("Nome Progetto: " + progetto.getNome());
            Printer.println("Data Inizio: " + progetto.getDataInzio());
            Printer.println("Data Scadenza: " + progetto.getDataScadenza());
            Printer.println("-------------------------------");
        }
    }


    private static void stampaListaCanali(List<Canale> canali) {

        for (Canale canale : canali) {
            Printer.println("ID Canale: " + canale.getIdCanale());
            Printer.println("Nome Canale: " + canale.getNome());
            Printer.println("CF creatore: " + canale.getCfCreatore());
            Printer.println("Data creazione: " + canale.getData());
            Printer.println("Tipo di canale: " + canale.getTipo());
            Printer.println("-------------------------------");
        }
    }



    private static void stampaListaLavoratori(List<Lavoratore> lavoratori) {
        int index = 1;
        for (Lavoratore lavoratore : lavoratori) {
            Printer.println(index + ". CF lavoratore: " + lavoratore.getCfLavoratore());
            Printer.println("   Nome lavoratore: " + lavoratore.getNomeLavoratore());
            Printer.println("   Cognome lavoratore: " + lavoratore.getCognomeLavoratore());
            Printer.println("   Ruolo: " + lavoratore.getRuolo());
            Printer.println("-------------------------------");
            index++;
        }
    }




    public static CanaleBean creaNuovoCanale(){

        Scanner input = new Scanner(System.in);
        CanaleBean canaleBean = new CanaleBean();
        Printer.printlnBlu("\n***** CREA NUOVO CANALE *****");

        //devo prima scegliere il progetto, poi devo creare il nuovo canale
        //recupero da DAO una lista di progetti in cui sono CapoProgetto

        List<Progetto> progettiCoordinati = CapoprogettoController.recuperoProgetti();

        Printer.printlnBlu("Lista di progetti da te coordinati: ");
        stampaListaProgetti(progettiCoordinati);


        // Chiedi all'utente di scegliere l'ID del progetto
        Printer.print("\nInserisci l'ID del progetto in cui vuoi creare un nuovo canale: ");
        int idProgettoScelto = input.nextInt();
        input.nextLine();  // Consuma la newline

        // Chiedi all'utente di compilare le informazioni per creare un nuovo canale
        Printer.printlnBlu("\n------ COMPILA MODULO -----");

        Printer.print("Nome canale: ");
        String nomeCanale = input.nextLine();

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
        canaleBean.setTipoById(1);  //pubblico

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
        List<Progetto> progettiCandidati;

        CapoprogettoController capoprogettoController = new CapoprogettoController();

        // Chiama il metodo di controller per restituire i progetti
        progettiCandidati = CapoprogettoController.recuperoProgetti();
        stampaListaProgetti(progettiCandidati);


        Printer.print("\nInserisci l'ID del progetto in cui vuoi inviare un messaggio: ");
        int idProgettoScelto = input.nextInt();
        input.nextLine();  // Consuma la newline


        // Chiama il metodo di controller che restituisce tutti i canali di tale progetto
        Printer.printlnBlu("\nLista di canali disponibili a cui fa parte: ");
        List<Canale> canaleList = capoprogettoController.recuperoCanali(idProgettoScelto);

        // Itera e stampa i canali che appartengono al id progetto scelto
        stampaListaCanali(canaleList);

        Printer.print("\nInserisci l'ID del canale in cui vuoi inviare un messaggio: ");
        int idCanaleScelto = input.nextInt();
        input.nextLine();  // Consuma la newline

        // Raccolgo il contenuto del messaggio
        Printer.println("\nInserisci il messaggio (Premere INVIO per confermare l'invio di messaggio): ");
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
        List<Progetto> progettiCandidati;

        CapoprogettoController capoprogettoController = new CapoprogettoController();

        // Chiama il metodo di controller per restituire i progetti
        progettiCandidati = CapoprogettoController.recuperoProgetti();

        // Itera e stampa dei dettagli dei progetti con capoProgetto loggato
        stampaListaProgetti(progettiCandidati);

        Printer.print("\nInserisci l'ID del progetto in cui vuoi inviare un messaggio: ");
        int idProgettoScelto = input.nextInt();
        input.nextLine();  // Consuma la newline


        // Chiama il metodo di controller che restituisce tutti i canali di tale progetto
        Printer.printlnBlu("\nLista di canali disponibili a cui fa parte: ");
        List<Canale> canaleList = capoprogettoController.recuperoCanali(idProgettoScelto);

        // Itera e stampa i canali che appartengono al id progetto scelto
        stampaListaCanali(canaleList);

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





    /* Metodo per stampare le pagine di messaggi di un canale pubblico, chiamato da controller */
    public void stampaConversazione(List<Messaggio> conversazione, int idCanale, int idProgetto, int tipoCanale) {
        Scanner scanner = new Scanner(System.in);
        int messaggiPerPagina = 5;  // Numero di messaggi per pagina
        List<Messaggio> messaggiDaVisualizzare = new ArrayList<>();

        /* I messaggi sono organizzati in pagine e i capi progetto e dipendenti possono visualizzare,
        una per una, le pagine della conversazione.*/

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
                    int numeroMessaggioOriginale = trovaIndiceMessaggioOriginale(messaggiDaVisualizzare, messaggioOriginale);

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
                if (tipoCanale == 1) {
                    rispondiAMessaggioPubblicoPrivato(messaggiDaVisualizzare, scanner, idCanale, idProgetto);
                    break;
                } else if (tipoCanale == 2) {
                    rispondiAMessaggioPubblico(messaggiDaVisualizzare, scanner, idCanale, idProgetto);
                    break;
                }
            } else if (scelta == 0) {
                break;  // Esci dalla visualizzazione
            } else {
                Printer.errorMessage("Scelta non valida, riprova.");
            }
        }
    }

    private int trovaIndiceMessaggioOriginale(List<Messaggio> messaggiDaVisualizzare, Messaggio messaggioOriginale) {
        for (int j = 0; j < messaggiDaVisualizzare.size(); j++) {
            Messaggio msg = messaggiDaVisualizzare.get(j);
            if (msg.getCfUtente().equals(messaggioOriginale.getCfUtente())
                    && msg.getDataInvio().equals(messaggioOriginale.getDataInvio())
                    && msg.getOrarioInvio().equals(messaggioOriginale.getOrarioInvio())) {
                return j + 1; // Indice del messaggio originale (inizia da 1)
            }
        }
        return -1;
    }

    private void rispondiAMessaggioPubblicoPrivato(List<Messaggio> messaggiDaVisualizzare, Scanner scanner, int idCanale, int idProgetto) {
        Printer.print("\nInserisci il numero del messaggio a cui vuoi rispondere: ");
        int numeroMessaggio = scanner.nextInt();
        scanner.nextLine();

        Messaggio messaggioOriginale = messaggiDaVisualizzare.get(numeroMessaggio - 1);
        messaggioOriginale.setIdCanale(idCanale);
        messaggioOriginale.setIdProgetto(idProgetto);

        Printer.println("\nVuoi rispondere in [1] Pubblico o [2] Privato?");
        int sceltaRisposta = scanner.nextInt();
        scanner.nextLine();

        CapoprogettoController capoprogettoController = new CapoprogettoController();
        if (sceltaRisposta == 1) {
            // Risposta pubblica
            Printer.print("Inserisci la risposta: ");
            String contenutoRisposta = scanner.nextLine();
            capoprogettoController.rispostaPublic(messaggioOriginale, contenutoRisposta);
        } else if (sceltaRisposta == 2) {
            // Risposta privata
            Printer.print("Inserisci il tuo messaggio privato: ");
            String contenutoRispostaPrivata = scanner.nextLine();
            capoprogettoController.memorizzaRispostaPrivata(messaggioOriginale, contenutoRispostaPrivata);
        }
    }

    private void rispondiAMessaggioPubblico(List<Messaggio> messaggiDaVisualizzare, Scanner scanner, int idCanale, int idProgetto) {
        Printer.print("\nInserisci il numero del messaggio a cui vuoi rispondere: ");
        int numeroMessaggio = scanner.nextInt();
        scanner.nextLine();

        Messaggio messaggioOriginale = messaggiDaVisualizzare.get(numeroMessaggio - 1);
        messaggioOriginale.setIdCanale(idCanale);
        messaggioOriginale.setIdProgetto(idProgetto);

        // Risposta pubblica automatica
        Printer.print("Inserisci la risposta: ");
        String contenutoRisposta = scanner.nextLine();

        CapoprogettoController capoprogettoController = new CapoprogettoController();
        capoprogettoController.rispostaPublic(messaggioOriginale, contenutoRisposta);
    }




    /* Metodo NUOVO per stampare messaggi di un canale privata:
     * - il messaggio originario: è il quale l'utente ha risposto in modo privato e viene creato un canale privato */
    public void stampaConversazionePrivata(List<Messaggio> conversazione, int idCanale, int idProgetto) {
        Scanner scanner = new Scanner(System.in);
        int messaggiPerPagina = 5;  // Numero di messaggi per pagina
        List<Messaggio> messaggiDaVisualizzare = new ArrayList<>(conversazione);

        /* I messaggi sono organizzati in pagine e i capi progetto e dipendenti possono visualizzare,
        una per una, le pagine della conversazione.*/

        // Estrai il primo messaggio dalla lista e recupera il messaggio originale
        Messaggio primoMessaggio = messaggiDaVisualizzare.getFirst();
        Messaggio messaggioOriginario = recuperaMessaggioOriginale(primoMessaggio);

        // Aggiungi il messaggio originario all'inizio della lista
        messaggiDaVisualizzare.addFirst(messaggioOriginario);

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
                    int numeroMessaggioOriginale = trovaIndiceMessaggioOriginale(messaggiDaVisualizzare, messaggio.getMessaggioOriginale());
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
                rispondiAMessaggioPubblico(messaggiDaVisualizzare, scanner, idCanale, idProgetto);
                break;  // Esci dalla visualizzazione dopo aver risposto
            } else if (scelta == 0) {
                break;  // Esci dalla visualizzazione
            } else {
                Printer.errorMessage("Scelta non valida, riprova.");
            }
        }
    }

    private Messaggio recuperaMessaggioOriginale(Messaggio primoMessaggio) {
        String cfMittente = primoMessaggio.getCfUtente();
        Date dataInvio = primoMessaggio.getDataInvio();
        Time orarioInvio = primoMessaggio.getOrarioInvio();

        CapoprogettoController controller = new CapoprogettoController();
        return controller.recuperoMessaggioOriginario(cfMittente, dataInvio, orarioInvio);
    }




    /* Metodo per assegnare un canale a un lavoratore appartenente al progetto */
    public Object[] assegnaCanale(){

        Scanner input = new Scanner(System.in);

        Printer.printlnBlu("\n***** ASSEGNAZIONE CANALE *****");

        CapoprogettoController capoprogettoController = new CapoprogettoController();

        List<Progetto> progettiCoordinati = CapoprogettoController.recuperoProgetti();

        Printer.printlnBlu("\nLista di progetti da te coordinati: ");

        // Itera e stampa dei dettagli dei progetti con capoProgetto loggato
        stampaListaProgetti(progettiCoordinati);

        // Chiedi all'utente di scegliere l'ID del progetto
        Printer.print("\nInserisci l'ID del progetto a cui vuoi coordinare i canali: ");
        int idProgettoScelto = input.nextInt();
        input.nextLine();  // Consuma la newline

        // Chiama il metodo di controller che restituisce tutti i canali di tale progetto
        Printer.printlnBlu("\nLista di canali disponibili per il progetto scelto: ");
        List<Canale> canaleList = capoprogettoController.recuperoCanaliPubblici(idProgettoScelto);

        // Itera e stampa i canali che appartengono al id progetto scelto
        stampaListaCanali(canaleList);

        Printer.print("\nInserisci l'ID del canale che vuoi assegnare: ");
        int idCanaleScelto = input.nextInt();
        input.nextLine();  // Consuma la newline

        // Lista dei lavoratori che appartiene al progetto scelto
        Printer.printlnBlu("\nLista dei lavoratori appartenenti al progetto scelto: ");
        List<Lavoratore> lavoratoreList = CapoprogettoController.recuperoLavoratori(idProgettoScelto);

        // Stampo la lista di lavoratori
        stampaListaLavoratori(lavoratoreList);

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



    /* Metodo per visualizzare i progetti a cui fa parte */
    public void listaProgetti() {

        Printer.printlnBlu("\n***** VISUALIZZA PARTECIPAZIONE PROGETTI *****");

        // L'utente devo scegliere ID progetto a cui fa parte
        Printer.printlnBlu("\nLista di progetti a cui fa parte: ");

        // Stampo lista di progetti a cui fa parte
        List<Progetto> progettiCandidati;

        // Chiama il metodo di controller per restituire i progetti
        progettiCandidati = CapoprogettoController.recuperoProgetti();

        // Itera e stampa dei dettagli dei progetti con capoProgetto loggato
        stampaListaProgetti(progettiCandidati);
    }



    /* Metodo per visualizzare lista di canali a cui fa parte, chiamato da controller */
    public void listaCanali(){
        Printer.printlnBlu("\n***** VISUALIZZA APPARTENENZA CANALI *****");

        Printer.printlnBlu("\nLista di canali a cui fa parte: ");

        // Stampo lista di progetti a cui fa parte
        List<Canale> canaleList;
        CapoprogettoController capoprogettoController = new CapoprogettoController();

        // Chiama il metodo di controller per restituire i progetti
        canaleList = capoprogettoController.recuperoCanaliAppartenenti();

        // Itera e stampa i canali che appartengono al id progetto scelto
        for (Canale canale : canaleList) {
            Printer.println("ID Canale: " + canale.getIdCanale());
            Printer.println("ID Progetto: " + canale.getIdProgetto());
            Printer.println("Nome Canale: " + canale.getNome());
            Printer.println("CF creatore: " + canale.getCfCreatore());
            Printer.println("Data creazione: " + canale.getData());
            Printer.println("Tipo di canale: " + canale.getTipo());
            Printer.println("-------------------------------");
        }
    }




    /* Metodo per visualizzare i partecipanti di un progetto */
    public void visualizzaPartecipantiProgetto(){
        Printer.printlnBlu("\n***** VISUALIZZA PARTECIPANTI DEL PROGETTO *****");

        Printer.printlnBlu("\nLista di progetti a cui fa parte: ");

        // Stampo lista di progetti a cui fa parte
        List<Progetto> progettiCandidati;

        // Chiama il metodo di controller per restituire i progetti
        progettiCandidati = CapoprogettoController.recuperoProgetti();

        // Itera e stampa dei dettagli dei progetti con capoProgetto loggato
        stampaListaProgetti(progettiCandidati);

        Scanner input = new Scanner(System.in);
        Printer.print("\nInserisci l'ID del progetto in cui vuoi visualizzare i partecipanti: ");
        int idProgettoScelto = input.nextInt();
        input.nextLine();  // Consuma la newline


        // Lista dei lavoratori che appartiene al progetto scelto
        Printer.printlnBlu("\nLista dei lavoratori appartenenti al progetto scelto: ");
        List<Lavoratore> lavoratoreList = CapoprogettoController.recuperoLavoratori(idProgettoScelto);

        // Stampo la lista di lavoratori
        stampaListaLavoratori(lavoratoreList);
    }




    /* Metodo per visualizzare i partecipanti di un canale scelto, chiamato da controller*/
    public void visualizzaPartecipantiCanali(){

        Printer.printlnBlu("\n***** VISUALIZZA PARTECIPANTI DEL CANALE *****");

        Printer.printlnBlu("\nLista di progetti a cui fa parte: ");

        // Stampo lista di progetti a cui fa parte
        List<Progetto> progettiCandidati;

        // Chiama il metodo di controller per restituire i progetti
        progettiCandidati = CapoprogettoController.recuperoProgetti();

        // Itera e stampa dei dettagli dei progetti con capoProgetto loggato
        stampaListaProgetti(progettiCandidati);

        Scanner input = new Scanner(System.in);
        Printer.print("\nInserisci l'ID del progetto in cui vuoi visualizzare i partecipanti: ");
        int idProgettoScelto = input.nextInt();
        input.nextLine();  // Consuma la newline

        CapoprogettoController capoprogettoController = new CapoprogettoController();

        // Chiama il metodo di controller che restituisce tutti i canali di tale progetto
        Printer.printlnBlu("\nLista di canali disponibili per il progetto scelto: ");
        List<Canale> canaleList = capoprogettoController.recuperoCanali(idProgettoScelto);

        // Itera e stampa i canali che appartengono al id progetto scelto
        stampaListaCanali(canaleList);

        Printer.print("\nInserisci l'ID del canale che vuoi visualizzare i partecipanti: ");
        int idCanaleScelto = input.nextInt();
        input.nextLine();  // Consuma la newline

        // Lista dei lavoratori che appartiene al progetto scelto
        Printer.printlnBlu("\nLista dei lavoratori appartenenti al canale scelto: ");
        List<Lavoratore> listLavoratori;
        listLavoratori = capoprogettoController.recuperoLavoratoriByCanale(idCanaleScelto,idProgettoScelto);

        // Stampo la lista di lavoratori
        stampaListaLavoratori(listLavoratori);
    }




    /* Metodo per assegnare il progetto a un lavoratore, chiamato da Controller */
    public Object[] assegnaProgetto(){

        CapoprogettoController capoprogettoController = new CapoprogettoController();
        Printer.printlnBlu("\n***** ASSEGNAZIONE PROGETTO *****");

        Printer.printlnBlu("\nLista di progetti a cui fa parte: ");

        // Stampo lista di progetti a cui fa parte
        List<Progetto> progettiCandidati;

        // Chiama il metodo di controller per restituire i progetti
        progettiCandidati = CapoprogettoController.recuperoProgetti();

        // Itera e stampa dei dettagli dei progetti con capoProgetto loggato
        stampaListaProgetti(progettiCandidati);

        Scanner input = new Scanner(System.in);
        Printer.print("\nInserisci l'ID del progetto in cui vuoi assegnare: ");
        int idProgettoScelto = input.nextInt();
        input.nextLine();  // Consuma la newline


        // Lista dei lavoratori in azienda
        Printer.printlnBlu("\nLista dei lavoratori: ");

        // Recuperare la lista dei lavoratori
        List<Lavoratore> tuttiLavoratori = capoprogettoController.recuperoTuttiLavoratori();

        // Stampo la lista di lavoratori
        stampaListaLavoratori(tuttiLavoratori);

        // Chiedi all'utente di scegliere il lavoratore per numero
        Printer.print("\nInserisci il numero del lavoratore che vuoi assegnare il progetto: ");
        int lavoratoreSceltoIndex = input.nextInt();
        input.nextLine();  // Consuma la newline

        Lavoratore lavoratoreScelto = new Lavoratore();

        // Recupera il lavoratore selezionato dalla lista usando l'indice
        if (lavoratoreSceltoIndex > 0 && lavoratoreSceltoIndex <= tuttiLavoratori.size()) {
            lavoratoreScelto = tuttiLavoratori.get(lavoratoreSceltoIndex - 1);
        }else {
            Printer.println("Numero del lavoratore non valido. Operazione annullata.");
        }

        return new Object[] {lavoratoreScelto, idProgettoScelto};
    }



    /* Metodo per visualizzare canali privati degli altri (SOLO LETTURA) */
    public Object[] visualizzaConversazionePrivata(){

        CapoprogettoController capoprogettoController = new CapoprogettoController();
        Printer.printlnBlu("\n***** VISUALIZZA CONVERSAZIONI PRIVATI *****");

        Printer.printlnBlu("\nLista di progetti a cui fa parte: ");

        // Stampo lista di progetti a cui fa parte
        List<Progetto> progettiCandidati;

        // Chiama il metodo di controller per restituire i progetti
        progettiCandidati = CapoprogettoController.recuperoProgetti();

        // Itera e stampa dei dettagli dei progetti con capoProgetto loggato
        stampaListaProgetti(progettiCandidati);

        Scanner input = new Scanner(System.in);
        Printer.print("\nInserisci l'ID del progetto: ");
        int idProgettoScelto = input.nextInt();
        input.nextLine();  // Consuma la newline

        // Chiama il metodo di controller che restituisce tutti i canali di tale progetto
        Printer.printlnBlu("\nLista di canali disponibili per il progetto scelto: ");

        List<Canale> canaleList = capoprogettoController.recuperoCanaliNonAppartiene(idProgettoScelto);

        // Itera e stampa i canali che appartengono al id progetto scelto
        for (Canale canale : canaleList) {
            Printer.println("ID Canale: " + canale.getIdCanale());
            Printer.println("CF creatore: " + canale.getCfCreatore());
            Printer.println("Data creazione: " + canale.getData());
            Printer.println("Tipo di canale: " + canale.getTipo());
            Printer.println("-------------------------------");
        }

        Printer.print("\nInserisci l'ID del canale: ");
        int idCanaleScelto = input.nextInt();
        input.nextLine();  // Consuma la newline

        return new Object[] {idCanaleScelto ,idProgettoScelto};
    }





    /* Metodo per stampare la conversazione privata (SOLO LETTURA) */
    public void stampaConversazionePrivataSoloLettura(List<Messaggio> conversazione) {
        Scanner scanner = new Scanner(System.in);
        int messaggiPerPagina = 5;  // Numero di messaggi per pagina
        List<Messaggio> messaggiDaVisualizzare = new ArrayList<>(conversazione);

        /* I messaggi sono organizzati in pagine e i capi progetto e dipendenti possono visualizzare,
        una per una, le pagine della conversazione.*/

        // Estrai il primo messaggio dalla lista e recupera il messaggio originale
        Messaggio primoMessaggio = messaggiDaVisualizzare.getFirst();
        Messaggio messaggioOriginario = recuperaMessaggioOriginale(primoMessaggio);

        // Aggiungi il messaggio originario all'inizio della lista
        messaggiDaVisualizzare.addFirst(messaggioOriginario);

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
                    int numeroMessaggioOriginale = trovaIndiceMessaggioOriginale(messaggiDaVisualizzare, messaggio.getMessaggioOriginale());
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
            Printer.println("\n[1] Pagina precedente | [2] Pagina successiva | [0] Esci");
            int scelta = scanner.nextInt();

            if (scelta == 1 && paginaCorrente > 0) {
                paginaCorrente--;  // Vai alla pagina precedente
            } else if (scelta == 2 && paginaCorrente < numeroPagine - 1) {
                paginaCorrente++;  // Vai alla pagina successiva
            } else if (scelta == 0) {
                break;  // Esci dalla visualizzazione
            } else {
                Printer.errorMessage("Scelta non valida, riprova.");
            }
        }
    }



}
