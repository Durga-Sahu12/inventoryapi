package com.dxc.inventoryapi.service;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dxc.inventoryapi.entity.Item;
import com.dxc.inventoryapi.exception.ItemException;
import com.dxc.inventoryapi.repository.ItemRepository;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private ItemRepository itemRepo;
	
	@Transactional
	@Override
	public Item add(Item item) throws ItemException {
		if(item!=null) {
			if(itemRepo.existsById(item.getIcode())) {
				throw new ItemException("An item with the icode "+item.getIcode()+" already exists!");
			}
			
			itemRepo.save(item);
		}
		return item;
	}

	@Transactional
	@Override
	public Item update(Item item) throws ItemException {
		if(item!=null) {
			if(!itemRepo.existsById(item.getIcode())) {
				throw new ItemException("No Such Item Found To Update!");
			}
			
			itemRepo.save(item);
		}
		return item;
	}

	@Transactional
	@Override
	public boolean deleteById(int icode) throws ItemException {
		boolean deleted=false;
		if(!itemRepo.existsById(icode)) {
			throw new ItemException("No Such Item Found To Delete!");
		}
		
		itemRepo.deleteById(icode);
		deleted=true;
		
		return deleted;
	}

	@Override
	public Item getById(int icode) {
		return itemRepo.findById(icode).orElse(null);
	}

	@Override
	public List<Item> getAllItems() {
		return itemRepo.findAll();
	}

	@Override
	public Item findByTitle(String title) {
		return itemRepo.findByTitle(title);
	}

	@Override
	public List<Item> findByPackageDate(LocalDate packageDate) {
		return itemRepo.findAllByPackageDate(packageDate);
	}

	@Override
	public List<Item> findInPriceRange(double lowerBound, double upperBound) {
		return itemRepo.getAllInPriceRange(lowerBound, upperBound);
	}

}
