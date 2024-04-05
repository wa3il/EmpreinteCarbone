package fr.univlyon1.m1if.m1if10.appec.dao;

import java.util.List;
import java.util.Optional;

/**
 * Interface for DAO.
 * @param <T> the type of the object
 */
public interface Dao<T> {

    Optional<T> get(Integer id);

    List<T> getAll();

    void save(T t);

    void update(T t, String[] params);

    void delete(T t);
}
