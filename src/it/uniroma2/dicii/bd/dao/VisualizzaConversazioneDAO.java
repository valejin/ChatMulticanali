package it.uniroma2.dicii.bd.dao;

import it.uniroma2.dicii.bd.exception.DAOException;
import it.uniroma2.dicii.bd.model.Canale;
import it.uniroma2.dicii.bd.model.Messaggio;
import it.uniroma2.dicii.bd.utils.Printer;
import it.uniroma2.dicii.bd.utils.UserSession;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
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

                    messaggio.setCfUtente(rs.getString("m.CF"));
                    messaggio.setNomeUtente(rs.getString("l.Nome"));
                    messaggio.setCognomeUtente(rs.getString("l.Cognome"));
                    messaggio.setDataInvio(rs.getDate("m.DataInvio"));
                    messaggio.setOrarioInvio(rs.getTime("m.OrarioInvio"));
                    messaggio.setContenuto(rs.getString("m.Contenuto"));
                    messaggio.setIsVisible(rs.getInt("r.Visibilita"));

                    // Controlla se il messaggio è una risposta
                    String cfRispondente = rs.getString("r.Rispondente_CF"); // Modifica in base al nome del campo
                    if (cfRispondente != null && !cfRispondente.isEmpty()) {
                        messaggio.setRisposta(true);

                        // Recupera il messaggio originale a cui si sta rispondendo
                        Messaggio messaggioOriginale = new Messaggio();
                        messaggioOriginale.setCfUtente(rs.getString("r.Mittente_CF"));
                        messaggioOriginale.setDataInvio(rs.getDate("r.DataInvioMittente"));
                        messaggioOriginale.setOrarioInvio(rs.getTime("r.OrarioInvioMittente"));

                        messaggio.setMessaggioOriginale(messaggioOriginale);

                    }
                    conversazioni.add(messaggio);
                }
                rs.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Errore recupero conversazione da DB");

        }
        return conversazioni;
    }




    /* Metodo per recuperare il messaggio originario da DB a partire da cfRispondente,
    dataInvioRispondente, orarioInvioRispondente */
    public Messaggio recuperoMessaggioOriginale(String cfRispondente, Date dataInvioRispondente, Time orarioInvioRispondente) throws DAOException{

        Connection conn;
        CallableStatement cs;
        ResultSet rs;
        Messaggio messaggioOriginario = new Messaggio();

        try{
            conn = ConnectionFactory.getConnection();

            // Prepara la chiamata alla stored procedure
            cs = conn.prepareCall("{call recupero_messaggio_originario(?,?,?)}");

            // Mettere id canale e id progetto
            cs.setString(1, cfRispondente);
            cs.setDate(2, dataInvioRispondente);
            cs.setTime(3, orarioInvioRispondente);

            // Esegui la stored procedure
            boolean result = cs.execute();

            // Se c'è un result set
            if (result) {
                rs = cs.getResultSet();

                rs.next();
                // popolo il messaggio originale
                messaggioOriginario.setCfUtente(rs.getString("Mittente_CF"));
                messaggioOriginario.setDataInvio(rs.getDate("DataInvioMittente"));
                messaggioOriginario.setOrarioInvio(rs.getTime("OrarioInvioMittente"));
                messaggioOriginario.setContenuto(rs.getString("Contenuto"));  //messaggio originario
                messaggioOriginario.setNomeUtente(rs.getString("Nome"));
                messaggioOriginario.setCognomeUtente(rs.getString("Cognome"));

            }

        }catch(SQLException e){
            e.printStackTrace();
        }
        return messaggioOriginario;
    }









    /* Metodo per memorizzare i dati della risposta di utente relativa a un messaggio in tabella risposta
    * e salvare la risposta come un messaggio */
    public static void inserisciRisposta(Messaggio messaggioOriginale, String contenutoRisposta, int visible) throws DAOException{

        Connection conn;
        CallableStatement cs;

        // Ottengo data e ora attuale
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        LocalTime truncatedTime = currentTime.truncatedTo(ChronoUnit.SECONDS);


        // Per inserire il messaggio risposta in tabella Messaggio insieme con il contenuto
        try{
            conn = ConnectionFactory.getConnection();
            UserSession session = UserSession.getInstance();

            // Prepara la chiamata alla stored procedure
            cs = conn.prepareCall("{call inserisci_messaggio(?,?,?,?,?,?)}");

            cs.setString(1, session.getCf());
            cs.setDate(2, Date.valueOf(currentDate));
            cs.setTime(3, Time.valueOf(truncatedTime));
            cs.setString(4, contenutoRisposta);
            cs.setInt(5, messaggioOriginale.getIdCanale());
            cs.setInt(6, messaggioOriginale.getIdProgetto());

            // Esegui la stored procedure
            cs.execute();

        } catch(SQLException s){
            s.printStackTrace();
            throw new DAOException("Errore l'inserimento messaggio risposta in DAO nella tabella Messaggio");

        }


        try {
            conn = ConnectionFactory.getConnection();
            UserSession session = UserSession.getInstance();


            // Prepara la chiamata alla stored procedure per inserimento in tabella risposta
            cs = conn.prepareCall("{call inserisci_risposta(?,?,?,?,?,?,?)}");

            // Mettere: CFMittente, DataInvio, OrarioInvio, visibilità, CFRispondente, DataInvioRispondente, OrarioInvioRispondente
            cs.setString(1, messaggioOriginale.getCfUtente());
            cs.setDate(2, messaggioOriginale.getDataInvio());
            cs.setTime(3, messaggioOriginale.getOrarioInvio());

            if(visible == 0) {
                cs.setInt(4, 0);  // visibilità: public=0 private=1
            }else if (visible == 1){
                cs.setInt(4, 1);  // visibilità: public=0 private=1
            }
            cs.setString(5, session.getCf());  //cf di rispondente
            cs.setDate(6, Date.valueOf(currentDate));
            cs.setTime(7, Time.valueOf(truncatedTime));

            // Esegui la stored procedure
            cs.execute();

        } catch(SQLException e){
            e.printStackTrace();
            throw new DAOException("Errore l'inserimento risposta in DAO nella tabella Risposta");
        }
    }



    /* Metodo per inserire una risposta privata in DB, creando un nuovo canale privato*/
    public void inserisciRispostaPrivata(Messaggio messaggioOriginale, String contenutoRisposta) throws DAOException{

        Connection conn;
        CallableStatement cs;

        // Ottengo data e ora attuale
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        LocalTime truncatedTime = currentTime.truncatedTo(ChronoUnit.SECONDS);

        try{
            conn = ConnectionFactory.getConnection();
            UserSession session = UserSession.getInstance();

            // Prepara la chiamata alla stored procedure
            cs = conn.prepareCall("{call inserisci_risposta_privata(?,?,?,?,?,?,?,?,?)}");

            //in tabella Risposta: mittenteCF, dataInvioM, orarioInvioM, visibilità, rispondenteCF, dataInvioR, orarioInvioR
            //in messaggio: CFmittente, dataInvio, orarioInvio, contenuto, idCanale, id Progetto

            // info per inserire in messaggio
            cs.setString(1, session.getCf());  //rispondenteCF
            cs.setDate(2, Date.valueOf(currentDate));  //dataInvioR
            cs.setTime(3, Time.valueOf(truncatedTime));  //orarioInvioR
            cs.setString(4, contenutoRisposta);  //contenutoRispostaPrivata
            cs.setInt(5, messaggioOriginale.getIdCanale());  //messaggio originario id canale
            cs.setInt(6, messaggioOriginale.getIdProgetto());  //messaggio originario id progetto

            // info per inserire in risposta
            cs.setString(7, messaggioOriginale.getCfUtente());  //CF mittente messaggio originario
            cs.setDate(8, messaggioOriginale.getDataInvio());  //dataInvio messaggio originario
            cs.setTime(9, messaggioOriginale.getOrarioInvio());  //orarioInvio messaggio originario
            //cs.setString(10, messaggioOriginale.getContenuto());  //contenuto messaggio originario
            // visibilità verrà settato in SQL

            // Esegui la stored procedure
            cs.execute();

        } catch(SQLException e){
            e.printStackTrace();
            throw new DAOException("Errore in DAO");
        }

    }




    /* Metodo per recuperare i canali privati, in cui capoProgetto non appartiene */
    public List<Canale> recuperoCanaliPrivati(int idProgetto){

        Connection conn;
        CallableStatement cs;
        ResultSet rs;
        List<Canale> canaleList = new ArrayList<>();

        try {
            conn = ConnectionFactory.getConnection();
            UserSession session = UserSession.getInstance();

            // Prepara la chiamata alla stored procedure
            cs = conn.prepareCall("{call lista_canali_privati_by_IDprogetto(?,?)}");

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
