package com.apurv.stockpredictor.dao;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.apurv.stockpredictor.entities.Call;

@Stateless
@LocalBean
public class CallDaoImpl extends AbstractDaoImpl<Call> {

	public CallDaoImpl() {
		super(Call.class);
	}

	@PersistenceContext(unitName = "StockPredictorEJB")
	EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
}
