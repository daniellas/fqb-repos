package com.lynx.fqb.repos;

import static org.hamcrest.collection.IsEmptyCollection.*;
import static org.hamcrest.core.Is.*;
import static org.hamcrest.core.IsNot.*;
import static org.hamcrest.core.IsNull.*;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.lynx.fqb.IntegrationTestBase;
import com.lynx.fqb.entity.Parent;
import com.lynx.fqb.entity.Parent_;
import com.lynx.fqb.repos.sort.Sort;
import com.lynx.fqb.repos.sort.Sort.Direction;
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
    public void shouldCountAll() {
        Assert.assertThat(repo.countAll(), is(not(nullValue())));
    }

    @Test
    public void shouldCountDistinct() {
        Assert.assertThat(repo.countDistinct(), is(not(nullValue())));
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

    @Test
    public void shouldSaveThanRemoveParallelByIdCollection() {
        Parent entity1 = TransactionalExecutor.using(em).get(() -> {
            return repo.save(new Parent());
        });

        Parent entity2 = TransactionalExecutor.using(em).get(() -> {
            return repo.save(new Parent());
        });

        TransactionalExecutor.using(em).run(() -> {
            Assert.assertEquals(2, repo.removeParallel(Arrays.asList(entity1.getId(), entity2.getId())));
        });
    }

    @Test
    public void shouldNotRemove() {
        Assert.assertThat(repo.remove(-1l), is(false));
    }

    @Test
    public void shouldSortAsc() {
        repo.findAll(Sort.by(Direction.ASC, Parent_.name));
    }

    @Test
    public void shouldSortDesc() {
        repo.findAll(Sort.by(Direction.DESC, Parent_.name));
    }

    @Test
    public void shouldSortByMultipleProperties() {
        repo.findAll(Sort.by(Direction.ASC, Parent_.name).thenBy(Direction.ASC, Parent_.id));
    }

}
