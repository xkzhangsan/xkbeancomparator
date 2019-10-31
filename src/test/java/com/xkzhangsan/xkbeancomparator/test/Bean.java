package com.xkzhangsan.xkbeancomparator.test;

import java.math.BigDecimal;
import java.util.Date;

public class Bean {

	private Integer id;
	private String name;
	private Date date;
	private BigDecimal bigD;
	
	private boolean isFull;
	private boolean started;

	public boolean isFull() {
		return isFull;
	}

	public void setFull(boolean isFull) {
		this.isFull = isFull;
	}

	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean started) {
		this.started = started;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public BigDecimal getBigD() {
		return bigD;
	}

	public void setBigD(BigDecimal bigD) {
		this.bigD = bigD;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
