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

import com.lynx.fqb.Find;
import com.lynx.fqb.Merge;
import com.lynx.fqb.Persist;
import com.lynx.fqb.Remove;
import com.lynx.fqb.Select.InterceptingSelect;
import com.lynx.fqb.expression.Expressions;
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

    protected InterceptingSelect<E> createSelect() {
        return new InterceptingSelect<>(predicatesInterceptor());
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
        return createSelect().from(entityCls).getResultList(em);
    }

    @Override
    public List<E> findAll(Sort<E> sort) {
        return createSelect().from(entityCls)
                .orderBy(Orders.of(sort.toOrders()))
                .getResultList(em);
    }

    @Override
    public Page<E> findAll(int offset, int limit) {
        return DefaultPage.of(offset, limit, countAll(),
                createSelect().from(entityCls)
                        .getResultList(em, offset, limit));
    }

    @Override
    public Page<E> findAll(Sort<E> sort, int offset, int limit) {
        return DefaultPage.of(offset, limit, countAll(),
                createSelect().from(entityCls).orderBy(Orders.of(sort.toOrders()))
                        .getResultList(em, offset, limit));
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

    private long remove(Stream<I> ids) {
        return ids.map(i -> remove(i))
                .filter(r -> r)
                .collect(Collectors.counting());
    }

    @Override
    public long remove(Collection<I> ids) {
        return remove(ids.stream());
    }

    @Override
    public long countAll() {
        return createSelect()
                .customFrom(Long.class, entityCls)
                .with(of(expr(count(entityCls))))
                .getSingleResult(em)
                .get();
    }

    @Override
    public long countDistinct() {
        return createSelect()
                .customFrom(Long.class, entityCls)
                .with(of(expr(Expressions.countDistinct(entityCls))))
                .getSingleResult(em)
                .get();
    }

}
