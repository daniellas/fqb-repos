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
import com.lynx.fqb.entity.SellOrder;
import com.lynx.fqb.entity.SellOrder_;
import com.lynx.fqb.repos.impl.SellOrderRepository;
import com.lynx.fqb.repos.impl.SellOrderRepositoryImpl;
import com.lynx.fqb.repos.sort.Sort;
import com.lynx.fqb.repos.sort.Sort.Direction;

public class RepositoryITest extends IntegrationTestBase {

    private SellOrderRepository repo;

    @Before
    public void initRepository() {
        repo = new SellOrderRepositoryImpl(em, SellOrder.class);
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
        assertThat(repo.getOne(IntegrationTestBase.ORDER_ONE_ID).get().getId(), is(IntegrationTestBase.ORDER_ONE_ID));
    }

    @Test
    public void shouldSaveThanRemove() {
        SellOrder entity = using(em).get(() -> {
            return repo.save(new SellOrder()).get();
        });

        using(em).run(() -> {
            assertTrue(repo.remove(entity));
        });
    }

    @Test
    public void shouldSaveThanRemoveById() {
        SellOrder entity = using(em).get(() -> {
            return repo.save(new SellOrder()).get();
        });

        using(em).run(() -> {
            assertTrue(repo.removeById(entity.getId()));
        });
    }

    @Test
    public void shouldSaveThanRemoveByIdCollection() {
        SellOrder entity1 = using(em).get(() -> {
            return repo.save(new SellOrder()).get();
        });

        SellOrder entity2 = using(em).get(() -> {
            return repo.save(new SellOrder()).get();
        });

        using(em).run(() -> {
            assertEquals(2, repo.removeById(Arrays.asList(entity1.getId(), entity2.getId())));
        });
    }

    @Test
    public void shouldNotRemove() {
        assertThat(repo.removeById(-1l), is(false));
    }

    @Test
    public void shouldSortAsc() {
        assertThat(firstElement(repo.findAll(Sort.by(Direction.ASC, SellOrder_.number))).getNumber(), is(IntegrationTestBase.ORDER_ONE_NUMBER));
    }

    @Test
    public void shouldSortDesc() {
        assertThat(firstElement(repo.findAll(Sort.by(Direction.DESC, SellOrder_.number))).getNumber(), is(IntegrationTestBase.ORDER_TWO_NUMBER));
    }

    @Test
    public void shouldSortByMultipleProperties() {
        assertThat(firstElement(repo.findAll(Sort.by(Direction.ASC, SellOrder_.number).thenBy(Direction.ASC, SellOrder_.id))).getNumber(),
                is(IntegrationTestBase.ORDER_ONE_NUMBER));
    }

    @Test
    public void shouldSortAscPaged() {
        assertThat(firstElement(repo.findAll(Sort.by(Direction.ASC, SellOrder_.number), 0, 1).getContent()).getNumber(),
                is(IntegrationTestBase.ORDER_ONE_NUMBER));
    }

    private <T> T firstElement(Collection<T> coll) {
        return coll.iterator().next();
    }
}
