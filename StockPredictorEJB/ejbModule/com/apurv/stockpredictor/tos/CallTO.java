package com.apurv.stockpredictor.tos;

import java.util.Date;

import com.apurv.stockpredictor.util.CallType;

public class CallTO {

	private Long id;

	private StockTO stock;

	private CallType type;

	private DateTO startDate;

	private DateTO endDate;

	private float startPrice;

	private float endPrice;

	private float l0;

	private float h0;

	private float l1;

	private float h1;

	private float l2;

	private float tempPrice;

	private Date tempDate;

	public CallTO() {
		super();
	}

	public CallTO(Long stockId, Date tempDate, float price) {
		this.stock = new StockTO(stockId);
		this.tempPrice = price;
		this.tempDate = tempDate;
	}
	
	public CallTO(Long stockId, float price){
		this.stock = new StockTO(stockId);
		this.tempPrice = price;
	}

	public CallTO(Long id, StockTO stock, CallType type, DateTO startDate,
			DateTO endDate, float startPrice, float endPrice, float l0,
			float h0, float l1, float h1, float l2) {
		super();
		this.id = id;
		this.stock = stock;
		this.type = type;
		this.startDate = startDate;
		this.endDate = endDate;
		this.startPrice = startPrice;
		this.endPrice = endPrice;
		this.l0 = l0;
		this.h0 = h0;
		this.l1 = l1;
		this.h1 = h1;
		this.l2 = l2;
	}

	public Date getTempDate() {
		return tempDate;
	}

	public void setTempDate(Date tempDate) {
		this.tempDate = tempDate;
	}

	public float getTempPrice() {
		return tempPrice;
	}

	public void setTempPrice(float tempPrice) {
		this.tempPrice = tempPrice;
	}

	public DateTO getStartDate() {
		return startDate;
	}

	public void setStartDate(DateTO startDate) {
		this.startDate = startDate;
	}

	public DateTO getEndDate() {
		return endDate;
	}

	public void setEndDate(DateTO endDate) {
		this.endDate = endDate;
	}

	public StockTO getStock() {
		return stock;
	}

	public void setStock(StockTO stock) {
		this.stock = stock;
	}

	public CallType getType() {
		return type;
	}

	public void setType(CallType type) {
		this.type = type;
	}

	public float getStartPrice() {
		return startPrice;
	}

	public void setStartPrice(float startPrice) {
		this.startPrice = startPrice;
	}

	public float getEndPrice() {
		return endPrice;
	}

	public void setEndPrice(float endPrice) {
		this.endPrice = endPrice;
	}

	public float getL0() {
		return l0;
	}

	public void setL0(float l0) {
		this.l0 = l0;
	}

	public float getH0() {
		return h0;
	}

	public void setH0(float h0) {
		this.h0 = h0;
	}

	public float getL1() {
		return l1;
	}

	public void setL1(float l1) {
		this.l1 = l1;
	}

	public float getH1() {
		return h1;
	}

	public void setH1(float h1) {
		this.h1 = h1;
	}

	public float getL2() {
		return l2;
	}

	public void setL2(float l2) {
		this.l2 = l2;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
