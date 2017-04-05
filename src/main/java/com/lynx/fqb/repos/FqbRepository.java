package com.lynx.fqb.repos;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.lynx.fqb.repos.page.Page;

public interface FqbRepository<E, I> {

    E save(E entity);

    List<E> findAll();

    Page<E> findAll(int offset, int limit);

    Optional<E> getOne(I id);

    boolean remove(I id);

    long remove(Collection<I> ids);
    
    long countAll();

    long countDistinct();

}
