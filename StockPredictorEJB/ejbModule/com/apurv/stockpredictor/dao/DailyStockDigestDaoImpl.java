/**
 * 
 */
package com.apurv.stockpredictor.dao;

import java.util.Date;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import com.apurv.stockpredictor.entities.DailyStockDigest;
import com.apurv.stockpredictor.exceptions.DatabaseException;

/**
 * @author Apurv Amrutkar
 *
 */
@Stateless
@LocalBean
public class DailyStockDigestDaoImpl extends AbstractDaoImpl<DailyStockDigest> {

	public DailyStockDigestDaoImpl() {
		super(DailyStockDigest.class);
	}

	@PersistenceContext(unitName = "StockPredictorEJB")
	EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public DailyStockDigest findStockDigestWherePrevCloseGreaterThanCurrent(
			Date date, Long stockId) throws DatabaseException {
		try {
			return (DailyStockDigest) em
					.createNamedQuery(
							"DailyStockDigest.findStockDigestWherePrevCloseGreaterThanCurrent")
					.setParameter("stockId", stockId)
					.setParameter("beforeDate", date).setMaxResults(1)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DatabaseException(
					"Error while retrieving dailyStockDigest");
		}
	}

	public DailyStockDigest findStockDigestWherePrevCloseLessThanCurrent(
			Date date, Long stockId) throws DatabaseException {
		try {
			return (DailyStockDigest) em
					.createNamedQuery(
							"DailyStockDigest.findStockDigestWherePrevCloseLessThanCurrent")
					.setParameter("stockId", stockId)
					.setParameter("beforeDate", date)
					.setMaxResults(1)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DatabaseException(e.getMessage());
		}

	}
	
	
	public DailyStockDigest findNextDailyStockDigest(
			Date date, Long stockId) throws DatabaseException {
		try {
			return (DailyStockDigest) em
					.createNamedQuery(
							"DailyStockDigest.findNextDailyStockDigest")
					.setParameter("stockId", stockId)
					.setParameter("date", date)
					.setMaxResults(1)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DatabaseException(e.getMessage());
		}

	}
}
