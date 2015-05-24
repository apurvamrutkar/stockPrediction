/**
 * 
 */
package com.apurv.stockpredictor.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.apurv.stockpredictor.tos.DailyStockDigestTO;
import com.apurv.stockpredictor.tos.StockTO;
import com.apurv.stockpredictor.util.EntityTOConverter;

/**
 * Stock info
 * 
 * @author Apurv Amrutkar
 *
 */
@Entity
@Table(name = "stocks")
@NamedQueries({
	@NamedQuery(name="Stock.findBySymbol",
			query="Select s from Stock s where s.stockSymbol=:symbol"),
	@NamedQuery(name="Stock.findAllStockWithPreviousCloseLessThanCurrent",
			query="Select NEW com.apurv.stockpredictor.tos.CallTO(s.id,sd.date,sd.closePrice) from Stock s JOIN s.stockDigests sd where sd.date=:currentDate and sd.previousClose < sd.closePrice"),
			@NamedQuery(name="Stock.findAllStockWithPreviousCloseGreaterThanCurrent",
			query="Select NEW com.apurv.stockpredictor.tos.CallTO(s.id,sd.date,sd.closePrice) from Stock s JOIN s.stockDigests sd where sd.date=:currentDate and sd.previousClose > sd.closePrice")
	
})
public class Stock extends Base{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "stock_id")
	private Long id;

	@Column(name = "stock_symbol", nullable = false, length = 30,unique=true)
	private String stockSymbol;

	@OneToMany(mappedBy = "stock",cascade=CascadeType.ALL)
	private List<DailyStockDigest> stockDigests;

	public Stock() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Stock(Long id, String stockSymbol,
			List<DailyStockDigest> stockDigests) {
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

	public List<DailyStockDigest> getStockDigests() {
		return stockDigests;
	}

	public void setStockDigests(List<DailyStockDigest> stockDigests) {
		this.stockDigests = stockDigests;
	}
	
	public void addStockDigest(DailyStockDigest stockDigest){
		if(stockDigests==null){
			stockDigests = new ArrayList<>();
		}
		stockDigest.setStock(this);
		stockDigests.add(stockDigest);
	}

	public Long getId() {
		return id;
	}

	@Override
	public Object convertToTO() {
		StockTO stockTO = new StockTO(id, stockSymbol, new EntityTOConverter<DailyStockDigest, DailyStockDigestTO>().convertToTOList(stockDigests));
		return stockTO;
	}

	@Override
	public Object convertToTOLazy() {
		StockTO stockTO = new StockTO(id);
		return stockTO;
	}
	
	

}
