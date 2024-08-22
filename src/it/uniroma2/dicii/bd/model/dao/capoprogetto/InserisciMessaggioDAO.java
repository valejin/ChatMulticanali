package it.uniroma2.dicii.bd.model.dao.capoprogetto;

import it.uniroma2.dicii.bd.model.dao.ConnectionFactory;
import it.uniroma2.dicii.bd.model.Canale;
import it.uniroma2.dicii.bd.utils.Printer;
import it.uniroma2.dicii.bd.utils.UserSession;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class InserisciMessaggioDAO {


    /* Metodo per inserire un nuovo messaggio in un canale */
    public static void inserisciMessaggio(String contenutoMessaggio, int idCanale, int idProgetto){

        Connection conn = null;
        CallableStatement cs = null;
        UserSession userSession = UserSession.getInstance();

        try{
            conn = ConnectionFactory.getConnection();
            cs = conn.prepareCall("{call inserisci_messaggio(?,?,?,?,?,?)}");

            // Ottengo la data e l'ora corrente (omettendo i nanosecondi)
            LocalDate currentDate = LocalDate.now();
            LocalTime currentTime = LocalTime.now();
            LocalTime truncatedTime = currentTime.truncatedTo(ChronoUnit.SECONDS);

            cs.setString(1, userSession.getCf());
            cs.setDate(2, Date.valueOf(currentDate));  // Imposta la data
            cs.setTime(3, Time.valueOf(truncatedTime)); // Imposta l'orario
            cs.setString(4, contenutoMessaggio);
            cs.setInt(5, idCanale);
            cs.setInt(6, idProgetto);

            // Esegui la stored procedure
            cs.execute();


        } catch(SQLException e){
            Printer.errorMessage("Errore all'inserimento messaggio DAO");
            e.printStackTrace();
        }



    }



    /* Metodo per recuperare lista di canali con id progetto fornito e CF del utente */
    public static List<Canale> recuperaCanaliByIdProgetto(int idProgetto){

        List<Canale> canaleList = new ArrayList<>();
        Connection conn = null;
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            conn = ConnectionFactory.getConnection();
            UserSession session = UserSession.getInstance();

            // Prepara la chiamata alla stored procedure
            cs = conn.prepareCall("{call lista_canali_by_IDprogetto(?,?)}");

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
                    //canale.setTipoById(rs.getInt("Tipo"));
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
