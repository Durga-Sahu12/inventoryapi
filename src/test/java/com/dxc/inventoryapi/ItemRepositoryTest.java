package com.dxc.inventoryapi;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;

import com.dxc.inventoryapi.entity.Item;
import com.dxc.inventoryapi.repository.ItemRepository;

@DataJpaTest // configuring H2, an in-memory database,setting Hibernate, Spring Data, and the
				// DataSource,performing an @EntityScan turning on SQL logging
public class ItemRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private ItemRepository itemRepository;

	private Item[] testData;

	@BeforeEach
	public void fillTestData() {
		testData = new Item[] { new Item(101, "RiceOrPAddy", 1025, LocalDate.now()),
				new Item(102, "Wheat", 2025, LocalDate.now()), new Item(103, "Barley", 3025, LocalDate.now()),
				new Item(104, "CocoSeed", 5025, LocalDate.now()), new Item(105, "CoffeeBean", 7025, LocalDate.now()) };

		// inserting test data into H2 database
		for (Item item : testData) {
			entityManager.persist(item);
		}
		entityManager.flush();

	}

	@AfterEach
	public void clearDatabase() {
		// removing test data into H2 database
		for (Item item : testData) {
			entityManager.remove(item);
		}
		entityManager.flush();
	}

	@Test
	public void findByTitleTest() {
		for (Item item : testData) {
			Assertions.assertEquals(item, itemRepository.findByTitle(item.getTitle()));
		}
	}

	@Test
	public void findByTitleTestWitnNonExistingTitle() {
		Assertions.assertNull(itemRepository.findByTitle("@#1234"));
	}

	@Test
	public void findAllByPackageDateTest() {
		Item[] actualData = itemRepository.findAllByPackageDate(LocalDate.now()).toArray(new Item[] {});
		for (int i = 0; i < actualData.length; i++) {
			Assertions.assertEquals(testData[i], actualData[i]);
		}
	}

	@Test
	public void findAllByPackageDateTestWithNonExisitngDate() {
		List<Item> actualData = itemRepository.findAllByPackageDate(LocalDate.now().plusDays(2));
		Assertions.assertEquals(0, actualData.size());
	}

	@Test
	public void getAllInPriceRangeTest() {
		Item[] actualData = itemRepository.getAllInPriceRange(2000, 5000).toArray(new Item[] {});
		Item[] expectedData = new Item[] { testData[1], testData[2] };

		for (int i = 0; i < actualData.length; i++) {
			Assertions.assertEquals(expectedData[i], actualData[i]);
		}

	}
}
