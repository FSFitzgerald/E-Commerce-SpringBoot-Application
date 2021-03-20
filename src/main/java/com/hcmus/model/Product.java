package com.hcmus.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "Product")
public class Product {
	@Id
	@GeneratedValue
	@Column(name = "Id")
	private int id;
	@Column(name = "Name")
	private String name;
	@Column(name = "Description")
	@Lob
	private String description;
	@Column(name = "ImageUrl")
	private String imageUrl;
	@Embedded
	private Price price;
	@Column(name = "CreatedAt")
	@Temporal(TemporalType.DATE)
	private Date createdAt;
	
	
}
