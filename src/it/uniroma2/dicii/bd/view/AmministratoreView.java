package it.uniroma2.dicii.bd.view;

import it.uniroma2.dicii.bd.bean.ProgettoBean;
import it.uniroma2.dicii.bd.utils.Printer;


import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

public class AmministratoreView {

    public static int stampaMenu() throws IOException {
        Printer.println("\n************************************");
        Printer.printlnBlu("*    Benvenuto a ChatMulticanale   *");
        Printer.println("************************************\n");
        Printer.printlnBlu("------- OPERAZIONI PRINCIPALI ------\n");
        Printer.println("1) Inserisci progetto");
        Printer.println("2) Assegna capo progetto");
        Printer.println("3) Stampa lista: progetti - capo progetto");
        Printer.println("0) Quit");


        Scanner input = new Scanner(System.in);
        int choice = -1;
        while (true) {
            try {
                Printer.print("Please enter your choice: ");
                choice = input.nextInt();
                if (choice >= 0 && choice <= 3) {
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
        Printer.printlnBlu("*****INSERISCI NUOVO PROGETTO*****");

        // Verifica ID progetto
        int id = -1;
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
        String nome = "";
        while (true) {
            Printer.print("Nome progetto: ");
            nome = input.nextLine();
            if (nome != null && !nome.trim().isEmpty()) {
                break;
            } else {
                Printer.println("Il nome del progetto non può essere vuoto.");
            }
        }


        java.sql.Date dataInizioSql = null;
        java.sql.Date dataScadenzaSql = null;

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







}
