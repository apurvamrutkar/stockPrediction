/**
 * 
 */
package com.apurv.stockpredictor.interfaces;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import javax.ejb.Local;

import com.apurv.stockpredictor.exceptions.BusinessException;

/**
 * @author Apurv Amrutkar
 *
 */
@Local
public interface StockServiceLocal {

	public void initialStockDatabaseFillUp(Date startDate, Date endDate) throws BusinessException;

	public void fillDatabaseForStockFromFile(String symbol, File tempFile,
			Date startDate, Date endDate) throws BusinessException;

	String hitNseURL(String symbol,String fromDate, String toDate) throws BusinessException;

	void createBuyCalls(Calendar calendar) throws BusinessException;

	void createSellCalls(Calendar calendar) throws BusinessException;
}
