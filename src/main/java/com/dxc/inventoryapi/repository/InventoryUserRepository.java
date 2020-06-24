package com.dxc.inventoryapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dxc.inventoryapi.entity.InventoryUser;

@Repository
public interface InventoryUserRepository extends JpaRepository<InventoryUser,String> {

}
