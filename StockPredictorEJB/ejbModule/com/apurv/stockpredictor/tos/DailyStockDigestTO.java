package com.apurv.stockpredictor.tos;

/**
 * Entity implementation class for Entity: DailyStockDigest
 *
 */
public class DailyStockDigestTO {

	private Long id;

	private StockTO stock;

	private DateTO date;

	private float previousClose;

	private float openPrice;

	private float highPrice;

	private float lowPrice;

	private float lastPrice;

	private float closePrice;

	private float avgPrice;

	private long totalTradedQuantity;

	private float turnoverInLacs;

	private long noOfTrades;

	private long deliverableQuantity;

	private float percentDelToTraded;

	public DailyStockDigestTO() {
		super();
	}

	public DailyStockDigestTO(StockTO stock, DateTO date, Float previousClose,
			Float openPrice, Float highPrice, Float lowPrice, Float lastPrice,
			Float closePrice, Float avgPrice, Long totalTradedQuantity,
			Float turnoverInLacs, Long noOfTrades, Long deliverableQuantity,
			Float percentDelToTraded) {
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

	public StockTO getStock() {
		return stock;
	}

	public void setStock(StockTO stock) {
		this.stock = stock;
	}

	public DateTO getDate() {
		return date;
	}

	public void setDate(DateTO date) {
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

	public void setId(Long id) {
		this.id = id;
	}

}
