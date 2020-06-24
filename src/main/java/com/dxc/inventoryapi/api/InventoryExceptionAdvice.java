package com.dxc.inventoryapi.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dxc.inventoryapi.exception.ItemException;

@RestControllerAdvice
public class InventoryExceptionAdvice {

	@ExceptionHandler(ItemException.class)
	public ResponseEntity<String> handleItemException(ItemException exception){
		return new ResponseEntity<>(exception.getMessage(),HttpStatus.BAD_REQUEST);
	}
}
