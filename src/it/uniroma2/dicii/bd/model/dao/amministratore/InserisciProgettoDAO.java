package it.uniroma2.dicii.bd.model.dao.amministratore;

import it.uniroma2.dicii.bd.bean.ProgettoBean;
import it.uniroma2.dicii.bd.exception.DAOException;
import it.uniroma2.dicii.bd.model.dao.ConnectionFactory;
import it.uniroma2.dicii.bd.model.domain.Credentials;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class InserisciProgettoDAO{

    public void execute(ProgettoBean progettoBean) throws SQLException {

        // prendo connesione al DB
        Connection conn = ConnectionFactory.getConnection();

        CallableStatement cs = conn.prepareCall("{call inserisci_progetto(?,?,?,?,?)}");
        cs.setInt(1, progettoBean.getIdProgetto());
        cs.setString(2, progettoBean.getNome());
        cs.setDate(3, progettoBean.getDataInizio());
        cs.setDate(4, progettoBean.getDataScadenza());
        cs.setString(5, progettoBean.getCFcapo());

        cs.executeQuery();

    }

}
