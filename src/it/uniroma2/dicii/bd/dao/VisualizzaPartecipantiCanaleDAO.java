package it.uniroma2.dicii.bd.dao;

import it.uniroma2.dicii.bd.model.Lavoratore;
import it.uniroma2.dicii.bd.model.domain.Role;
import it.uniroma2.dicii.bd.utils.Printer;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VisualizzaPartecipantiCanaleDAO {

    public List<Lavoratore> recuperoLavoratoriByCanale(int idCanale, int idProgetto){

        List<Lavoratore> lavoratoreList = new ArrayList<>();
        Connection conn;
        CallableStatement cs;
        ResultSet rs;

        try {
            conn = ConnectionFactory.getConnection();

            // Prepara la chiamata alla stored procedure
            cs = conn.prepareCall("{call recupero_lavoratori_by_IdCanale(?,?)}");

            cs.setInt(1, idCanale);  //passo id canale scelto da capo
            cs.setInt(2, idProgetto);  // passo id progetto scelto da capo

            // Esegui la stored procedure
            boolean result = cs.execute();

            // Se c'è un result set
            if (result) {
                rs = cs.getResultSet();
                while (rs.next()) {
                    // Crea un nuovo oggetto Lavoratore per ogni riga del result set
                    Lavoratore lavoratore = new Lavoratore();

                    lavoratore.setCfLavoratore(rs.getString("L.CF"));
                    lavoratore.setNomeLavoratore(rs.getString("L.Nome"));
                    lavoratore.setCognomeLavoratore(rs.getString("L.Cognome"));
                    // Recupera il valore del ruolo come stringa
                    String ruoloString = rs.getString("L.ruolo").toUpperCase();

                    // Converti la stringa in un enum Role
                    Role ruolo = Role.valueOf(ruoloString);

                    // Imposta il ruolo nel lavoratore
                    lavoratore.setRuolo(ruolo.getId());  // Se 'ruolo' è int nel modello Lavoratore

                    lavoratoreList.add(lavoratore);
                }
                rs.close();
            }


        }catch(SQLException e){
            e.printStackTrace();
            Printer.errorMessage("Errore recupero lavoratore by id progetto in DAO");
        }

        return lavoratoreList;

    }



}
