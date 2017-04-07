package com.lynx.fqb.repos;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RunWith(MockitoJUnitRunner.class)
public class ReposMockTestBase {

    @Mock
    protected EntityManager em;

    @Before
    public void initBase() {
    }
}
