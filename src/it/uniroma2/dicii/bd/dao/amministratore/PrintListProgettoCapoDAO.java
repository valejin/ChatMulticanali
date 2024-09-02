package it.uniroma2.dicii.bd.dao.amministratore;

import it.uniroma2.dicii.bd.exception.DAOException;
import it.uniroma2.dicii.bd.model.Progetto;
import it.uniroma2.dicii.bd.dao.ConnectionFactory;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PrintListProgettoCapoDAO {

    public static List<Progetto> listaProgettiConCapo() throws DAOException{

        ArrayList<Progetto> progettiConCapo = new ArrayList<>();

        try {
            // connesione al DB
            Connection conn = ConnectionFactory.getConnection();
            //recupera lista di progetti senza capo
            CallableStatement cs = conn.prepareCall("{call lista_progetti_con_capo()}");

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
                    progetto.setCapoProgetto(rs.getString("CapoProgetto"));

                    // Aggiungi il progetto alla lista
                    progettiConCapo.add(progetto);

                }
                rs.close();
            }

        } catch(SQLException e){
            throw new DAOException();
        }

        return progettiConCapo;
    }





}
