package it.uniroma2.dicii.bd.controller;

import it.uniroma2.dicii.bd.model.dao.ConnectionFactory;
import it.uniroma2.dicii.bd.model.domain.Role;
import it.uniroma2.dicii.bd.view.AmministratoreView;

import java.io.IOException;
import java.sql.SQLException;

public class AmministratoreController implements Controller {

    @Override
    public void start() {
        //we connect to the database with the role AMMINISTRATORE
        try {
            ConnectionFactory.changeRole(Role.AMMINISTRATORE);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        while(true) {
            int choice;
            try {
                choice = AmministratoreView.stampaMenu();
            } catch(IOException e) {
                throw new RuntimeException(e);
            }

            switch(choice) {
                //case 1 -> inserisciProgetto();
                //case 2 -> assegnaCapoprogetto();
                //case 3 -> stampaLista();
                case 0 -> System.exit(0);
                default -> throw new RuntimeException("Invalid choice");
            }
        }


    }
}
