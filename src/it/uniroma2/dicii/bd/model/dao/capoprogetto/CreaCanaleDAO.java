package it.uniroma2.dicii.bd.model.dao.capoprogetto;

import it.uniroma2.dicii.bd.bean.CanaleBean;
import it.uniroma2.dicii.bd.model.Progetto;
import it.uniroma2.dicii.bd.model.dao.ConnectionFactory;
import it.uniroma2.dicii.bd.utils.Printer;
import it.uniroma2.dicii.bd.utils.UserSession;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CreaCanaleDAO {

    public List<Progetto> recuperoProgettiByCf() throws SQLException {

        List<Progetto> progettiTarget = new ArrayList<>();
        Connection conn = null;
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            conn = ConnectionFactory.getConnection();
            UserSession session = UserSession.getInstance();

            // Prepara la chiamata alla stored procedure
            cs = conn.prepareCall("{call lista_progetti_coordinato(?)}");
            cs.setString(1, session.getCf());

            // Esegui la stored procedure
            boolean result = cs.execute();

            // Se c'è un result set
            if (result) {
                rs = cs.getResultSet();
                while (rs.next()) {
                    // Creazione di un nuovo oggetto Progetto
                    Progetto progetto = new Progetto();
                    progetto.setId(rs.getInt("id"));
                    progetto.setNome(rs.getString("Nome"));
                    progetto.setDataInzio(rs.getDate("DataInizio"));
                    progetto.setDataScadenza(rs.getDate("DataScadenza"));


                    // Aggiungi il progetto alla lista
                    progettiTarget.add(progetto);
                }
            }
        } catch (SQLException e) {
            Printer.errorMessage("Errore SQL: " + e.getMessage());
        }

        return progettiTarget;
    }




    /* Metodo per inserire il nuovo canale compilato dal capo in DB */
    public void inserireNuovoCanale(CanaleBean canaleBean) throws SQLException {

        Connection conn = ConnectionFactory.getConnection();
        UserSession userSession = UserSession.getInstance();

        CallableStatement cs = conn.prepareCall("{call inserisci_nuovo_canale(?,?,?,?,?)}");

        cs.setInt(1, canaleBean.getIdProgetto());
        cs.setString(2, canaleBean.getNome());
        cs.setString(3, userSession.getCf());
        cs.setInt(4, canaleBean.getTipoId());
        cs.setDate(5,canaleBean.getData());

        // Esegui la stored procedure
        cs.execute();



    }



}
