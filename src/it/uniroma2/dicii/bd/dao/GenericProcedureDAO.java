package it.uniroma2.dicii.bd.dao;

import it.uniroma2.dicii.bd.exception.DAOException;

import java.sql.SQLException;

public interface GenericProcedureDAO<P> {

    P execute(Object... params) throws DAOException, SQLException;

}
