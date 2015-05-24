/**
 * 
 */
package com.apurv.stockpredictor.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.xml.ws.http.HTTPException;

import com.apurv.stockpredictor.dao.CallDaoImpl;
import com.apurv.stockpredictor.dao.DailyStockDigestDaoImpl;
import com.apurv.stockpredictor.dao.StockDaoImpl;
import com.apurv.stockpredictor.entities.Call;
import com.apurv.stockpredictor.entities.DailyStockDigest;
import com.apurv.stockpredictor.entities.Stock;
import com.apurv.stockpredictor.exceptions.BusinessException;
import com.apurv.stockpredictor.exceptions.DatabaseException;
import com.apurv.stockpredictor.interfaces.StockServiceLocal;
import com.apurv.stockpredictor.tos.CallTO;
import com.apurv.stockpredictor.util.CallType;

/**
 * @author Apurv Amrutkar
 *
 */
@Stateless
public class StockService implements StockServiceLocal {

	@EJB
	private StockServiceLocal stockService;

	@EJB
	private StockDaoImpl stockDaoImpl;

	@EJB
	private DailyStockDigestDaoImpl dailyStockDigestDaoImpl;

	@EJB
	private CallDaoImpl callDaoImpl;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.apurv.stockpredictor.interfaces.StockServiceLocal#
	 * initialStockDatabaseFillUp()
	 */
	@Override
	public void initialStockDatabaseFillUp(Date startDate, Date endDate) throws BusinessException {
		String csvFile = "/root/Finance_App/Stock_temp_data/nse_equity_stocks.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		String cookies;
		try {

			br = new BufferedReader(new FileReader(csvFile));
			String[] stockSymbols = null;
			while ((line = br.readLine()) != null) {

				// use comma as separator
				stockSymbols = line.split(cvsSplitBy);
				String urlBuilder = "http://www.nse-india.com/content/equities/scripvol/datafiles/";
				String endUrl = "ALLN.csv";
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				String startDateFormat = dateFormat.format(startDate);
				String endDateFormat = dateFormat.format(endDate);
				urlBuilder += startDateFormat + "-TO-" + endDateFormat;
				String tempFilePath = "/root/Finance_App/Stock_temp_data/temp.csv";
				File tempFile = new File(tempFilePath);

				for (String symbol : stockSymbols) {
					urlBuilder += symbol + endUrl;
					// System.out.println(urlBuilder);
					// hit Nse url before download
					cookies = stockService.hitNseURL(symbol, startDateFormat, endDateFormat);
					// URL url = new URL(urlBuilder);
					// FileUtils.copyURLToFile(url, tempFile);
					download(urlBuilder, tempFilePath, cookies);
					stockService.fillDatabaseForStockFromFile(symbol, tempFile, startDate, endDate);
				}

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	@Override
	public void fillDatabaseForStockFromFile(String symbol, File tempFile, Date startDate, Date endDate) throws BusinessException {
		System.out.println("Enter stockDailyDigest for symbol:" + symbol);
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		Date tempDate;
		String tempLine;
		try {

			br = new BufferedReader(new FileReader(tempFile));
			String[] stockDetails = null;
			Stock stock = null;
			// get stock from DB or create a new one
			try {
				stock = stockDaoImpl.findBySymbol(symbol);

			} catch (DatabaseException e) {
				e.printStackTrace();
				throw new BusinessException(e.getMessage());
			}
			if (stock == null) {
				stock = new Stock();
				stock.setStockSymbol(symbol);

			}
			DailyStockDigest dailyStockDigest = null;
			// read first line by default which is column names
			line = br.readLine();
			// then parse rest of the file
			while ((line = br.readLine()) != null) {
				tempLine = line.replaceAll("\"", "");

				stockDetails = tempLine.split(cvsSplitBy);
				try {
					tempDate = dateFormat.parse(stockDetails[2]);
				} catch (ParseException e1) {
					e1.printStackTrace();
					throw new BusinessException("Unable to parse date " + stockDetails[2]);
				}
				if (stockDetails[0].equals(symbol) && (tempDate.compareTo(startDate) >= 0) && (tempDate.compareTo(endDate) <= 0)
						&& stockDetails.length == 15) {

					dailyStockDigest = new DailyStockDigest(stock, tempDate, Float.parseFloat(stockDetails[3].trim().replaceAll("-", "0.0")),
							Float.parseFloat(stockDetails[4].trim().replaceAll("-", "0.0")), Float.parseFloat(stockDetails[5].trim().replaceAll("-",
									"0.0")), Float.parseFloat(stockDetails[6].trim().replaceAll("-", "0.0")), Float.parseFloat(stockDetails[7].trim()
									.replaceAll("-", "0.0")), Float.parseFloat(stockDetails[8].trim().replaceAll("-", "0.0")),
							Float.parseFloat(stockDetails[9].trim().replaceAll("-", "0.0")), Long.parseLong(stockDetails[10].trim().replaceAll("-",
									"0")), Float.parseFloat(stockDetails[11].trim().replaceAll("-", "0.0")), Long.parseLong(stockDetails[12].trim()
									.replaceAll("-", "0")), Long.parseLong(stockDetails[13].trim().replaceAll("-", "0")),
							Float.parseFloat(stockDetails[14].trim().replaceAll("-", "0.0")));
					stock.addStockDigest(dailyStockDigest);

				}
			}

			try {
				if (stock.getId() == null || stock.getId() <= 0)
					stockDaoImpl.create(stock);
				else
					stockDaoImpl.edit(stock);
			} catch (DatabaseException e) {
				e.printStackTrace();
				System.out.println("Error while adding data for symbol:" + symbol);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public String hitNseURL(String symbol, String fromDate, String toDate) throws BusinessException {
		String encodedSymbol;
		try {
			encodedSymbol = URLEncoder.encode(symbol, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			throw new BusinessException(e1.getMessage());
		}
		String targetUrl = "http://www.nse-india.com/products/dynaContent/common/productsSymbolMapping.jsp?symbol=" + encodedSymbol
				+ "&segmentLink=3&series=ALL&dateRange=+&fromDate=" + fromDate + "&toDate=" + toDate + "&dataType=priceVolumeDeliverable";
		System.out.println("hitting Nse URL:" + targetUrl + " ....");
		URL url;
		try {
			url = new URL(targetUrl);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new BusinessException(e.getMessage());
		}
		HttpURLConnection connection;
		try {
			connection = (HttpURLConnection) url.openConnection();
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessException(e.getMessage());
		}
		try {
			connection.setRequestMethod("GET");
		} catch (ProtocolException e) {
			e.printStackTrace();
			throw new BusinessException(e.getMessage());
		}
		// connection.setRequestProperty("Connection", "keep-alive");
		int responseCode;
		try {
			responseCode = connection.getResponseCode();
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessException(e.getMessage());
		}
		if (responseCode != 200) {
			throw new HTTPException(responseCode);
		}
		System.out.println("ResponseCode:" + responseCode);

		return connection.getHeaderField("Set-Cookie");
	}

	/**
	 * Method downloads file from URL to a given directory.
	 * 
	 * @param fileURL
	 *            - file URL to download
	 * @param destinationDirectory
	 *            - directory to download file to
	 * @throws IOException
	 */
	private void download(String fileURL, String downloadedFileName, String cookies) throws IOException {
		// File name that is being downloaded
		// String downloadedFileName =
		// fileURL.substring(fileURL.lastIndexOf("/")+1);

		// Open connection to the file
		URL url = new URL(fileURL);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestProperty("Cookie", cookies);
		InputStream is = connection.getInputStream();
		// Stream to the destionation file
		FileOutputStream fos = new FileOutputStream(downloadedFileName);

		// Read bytes from URL to the local file
		byte[] buffer = new byte[4096];
		int bytesRead = 0;

		// System.out.print("Downloading " + downloadedFileName);
		while ((bytesRead = is.read(buffer)) != -1) {
			// System.out.print("."); // Progress bar :)
			fos.write(buffer, 0, bytesRead);
		}
		System.out.println("done!");

		// Close destination stream
		fos.close();
		// Close URL stream
		is.close();
	}

	@Override
	public void createBuyCalls(Calendar calendar) throws BusinessException {
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		if (calendar.get(Calendar.DAY_OF_WEEK) == 7 || calendar.get(Calendar.DAY_OF_WEEK) == 1 || calendar.get(Calendar.DAY_OF_WEEK) == 2) {
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
		} else {
			calendar.add(Calendar.DATE, -1);
		}

		List<CallTO> firstFilterStocks;
		try {
			firstFilterStocks = stockDaoImpl.findAllStockWithPreviousCloseLessThanCurrent(calendar.getTime());
		} catch (DatabaseException e) {
			e.printStackTrace();
			throw new BusinessException(e.getMessage());
		}
		// hashmap of stockId and call and call with entry of only h0 as this is
		// just a BUY call
		HashMap<Long, Call> mapOfStockAndCall = new HashMap<>();
		// hashmap of stockId and last pivot point stockDigest date
		HashMap<Long, Date> mapOfStockAndPivotDate = new HashMap<>();

		// next DailyStock digest for getting next close price
		DailyStockDigest nextDailyStockDigest = null;

		Call tempCall = null;
		Stock tempStock = null;
		for (CallTO c : firstFilterStocks) {
			tempCall = new Call();
			tempStock = stockDaoImpl.find(c.getStock().getId());
			tempCall.setStock(tempStock);
			tempCall.setType(CallType.BUY);
			tempCall.setStartDate(c.getTempDate());
			tempCall.setH0(c.getTempPrice());
			mapOfStockAndCall.put(tempStock.getId(), tempCall);
			mapOfStockAndPivotDate.put(tempStock.getId(), c.getTempDate());
		}
		// for each stock find the l1 price
		DailyStockDigest tempDailyStockDigest = null;
		for (Long stockId : mapOfStockAndPivotDate.keySet()) {
			// l1
			try {
				tempDailyStockDigest = dailyStockDigestDaoImpl.findStockDigestWherePrevCloseGreaterThanCurrent(mapOfStockAndPivotDate.get(stockId),
						stockId);
			} catch (DatabaseException e) {
				e.printStackTrace();
				throw new BusinessException(e.getMessage());
			}
			if (tempDailyStockDigest != null) {
				try {
					nextDailyStockDigest = dailyStockDigestDaoImpl.findNextDailyStockDigest(tempDailyStockDigest.getDate(), stockId);
				} catch (DatabaseException e1) {
					e1.printStackTrace();
					throw new BusinessException(e1.getMessage());
				}
				// check if l1 is atleast 1% less than its next close price
				// so that we do not get straight line in graph
				if (tempDailyStockDigest.getClosePrice() <= (nextDailyStockDigest.getClosePrice() * 0.99)) {
					tempCall = mapOfStockAndCall.get(stockId);
					mapOfStockAndPivotDate.put(stockId, tempDailyStockDigest.getDate());
					tempCall.setL1(tempDailyStockDigest.getClosePrice());
					// h1

					try {
						tempDailyStockDigest = dailyStockDigestDaoImpl.findStockDigestWherePrevCloseLessThanCurrent(
								mapOfStockAndPivotDate.get(stockId), stockId);
					} catch (DatabaseException e) {
						e.printStackTrace();
						throw new BusinessException(e.getMessage());
					}
					if (tempDailyStockDigest != null) {
						try {
							nextDailyStockDigest = dailyStockDigestDaoImpl.findNextDailyStockDigest(tempDailyStockDigest.getDate(), stockId);
						} catch (DatabaseException e1) {
							e1.printStackTrace();
							throw new BusinessException(e1.getMessage());
						}
						// check if h1 is atleast 1% greater than its next close
						// price
						// so that we do not get straight line in graph
						if (tempDailyStockDigest.getClosePrice() >= (nextDailyStockDigest.getClosePrice() * 1.01)) {
							// h1 should be >= -2% of h0 and less than h0
							if ((tempDailyStockDigest.getClosePrice() <= (tempCall.getH0()))
									&& (tempDailyStockDigest.getClosePrice() >= (tempCall.getH0() * 0.98)) 
									&& (tempDailyStockDigest.getClosePrice() <= (tempCall.getH0() * 0.99))) {
								tempCall.setH1(tempDailyStockDigest.getClosePrice());
								mapOfStockAndPivotDate.put(stockId, tempDailyStockDigest.getDate());

								// l2
								try {
									tempDailyStockDigest = dailyStockDigestDaoImpl.findStockDigestWherePrevCloseGreaterThanCurrent(
											mapOfStockAndPivotDate.get(stockId), stockId);
								} catch (DatabaseException e) {
									e.printStackTrace();
									throw new BusinessException(e.getMessage());
								}
								try {
									nextDailyStockDigest = dailyStockDigestDaoImpl.findNextDailyStockDigest(tempDailyStockDigest.getDate(), stockId);
								} catch (DatabaseException e1) {
									e1.printStackTrace();
									throw new BusinessException(e1.getMessage());
								}
								// check if l2 is atleast 1% less than its next
								// close price
								// so that we do not get straight line in graph
								if (tempDailyStockDigest.getClosePrice() <= (nextDailyStockDigest.getClosePrice() * 0.99)) {
									// l2 should be < l1 and l2 >= l1-2%
									if (tempDailyStockDigest != null && tempDailyStockDigest.getClosePrice() < tempCall.getL1()
											&& tempDailyStockDigest.getClosePrice() >= (tempCall.getL1() * 0.98)
											&& tempDailyStockDigest.getClosePrice() <= (tempCall.getL1() * 0.99)) {
										tempCall.setL2(tempDailyStockDigest.getClosePrice());
										tempCall.setStartPrice(tempCall.getH0());
										System.out.println(tempCall);
										/*
										 * try { callDaoImpl.create(tempCall); }
										 * catch (DatabaseException e) {
										 * e.printStackTrace(); throw new
										 * BusinessException(e.getMessage()); }
										 */
									} else {
										mapOfStockAndCall.remove(stockId);
									}
								} else {
									mapOfStockAndCall.remove(stockId);
								}
							} else {
								mapOfStockAndCall.remove(stockId);
							}
						}
					} else {
						mapOfStockAndCall.remove(stockId);
					}
				} else {
					mapOfStockAndCall.remove(stockId);
				}
			} else {
				mapOfStockAndCall.remove(stockId);
			}

		}
	}

	@Override
	public void createSellCalls(Calendar calendar) throws BusinessException {
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		if (calendar.get(Calendar.DAY_OF_WEEK) == 7 || calendar.get(Calendar.DAY_OF_WEEK) == 1 || calendar.get(Calendar.DAY_OF_WEEK) == 2) {
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
		} else {
			calendar.add(Calendar.DATE, -1);
		}

		List<CallTO> firstFilterStocks;
		try {
			firstFilterStocks = stockDaoImpl.findAllStockWithPreviousCloseGreaterThanCurrent(calendar.getTime());
		} catch (DatabaseException e) {
			e.printStackTrace();
			throw new BusinessException(e.getMessage());
		}
		// hashmap of stockId and call and call with entry of only h0 as this is
		// just a BUY call
		HashMap<Long, Call> mapOfStockAndCall = new HashMap<>();
		// hashmap of stockId and last pivot point stockDigest date
		HashMap<Long, Date> mapOfStockAndPivotDate = new HashMap<>();

		// for checking the next close price
		DailyStockDigest nextDailyStockDigest = null;

		Call tempCall = null;
		Stock tempStock = null;
		for (CallTO c : firstFilterStocks) {
			tempCall = new Call();
			tempStock = stockDaoImpl.find(c.getStock().getId());
			tempCall.setStock(tempStock);
			tempCall.setType(CallType.SELL);
			tempCall.setStartDate(c.getTempDate());
			tempCall.setL0(c.getTempPrice());
			mapOfStockAndCall.put(tempStock.getId(), tempCall);
			mapOfStockAndPivotDate.put(tempStock.getId(), c.getTempDate());
		}
		// for each stock find the l1 price
		DailyStockDigest tempDailyStockDigest = null;
		for (Long stockId : mapOfStockAndPivotDate.keySet()) {
			// h0
			try {
				tempDailyStockDigest = dailyStockDigestDaoImpl.findStockDigestWherePrevCloseLessThanCurrent(mapOfStockAndPivotDate.get(stockId),
						stockId);
			} catch (DatabaseException e) {
				e.printStackTrace();
				throw new BusinessException(e.getMessage());
			}
			if (tempDailyStockDigest != null) {
				try {
					nextDailyStockDigest = dailyStockDigestDaoImpl.findNextDailyStockDigest(tempDailyStockDigest.getDate(), stockId);
				} catch (DatabaseException e1) {
					e1.printStackTrace();
					throw new BusinessException(e1.getMessage());
				}
				// check if h0 is atleast 1% gretaer than its next close price
				// so that we do not get straight line in graph
				if (tempDailyStockDigest.getClosePrice() >= (nextDailyStockDigest.getClosePrice() * 1.01)) {
					tempCall = mapOfStockAndCall.get(stockId);
					mapOfStockAndPivotDate.put(stockId, tempDailyStockDigest.getDate());
					tempCall.setH0(tempDailyStockDigest.getClosePrice());
					// l1

					try {
						tempDailyStockDigest = dailyStockDigestDaoImpl.findStockDigestWherePrevCloseGreaterThanCurrent(
								mapOfStockAndPivotDate.get(stockId), stockId);
					} catch (DatabaseException e) {
						e.printStackTrace();
						throw new BusinessException(e.getMessage());
					}
					if (tempDailyStockDigest != null) {
						try {
							nextDailyStockDigest = dailyStockDigestDaoImpl.findNextDailyStockDigest(tempDailyStockDigest.getDate(), stockId);
						} catch (DatabaseException e1) {
							e1.printStackTrace();
							throw new BusinessException(e1.getMessage());
						}
						// check if l1 is atleast 1% less than its next close
						// price
						// so that we do not get straight line in graph
						if (tempDailyStockDigest.getClosePrice() <= (nextDailyStockDigest.getClosePrice() * 0.99)) {
							// l1 should be <= +2% of l0 and less than h0
							if ((tempDailyStockDigest.getClosePrice() >= (tempCall.getL0()))
									&& (tempDailyStockDigest.getClosePrice() <= (tempCall.getL0() * 1.02))
									&& (tempDailyStockDigest.getClosePrice() >= (tempCall.getL0() * 1.01))) {
								tempCall.setL1(tempDailyStockDigest.getClosePrice());
								mapOfStockAndPivotDate.put(stockId, tempDailyStockDigest.getDate());

								// h1
								try {
									tempDailyStockDigest = dailyStockDigestDaoImpl.findStockDigestWherePrevCloseLessThanCurrent(
											mapOfStockAndPivotDate.get(stockId), stockId);
								} catch (DatabaseException e) {
									e.printStackTrace();
									throw new BusinessException(e.getMessage());
								}
								try {
									nextDailyStockDigest = dailyStockDigestDaoImpl.findNextDailyStockDigest(tempDailyStockDigest.getDate(), stockId);
								} catch (DatabaseException e1) {
									e1.printStackTrace();
									throw new BusinessException(e1.getMessage());
								}
								// check if h0 is atleast 1% gretaer than its
								// next close price
								// so that we do not get straight line in graph
								if (tempDailyStockDigest.getClosePrice() >= (nextDailyStockDigest.getClosePrice() * 1.01)) {
									// h1 should be >h0 and h1 <= h0+2%
									if (tempDailyStockDigest != null && tempDailyStockDigest.getClosePrice() > tempCall.getH0()
											&& tempDailyStockDigest.getClosePrice() <= (tempCall.getH0() * 1.02)
											&& tempDailyStockDigest.getClosePrice() >= (tempCall.getH0() * 1.01)) {
										tempCall.setH1(tempDailyStockDigest.getClosePrice());
										tempCall.setStartPrice(tempCall.getL0());
										System.out.println(tempCall);
										/*
										 * try { callDaoImpl.create(tempCall); }
										 * catch (DatabaseException e) {
										 * e.printStackTrace(); throw new
										 * BusinessException(e.getMessage()); }
										 */
									} else {
										mapOfStockAndCall.remove(stockId);
									}
								} else {
									mapOfStockAndCall.remove(stockId);
								}
							} else {
								mapOfStockAndCall.remove(stockId);
							}
						} else {
							mapOfStockAndCall.remove(stockId);
						}
					} else {
						mapOfStockAndCall.remove(stockId);
					}
				} else {
					mapOfStockAndCall.remove(stockId);
				}
			} else {
				mapOfStockAndCall.remove(stockId);
			}

		}
	}
}
