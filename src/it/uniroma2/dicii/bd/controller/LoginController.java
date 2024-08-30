package it.uniroma2.dicii.bd.controller;

import it.uniroma2.dicii.bd.exception.DAOException;
import it.uniroma2.dicii.bd.dao.LoginProcedureDAO;
import it.uniroma2.dicii.bd.model.domain.Credentials;
import it.uniroma2.dicii.bd.utils.UserSession;
import it.uniroma2.dicii.bd.view.LoginView;

import java.io.IOException;

public class LoginController implements Controller {
    Credentials cred = null;

    @Override
    public void start() {
        try {
            cred = LoginView.authenticate();    //controller chiama view per prendere i dati
        } catch(IOException e) {
            throw new RuntimeException(e);
        }

        try {
            // per ottenere i credenziali con il ruolo restituito, controller chiama DAO per ottenere ruolo
            cred = new LoginProcedureDAO().execute(cred.getCF(), cred.getPassword());


            // Se l'autenticazione ha successo, salva i dati dell'utente nella sessione
            UserSession session = UserSession.getInstance();
            session.login(cred);

        } catch(DAOException e) {
            throw new RuntimeException(e);
        }
    }

    public Credentials getCred() {
        return cred;
    }  //contiene i credenziali: CF, password, ruolo
}

