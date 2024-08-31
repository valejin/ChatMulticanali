package it.uniroma2.dicii.bd.dao.capoprogetto;

import it.uniroma2.dicii.bd.exception.DAOException;
import it.uniroma2.dicii.bd.model.Canale;
import it.uniroma2.dicii.bd.model.Lavoratore;
import it.uniroma2.dicii.bd.dao.ConnectionFactory;
import it.uniroma2.dicii.bd.model.domain.Role;
import it.uniroma2.dicii.bd.utils.Printer;
import it.uniroma2.dicii.bd.utils.UserSession;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AssegnaCanaleDAO {

    public List<Lavoratore> recuperoLavoratoriByIdProgetto(int idProgetto){

        List<Lavoratore> lavoratoreList = new ArrayList<>();
        Connection conn;
        CallableStatement cs;
        ResultSet rs;

        try{
            conn = ConnectionFactory.getConnection();

            // Prepara la chiamata alla stored procedure
            cs = conn.prepareCall("{call recupero_lavoratori_by_IdProgetto(?)}");

            cs.setInt(1, idProgetto);  // passo id progetto scelto da capo

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
            }

        } catch(SQLException e){
            Printer.errorMessage("Errore recupero lavoratore by id progetto in DAO");
            e.printStackTrace();
        }
        return lavoratoreList;
    }



    /* Metodo per salvare assegnazione del canale al lavoratore */
    public void salvaAssegnazioneCanale(Lavoratore lavoratore, int idCanale, int idProgetto) throws DAOException {

        Connection conn;
        CallableStatement cs;

        try{
            conn = ConnectionFactory.getConnection();
            cs = conn.prepareCall("{call assegnazione_canale(?,?,?)}");

            String cflavoratore = lavoratore.getCfLavoratore();

            cs.setString(1, cflavoratore);
            cs.setInt(2, idCanale);
            cs.setInt(3, idProgetto);

            // Esegui la stored procedure
            cs.execute();

        }catch (SQLException e){
            throw new DAOException();
        }
        Printer.printlnViola("Assegnazione avvenuta con successo!");
    }



    /* Metodo per recuperare la lista dei canali pubblici a cui capo fa parte */
    public List<Canale> recuperoCanaliPubblici(int idProgetto){

        List<Canale> canaleList = new ArrayList<>();
        Connection conn;
        CallableStatement cs;
        ResultSet rs;

        try {
            conn = ConnectionFactory.getConnection();
            UserSession session = UserSession.getInstance();

            // Prepara la chiamata alla stored procedure
            cs = conn.prepareCall("{call lista_canali_public_by_IDprogetto(?,?)}");

            //mettere id progetto
            cs.setString(1, session.getCf());
            cs.setInt(2, idProgetto);

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
                    canale.setNome(rs.getString("Nome"));
                    canale.setCfCreatore(rs.getString("Creatore"));
                    canale.setData(rs.getDate("DataCreazione"));

                    // Recupera il valore ENUM dal database come stringa
                    String tipoString = rs.getString("Tipo");
                    Canale.Tipo tipo = Canale.Tipo.valueOf(tipoString.toUpperCase()); // Conversione da stringa a ENUM
                    canale.setTipo(tipo);

                    canaleList.add(canale);
                }
            }

        } catch(SQLException e){
            Printer.errorMessage("Errore nell'recupera canali by Id progetto");
            e.printStackTrace();
        }
        return canaleList;
    }




}
