package com.lynx.fqb.repos;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.lynx.fqb.repos.page.Page;
import com.lynx.fqb.repos.sort.Sort;

/**
 * Standard repository operations
 * 
 * @author Daniel Łaś
 *
 * @param <E>
 *            {@link Entity} type
 * @param <I>
 *            {@link Id} type
 */
public interface FqbRepository<E, I> {

    /**
     * Performs persist or merge depending on entity id value
     * 
     * @param entity
     *            to save
     * @return saved entity
     */
    E save(E entity);

    E saveAndFlush(E entity);

    List<E> findAll();

    List<E> findAll(Sort<E> sort);

    Page<E> findAll(int offset, int limit);

    Page<E> findAll(Sort<E> sort, int offset, int limit);

    Optional<E> getOne(I id);

    boolean remove(E entity);

    boolean removeById(I id);

    /**
     * Remove entities by given identifiers
     * 
     * 
     * @param ids
     *            to remove
     * @return number of removed entities
     */
    long removeByIds(Collection<I> ids);

    long countAll();

    long countDistinct();

}
