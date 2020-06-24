package com.dxc.inventoryapi.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="inventory_users")
public class InventoryUser implements Serializable
{
	@Id
	@Column(name="num")
	private String userName;
	
	@Transient
	//@JsonIgnore   //neither which stored in server nd nor shared any one of them.
	private String password;
	
	@Column(name="pwd")
	@JsonIgnore
	private String encodedPassword;
	
	public InventoryUser() {
		//left blank
	}

	public InventoryUser(String userName, String password, String encodedPassword) {
		super();
		this.userName = userName;
		this.password = password;
		this.encodedPassword = encodedPassword;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEncodedPassword() {
		return encodedPassword;
	}

	public void setEncodedPassword(String encodedPassword) {
		this.encodedPassword = encodedPassword;
	}
	
	
	

}
