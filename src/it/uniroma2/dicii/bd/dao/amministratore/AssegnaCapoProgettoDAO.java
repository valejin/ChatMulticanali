package it.uniroma2.dicii.bd.dao.amministratore;
import it.uniroma2.dicii.bd.exception.DAOException;
import it.uniroma2.dicii.bd.model.CapoProgetto;
import it.uniroma2.dicii.bd.model.Progetto;
import it.uniroma2.dicii.bd.dao.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AssegnaCapoProgettoDAO {

    public List<Progetto> listaProgettiSenzaCapo() throws DAOException {

        Connection conn;
        CallableStatement cs;
        ArrayList<Progetto> progettiSenzaCapo = new ArrayList<>();

        try {
            // prendo connesione al DB
            conn = ConnectionFactory.getConnection();

            //recupera lista di progetti senza capo
            cs = conn.prepareCall("{call lista_progetti_senza_capo()}");

            boolean result = cs.execute();


            // Se la stored procedure restituisce un result set
            if (result) {
                ResultSet rs = cs.getResultSet();
                while (rs.next()) {
                    // Creazione di un nuovo oggetto Progetto
                    Progetto progetto = new Progetto();
                    progetto.setId(rs.getInt("id"));
                    progetto.setNome(rs.getString("Nome"));
                    progetto.setDataInzio(rs.getDate("DataInizio"));
                    progetto.setDataScadenza(rs.getDate("DataScadenza"));
                    progetto.setCapoProgetto(rs.getString("CapoProgetto")); // dovrebbe essere null

                    // Aggiungi il progetto alla lista
                    progettiSenzaCapo.add(progetto);
                }
                rs.close();
            }
        }catch(SQLException e){
            throw new DAOException();
        }

        return progettiSenzaCapo;

    }


    public List<CapoProgetto> listaCandidatiCapo() throws SQLException {
        Connection conn = ConnectionFactory.getConnection();

        //recupera lista di capi progetti
        CallableStatement cs = conn.prepareCall("{call lista_capi()}");

        boolean result = cs.execute();
        ArrayList<CapoProgetto> listaCandidati = new ArrayList<>();

        // Se la stored procedure restituisce un result set
        if (result) {
            ResultSet rs = cs.getResultSet();
            while (rs.next()) {
                // Creazione di un nuovo oggetto CapoProgetto
                CapoProgetto capoProgetto = new CapoProgetto();
                capoProgetto.setCf(rs.getString("CF"));
                capoProgetto.setNome(rs.getString("Nome"));
                capoProgetto.setCognome(rs.getString("Cognome"));

                // Aggiungi il capo progetto alla lista
                listaCandidati.add(capoProgetto);

            }
            rs.close();
        }

        return listaCandidati;
    }



    public void assegnaCapoProgetto(int idProgetto, String cfCapo) throws SQLException {
        Connection conn = ConnectionFactory.getConnection();

        CallableStatement cs = conn.prepareCall("{call assegna_capo(?,?)}");

        cs.setInt(1, idProgetto);
        cs.setString(2, cfCapo);

        cs.executeQuery();

    }



}
