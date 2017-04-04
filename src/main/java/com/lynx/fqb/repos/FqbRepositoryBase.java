package com.lynx.fqb.repos;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import com.lynx.fqb.Find;
import com.lynx.fqb.Persist;
import com.lynx.fqb.Remove;
import com.lynx.fqb.Select;
import com.lynx.fqb.repos.page.DefaultPage;
import com.lynx.fqb.repos.page.Page;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class FqbRepositoryBase<E, I> implements FqbRepository<E, I> {

    private final EntityManager em;

    private final Class<E> entityCls;

    protected abstract I entityId(E entity);

    // TODO Decide on Persist vs Merge
    @Override
    public E save(E entity) {
        return Persist.entity(entity).apply(em);
    }

    @Override
    public List<E> findAll() {
        return Select.from(entityCls).getResultList(em);
    }

    // TODO Implement total
    @Override
    public Page<E> findAll(int offset, int limit) {
        return DefaultPage.of(offset, limit, 0, Select.from(entityCls).getResultList(em, offset, limit));
    }

    @Override
    public Optional<E> getOne(I id) {
        return Find.entity(entityCls).byId(id).apply(em);
    }

    @Override
    public boolean remove(I id) {
        Optional<E> entity = Find.entity(entityCls).byId(id).apply(em);

        if (entity.isPresent()) {
            Remove.entity(entity.get()).accept(em);

            return true;
        }

        return false;
    }

    @Override
    public long remove(Collection<I> ids) {
        return ids.stream()
                .map(i -> remove(i))
                .filter(r -> r)
                .collect(Collectors.counting());
    }
}
