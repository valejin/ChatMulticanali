package it.uniroma2.dicii.bd.view;

import it.uniroma2.dicii.bd.model.domain.Credentials;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LoginView {
    public static Credentials authenticate() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("CF: ");
        String username = reader.readLine();
        System.out.println("valore CF inserito: " + username);
        System.out.print("password: ");
        String password = reader.readLine();
        System.out.println("valore password inserito: " + password);

        return new Credentials(username, password, null);
    }
}
