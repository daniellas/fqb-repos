package com.lynx.fqb.repos;

import java.util.function.Function;

import javax.persistence.EntityManager;

import com.lynx.fqb.entity.Parent;

public class ParentRepositoryImpl extends FqbRepositoryBase<Parent, Long> implements ParentRepository {

    public ParentRepositoryImpl(EntityManager em, Class<Parent> entityCls) {
        super(em, entityCls);
    }

    @Override
    protected Function<Parent, Long> entityId() {
        return Parent::getId;
    }

}
