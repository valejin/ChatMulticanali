package it.uniroma2.dicii.bd.controller;

import it.uniroma2.dicii.bd.model.domain.Credentials;

public class ApplicationController implements Controller{

    /* è il controller di main, ovvero permette di accedere in applicazione*/
    Credentials cred;

    @Override
    public void start() {
        LoginController loginController = new LoginController();
        loginController.start();
        cred = loginController.getCred();  //ottiene i credenziali dell'utente accesso (CF, password, ruolo)


        if(cred.getRole() == null) {
            throw new RuntimeException("Invalid credentials");
        }


        switch(cred.getRole()) {
            //in base al ruolo, accedo al controller diverso

            case AMMINISTRATORE -> new AmministratoreController().start();
            case CAPOPROGETTO -> new CapoprogettoController().start();
            case DIPENDENTE -> new DipendenteController().start();

            default -> throw new RuntimeException("Invalid credentials");
        }
    }
}
