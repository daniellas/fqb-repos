package com.lynx.fqb.repos.impl;

import java.util.function.Function;

import javax.persistence.EntityManager;

import com.lynx.fqb.entity.Parent;
import com.lynx.fqb.repos.FqbRepositoryBaseNoIntercept;

public class ParentRepositoryImpl extends FqbRepositoryBaseNoIntercept<Parent, Long> implements ParentRepository {

    public ParentRepositoryImpl(EntityManager em, Class<Parent> entityCls) {
        super(em, entityCls);
    }

    @Override
    protected Function<Parent, Long> entityId() {
        return Parent::getId;
    }


}
