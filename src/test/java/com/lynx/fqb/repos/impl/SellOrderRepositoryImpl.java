package com.lynx.fqb.repos.impl;

import java.util.function.Function;

import javax.persistence.EntityManager;

import com.lynx.fqb.entity.SellOrder;
import com.lynx.fqb.repos.FqbRepositoryBase;

public class SellOrderRepositoryImpl extends FqbRepositoryBase<SellOrder, Long> implements SellOrderRepository {

    public SellOrderRepositoryImpl(EntityManager em, Class<SellOrder> entityCls) {
        super(em, entityCls);
    }

    @Override
    protected Function<SellOrder, Long> entityId() {
        return SellOrder::getId;
    }


}
