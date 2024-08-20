package it.uniroma2.dicii.bd.view;

import it.uniroma2.dicii.bd.model.domain.Credentials;
import it.uniroma2.dicii.bd.utils.Printer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LoginView {
    public static Credentials authenticate() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Printer.print("CF: ");
        String cf = reader.readLine();
        Printer.println("valore CF inserito: " + cf);
        Printer.print("password: ");
        String password = reader.readLine();
        Printer.println("valore password inserito: " + password);

        return new Credentials(cf, password, null, null);
    }
}
