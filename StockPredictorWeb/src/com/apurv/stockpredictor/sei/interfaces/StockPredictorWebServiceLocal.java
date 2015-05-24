/**
 * 
 */
package com.apurv.stockpredictor.sei.interfaces;

import java.text.ParseException;

import javax.ejb.Local;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.apurv.stockpredictor.exceptions.BusinessException;

/**
 * @author Apurv Amrutkar
 *
 */
@Local
public interface StockPredictorWebServiceLocal {

    public Response fillDatabase(@QueryParam("startDate") String startDateFormat, @QueryParam("endDate") String endDateFormat) throws BusinessException, ParseException;
}
