/**
 * 
 */
package com.apurv.stockpredictor.tos;

import java.util.ArrayList;
import java.util.List;

/**
 * Stock info
 * 
 * @author Apurv Amrutkar
 *
 */
public class StockTO {

	private Long id;

	private String stockSymbol;

	private List<DailyStockDigestTO> stockDigests;

	public StockTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public StockTO(Long id) {
		super();
		this.id = id;
	}

	public StockTO(Long id, String stockSymbol,
			List<DailyStockDigestTO> stockDigests) {
		super();
		this.id = id;
		this.stockSymbol = stockSymbol;
		this.stockDigests = stockDigests;
	}

	public String getStockSymbol() {
		return stockSymbol;
	}

	public void setStockSymbol(String stockSymbol) {
		this.stockSymbol = stockSymbol;
	}

	public List<DailyStockDigestTO> getStockDigests() {
		return stockDigests;
	}

	public void setStockDigests(List<DailyStockDigestTO> stockDigests) {
		this.stockDigests = stockDigests;
	}

	public void addStockDigest(DailyStockDigestTO stockDigest) {
		if (stockDigests == null) {
			stockDigests = new ArrayList<>();
		}
		stockDigest.setStock(this);
		stockDigests.add(stockDigest);
	}

	public Long getId() {
		return id;
	}

}
