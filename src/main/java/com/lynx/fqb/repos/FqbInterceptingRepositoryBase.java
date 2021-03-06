package com.lynx.fqb.repos;

import static com.lynx.fqb.expression.Expressions.*;
import static com.lynx.fqb.selection.Selections.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import javax.persistence.EntityManager;

import com.lynx.fqb.Find.InterceptingFind;
import com.lynx.fqb.Merge.InterceptingMerge;
import com.lynx.fqb.Persist.InterceptingPersist;
import com.lynx.fqb.Remove.InterceptingRemove;
import com.lynx.fqb.Select.InterceptingSelect;
import com.lynx.fqb.expression.Expressions;
import com.lynx.fqb.intercept.EntityInterceptor;
import com.lynx.fqb.intercept.PredicatesInterceptor;
import com.lynx.fqb.order.Orders;
import com.lynx.fqb.repos.page.DefaultPage;
import com.lynx.fqb.repos.page.Page;
import com.lynx.fqb.repos.sort.Sort;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class FqbInterceptingRepositoryBase<E, I> implements FqbRepository<E, I> {

    protected final EntityManager em;

    protected final Class<E> entityCls;

    protected abstract Function<E, I> entityId();

    protected abstract PredicatesInterceptor<E> predicatesInterceptor();

    protected abstract EntityInterceptor<E> entityPostInterceptor();

    protected abstract EntityInterceptor<E> entityPreInterceptor();

    protected InterceptingSelect<E> select() {
        return new InterceptingSelect<>(predicatesInterceptor());
    }

    protected InterceptingFind<E> find() {
        return new InterceptingFind<>(entityPostInterceptor());
    }

    protected InterceptingRemove<E> remove() {
        return new InterceptingRemove<>(entityPreInterceptor());
    }

    protected InterceptingMerge<E> merge() {
        return new InterceptingMerge<>(entityPreInterceptor());
    }

    protected InterceptingPersist<E> persist() {
        return new InterceptingPersist<>(entityPreInterceptor());
    }

    @Override
    public Optional<E> save(E entity) {
        return Optional.ofNullable(entityId().apply(entity))
                .map(id -> merge().entity(entity).apply(em))
                .orElseGet(() -> persist().entity(entity).apply(em));
    }

    @Override
    public Optional<E> saveAndFlush(E entity) {
        return save(entity).map(e -> {
            em.flush();

            return e;
        });
    }

    @Override
    public List<E> findAll() {
        return select().from(entityCls).getResultList(em);
    }

    @Override
    public List<E> findAll(Sort<E> sort) {
        return select().from(entityCls)
                .orderBy(Orders.of(sort.toOrders()))
                .getResultList(em);
    }

    @Override
    public Page<E> findAll(int offset, int limit) {
        return DefaultPage.of(offset, limit, countAll(),
                select().from(entityCls)
                        .getResultList(em, offset, limit));
    }

    @Override
    public Page<E> findAll(Sort<E> sort, int offset, int limit) {
        return DefaultPage.of(offset, limit, countAll(),
                select().from(entityCls).orderBy(Orders.of(sort.toOrders()))
                        .getResultList(em, offset, limit));
    }

    @Override
    public Optional<E> getOne(I id) {
        return find().entity(entityCls).byId(id).apply(em);
    }

    @Override
    public E findOne(I id) {
        return getOne(id).orElse(null);
    }

    @Override
    public boolean remove(E entity) {
        return remove().entity(entity).apply(em);
    }

    @Override
    public long remove(Collection<E> entities) {
        return entities.stream()
                .map(this::remove)
                .filter(r -> r)
                .count();
    }

    @Override
    public boolean removeById(I id) {
        return find().entity(entityCls).byId(id)
                .andThen(e -> e.map(this::remove).orElse(false))
                .apply(em);
    }

    private long remove(Stream<I> ids) {
        return ids.map(this::removeById)
                .filter(r -> r)
                .count();
    }

    @Override
    public long removeById(Collection<I> ids) {
        return remove(ids.stream());
    }

    @Override
    public long countAll() {
        return select()
                .customFrom(Long.class, entityCls)
                .with(of(expr(count(entityCls))))
                .getSingleResult(em)
                .getResult();
    }

    @Override
    public long countDistinct() {
        return select()
                .customFrom(Long.class, entityCls)
                .with(of(expr(Expressions.countDistinct(entityCls))))
                .getSingleResult(em)
                .getResult();
    }

}
