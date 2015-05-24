/**
 * 
 */
package com.apurv.stockpredictor.sei;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.annotation.PostConstruct;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.apurv.stockpredictor.exceptions.BusinessException;
import com.apurv.stockpredictor.interfaces.StockServiceLocal;
import com.owlike.genson.Genson;

/**
 * @author Apurv Amrutkar
 *
 */
@Stateless
@LocalBean
@Path("/StockPredictorWebService")
@Consumes("application/json")
@TransactionManagement(TransactionManagementType.CONTAINER)
public class StockPredictorWebService{
	
	@EJB
	private StockServiceLocal stockService;
	/**
    * To not send 'null' characters in JSON output, by serializing using this genson context
    */
    private Genson genson;

    @SuppressWarnings("deprecation")
    @PostConstruct
    public void initializeGenson() {
        genson = new Genson.Builder().setSkipNull(true).create();
    }
    
    @GET
    @Path("/initialDatabaseFill")
    @Asynchronous
    public void fillDatabase(@QueryParam("startDate") String startDateFormat, @QueryParam("endDate") String endDateFormat) throws BusinessException, ParseException{
    	SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    	Date startDate = dateFormat.parse(startDateFormat);
    	Date endDate = dateFormat.parse(endDateFormat);
    	//File f = new File("/root/Finance_App/Stock_temp_data/AXISBANK.csv");
    	System.out.println(stockService);
    	stockService.initialStockDatabaseFillUp(startDate, endDate);
    	//stockService.fillDatabaseForStockFromFile("AXISBANK", f, startDate, endDate);
    	//Response r = Response.ok().build();
    	return;
    }
    
    @GET
    @Path("/createBuyCalls")
    @Asynchronous
    public void buyCallCreation(@QueryParam("buyCallForDate") String callDateFormat) throws BusinessException, ParseException{
    	SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    	Date callDate = dateFormat.parse(callDateFormat);
    	Calendar calendar = new GregorianCalendar();
    	calendar.setTime(callDate);
    	//File f = new File("/root/Finance_App/Stock_temp_data/AXISBANK.csv");
    	stockService.createBuyCalls(calendar);
    	//stockService.fillDatabaseForStockFromFile("AXISBANK", f, startDate, endDate);
    	//Response r = Response.ok().build();
    	return;
    }
    
    @GET
    @Path("/createSellCalls")
    @Asynchronous
    public void sellCallCreation(@QueryParam("sellCallForDate") String callDateFormat) throws BusinessException, ParseException{
    	SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    	Date callDate = dateFormat.parse(callDateFormat);
    	Calendar calendar = new GregorianCalendar();
    	calendar.setTime(callDate);
    	//File f = new File("/root/Finance_App/Stock_temp_data/AXISBANK.csv");
    	stockService.createSellCalls(calendar);
    	//stockService.fillDatabaseForStockFromFile("AXISBANK", f, startDate, endDate);
    	//Response r = Response.ok().build();
    	return;
    }
}
