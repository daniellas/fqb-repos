package com.lynx.fqb.repos;

import static org.hamcrest.collection.IsEmptyCollection.*;
import static org.hamcrest.core.Is.*;
import static org.hamcrest.core.IsNot.*;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.lynx.fqb.IntegrationTestBase;
import com.lynx.fqb.entity.Parent;
import com.lynx.fqb.transaction.TransactionalExecutor;

public class RepositoryITest extends IntegrationTestBase {

    private ParentRepository repo;

    @Before
    public void initRepository() {
        repo = new ParentRepositoryImpl(em, Parent.class);
    }

    @Test
    public void shouldFindAll() {
        Assert.assertThat(repo.findAll(), is(not(empty())));
    }

    @Test
    public void shouldFindAllPaged() {
        Assert.assertThat(repo.findAll(0, 1).getContent(), is(not(empty())));
    }

    @Test
    public void shouldGetOne() {
        Assert.assertThat(repo.getOne(1l).get().getId(), is(1l));
    }

    @Test
    public void shouldSaveThanRemoveById() {
        Parent entity = TransactionalExecutor.using(em).get(() -> {
            return repo.save(new Parent());
        });

        TransactionalExecutor.using(em).run(() -> {
            Assert.assertTrue(repo.remove(entity.getId()));
        });
    }

    @Test
    public void shouldSaveThanRemoveByIdCollection() {
        Parent entity1 = TransactionalExecutor.using(em).get(() -> {
            return repo.save(new Parent());
        });

        Parent entity2 = TransactionalExecutor.using(em).get(() -> {
            return repo.save(new Parent());
        });

        TransactionalExecutor.using(em).run(() -> {
            Assert.assertEquals(2, repo.remove(Arrays.asList(entity1.getId(), entity2.getId())));
        });
    }

}
