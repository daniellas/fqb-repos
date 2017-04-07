package com.lynx.fqb.repos.repos;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

import com.lynx.fqb.entity.Parent;
import com.lynx.fqb.repos.ReposMockTestBase;
import com.lynx.fqb.repos.impl.ParentRepositoryImpl;

public class SaveTest extends ReposMockTestBase {

    @InjectMocks
    private ParentRepositoryImpl repo;

    @Test
    public void shoulPersistEntityWithoutId() {
        repo.save(new Parent());

        Mockito.verify(em).persist(Mockito.any());
    }

    @Test
    public void shoulMergeEntityWithId() {
        repo.save(new Parent(1l, null, null));

        Mockito.verify(em).merge(Mockito.any());
    }

    @Test
    public void shouldFlush() {
        repo.saveAndFlush(new Parent());

        Mockito.verify(em).flush();
    }

}
