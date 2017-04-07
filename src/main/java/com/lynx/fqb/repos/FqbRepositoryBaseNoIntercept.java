package com.lynx.fqb.repos;

import javax.persistence.EntityManager;

import com.lynx.fqb.predicate.PredicatesInterceptor;

public abstract class FqbRepositoryBaseNoIntercept<E, I> extends FqbRepositoryBase<E, I> {

    public FqbRepositoryBaseNoIntercept(EntityManager em, Class<E> entityCls) {
        super(em, entityCls);
    }

    @Override
    protected PredicatesInterceptor<E> predicatesInterceptor() {
        return PredicatesInterceptor.identity();
    }

}
