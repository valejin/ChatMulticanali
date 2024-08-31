package it.uniroma2.dicii.bd.dao;

import it.uniroma2.dicii.bd.exception.DAOException;
import it.uniroma2.dicii.bd.model.Canale;
import it.uniroma2.dicii.bd.model.Lavoratore;
import it.uniroma2.dicii.bd.model.domain.Role;
import it.uniroma2.dicii.bd.utils.UserSession;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AssegnaProgettoDAO {


    /* Metodo per recuperare tutti i lavoratori di azienda */
    public List<Lavoratore> recuperoTuttiLavoratori(){

        List<Lavoratore> lavoratoreList = new ArrayList<>();
        Connection conn;
        CallableStatement cs;
        ResultSet rs;

        try {
            conn = ConnectionFactory.getConnection();

            // Prepara la chiamata alla stored procedure
            cs = conn.prepareCall("{call recupero_tutti_lavoratori()}");

            // Esegui la stored procedure
            boolean result = cs.execute();

            // Se c'è un result set
            if (result) {
                rs = cs.getResultSet();
                while (rs.next()) {
                    // Crea un nuovo oggetto Lavoratore per ogni riga del result set
                    Lavoratore lavoratore = new Lavoratore();

                    lavoratore.setCfLavoratore(rs.getString("CF"));
                    lavoratore.setNomeLavoratore(rs.getString("Nome"));
                    lavoratore.setCognomeLavoratore(rs.getString("Cognome"));
                    // Recupera il valore del ruolo come stringa
                    String ruoloString = rs.getString("ruolo").toUpperCase();

                    // Converti la stringa in un enum Role
                    Role ruolo = Role.valueOf(ruoloString);

                    // Imposta il ruolo nel lavoratore
                    lavoratore.setRuolo(ruolo.getId());  // Se 'ruolo' è int nel modello Lavoratore


                    lavoratoreList.add(lavoratore);
                }
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return lavoratoreList;
    }



    /* Metodo per assegnare il progetto scelto al lavoratore scelto */
    public void assegnaProgetto(Lavoratore lavoratore, int idProgetto)throws DAOException {

        Connection conn;
        CallableStatement cs;

        try{
            conn = ConnectionFactory.getConnection();
            cs = conn.prepareCall("{call assegnazione_progetto(?,?)}");

            String cflavoratore = lavoratore.getCfLavoratore();

            cs.setString(1, cflavoratore);
            cs.setInt(2, idProgetto);

            // Esegui la stored procedure
            cs.execute();

        }catch (SQLException e){
            throw new DAOException();
        }



    }



}






