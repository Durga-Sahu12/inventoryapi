package com.dxc.inventoryapi.entity;

import java.io.Serializable;

public class JwtToken implements Serializable  {

	private String jwtToken;

	public JwtToken(String jwtToken) {
		super();
		this.jwtToken = jwtToken;
	}

	public String getJwtToken() {
		return jwtToken;
	}

	public void setJwtToken(String jwtToken) {
		this.jwtToken = jwtToken;
	}
	
	
}
