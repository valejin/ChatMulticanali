package it.uniroma2.dicii.bd.view;

import it.uniroma2.dicii.bd.bean.CanaleBean;
import it.uniroma2.dicii.bd.controller.CapoprogettoController;
import it.uniroma2.dicii.bd.model.CapoProgetto;
import it.uniroma2.dicii.bd.model.Progetto;
import it.uniroma2.dicii.bd.utils.Printer;
import it.uniroma2.dicii.bd.utils.UserSession;

import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.Scanner;

public class CapoprogettoView {

    public static void stampaTitolo(){
        Printer.println("\n************************************");
        Printer.printlnBlu("*    Benvenuto a ChatMulticanale   *");
        Printer.println("************************************\n");
    }



    public static int stampaMenu() throws IOException {

        Printer.printlnBlu("------- SCEGLI TRA OPERAZIONI ------\n");
        Printer.println("1) Crea nuovo canale");
        Printer.println("2) Visualizza conversazione");
        Printer.println("3) Inserisci messaggio");
        Printer.println("0) Quit");


        Scanner input = new Scanner(System.in);
        int choice = -1;
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

        Printer.println("Data di creazione (Inserire giorno odierno YYYY-MM-DD): ");
        Date giornoCreazione = Date.valueOf(input.next());

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














}
