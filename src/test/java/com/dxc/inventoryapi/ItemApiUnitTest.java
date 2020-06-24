package com.dxc.inventoryapi;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;

import com.dxc.inventoryapi.api.ItemApi;
import com.dxc.inventoryapi.entity.Item;
import com.dxc.inventoryapi.service.ItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

@SpringJUnitConfig
@WebMvcTest(ItemApi.class)
public class ItemApiUnitTest {

	@Autowired
	private MockMvc mvcClient;

	@MockBean
	private ItemService itemService;

	private List<Item> testData;

	private static final String API_URL = "/items";
	
	@BeforeEach
	public void fillTestData() {
		testData = new ArrayList<>();
		testData.add(new Item(101, "RiceOrPAddy", 1025, LocalDate.now()));
		testData.add(new Item(102, "Wheat", 2025, LocalDate.now()));
		testData.add(new Item(103, "Barley", 3025, LocalDate.now()));
		testData.add(new Item(104, "CocoSeed", 5025, LocalDate.now())); 
		testData.add(new Item(105, "CoffeeBean", 7025, LocalDate.now())); 
	}

	@AfterEach
	public void clearDatabase() {
		testData = null;
	}

	@Test
	public void getAllItemsTest()  {
		Mockito.when(itemService.getAllItems()).thenReturn(testData);

		try {
			mvcClient.perform(get(API_URL))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(5)))
				.andDo(print());
		} catch (Exception e) {
			Assertions.fail(e.getMessage());
		}
	}
	
	@Test
	public void getItemByIdTest() {
		Item testRec = testData.get(0);
		
		Mockito.when(itemService.getById(testRec.getIcode())).thenReturn(testRec);
	
		try {
			mvcClient.perform(get(API_URL + "/" + testRec.getIcode()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.title", is(testRec.getTitle())))
				.andExpect(jsonPath("$.price", is(testRec.getPrice())))
				.andDo(print());
		} catch (Exception e) {
			Assertions.fail(e.getMessage());
		}		
	}
	
	@Test
	public void getItemByIdTestNonExsiting() {
		
		Mockito.when(itemService.getById(8888)).thenReturn(null);
	
		try {
			mvcClient.perform(get(API_URL + "/8888"))
				.andExpect(status().isNotFound())
				.andDo(print());
		} catch (Exception e) {
			Assertions.fail(e.getMessage());
		}		
	}
	
	@Test
	public void getItemByTitleTest(){
	Item testRec = testData.get(0);
		
		Mockito.when(itemService.findByTitle(testRec.getTitle())).thenReturn(testRec);
	
		try {
			mvcClient.perform(get(API_URL + "/" + testRec.getTitle()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.icode", is(testRec.getIcode())))
				.andExpect(jsonPath("$.price", is(testRec.getPrice())))
				.andDo(print());
		} catch (Exception e) {
			Assertions.fail(e.getMessage());
		}	
	}
	
	@Test
	public void getItemByPackageDateTest(){
		Item testRec = testData.get(0);
		
		Mockito.when(itemService.findByPackageDate(testRec.getPackageDate())).thenReturn(testData);
	
		try {
			mvcClient.perform(get(API_URL + "/" + testRec.getPackageDate()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(5)))
				.andDo(print());
		} catch (Exception e) {
			Assertions.fail(e.getMessage());
		}	
	}
	
	@Test
	public void getItemByPriceRangeTest() {
		
		Mockito.when(itemService.findInPriceRange(1000, 8000)).thenReturn(testData);
	
		try {
			mvcClient.perform(get(API_URL + "/priceBetween/1000/and/8000"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(5)))
				.andDo(print());
		} catch (Exception e) {
			Assertions.fail(e.getMessage());
		}
	}
	
	private static final ObjectMapper makeMapper() {
	    ObjectMapper mapper = new ObjectMapper();
	    mapper.registerModule(new ParameterNamesModule());
	    mapper.registerModule(new Jdk8Module());
	    mapper.registerModule(new JavaTimeModule());
	    return mapper;
	}
	
	@Test
	public void createItemTest() {
		Item testRec = testData.get(0);
		
		try {
			Mockito.when(itemService.add(testRec)).thenReturn(testRec);
			
			mvcClient.perform(post(API_URL)
					.contentType(MediaType.APPLICATION_JSON)
					.content(makeMapper().writeValueAsString(testRec))
					).andExpect(status().isOk()).andDo(print());
		} catch (Exception e) {
			Assertions.fail(e.getMessage());
		}
	
	}
	
	@Test
	public void updateItemTest() {
		Item testRec = testData.get(0);
		
		try {
			Mockito.when(itemService.update(testRec)).thenReturn(testRec);
			System.out.println((new ObjectMapper()).writeValueAsString(testRec));
			mvcClient.perform(put(API_URL)
					.contentType(MediaType.APPLICATION_JSON)
					.content(makeMapper().writeValueAsString(testRec))
					).andExpect(status().isOk()).andDo(print());
		} catch (Exception e) {
			Assertions.fail(e.getMessage());
		}
	
	}
	
	@Test
	public void deleteItemsByIdTest() {
		
		try {
			Mockito.when(itemService.deleteById(testData.get(0).getIcode())).thenReturn(true);
			
			mvcClient.perform(delete(API_URL +"/"+testData.get(0).getIcode()))
			.andExpect(status().isOk())
			.andDo(print());
		} catch (Exception e) {
			Assertions.fail(e.getMessage());
		}
	}
	
	
}
