package com.lynx.fqb.repos;

import javax.persistence.EntityManager;

import com.lynx.fqb.entity.Parent;

public class ParentRepositoryImpl extends FqbRepositoryBase<Parent, Long> implements ParentRepository {

    public ParentRepositoryImpl(EntityManager em, Class<Parent> entityCls) {
        super(em, entityCls);
    }

    @Override
    protected Long entityId(Parent entity) {
        return entity.getId();
    }

}
