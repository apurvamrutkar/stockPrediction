package com.apurv.stockpredictor.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.apurv.stockpredictor.tos.CallTO;
import com.apurv.stockpredictor.tos.DateTO;
import com.apurv.stockpredictor.tos.StockTO;
import com.apurv.stockpredictor.util.CallType;

@Entity
@Table(name = "calls")
public class Call extends Base {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "call_id")
	private Long id;

	@OneToOne
	@JoinColumn(name = "stock_id", referencedColumnName = "stock_id")
	private Stock stock;

	@Enumerated(EnumType.STRING)
	@Column(name = "type", length = 20)
	private CallType type;

	@Temporal(TemporalType.DATE)
	@Column(name = "start_date")
	private Date startDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "end_date")
	private Date endDate;

	@Column(name = "start_price", precision = 20, scale = 2)
	private float startPrice;

	@Column(name = "end_price", precision = 20, scale = 2)
	private float endPrice;

	@Column(name = "l0", precision = 20, scale = 2)
	private float l0;

	@Column(name = "h0", precision = 20, scale = 2)
	private float h0;

	@Column(name = "l1", precision = 20, scale = 2)
	private float l1;

	@Column(name = "h1", precision = 20, scale = 2)
	private float h1;

	@Column(name = "l2", precision = 20, scale = 2)
	private float l2;

	public Call() {
		super();
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Stock getStock() {
		return stock;
	}

	public void setStock(Stock stock) {
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

	@Override
	public Object convertToTO() {
		CallTO callTO = new CallTO(id, (StockTO) stock.convertToTOLazy(), type,
				new DateTO(startDate), new DateTO(endDate), startPrice,
				endPrice, l0, h0, l1, h1, l2);
		return callTO;
	}

	@Override
	public Object convertToTOLazy() {
		CallTO callTO = new CallTO();
		callTO.setId(id);
		return callTO;
	}

	@Override
	public String toString() {
		return "Call [stock=" + stock.getStockSymbol() + ", type=" + type + ", startDate="
				+ startDate + ", endDate=" + endDate + ", startPrice="
				+ startPrice + ", endPrice=" + endPrice + ", l0=" + l0
				+ ", h0=" + h0 + ", l1=" + l1 + ", h1=" + h1 + ", l2=" + l2
				+ "]";
	}

	
}
