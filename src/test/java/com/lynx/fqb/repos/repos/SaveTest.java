package com.lynx.fqb.repos.repos;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

import com.lynx.fqb.entity.SellOrder;
import com.lynx.fqb.repos.ReposMockTestBase;
import com.lynx.fqb.repos.impl.SellOrderRepositoryImpl;

public class SaveTest extends ReposMockTestBase {

    @InjectMocks
    private SellOrderRepositoryImpl repo;

    @Test
    public void shoulPersistEntityWithoutId() {
        repo.save(new SellOrder());

        Mockito.verify(em).persist(Mockito.any());
    }

    @Test
    public void shoulMergeEntityWithId() {
        repo.save(new SellOrder(1l, null, null, null));

        Mockito.verify(em).merge(Mockito.any());
    }

    @Test
    public void shouldFlush() {
        repo.saveAndFlush(new SellOrder());

        Mockito.verify(em).flush();
    }

}
