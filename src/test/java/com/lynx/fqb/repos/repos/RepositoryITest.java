package com.lynx.fqb.repos.repos;

import static com.lynx.fqb.transaction.TransactionalExecutor.*;
import static org.hamcrest.collection.IsEmptyCollection.*;
import static org.hamcrest.core.Is.*;
import static org.hamcrest.core.IsNot.*;
import static org.hamcrest.core.IsNull.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import com.lynx.fqb.IntegrationTestBase;
import com.lynx.fqb.entity.Parent;
import com.lynx.fqb.entity.Parent_;
import com.lynx.fqb.repos.impl.ParentRepository;
import com.lynx.fqb.repos.impl.ParentRepositoryImpl;
import com.lynx.fqb.repos.sort.Sort;
import com.lynx.fqb.repos.sort.Sort.Direction;

public class RepositoryITest extends IntegrationTestBase {

    private ParentRepository repo;

    @Before
    public void initRepository() {
        repo = new ParentRepositoryImpl(em, Parent.class);
    }

    @Test
    public void shouldFindAll() {
        assertThat(repo.findAll(), is(not(empty())));
    }

    @Test
    public void shouldFindAllPaged() {
        assertThat(repo.findAll(0, 1).getContent(), is(not(empty())));
    }

    @Test
    public void shouldCountAll() {
        assertThat(repo.countAll(), is(not(nullValue())));
    }

    @Test
    public void shouldCountDistinct() {
        assertThat(repo.countDistinct(), is(not(nullValue())));
    }

    @Test
    public void shouldGetOne() {
        assertThat(repo.getOne(1l).get().getId(), is(1l));
    }

    @Test
    public void shouldSaveThanRemove() {
        Parent entity = using(em).get(() -> {
            return repo.save(new Parent());
        });

        using(em).run(() -> {
            assertTrue(repo.remove(entity));
        });
    }

    @Test
    public void shouldSaveThanRemoveById() {
        Parent entity = using(em).get(() -> {
            return repo.save(new Parent());
        });

        using(em).run(() -> {
            assertTrue(repo.removeById(entity.getId()));
        });
    }

    @Test
    public void shouldSaveThanRemoveByIdCollection() {
        Parent entity1 = using(em).get(() -> {
            return repo.save(new Parent());
        });

        Parent entity2 = using(em).get(() -> {
            return repo.save(new Parent());
        });

        using(em).run(() -> {
            assertEquals(2, repo.removeByIds(Arrays.asList(entity1.getId(), entity2.getId())));
        });
    }

    @Test
    public void shouldNotRemove() {
        assertThat(repo.removeById(-1l), is(false));
    }

    @Test
    public void shouldSortAsc() {
        assertThat(firstElement(repo.findAll(Sort.by(Direction.ASC, Parent_.name))).getName(), is(IntegrationTestBase.PARENT_JOHN_NAME));
    }

    @Test
    public void shouldSortDesc() {
        assertThat(firstElement(repo.findAll(Sort.by(Direction.DESC, Parent_.name))).getName(), is(IntegrationTestBase.PARENT_MAX_NAME));
    }

    @Test
    public void shouldSortByMultipleProperties() {
        assertThat(firstElement(repo.findAll(Sort.by(Direction.ASC, Parent_.name).thenBy(Direction.ASC, Parent_.id))).getName(),
                is(IntegrationTestBase.PARENT_JOHN_NAME));
    }

    @Test
    public void shouldSortAscPaged() {
        assertThat(firstElement(repo.findAll(Sort.by(Direction.ASC, Parent_.name), 0, 1).getContent()).getName(), is(IntegrationTestBase.PARENT_JOHN_NAME));
    }

    private <T> T firstElement(Collection<T> coll) {
        return coll.iterator().next();
    }
}
