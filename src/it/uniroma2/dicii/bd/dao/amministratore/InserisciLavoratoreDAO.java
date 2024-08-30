package it.uniroma2.dicii.bd.dao.amministratore;

import it.uniroma2.dicii.bd.bean.UserBean;
import it.uniroma2.dicii.bd.dao.ConnectionFactory;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class InserisciLavoratoreDAO {

    /* Metodo per inserire il nuovo lavoratore in DB */
    public void inserisciLavoratore(UserBean userBean){

        Connection conn = null;
        CallableStatement cs;

        try{
            conn = ConnectionFactory.getConnection();

            // Prepara la chiamata alla stored procedure
            cs = conn.prepareCall("{call inserisci_lavoratore(?,?,?,?)}");

            // INFO: cf, nome, cognome, ruolo
            cs.setString(1,userBean.getCF());
            cs.setString(2, userBean.getNome());
            cs.setString(3, userBean.getCognome());
            cs.setInt(4, userBean.getRuoloId());

            // Esegui la stored procedure
            cs.execute();


        } catch(SQLException e){
            e.printStackTrace();
        }



    }





}
