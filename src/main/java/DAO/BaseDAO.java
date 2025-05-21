package DAO;


import java.sql.SQLException;
import java.util.List;

public interface BaseDAO<T>{
    void  save(T t) throws SQLException;

    T findById(Long id ) throws SQLException;

    List<T> findAll() throws SQLException;

    void update(T t) throws SQLException;

    void  delete(long id ) throws SQLException;
}
