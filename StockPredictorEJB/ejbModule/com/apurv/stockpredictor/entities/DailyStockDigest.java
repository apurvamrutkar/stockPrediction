package com.apurv.stockpredictor.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.apurv.stockpredictor.tos.DailyStockDigestTO;
import com.apurv.stockpredictor.tos.DateTO;
import com.apurv.stockpredictor.tos.StockTO;

/**
 * Entity implementation class for Entity: DailyStockDigest
 *
 */
@Entity
@Table(name = "daily_stock_digests")
@NamedQueries({
	@NamedQuery(name="DailyStockDigest.findStockDigestWherePrevCloseGreaterThanCurrent",
			query="Select d from DailyStockDigest d where d.stock.id=:stockId and d.date<:beforeDate and d.previousClose>d.closePrice "
					+ " ORDER BY d.date DESC"),
	@NamedQuery(name="DailyStockDigest.findNextDailyStockDigest",
			query="select ds from DailyStockDigest ds "
					+ "where ds.stock.id=:stockId and ds.date>:date and "
					+ "ds.date=(Select min(dsd.date) from DailyStockDigest dsd "
					+ "where dsd.stock.id=:stockId and dsd.date>:date)"),
	@NamedQuery(name="DailyStockDigest.findStockDigestWherePrevCloseLessThanCurrent",
			query="Select d from DailyStockDigest d where d.stock.id=:stockId and d.date<:beforeDate and d.previousClose < d.closePrice "
					+ "ORDER BY d.date DESC")
})
public class DailyStockDigest extends Base{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "daily_stock_digest_id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "stock_id", referencedColumnName = "stock_id")
	private Stock stock;

	@Temporal(TemporalType.DATE)
	@Column(name = "date")
	private Date date;

	@Column(name = "previous_close", precision = 10, scale = 3)
	private float previousClose;

	@Column(name = "open_price", precision = 10, scale = 3)
	private float openPrice;

	@Column(name = "high_price", precision = 10, scale = 3)
	private float highPrice;

	@Column(name = "low_price", precision = 10, scale = 3)
	private float lowPrice;

	@Column(name = "last_price", precision = 10, scale = 3)
	private float lastPrice;

	@Column(name = "close_price", precision = 10, scale = 3)
	private float closePrice;

	@Column(name = "avg_price", precision = 10, scale = 3)
	private float avgPrice;

	@Column(name = "total_traded_quantity", precision = 15)
	private long totalTradedQuantity;

	@Column(name = "turnover_in_lacs", precision = 10, scale = 3)
	private float turnoverInLacs;

	@Column(name = "no_of_trades", precision = 15)
	private long noOfTrades;

	@Column(name = "deliverable_quantity", precision = 15)
	private long deliverableQuantity;

	@Column(name = "percent_delv_to_traded_qty", precision = 3, scale = 3)
	private float percentDelToTraded;

	public DailyStockDigest() {
		super();
	}

	public DailyStockDigest(Stock stock, Date date,
			Float previousClose, Float openPrice, Float highPrice,
			Float lowPrice, Float lastPrice, Float closePrice, Float avgPrice,
			Long totalTradedQuantity, Float turnoverInLacs, Long noOfTrades,
			Long deliverableQuantity, Float percentDelToTraded) {
		super();
		this.stock = stock;
		this.date = date;
		this.previousClose = previousClose;
		this.openPrice = openPrice;
		this.highPrice = highPrice;
		this.lowPrice = lowPrice;
		this.lastPrice = lastPrice;
		this.closePrice = closePrice;
		this.avgPrice = avgPrice;
		this.totalTradedQuantity = totalTradedQuantity;
		this.turnoverInLacs = turnoverInLacs;
		this.noOfTrades = noOfTrades;
		this.deliverableQuantity = deliverableQuantity;
		this.percentDelToTraded = percentDelToTraded;
	}

	public Stock getStock() {
		return stock;
	}

	public void setStock(Stock stock) {
		this.stock = stock;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public float getPreviousClose() {
		return previousClose;
	}

	public void setPreviousClose(float previousClose) {
		this.previousClose = previousClose;
	}

	public float getOpenPrice() {
		return openPrice;
	}

	public void setOpenPrice(float openPrice) {
		this.openPrice = openPrice;
	}

	public float getHighPrice() {
		return highPrice;
	}

	public void setHighPrice(float highPrice) {
		this.highPrice = highPrice;
	}

	public float getLowPrice() {
		return lowPrice;
	}

	public void setLowPrice(float lowPrice) {
		this.lowPrice = lowPrice;
	}

	public float getLastPrice() {
		return lastPrice;
	}

	public void setLastPrice(float lastPrice) {
		this.lastPrice = lastPrice;
	}

	public float getClosePrice() {
		return closePrice;
	}

	public void setClosePrice(float closePrice) {
		this.closePrice = closePrice;
	}

	public float getAvgPrice() {
		return avgPrice;
	}

	public void setAvgPrice(float avgPrice) {
		this.avgPrice = avgPrice;
	}

	public long getTotalTradedQuantity() {
		return totalTradedQuantity;
	}

	public void setTotalTradedQuantity(long totalTradedQuantity) {
		this.totalTradedQuantity = totalTradedQuantity;
	}

	public float getTurnoverInLacs() {
		return turnoverInLacs;
	}

	public void setTurnoverInLacs(float turnoverInLacs) {
		this.turnoverInLacs = turnoverInLacs;
	}

	public long getNoOfTrades() {
		return noOfTrades;
	}

	public void setNoOfTrades(long noOfTrades) {
		this.noOfTrades = noOfTrades;
	}

	public long getDeliverableQuantity() {
		return deliverableQuantity;
	}

	public void setDeliverableQuantity(long deliverableQuantity) {
		this.deliverableQuantity = deliverableQuantity;
	}

	public float getPercentDelToTraded() {
		return percentDelToTraded;
	}

	public void setPercentDelToTraded(float percentDelToTraded) {
		this.percentDelToTraded = percentDelToTraded;
	}

	public Long getId() {
		return id;
	}

	@Override
	public Object convertToTO() {
		DailyStockDigestTO dailyStockDigestTO = new DailyStockDigestTO((StockTO)stock.convertToTOLazy(), new DateTO(date), previousClose, openPrice, highPrice, lowPrice, lastPrice, closePrice, avgPrice, totalTradedQuantity, turnoverInLacs, noOfTrades, deliverableQuantity, percentDelToTraded);
		dailyStockDigestTO.setId(id);
		return dailyStockDigestTO;
	}

	@Override
	public Object convertToTOLazy() {
		DailyStockDigestTO dailyStockDigestTO = new DailyStockDigestTO();
		dailyStockDigestTO.setId(id);
		return dailyStockDigestTO;
	}
	
	

}
