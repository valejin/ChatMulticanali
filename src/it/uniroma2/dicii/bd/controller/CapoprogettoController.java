package it.uniroma2.dicii.bd.controller;

import it.uniroma2.dicii.bd.bean.CanaleBean;
import it.uniroma2.dicii.bd.model.Progetto;
import it.uniroma2.dicii.bd.model.dao.ConnectionFactory;
import it.uniroma2.dicii.bd.model.dao.capoprogetto.CreaCanaleDAO;
import it.uniroma2.dicii.bd.model.domain.Role;
import it.uniroma2.dicii.bd.utils.Printer;
import it.uniroma2.dicii.bd.view.CapoprogettoView;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class CapoprogettoController implements Controller{

    @Override
    public void start() {
        //we connect to the database with the role CapoProgetto
        try {
            ConnectionFactory.changeRole(Role.CAPOPROGETTO);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        CapoprogettoView.stampaTitolo();

        while(true) {
            int choice;
            try {
                choice = CapoprogettoView.stampaMenu();
            } catch(IOException e) {
                throw new RuntimeException(e);
            }

            switch(choice) {
                case 1 -> creaCanale();
                //case 2 -> assegnaCapoprogetto();
                //case 3 -> stampaLista();
                case 0 -> System.exit(0);
                default -> throw new RuntimeException("Invalid choice");
            }
        }

    }


    /* Metodo per creare il nuovo canale, chiamato da switch case 1 */
    public void creaCanale(){

        // Uso CanaleBean per passare gli input del utente
        CanaleBean canaleBean = CapoprogettoView.creaNuovoCanale();

        // Adesso devo inserire questo canaleBean in DB
        CreaCanaleDAO creaCanaleDAO = new CreaCanaleDAO();


        try{
            creaCanaleDAO.inserireNuovoCanale(canaleBean);
            Printer.println("Canale creato con successo!");
        } catch(SQLException e){
            Printer.errorMessage("Errore all'inserimento di nuovo canale da Capo progetto");
            e.printStackTrace();  // Per debug, stampa lo stack trace
            throw new RuntimeException("Errore durante la creazione del canale: " + e.getMessage(), e);
        }




    }




    /* Metodo per recuperare lista di progetti con cf di capoprogetto dato da DB */
    public static List<Progetto> recuperoProgetti(){

        CreaCanaleDAO creaCanaleDAO = new CreaCanaleDAO();
        List<Progetto> progettiTarget = null;


        try {
            progettiTarget = creaCanaleDAO.recuperoProgettiByCf();

        } catch(SQLException e){
            RuntimeException exception;
        }


        return progettiTarget;
    }



}
