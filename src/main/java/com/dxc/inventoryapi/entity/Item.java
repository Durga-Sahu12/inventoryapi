package com.dxc.inventoryapi.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Entity
@Table(name = "items")
public class Item implements Serializable, Comparable<Item> {

	@Id
	@Column(name = "icode")
	@NotNull(message = "icode can not be blank")
	private Integer icode;

	@Column(name = "title", nullable = false)
	@NotBlank(message = "ttile can not be blank")
	@Size(min = 5, max = 45, message = "title must be of 5 to 45 chars in length")
	private String title;

	@Column(name = "price", nullable = false)
	@NotNull(message = "price can not be blank")
	@Min(value = 100, message = "price is expected to be not less than 100")
	@Max(value = 25000, message = "price is expected not more than 25000")
	private double price;

	@Column(name = "pdate", nullable = true)
	@NotNull(message = "date can not be blank")
	@PastOrPresent(message = "Package Date can not be a future date")
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate packageDate;

	public Item() {
		// left unimplemented
	}

	public Item(int icode, String title, double price, LocalDate packageDate) {
		super();
		this.icode = icode;
		this.title = title;
		this.price = price;
		this.packageDate = packageDate;
	}

	public Integer getIcode() {
		return icode;
	}

	public void setIcode(Integer icode) {
		this.icode = icode;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public LocalDate getPackageDate() {
		return packageDate;
	}

	public void setPackageDate(LocalDate packageDate) {
		this.packageDate = packageDate;
	}

	@Override
	public int compareTo(Item arg0) {
		return icode.compareTo(arg0.icode);
	}

	@Override
	public boolean equals(Object obj) {
		return (obj instanceof Item) ? icode.equals(((Item) obj).icode) : false;
	}

	@Override
	public String toString() {
		return "Item [icode=" + icode + ", title=" + title + ", price=" + price + ", packageDate=" + packageDate + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(icode);
	}
}