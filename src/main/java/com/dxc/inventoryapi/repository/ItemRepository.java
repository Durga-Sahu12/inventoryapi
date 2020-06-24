package com.dxc.inventoryapi.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dxc.inventoryapi.entity.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer>{

	Item findByTitle(String title);
	
	List<Item> findAllByPackageDate(LocalDate packageDate);
	
	@Query("SELECT i FROM Item i WHERE i.price between :lowerBound and :upperBound")
	List<Item> getAllInPriceRange(double lowerBound,double upperBound);
}