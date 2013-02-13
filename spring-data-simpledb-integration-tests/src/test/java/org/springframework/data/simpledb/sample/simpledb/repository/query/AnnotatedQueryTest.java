package org.springframework.data.simpledb.sample.simpledb.repository.query;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.data.simpledb.sample.simpledb.domain.SimpleDbUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.springframework.data.simpledb.sample.simpledb.repository.util.SimpleDbUserBuilder;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:simpledb-consistent-repository-context.xml")
public class AnnotatedQueryTest {

    List<SimpleDbUser> testUsers;

    @Autowired
    AnnotatedQueryRepository repository;

    @Before
    public void setUp() {
        testUsers = SimpleDbUserBuilder.createListOfItems(3);
        repository.save(testUsers);
    }

    @After
    public void tearDown() {
        repository.deleteAll();
    }

    @Test
    public void customSelectAll_should_return_the_list_of_users() {
        List<SimpleDbUser> result = repository.customSelectAll();
        assertNotNull(result);
        assertEquals(testUsers, result);
    }

    @Test
    public void customSelectAllWrongReturnType_should_fail_wrong_returned_collection_generic_type() {
        List<SimpleDbUser> testUsers = SimpleDbUserBuilder.createListOfItems(3);
        repository.save(testUsers);

        try {
            List<String> result = repository.customSelectAllWrongReturnType();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            assertTrue(e.getMessage().contains("is not assignable"));
            return;
        }
        fail();
    }


    @Test
    public void customSelectWithNamedParamsQuery_should_return_a_list_a_list_of() {
        List<SimpleDbUser> result = repository.customSelectWithNamedParamsQuery(String.valueOf(0.01f), String.valueOf("Item_1"));
        assertNotNull(result);
    }

    @Test
    public void customSelectWithIndexedParams_should_return_a_list_of() {
        List<SimpleDbUser> result = repository.customSelectWithIndexedParams(String.valueOf("tes_string$"), String.valueOf(0.01f));
        assertNotNull(result);
    }

}
