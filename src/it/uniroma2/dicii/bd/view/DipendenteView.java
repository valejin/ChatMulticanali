package it.uniroma2.dicii.bd.view;

import it.uniroma2.dicii.bd.controller.DipendenteController;
import it.uniroma2.dicii.bd.model.Canale;
import it.uniroma2.dicii.bd.model.Messaggio;
import it.uniroma2.dicii.bd.model.Progetto;
import it.uniroma2.dicii.bd.utils.Printer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DipendenteView {

    public static void stampaTitolo(){
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




    /* Metodo per ottenere scelta di utente su inserimento di messaggio, chiamato da controller */
    public Object[] inserisciMessaggio(){

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
        Printer.println("\nInserisci il messaggio (Permere INVIO per confermare l'invio di messaggio): ");
        String contenutoMessaggio = input.nextLine();


        // Restituisco un array di oggetti con il contenuto del messaggio, ID del canale e ID del progetto
        return new Object[] {contenutoMessaggio, idCanaleScelto, idProgettoScelto};
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
        return new Object[] {idCanaleScelto, idProgettoScelto, tipoCanaleScelto};

    }




    /* Metodo per stampare le pagine di messaggi, chiamato da controller */
    public void stampaConversazione(List<Messaggio> conversazione, int idCanale, int idProgetto, int tipoCanale) {

        // quando stampo devo considerare per i canali pubblici solo i messaggi pubblici
        // per i canali privati, posso stampare solo i messaggi di quel canale, con il messaggio originario




    }





    /* Metodo per visualizzare i progetti a cui fa parte, chiamato da controller */
    public void listaProgetti(){

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
    public void listaCanali(){
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



}
