package it.uniroma2.dicii.bd.view;

import it.uniroma2.dicii.bd.controller.CapoprogettoController;
import it.uniroma2.dicii.bd.controller.DipendenteController;
import it.uniroma2.dicii.bd.model.Canale;
import it.uniroma2.dicii.bd.model.Lavoratore;
import it.uniroma2.dicii.bd.model.Messaggio;
import it.uniroma2.dicii.bd.model.Progetto;
import it.uniroma2.dicii.bd.utils.Printer;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DipendenteView {

    public static void stampaTitolo() {
        Printer.println("\n************************************");
        Printer.printlnBlu("*    Benvenuto a ChatMulticanale   *");
        Printer.println("************************************");
    }


    public static int stampaMenu() throws IOException {

        Printer.printlnBlu("\n------- SCEGLI TRA OPERAZIONI ------");
        Printer.println("1) Inserisci messaggio");
        Printer.println("2) Visualizza conversazione");   // da implementare
        Printer.println("3) Visualizza partecipazione progetti");
        Printer.println("4) Visualizza appartenenza canali");
        Printer.println("5) Visualizza partecipanti del canale");
        Printer.println("0) Quit");


        Scanner input = new Scanner(System.in);
        int choice;
        while (true) {
            Printer.print("Please enter your choice: ");
            choice = input.nextInt();
            if (choice >= 0 && choice <= 5) {  //da cambiare il valore di limite
                break;
            }
            Printer.errorMessage("Invalid option");
        }

        return choice;
    }


    /* Metodo per ottenere scelta di utente su inserimento di messaggio, chiamato da controller */
    public Object[] inserisciMessaggio() {

        // Info da sapere: idProgetto, idCanale, contenutoMessaggio
        Scanner input = new Scanner(System.in);

        Printer.printlnBlu("\n***** INSERISCI UN NUOVO MESSAGGIO *****");

        Printer.printlnBlu("\nLista di progetti a cui fa parte: ");

        // Stampo lista di progetti a cui fa parte
        List<Progetto> progettiCandidati;

        DipendenteController dipendenteController = new DipendenteController();
        progettiCandidati = dipendenteController.recuperoProgetti();

        // Itera e stampa dei dettagli dei progetti con dipendente loggato
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

        List<Canale> canaleList = dipendenteController.recuperoCanali(idProgettoScelto);

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
        Printer.println("\nInserisci il messaggio (Premere INVIO per confermare l'invio di messaggio): ");
        String contenutoMessaggio = input.nextLine();


        // Restituisco un array di oggetti con il contenuto del messaggio, ID del canale e ID del progetto
        return new Object[]{contenutoMessaggio, idCanaleScelto, idProgettoScelto};
    }


    /* Metodo per visualizzare i messaggi di un canale scelto da utente */
    public Object[] visualizzaCanaliConversazione() {

        Scanner input = new Scanner(System.in);

        Printer.printlnBlu("\n***** VISUALIZZA CONVERSAZIONE *****");

        // L'utente devo scegliere ID progetto a cui fa parte, poi ID canale a cui fa parte
        Printer.printlnBlu("\nLista di progetti a cui fa parte: ");

        // Stampo lista di progetti a cui fa parte
        List<Progetto> progettiCandidati = new ArrayList<>();
        DipendenteController dipendenteController = new DipendenteController();

        // Chiama il metodo di controller per restituire i progetti
        progettiCandidati = dipendenteController.recuperoProgetti();

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
        List<Canale> canaleList = dipendenteController.recuperoCanali(idProgettoScelto);

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
        return new Object[]{idCanaleScelto, idProgettoScelto, tipoCanaleScelto};

    }


    /* Metodo per stampare le pagine di messaggi, chiamato da controller
     * in caso di canale pubblico, viene stampato solo i messaggi pubblici
     * in caso di canale privato (in cui utente appartiene), viene stampato solo i messaggi privati di quel canale
     * bisogna capire come formulare stored produceres */
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

        DipendenteController dipendenteController = new DipendenteController();

        if (sceltaRisposta == 1) {
            // Risposta pubblica
            Printer.print("Inserisci la risposta: ");
            String contenutoRisposta = scanner.nextLine();
            dipendenteController.rispostaPublic(messaggioOriginale, contenutoRisposta);
        } else if (sceltaRisposta == 2) {
            // Risposta privata
            Printer.print("Inserisci il tuo messaggio privato: ");
            String contenutoRispostaPrivata = scanner.nextLine();
            dipendenteController.memorizzaRispostaPrivata(messaggioOriginale, contenutoRispostaPrivata);
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

        DipendenteController dipendenteController = new DipendenteController();
        dipendenteController.rispostaPublic(messaggioOriginale, contenutoRisposta);
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

        DipendenteController dipendenteController = new DipendenteController();
        return dipendenteController.recuperoMessaggioOriginario(cfMittente, dataInvio, orarioInvio);
    }


    /* Metodo per visualizzare i progetti a cui fa parte, chiamato da controller */
    public void listaProgetti() {

        Printer.printlnBlu("\n***** VISUALIZZA PARTECIPAZIONE PROGETTI *****");

        Printer.printlnBlu("\nLista di progetti a cui fa parte: ");

        // Stampo lista di progetti a cui fa parte
        List<Progetto> progettiCandidati;
        DipendenteController dipendenteController = new DipendenteController();

        // Chiama il metodo di controller per restituire i progetti
        progettiCandidati = dipendenteController.recuperoProgetti();

        // Itera e stampa dei dettagli dei progetti con capoprogetto loggato
        for (Progetto progetto : progettiCandidati) {
            Printer.println("ID Progetto: " + progetto.getId());
            Printer.println("Nome Progetto: " + progetto.getNome());
            Printer.println("Data Inizio: " + progetto.getDataInzio());
            Printer.println("Data Scadenza: " + progetto.getDataScadenza());
            Printer.println("-------------------------------");
        }

    }


    /* Metodo per visualizzare lista di canali a cui fa parte, chiamato da controller */
    public void listaCanali() {
        Printer.printlnBlu("\n***** VISUALIZZA APPARTENENZA CANALI *****");

        Printer.printlnBlu("\nLista di canali a cui fa parte: ");

        // Stampo lista di progetti a cui fa parte
        List<Canale> canaleList;
        DipendenteController dipendenteController = new DipendenteController();

        // Chiama il metodo di controller per restituire i progetti
        canaleList = dipendenteController.recuperoCanaliAppartenenti();

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





    /* Metodo per visualizzare i partecipanti di un canale scelto, chiamato da controller*/
    public void visualizzaPartecipantiCanali() {

        Printer.printlnBlu("\n***** VISUALIZZA PARTECIPANTI DEL CANALE *****");

        Printer.printlnBlu("\nLista di progetti a cui fa parte: ");

        // Stampo lista di progetti a cui fa parte
        List<Progetto> progettiCandidati;

        // Chiama il metodo di controller per restituire i progetti
        progettiCandidati = DipendenteController.recuperoProgetti();

        // Itera e stampa dei dettagli dei progetti con capoProgetto loggato
        for (Progetto progetto : progettiCandidati) {
            Printer.println("ID Progetto: " + progetto.getId());
            Printer.println("Nome Progetto: " + progetto.getNome());
            Printer.println("Data Inizio: " + progetto.getDataInzio());
            Printer.println("Data Scadenza: " + progetto.getDataScadenza());
            Printer.println("-------------------------------");
        }

        Scanner input = new Scanner(System.in);
        Printer.print("\nInserisci l'ID del progetto in cui vuoi visualizzare i partecipanti: ");
        int idProgettoScelto = input.nextInt();
        input.nextLine();  // Consuma la newline

        DipendenteController dipendenteController = new DipendenteController();

        // Chiama il metodo di controller che restituisce tutti i canali di tale progetto
        Printer.printlnBlu("\nLista di canali disponibili per il progetto scelto: ");
        List<Canale> canaleList = dipendenteController.recuperoCanali(idProgettoScelto);

        // Itera e stampa i canali che appartengono al id progetto scelto
        for (Canale canale : canaleList) {
            Printer.println("ID Canale: " + canale.getIdCanale());
            Printer.println("Nome Canale: " + canale.getNome());
            Printer.println("CF creatore: " + canale.getCfCreatore());
            Printer.println("Data creazione: " + canale.getData());
            Printer.println("Tipo di canale: " + canale.getTipo());
            Printer.println("-------------------------------");
        }

        Printer.print("\nInserisci l'ID del canale che vuoi visualizzare i partecipanti: ");
        int idCanaleScelto = input.nextInt();
        input.nextLine();  // Consuma la newline

        // Lista dei lavoratori che appartiene al progetto scelto
        Printer.printlnBlu("\nLista dei lavoratori appartenenti al canale scelto: ");
        List<Lavoratore> listLavoratori = new ArrayList<>();
        listLavoratori = dipendenteController.recuperoLavoratoriByCanale(idCanaleScelto, idProgettoScelto);

        int index = 1;
        // Stampo la lista di lavoratori
        for (Lavoratore lavoratore : listLavoratori) {
            Printer.println(index + ". CF lavoratore: " + lavoratore.getCfLavoratore());
            Printer.println("   Nome lavoratore: " + lavoratore.getNomeLavoratore());
            Printer.println("   Cognome lavoratore: " + lavoratore.getCognomeLavoratore());
            Printer.println("   Ruolo: " + lavoratore.getRuolo());
            Printer.println("-------------------------------");
            index++;
        }


    }





}
