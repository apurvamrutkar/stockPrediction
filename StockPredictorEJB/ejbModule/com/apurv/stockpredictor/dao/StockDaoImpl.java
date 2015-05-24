/**
 * 
 */
package com.apurv.stockpredictor.dao;

import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import com.apurv.stockpredictor.entities.Stock;
import com.apurv.stockpredictor.exceptions.DatabaseException;
import com.apurv.stockpredictor.tos.CallTO;

/**
 * @author Apurv Amrutkar
 *
 */
@Stateless
@LocalBean
public class StockDaoImpl extends AbstractDaoImpl<Stock> {

	public StockDaoImpl() {
		super(Stock.class);
	}

	@PersistenceContext(unitName = "StockPredictorEJB")
	EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public Stock findBySymbol(String symbol) throws DatabaseException {
		try {
			return (Stock) em.createNamedQuery("Stock.findBySymbol")
					.setParameter("symbol", symbol)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DatabaseException(
					"Error while finding stock by symbol for " + symbol);
		}
	}

	public List<CallTO> findAllStockWithPreviousCloseLessThanCurrent(Date currentDate) throws DatabaseException {
		try {
			return (List<CallTO>) em.createNamedQuery("Stock.findAllStockWithPreviousCloseLessThanCurrent")
					.setParameter("currentDate", currentDate)
					.getResultList();
		}catch(Exception e){
			e.printStackTrace();
			throw new DatabaseException("error while retrieving findAllStockWithPreviousCloseLessThanCurrentDate");
		}
	}

	public List<CallTO> findAllStockWithPreviousCloseGreaterThanCurrent(
			Date currentDate) throws DatabaseException {
		try {
			return (List<CallTO>) em.createNamedQuery("Stock.findAllStockWithPreviousCloseGreaterThanCurrent")
					.setParameter("currentDate", currentDate)
					.getResultList();
		}catch(Exception e){
			e.printStackTrace();
			throw new DatabaseException("error while retrieving findAllStockWithPreviousCloseGreaterThanCurrentDate");
		}
	}


}
