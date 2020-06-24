package com.dxc.inventoryapi.service;

import java.time.LocalDate;
import java.util.List;

import com.dxc.inventoryapi.entity.Item;
import com.dxc.inventoryapi.exception.ItemException;

public interface ItemService {

	Item add(Item item) throws ItemException;
	Item update(Item item) throws ItemException;
	boolean deleteById(int icode) throws ItemException;
	Item getById(int icode);
	List<Item> getAllItems();
	
	Item findByTitle(String title);
	List<Item> findByPackageDate(LocalDate packageDate);
	List<Item> findInPriceRange(double lowerBound,double upperBound);
	
}
