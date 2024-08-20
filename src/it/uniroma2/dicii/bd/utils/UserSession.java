package it.uniroma2.dicii.bd.utils;

import it.uniroma2.dicii.bd.model.domain.Credentials;
import it.uniroma2.dicii.bd.model.domain.Role;

public class UserSession {

    // gestendo lo stato dell'utente loggato come un Singleton

    private static UserSession instance;

    private Credentials credentials;
    // in credentials contiene: CF, password, ruolo, username (password non è necessario mantenerla)

    private boolean loggedIn;

    private UserSession() {
        this.loggedIn = false;
    }

    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }


    public void login(Credentials credentials) {
        this.credentials = credentials;
        this.loggedIn = true;
    }


    public void logout() {
        this.credentials = null;
        this.loggedIn = false;
    }

    public String getUsername() {
        return credentials != null ? credentials.getUsername() : null;
    }

    public Role getRole() {
        return credentials != null ? credentials.getRole() : null;
    }

    public String getCf() {
        return credentials != null ? credentials.getCF() : null;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }



}
