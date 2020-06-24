package com.dxc.inventoryapi;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.dxc.inventoryapi.entity.Item;
import com.dxc.inventoryapi.exception.ItemException;
import com.dxc.inventoryapi.repository.ItemRepository;
import com.dxc.inventoryapi.service.ItemService;

@SpringJUnitConfig
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class ItemServiceImplIntegrationTest {

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private ItemService itemService;

	private Item[] testData;

	@BeforeEach
	public void fillTestData() {
		testData = new Item[] { new Item(101, "RiceOrPAddy", 1025, LocalDate.now()),
				new Item(102, "Wheat", 2025, LocalDate.now()), new Item(103, "Barley", 3025, LocalDate.now()),
				new Item(104, "CocoSeed", 5025, LocalDate.now()), new Item(105, "CoffeeBean", 7025, LocalDate.now()) };

		for (Item item : testData) {
			itemRepository.saveAndFlush(item);
		}
	}

	@AfterEach
	public void clearDatabase() {
		itemRepository.deleteAll();
		testData = null;
	}

	@Test
	public void addTest() {
		try {
			Item expected = new Item(106, "CocaCola", 500, LocalDate.now().minusYears(1));
			Item actual = itemService.add(expected);
			Assertions.assertEquals(expected, actual);
		} catch (ItemException e) {
			Assertions.fail(e.getMessage());
		}
	}

	@Test
	public void addExistingItemTest() {
		Assertions.assertThrows(ItemException.class, () -> {
			itemService.add(testData[0]);
		});
	}

	@Test
	public void updateExistingItemTest() {
		try {
			Item actual = itemService.update(testData[0]);
			Assertions.assertEquals(testData[0], actual);
		} catch (ItemException e) {
			Assertions.fail(e.getMessage());
		}
	}

	@Test
	public void updateNonExistingItemTest() {
		Item nonExistingItem = new Item(106, "CocaCola", 500, LocalDate.now().minusYears(1));
		Assertions.assertThrows(ItemException.class, () -> {
			itemService.update(nonExistingItem);
		});
	}

	@Test
	public void deleteByIdExistingRecordTest() {
		try {
			Assertions.assertTrue(itemService.deleteById(testData[0].getIcode()));
		} catch (ItemException e) {
			Assertions.fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void deleteByIdNonExistingRecordTest() {
		Assertions.assertThrows(ItemException.class, () -> {
			itemService.deleteById(333);
		});
	}

	@Test
	public void getByIdExisitngRecordTest() {
		Assertions.assertEquals(testData[0].getIcode(), 
				itemService.getById(testData[0].getIcode()).getIcode());
	}

	@Test
	public void getByIdNonExisitngRecordTest() {
		Assertions.assertNull(itemService.getById(333));
	}

	@Test
	public void getAllItemsWhenDataExists() {
		List<Item> expected = Arrays.asList(testData);
		//Assertions.assertSame(expected, itemService.getAllItems());
		Assertions.assertIterableEquals(expected, itemService.getAllItems());
	}

	@Test
	public void getAllItemsWhenNoDataExists() {
		List<Item> expected = new ArrayList<>();
		itemRepository.deleteAll();
		Assertions.assertEquals(expected, itemService.getAllItems());
	}
}
