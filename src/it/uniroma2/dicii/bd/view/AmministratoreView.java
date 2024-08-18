package it.uniroma2.dicii.bd.view;

import it.uniroma2.dicii.bd.utils.Printer;

import java.io.IOException;
import java.util.Scanner;

public class AmministratoreView {

    public static int stampaMenu() throws IOException {
        Printer.println("************************************");
        Printer.println("*    Benvenuto a ChatMulticanale   *");
        Printer.println("************************************\n");
        Printer.println("------- OPERAZIONI PRINCIPALI ------\n");
        Printer.println("1) Inserisci progetto");
        Printer.println("2) Assegna capo progetto");
        Printer.println("3) Stampa lista: progetti - capo progetto");
        Printer.println("0) Quit");


        Scanner input = new Scanner(System.in);
        int choice = -1;
        while (true) {
            Printer.print("Please enter your choice: ");
            choice = input.nextInt();
            if (choice >= 0 && choice <= 3) {
                break;
            }
            Printer.println("Invalid option");
        }

        return choice;
    }
}
