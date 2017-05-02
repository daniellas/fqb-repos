package com.lynx.fqb.repos;

import javax.persistence.EntityManager;

import com.lynx.fqb.intercept.EntityInterceptor;
import com.lynx.fqb.predicate.PredicatesInterceptor;

public abstract class FqbRepositoryBase<E, I> extends FqbInterceptingRepositoryBase<E, I> {

    public FqbRepositoryBase(EntityManager em, Class<E> entityCls) {
        super(em, entityCls);
    }

    @Override
    protected PredicatesInterceptor<E> predicatesInterceptor() {
        return PredicatesInterceptor.identity();
    }

    @Override
    protected EntityInterceptor<E> entityPostInterceptor() {
        return EntityInterceptor.noOp();
    }

    @Override
    protected EntityInterceptor<E> entityPreInterceptor() {
        return EntityInterceptor.noOp();
    }

}
