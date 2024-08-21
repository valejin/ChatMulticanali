package it.uniroma2.dicii.bd.view;

import it.uniroma2.dicii.bd.bean.ProgettoBean;
import it.uniroma2.dicii.bd.controller.AmministratoreController;
import it.uniroma2.dicii.bd.model.CapoProgetto;
import it.uniroma2.dicii.bd.model.Progetto;
import it.uniroma2.dicii.bd.utils.Printer;


import java.io.IOException;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class AmministratoreView {

    public static void stampaTitolo(){
        Printer.println("\n************************************");
        Printer.printlnBlu("*    Benvenuto a ChatMulticanale   *");
        Printer.println("************************************\n");
    }




    public static int stampaMenu() throws IOException {

        Printer.printlnBlu("------- SCEGLI TRA OPERAZIONI ------\n");
        Printer.println("1) Inserisci progetto");
        Printer.println("2) Assegna capo progetto");
        Printer.println("3) Visualizza lista progetti con capo progetto");
        Printer.println("4) Visualizza lista progetti senza capo progetto");
        Printer.println("0) Quit");


        Scanner input = new Scanner(System.in);
        int choice;
        while (true) {
            try {
                Printer.print("Please enter your choice: ");
                choice = input.nextInt();
                if (choice >= 0 && choice <= 4) {
                    break;
                }
                Printer.println("Invalid option, please choose between 0 and 3.");
            } catch (InputMismatchException e) {
                Printer.println("Invalid input. Please enter a number between 0 and 3.");
                input.next(); // Consuma l'input non valido
            }
        }
        return choice;
    }



    public static ProgettoBean inserisciProgetto(){

        Scanner input = new Scanner(System.in);

        ProgettoBean progettoBean = new ProgettoBean();
        Printer.printlnBlu("***** INSERISCI NUOVO PROGETTO *****");

        // Verifica ID progetto
        int id;
        while (true) {
            try {
                Printer.print("ID progetto: ");
                id = input.nextInt();
                if (id > 0) {
                    input.nextLine(); // Consuma il newline
                    break;
                } else {
                    Printer.println("ID progetto deve essere un numero intero positivo.");
                }
            } catch (InputMismatchException e) {
                Printer.println("Input non valido. Inserisci un numero intero positivo.");
                input.next(); // Consuma l'input non valido
            }
        }


        // Verifica Nome progetto
        String nome;
        while (true) {
            Printer.print("Nome progetto: ");
            nome = input.nextLine();
            if (nome != null && !nome.trim().isEmpty()) {
                break;
            } else {
                Printer.println("Il nome del progetto non può essere vuoto.");
            }
        }


        java.sql.Date dataInizioSql;
        java.sql.Date dataScadenzaSql;

        // Ciclo per verificare che la data di inizio sia precedente alla data di scadenza
        while (true) {
            try {
                Printer.print("Data inizio progetto (formato: yyyy-MM-dd): ");
                String dataInizioInput = input.nextLine();
                dataInizioSql = java.sql.Date.valueOf(dataInizioInput);

                Printer.print("Data scadenza progetto (formato: yyyy-MM-dd): ");
                String dataScadenzaInput = input.nextLine();
                dataScadenzaSql = java.sql.Date.valueOf(dataScadenzaInput);

                // Verifica se la data di inizio è precedente alla data di scadenza
                if (dataInizioSql.before(dataScadenzaSql)) {
                    break;  // Le date sono valide, esci dal ciclo
                } else {
                    Printer.errorMessage("Errore: La data di inizio deve essere precedente alla data di scadenza. Riprova.");
                }
            } catch (IllegalArgumentException e) {
                Printer.println("Formato data non valido. Riprova (formato: yyyy-MM-dd).");
            }
        }


        // Imposto i valori nel ProgettoBean
        progettoBean.setIdProgetto(id);
        progettoBean.setNome(nome);
        progettoBean.setDataInizio(dataInizioSql);
        progettoBean.setDataScadenza(dataScadenzaSql);
        progettoBean.setCFcapo(null);

        return progettoBean;
    }




    public static Object[] assegnaCapoProgetto() throws SQLException {

        //devo prima stampare i progetti senza capi, poi faccio scegliere i capi tra  una lista di lavoratori
        Scanner input = new Scanner(System.in);
        List<Progetto> progettiSenzaCapo;

        Printer.printlnBlu("\n***** ASSEGNA CAPO PROGETTO *****\n");
        Printer.println("Lista di progetti senza capo progetto: ");

        AmministratoreController amministratoreController = new AmministratoreController();

        progettiSenzaCapo = amministratoreController.stampaLista();

        // Itera e stampa dei dettagli dei progetti senza capo
        for (Progetto progetto : progettiSenzaCapo) {
            Printer.println("ID Progetto: " + progetto.getId());
            Printer.println("Nome Progetto: " + progetto.getNome());
            Printer.println("Data Inizio: " + progetto.getDataInzio());
            Printer.println("Data Scadenza: " + progetto.getDataScadenza());
            Printer.println("-------------------------------");
        }

        // Chiedi all'utente di scegliere l'ID del progetto
        Printer.print("\nInserisci l'ID del progetto al quale vuoi assegnare un capo progetto: ");
        int idProgettoScelto = input.nextInt();
        input.nextLine();  // Consuma la newline

        // Trova il progetto corrispondente all'ID inserito
        Progetto progettoScelto = null;
        for (Progetto progetto : progettiSenzaCapo) {
            if (progetto.getId() == idProgettoScelto) {
                progettoScelto = progetto;
                break;
            }
        }

        if (progettoScelto == null) {
            Printer.errorMessage("ID progetto non valido. Riprova.");
        }


        // Chiedi all'utente di scegliere il capo progetto
        Printer.println("\nLista dei lavoratori candidati: ");
        List<CapoProgetto> listaCandidati = amministratoreController.stampaCandidatiCapo();


        // Itera e stampa la lista dei CapoProgetto con una numerazione
        int index = 1;
        for (CapoProgetto candidato : listaCandidati) {
            Printer.println(index + ") " + "CF: " + candidato.getCf() + "   Username: " + candidato.getNome());
            index++;
        }


        // Chiedi all'utente di scegliere il numero associato al capo progetto
        Printer.print("\nInserisci il numero associato al capo progetto che vuoi assegnare: ");
        int numeroCandidatoScelto = input.nextInt();
        input.nextLine();  // Consuma la newline

        // Verifica se il numero scelto è valido
        if (numeroCandidatoScelto < 1 || numeroCandidatoScelto > listaCandidati.size()) {
            Printer.errorMessage("Numero non valido. Riprova.");
        }


        // Assegna il capo progetto scelto
        CapoProgetto capoProgettoScelto = listaCandidati.get(numeroCandidatoScelto - 1);


        // Restituisci l'ID del progetto scelto e il CF del capo progetto
        return new Object[] { idProgettoScelto, capoProgettoScelto };
    }



    public void listaConCapo(){

        Printer.printlnBlu("\n***** VISUALIZZA LISTA PROGETTO - CAPO PROGETTO *****\n");
        Printer.println("Lista di progetti con capo progetto assegnato: ");

        AmministratoreController amministratoreController = new AmministratoreController();
        List<Progetto> listProgetti = amministratoreController.ListaConCapo();

        // Itera e stampa dei dettagli dei progetti con capo
        for (Progetto progetto : listProgetti) {
            Printer.println("ID Progetto: " + progetto.getId());
            Printer.println("Nome Progetto: " + progetto.getNome());
            Printer.println("Data Inizio: " + progetto.getDataInzio());
            Printer.println("Data Scadenza: " + progetto.getDataScadenza());
            Printer.println("Capo progetto: " + progetto.getCapoProgetto());
            Printer.println("-------------------------------");
        }


    }


    public void listaSenzaCapo(){
        Printer.printlnBlu("\n***** VISUALIZZA LISTA PROGETTO SENZA CAPO PROGETTO *****\n");
        Printer.println("Lista di progetti senza capo progetto assegnato: ");

        AmministratoreController amministratoreController = new AmministratoreController();
        List<Progetto> listProgetti = amministratoreController.stampaLista();

        // Itera e stampa dei dettagli dei progetti senza capo
        for (Progetto progetto : listProgetti) {
            Printer.println("ID Progetto: " + progetto.getId());
            Printer.println("Nome Progetto: " + progetto.getNome());
            Printer.println("Data Inizio: " + progetto.getDataInzio());
            Printer.println("Data Scadenza: " + progetto.getDataScadenza());
            Printer.println("Capo progetto: " + progetto.getCapoProgetto());
            Printer.println("-------------------------------");
        }


    }




}
