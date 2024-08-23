package it.uniroma2.dicii.bd.model.dao.capoprogetto;

import it.uniroma2.dicii.bd.exception.DAOException;
import it.uniroma2.dicii.bd.model.Messaggio;
import it.uniroma2.dicii.bd.model.dao.ConnectionFactory;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VisualizzaConversazioneDAO {

    /* Metodo per recuperare i messaggi di un canale (di un progetto) scelto da utente */
    public static List<Messaggio> recuperoConversazione(int idCanale, int idProgetto) throws DAOException {

        Connection conn;
        CallableStatement cs;
        ResultSet rs;
        List<Messaggio> conversazioni = new ArrayList<>();  // Inizializza la lista

        try {
            conn = ConnectionFactory.getConnection();

            // Prepara la chiamata alla stored procedure
            cs = conn.prepareCall("{call conversazione_by_idCanale_idProgetto(?,?)}");

            // Mettere id canale e id progetto
            cs.setInt(1, idCanale);
            cs.setInt(2, idProgetto);

            // Esegui la stored procedure
            boolean result = cs.execute();

            // Se c'è un result set
            if (result) {
                rs = cs.getResultSet();
                while (rs.next()) {

                    Messaggio messaggio = new Messaggio();

                    messaggio.setNomeUtente(rs.getString("Nome"));
                    messaggio.setCognomeUtente(rs.getString("Cognome"));
                    messaggio.setDataInvio(rs.getDate("DataInvio"));
                    messaggio.setOrarioInvio(rs.getTime("OrarioInvio"));
                    messaggio.setContenuto(rs.getString("Contenuto"));

                    conversazioni.add(messaggio);

                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Errore recupero conversazione da DB");

        }
        return conversazioni;
    }



}
