package it.uniroma2.dicii.bd.utils;

public class Printer {

    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_RESET1 = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";

    public static void print(String string){System.out.print(string);}
    public static void println(String string){System.out.println(string);}


    //stampa in rosso ERRORE
    public static void errorMessage(String message){System.out.println(ANSI_RED + message + ANSI_RESET1);}

    //stampa in blu MENU
    public static void printlnBlu(String message) {
        System.out.println(ANSI_BLUE + message + ANSI_RESET1);
    }

}
