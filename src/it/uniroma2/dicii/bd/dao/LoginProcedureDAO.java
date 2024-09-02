package it.uniroma2.dicii.bd.dao;

import it.uniroma2.dicii.bd.exception.DAOException;
import it.uniroma2.dicii.bd.exception.LoginException;
import it.uniroma2.dicii.bd.model.domain.Credentials;
import it.uniroma2.dicii.bd.model.domain.Role;
import it.uniroma2.dicii.bd.utils.Printer;

import java.sql.*;

public class LoginProcedureDAO implements GenericProcedureDAO<Credentials> {

    @Override
    public Credentials execute(Object... params) throws LoginException {
        String cf = (String) params[0];
        String password = (String) params[1];
        int role;
        String userName;

        try {
            //Connessione a DB
            Connection conn = ConnectionFactory.getConnection();

            //Viene preparato un CallableStatement per chiamare la stored procedure: login
            CallableStatement cs = conn.prepareCall("{call login(?,?,?,?)}");

            //Setto i parametri della stored procedure
            cs.setString(1, cf);
            cs.setString(2, password);

            /* Viene utilizzato per registrare il terzo parametro come un parametro di uscita
             (un valore che la stored procedure restituirà), di tipo numerico */
            cs.registerOutParameter(3, Types.NUMERIC);

            cs.registerOutParameter(4, Types.VARCHAR);

            //La stored procedure viene eseguita
            cs.executeQuery();

            //Il ruolo dell'utente viene recuperato dal terzo parametro di output
            role = cs.getInt(3);  //restituisce un intero che è mio ruolo utente
            Printer.println("Tipo di ruolo: " + Role.fromInt(role));

            userName = cs.getString(4);
            Printer.println("Username: " + userName);
        } catch(SQLException e) {
            throw new LoginException("Login error: " + e.getMessage());
        }


        return new Credentials(cf, password, Role.fromInt(role), userName);
    }
}
