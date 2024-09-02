package it.uniroma2.dicii.bd.controller;

import it.uniroma2.dicii.bd.utils.UserSession;

public class ApplicationController implements Controller{

    /* è il controller di main, ovvero permette di accedere in applicazione*/

    @Override
    public void start() {
        LoginController loginController = new LoginController();
        loginController.start();

        // Ottieni l'istanza di UserSession per accedere alle credenziali dell'utente
        UserSession session = UserSession.getInstance();

        // Verifica che l'utente sia loggato e abbia un ruolo valido
        if (!session.isLoggedIn() || session.getRole() == null) {
            throw new RuntimeException("Invalid credentials");
        }




        // Switch sul ruolo per avviare il controller appropriato
        switch(session.getRole()) {
            //in base al ruolo, accedo al controller diverso

            case AMMINISTRATORE -> new AmministratoreController().start();
            case CAPOPROGETTO -> new CapoprogettoController().start();
            case DIPENDENTE -> new DipendenteController().start();

            default -> throw new RuntimeException("Invalid role");
        }
    }
}
