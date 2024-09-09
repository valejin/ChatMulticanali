package it.uniroma2.dicii.bd.dao;

import it.uniroma2.dicii.bd.model.Canale;
import it.uniroma2.dicii.bd.utils.Printer;
import it.uniroma2.dicii.bd.utils.UserSession;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VisualizzaAppartenenzaCanaliDAO {

    public static List<Canale> recuperaCanali(){

        List<Canale> canaleList = new ArrayList<>();
        Connection conn;
        CallableStatement cs;
        ResultSet rs;

        try {
            conn = ConnectionFactory.getConnection();
            UserSession session = UserSession.getInstance();

            // Prepara la chiamata alla stored procedure
            cs = conn.prepareCall("{call recupero_tutti_canali_appartenenti(?)}");

            //mettere id progetto
            cs.setString(1, session.getCf());

            // Esegui la stored procedure
            boolean result = cs.execute();

            // risultati
            // Se c'è un result set
            if (result) {
                rs = cs.getResultSet();
                while (rs.next()) {
                    // Crea un nuovo oggetto Canale per ogni riga del result set
                    Canale canale = new Canale();

                    canale.setIdCanale(rs.getInt("IDCanale"));
                    canale.setIdProgetto(rs.getInt("IDProgetto"));
                    canale.setNome(rs.getString("Nome"));
                    canale.setCfCreatore(rs.getString("Creatore"));
                    canale.setData(rs.getDate("DataCreazione"));

                    // Recupera il valore ENUM dal database come stringa
                    String tipoString = rs.getString("Tipo");
                    Canale.Tipo tipo = Canale.Tipo.valueOf(tipoString.toUpperCase()); // Conversione da stringa a ENUM
                    canale.setTipo(tipo);

                    canaleList.add(canale);
                }
                rs.close();
            }

        } catch(SQLException e){
            Printer.errorMessage("Errore nell'recupera canali by Id progetto");
            e.printStackTrace();
        }

        return canaleList;
    }


}

