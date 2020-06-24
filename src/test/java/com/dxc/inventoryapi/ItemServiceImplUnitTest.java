package com.dxc.inventoryapi;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.dxc.inventoryapi.entity.Item;
import com.dxc.inventoryapi.exception.ItemException;
import com.dxc.inventoryapi.repository.ItemRepository;
import com.dxc.inventoryapi.service.ItemService;
import com.dxc.inventoryapi.service.ItemServiceImpl;

@SpringJUnitConfig
public class ItemServiceImplUnitTest {

	@TestConfiguration
	static class ItemServiceImplTestContextConfiguration {

		@Bean
		public ItemService itemService() {
			return new ItemServiceImpl();
		}
	}

	@MockBean
	private ItemRepository itemRepository;

	@Autowired
	private ItemService itemService;

	private Item[] testData;

	@BeforeEach
	public void fillTestData() {
		testData = new Item[] { new Item(101, "RiceOrPAddy", 1025, LocalDate.now()),
				new Item(102, "Wheat", 2025, LocalDate.now()), new Item(103, "Barley", 3025, LocalDate.now()),
				new Item(104, "CocoSeed", 5025, LocalDate.now()), new Item(105, "CoffeeBean", 7025, LocalDate.now()) };
	}

	@AfterEach
	public void clearDatabase() {
		testData = null;
	}

	@Test
	public void addTest() {

		Mockito.when(itemRepository.save(Mockito.any(Item.class))).thenReturn(null);

		Mockito.when(itemRepository.existsById(testData[0].getIcode())).thenReturn(false);

		try {
			Item actual = itemService.add(testData[0]);
			Assertions.assertEquals(testData[0], actual);
		} catch (ItemException e) {
			Assertions.fail(e.getMessage());
		}
	}

	@Test
	public void addExistingItemTest() {
		Mockito.when(itemRepository.save(Mockito.any(Item.class))).thenReturn(null);

		Mockito.when(itemRepository.existsById(testData[0].getIcode())).thenReturn(true);

		Assertions.assertThrows(ItemException.class, () -> {
			itemService.add(testData[0]);
		});

	}

	@Test
	public void updateExistingItemTest() {

		Mockito.when(itemRepository.save(Mockito.any(Item.class))).thenReturn(null);

		Mockito.when(itemRepository.existsById(testData[0].getIcode())).thenReturn(true);

		try {
			Item actual = itemService.update(testData[0]);
			Assertions.assertEquals(testData[0], actual);
		} catch (ItemException e) {
			Assertions.fail(e.getMessage());
		}
	}

	@Test
	public void updateNonExistingItemTest() {
		Mockito.when(itemRepository.save(Mockito.any(Item.class))).thenReturn(null);

		Mockito.when(itemRepository.existsById(testData[0].getIcode())).thenReturn(false);

		Assertions.assertThrows(ItemException.class, () -> {
			itemService.update(testData[0]);
		});

	}

	@Test
	public void deleteByIdExistingRecordTest() {
		Mockito.when(itemRepository.existsById(Mockito.isA(Integer.class))).thenReturn(true);

		Mockito.doNothing().when(itemRepository).deleteById(Mockito.isA(Integer.class));

		try {
			Assertions.assertTrue(itemService.deleteById(testData[0].getIcode()));
		} catch (ItemException e) {
			Assertions.fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void deleteByIdNonExistingRecordTest() {
		Mockito.when(itemRepository.existsById(Mockito.isA(Integer.class)))
		.thenReturn(false);
		
		Mockito.doNothing().when(itemRepository).deleteById(Mockito.isA(Integer.class));
		
		
		Assertions.assertThrows(ItemException.class,
				()->{itemService.deleteById(testData[0].getIcode());});
		
	}
	
	@Test
	public void getByIdExisitngRecordTest() {
		Mockito.when(itemRepository.findById(testData[0].getIcode()))
		.thenReturn(Optional.of(testData[0]));
		Assertions.assertEquals(testData[0], itemService.getById(testData[0].getIcode()));
	}
	
	@Test
	public void getByIdNonExisitngRecordTest() {
		Mockito.when(itemRepository.findById(testData[0].getIcode()))
		.thenReturn(Optional.empty());
		Assertions.assertNull(itemService.getById(testData[0].getIcode()));
	}
	
	@Test
	public void getAllItemsWhenDataExists() {
		List<Item> expected =Arrays.asList(testData);
		
		Mockito.when(itemRepository.findAll())
		.thenReturn(expected);
		
		Assertions.assertEquals(expected,itemService.getAllItems());
	}
	
	@Test
	public void getAllItemsWhenNoDataExists() {
		List<Item> expected =new ArrayList<>();
		
		Mockito.when(itemRepository.findAll())
		.thenReturn(expected);
		
		Assertions.assertEquals(expected,itemService.getAllItems());
	}
}
