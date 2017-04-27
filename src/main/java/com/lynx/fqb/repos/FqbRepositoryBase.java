package com.lynx.fqb.repos;

import static com.lynx.fqb.expression.Expressions.*;
import static com.lynx.fqb.select.Selections.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityManager;

import com.lynx.fqb.Find.InterceptingFind;
import com.lynx.fqb.Merge;
import com.lynx.fqb.Persist;
import com.lynx.fqb.Remove.InterceptingRemove;
import com.lynx.fqb.Select.InterceptingSelect;
import com.lynx.fqb.expression.Expressions;
import com.lynx.fqb.intercept.EntityInterceptor;
import com.lynx.fqb.order.Orders;
import com.lynx.fqb.predicate.PredicatesInterceptor;
import com.lynx.fqb.repos.page.DefaultPage;
import com.lynx.fqb.repos.page.Page;
import com.lynx.fqb.repos.sort.Sort;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class FqbRepositoryBase<E, I> implements FqbRepository<E, I> {

    protected final EntityManager em;

    protected final Class<E> entityCls;

    protected abstract Function<E, I> entityId();

    protected abstract PredicatesInterceptor<E> predicatesInterceptor();

    protected abstract EntityInterceptor<E> entityInterceptor();

    protected InterceptingSelect<E> select() {
        return new InterceptingSelect<>(predicatesInterceptor());
    }

    protected InterceptingFind<E> find() {
        return new InterceptingFind<>(entityInterceptor());
    }

    protected InterceptingRemove<E> remove() {
        return new InterceptingRemove<>(entityInterceptor());
    }

    @Override
    public E save(E entity) {
        return Optional.ofNullable(entityId().apply(entity))
                .map(id -> {
                    return Merge.entity(entity).apply(em);
                })
                .orElseGet(() -> {
                    return Persist.entity(entity).apply(em);
                });
    }

    @Override
    public E saveAndFlush(E entity) {
        E result = save(entity);

        em.flush();

        return result;
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
    public boolean remove(E entity) {
        return remove().entity(entity).apply(em);
    }

    @Override
    public boolean removeById(I id) {
        return find().entity(entityCls).byId(id)
                .andThen(e -> {
                    return e.map(this::remove).orElse(false);
                }).apply(em);
    }

    private long remove(Stream<I> ids) {
        return ids.map(this::removeById)
                .filter(r -> r)
                .collect(Collectors.counting());
    }

    @Override
    public long removeByIds(Collection<I> ids) {
        return remove(ids.stream());
    }

    @Override
    public long countAll() {
        return select()
                .customFrom(Long.class, entityCls)
                .with(of(expr(count(entityCls))))
                .getSingleResult(em)
                .get();
    }

    @Override
    public long countDistinct() {
        return select()
                .customFrom(Long.class, entityCls)
                .with(of(expr(Expressions.countDistinct(entityCls))))
                .getSingleResult(em)
                .get();
    }

}
