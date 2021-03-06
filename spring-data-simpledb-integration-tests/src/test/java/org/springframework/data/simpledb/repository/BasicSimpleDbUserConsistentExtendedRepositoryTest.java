package org.springframework.data.simpledb.repository;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.simpledb.domain.SimpleDbUser;
import org.springframework.data.simpledb.repository.util.SimpleDbUserBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:simpledb-repository-context.xml")
public class BasicSimpleDbUserConsistentExtendedRepositoryTest {

	@Autowired
	SimpleDbUserRepositoryConsistent repository;

	@After
	public void tearDown() {
		// may fail
		repository.deleteAll(true);
	}


	@Test
	public void consistent_count_should_return_total_number_of_item_with_no_delay() {
		String itemName = "FirstItem";

		SimpleDbUser user = SimpleDbUserBuilder.createUserWithSampleAttributes(itemName);
		repository.save(user, true);
		assertEquals(1, repository.count(true));

		repository.delete(itemName, true);
		assertEquals(0, repository.count(true));
	}

}
