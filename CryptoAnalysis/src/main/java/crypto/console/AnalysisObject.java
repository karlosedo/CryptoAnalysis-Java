package crypto.console;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.litesoftwares.coingecko.CoinGeckoApiClient;
import com.litesoftwares.coingecko.domain.Coins.CoinList;
import com.litesoftwares.coingecko.impl.CoinGeckoApiClientImpl;

import crypto.obj.GainsInterval;
import crypto.obj.PriceDate;

public class AnalysisObject extends AnalysisConsole {
	
//	int manual = 1;
//	double addedInvest = 0d;
//	double initInvest = 1000d;
//	int buyDay =28;
//	int saleDay = 10;
//	String token = "btc";
//	String initDateStr = "2021-4-1";
//	String endDateStr = "2021-5-28";
//	Date initDate = new Date();
//	Date endDate = new Date();
//	StringBuilder messages = new StringBuilder();
//	int option = 0;
//	double ideal = 0;
	
	
	
	public void process() throws Exception {
		try {
			Date initDate = new SimpleDateFormat("yyyy-MM-dd").parse(initDateStr);
			Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDateStr);
			if (initDate.after(endDate))
				throw new Exception("Fecha inicial debe ser antes a fecha final");
			
			BufferedReader reader = new BufferedReader(
					new FileReader(token+"-usd-max.csv")) ;
			String line = reader.readLine();
			List<PriceDate> pricesList = new ArrayList<PriceDate>();
			int i=0;
			while (line!=null) {
				if (!line.startsWith("s")) {
					String[] arrayLine = line.split(",");
					PriceDate pd = new PriceDate(new SimpleDateFormat("yyyy-MM-dd").parse(arrayLine[0].replace(" UTC", "")), 
							Double.parseDouble(arrayLine[1]));
					pricesList.add(pd);
					i++;
				}
				line = reader.readLine();
			}
			reader.close();
			messages.append("Total líneas "+i+"\n");
			if (option == 1)
				printAnalysis(pricesList, initInvest, buyDay, saleDay, initDate, endDate);
			else if (option == 0)
				doAnalysis(pricesList, initInvest, buyDay, saleDay, initDate, endDate);
			else if (option == 2)
				onlyGainsAnalysis(pricesList, initInvest, buyDay, saleDay, initDate, endDate);
			else if (option == 3)
				recoverBuyAnalysis(pricesList, initInvest, buyDay, saleDay, initDate, endDate);
			else if (option == 4)
				idealGainAnalysis(pricesList, initInvest, buyDay, saleDay, initDate, endDate);
			else if (option == 5)
				idealGainAnalysisReinvert(pricesList, initInvest, buyDay, saleDay, initDate, endDate);
			else if (option == 6)
				idealGainAnalysisPercent(pricesList, initInvest, buyDay, saleDay, initDate, endDate);
			else if (option == 7)
				dayBuy(pricesList, initInvest, buyDay, saleDay, initDate, endDate);
			else if (option == 8)
				dailyBuy(pricesList, initInvest, buyDay, saleDay, initDate, endDate);
			else if (option == 9)
				buybackPlan(pricesList, initInvest, buyDay, saleDay, initDate, endDate);
			else
				hodlResult(pricesList, initInvest, buyDay, saleDay, initDate, endDate);
//				printAnalysisMonth(pricesList, initInvest, buyDay, saleDay, initDate, endDate);;
		} catch (ParseException e) {
			throw new Exception("Error formato de fecha");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			throw new Exception("Archivo "+token+" no disponible");
		}
	}
	
//	public void printAnalysisMonth(List<PriceDate> pricesList, double initInvest, int buyDay, int saleDay, Date initDate, Date endDate) {
//		messages.append("Fecha inicial: "+(initDate.getYear()+1900)+"-"+(initDate.getMonth()+1)+"-"+initDate.getDate()+"\n");
//		messages.append("Fecha final:   "+(endDate.getYear()+1900)+"-"+(endDate.getMonth()+1)+"-"+endDate.getDate()+"\n");
//		Map<String, Double> positives = new HashMap<String, Double>();
//		List<String> blackList= new ArrayList<String>();
//		while (initDate.compareTo(endDate)<0) {
//			Date endDateTmp = new Date(initDate.getYear(), initDate.getMonth(), 28);
//			while (endDateTmp.compareTo(endDate)<=0) {
//				messages.append("Intervalo de fecha: "+(initDate.getYear()+1900)+"-"+(initDate.getMonth()+1)+"-"+initDate.getDate()+" --> "+
//						(endDateTmp.getYear()+1900)+"-"+(endDateTmp.getMonth()+1)+"-"+endDateTmp.getDate()+"\n");
//				for (int i=1;i<29;i++) {
//					for (int j=1;j<29;j++) {
//						double totalGains = doAnalysis(pricesList, initInvest, i, j, initDate, endDate);
//						messages.append(i+" , "+j+" --> "+totalGains+"\n");
//						if (totalGains>0d) {
//							if (totalGains<initInvest-300) {
//								messages.append(i+"-"+j+"  Perdida: "+totalGains+"\n");
//								positives.remove(i+"-"+j);
//								blackList.add(i+"-"+j);
//							} else {
//								if (!blackList.contains(i+"-"+j)) {
//									positives.put(i+"-"+j, totalGains);
//								}
//							}
//						}
//					}
//				}
//				endDateTmp.setMonth(endDateTmp.getMonth()+1);
//			}
//			initDate.setMonth(initDate.getMonth()+1);	
//		}	
//		messages.append("Tamaño listado: "+positives.keySet().size()+"\n");
//		Iterator<String> it = positives.keySet().iterator();
//		while (it.hasNext()) {
//			String key = it.next();
//			messages.append(key+" ; "+positives.get(key)+"\n");
//		}
//	}
//	
//	public void printAnalysis(List<PriceDate> pricesList, double initInvest, int buyDay, int saleDay, Date initDate, Date endDate) {
//		double max = 1d;
//		double min = 999999999999d;
//		GainsInterval gMax = null;
//		GainsInterval gMin = null;
//			
//		for (int i=1;i<29;i++) {
//			for (int j=1;j<29;j++) {
//				double totalGains = doAnalysis(pricesList, initInvest, i, j, initDate, endDate);
//				messages.append(i+" , "+j+" --> "+totalGains+"\n");
//				if (totalGains>0 && totalGains<min) {
//					gMin = new GainsInterval(i, j, totalGains);
//					min = totalGains;
//				} else if (totalGains>0 && totalGains>=max) {
//					gMax = new GainsInterval(i, j, totalGains);
//					max = totalGains;
//				}
//			}
//		}
//		messages.append("Mejor: "+gMax.getInitDay()+" -> "+gMax.getEndDay()+"  Acumulado: "+gMax.getTotalGains()+"\n");
//		messages.append("Peor : "+gMin.getInitDay()+" -> "+gMin.getEndDay()+"  Acumulado: "+gMin.getTotalGains()+"\n");
//	}
//	
//	private double doAnalysis(List<PriceDate> pricesList, double initInvest, int buyDay, int saleDay, Date initDate, Date endDate) {
//		double totalGains = 0d;
//		double prevGains = 0d;
//		double cryptoPrice = 0d;
//		boolean hodl = false;
//		double addedAcum = 0d;
//		double saleCount = 0d;
//		double profitCount = 0d;
//		for (PriceDate pd:pricesList) {
//			if (pd.getDate().getYear()==endDate.getYear() && 
//					pd.getDate().getMonth()==endDate.getMonth() &&
//					pd.getDate().getDate()==endDate.getDate()) {
//				if (hodl) {
//					totalGains = (totalGains*pd.getPrice())/cryptoPrice;
//					messages.append("VENTA el "+(pd.getDate().getYear()+1900)+"-"+(pd.getDate().getMonth()+1)+"-"+pd.getDate().getDate()+" \\ Precio Crypto: "+pd.getPrice()+ " \\ Ganancia acum: "+totalGains+"\n");
//				}
//				break;
//			} else if (pd.getDate().compareTo(initDate)>=0 && pd.getDate().compareTo(endDate)<0 &&
//					pd.getDate().getDate()==buyDay) {
//				//COMPRA
//				hodl = true;
//				if (totalGains==0d)
//					totalGains = initInvest;
//				else {
//					totalGains = (addedInvest==0d?totalGains:totalGains+addedInvest);
//					addedAcum+=addedInvest;
//				}
//				prevGains = totalGains;
//				cryptoPrice = pd.getPrice();
//				messages.append("COMPRA el "+(pd.getDate().getYear()+1900)+"-"+(pd.getDate().getMonth()+1)+"-"+pd.getDate().getDate()+" \\ Precio Crypto: "+pd.getPrice()+ " \\ Ganancia acum: "+totalGains+"\n");
//			} else if (hodl && pd.getDate().after(initDate) &&
//					pd.getDate().getDate()==saleDay) {
//				//VENTA
//				hodl = false;
//				saleCount++;
////				System.out.println(totalGains+"*"+pd.getPrice()+" div "+cryptoPrice);
//				totalGains = (totalGains*pd.getPrice())/cryptoPrice;
//				if (totalGains>=prevGains)	profitCount++;
//				messages.append("VENTA el "+(pd.getDate().getYear()+1900)+"-"+(pd.getDate().getMonth()+1)+"-"+pd.getDate().getDate()+" \\ Precio Crypto: "+pd.getPrice()+ " \\ Ganancia acum: "+totalGains+"\n");
//			}
//			
//		}
//		messages.append("Fecha inicio: "+initDate+"\n");
//		messages.append("Fecha Fin:    "+endDate+"\n");
//		messages.append("Probabilidad ganancias por ciclo: "+((profitCount/saleCount)*100d)+"%\n");
//		messages.append("Inversión inicial:   "+initInvest+"\n");
//		if (addedAcum>0d)
//			messages.append("Total inversion:      "+(addedAcum+initInvest)+"\n");
//		messages.append("Inversión al final:   "+totalGains+"\n");
//		return totalGains;
//	}
//	
//	private double onlyGainsAnalysis(List<PriceDate> pricesList, double initInvest, int buyDay, int saleDay, Date initDate, Date endDate) {
//		double totalGains = 0d;
//		double prevGains = 0d;
//		double cryptoPrice = 0d;
//		boolean hodl = false;
//		boolean accumHodl = false;
//		double addedAcum = 0d;
//		double saleCount = 0d;
//		double profitCount = 0d;
//		for (PriceDate pd:pricesList) {
//			if (pd.getDate().getYear()==endDate.getYear() && 
//					pd.getDate().getMonth()==endDate.getMonth() &&
//					pd.getDate().getDate()==endDate.getDate()) {
//				if (hodl) {
//					totalGains = (!accumHodl?(totalGains*pd.getPrice())/cryptoPrice:(prevGains*pd.getPrice())/cryptoPrice);
//					messages.append("VENTA el "+(pd.getDate().getYear()+1900)+"-"+(pd.getDate().getMonth()+1)+"-"+pd.getDate().getDate()+" \\ Precio Crypto: "+pd.getPrice()+ " \\ Ganancia acum: "+totalGains+"\n");
//				}
//				break;
//			} else if (pd.getDate().compareTo(initDate)>=0 && pd.getDate().compareTo(endDate)<0 &&
//					pd.getDate().getDate()==buyDay) {
//				//COMPRA
//				if (!hodl) {
//					hodl = true;
//					if (totalGains==0d)
//						totalGains = initInvest;
//					else {
//						totalGains = (addedInvest==0d?totalGains:totalGains+addedInvest);
//						addedAcum+=addedInvest;
//					}
//					prevGains = totalGains;
//					cryptoPrice = pd.getPrice();
//					accumHodl = false;
//					messages.append("COMPRA el "+(pd.getDate().getYear()+1900)+"-"+(pd.getDate().getMonth()+1)+"-"+pd.getDate().getDate()+" \\ Precio Crypto: "+pd.getPrice()+ " \\ Ganancia acum: "+totalGains+"\n");
//				} else {
//					totalGains = (prevGains*pd.getPrice())/cryptoPrice;
//					totalGains+=addedInvest;
//					accumHodl = true;
//					messages.append("NO COMPRA el "+(pd.getDate().getYear()+1900)+"-"+(pd.getDate().getMonth()+1)+"-"+pd.getDate().getDate()+" \\ Precio Crypto: "+pd.getPrice()+ " \\ Ganancia acum: "+totalGains+"\n");
//				}
////				System.out.println(cryptoPrice);
//			} else if (hodl && pd.getDate().after(initDate) &&
//					pd.getDate().getDate()==saleDay) {
//				//VENTA
//				hodl = false;
//				saleCount++;
//				System.out.println(prevGains+"*"+pd.getPrice()+"/"+cryptoPrice);
////				totalGains = (totalGains*pd.getPrice())/cryptoPrice;
//				totalGains = (prevGains*pd.getPrice())/cryptoPrice;
//				if (totalGains>=prevGains) {
//					profitCount++;
//					accumHodl = false;
//					hodl = false;
//					messages.append("VENTA el "+(pd.getDate().getYear()+1900)+"-"+(pd.getDate().getMonth()+1)+"-"+pd.getDate().getDate()+" \\ Precio Crypto: "+pd.getPrice()+ " \\ Ganancia acum: "+totalGains+"\n");
//				} else {
//					hodl = true;
//					accumHodl = true;
//					totalGains = (prevGains*pd.getPrice())/cryptoPrice;
//					messages.append("NO VENTA el "+(pd.getDate().getYear()+1900)+"-"+(pd.getDate().getMonth()+1)+"-"+pd.getDate().getDate()+" \\ Precio Crypto: "+pd.getPrice()+ " \\ Ganancia acum: "+totalGains+"\n");
//					
//				}
//				
//			}
//			
//		}
//		messages.append("Fecha inicio: "+initDate+"\n");
//		messages.append("Fecha Fin:    "+endDate+"\n");
//		messages.append("Probabilidad ganancias por ciclo: "+((profitCount/saleCount)*100d)+"%\n");
//		messages.append("Inversión inicial:   "+initInvest+"\n");
//		if (addedAcum>0d)
//			messages.append("Total inversion:      "+(addedAcum+initInvest)+"\n");
//		messages.append("Inversión al final:   "+totalGains+"\n");
//		return totalGains;
//	}
//	
//	private double recoverBuyAnalysis(List<PriceDate> pricesList, double initInvest, int buyDay, int saleDay, Date initDate, Date endDate) {
//		double totalGains = 0d;
//		double prevGains = 0d;
//		double cryptoPrice = 0d;
//		boolean hodl = false;
//		boolean accumHodl = false;
//		double addedAcum = 0d;
//		double saleCount = 0d;
//		double profitCount = 0d;
//		for (PriceDate pd:pricesList) {
//			if (pd.getDate().getYear()==endDate.getYear() && 
//					pd.getDate().getMonth()==endDate.getMonth() &&
//					pd.getDate().getDate()==endDate.getDate()) {
//				if (hodl) {
//					totalGains = (!accumHodl?(totalGains*pd.getPrice())/cryptoPrice:(prevGains*pd.getPrice())/cryptoPrice);
//					messages.append("VENTA el "+(pd.getDate().getYear()+1900)+"-"+(pd.getDate().getMonth()+1)+"-"+pd.getDate().getDate()+" \\ Precio Crypto: "+pd.getPrice()+ " \\ Ganancia acum: "+totalGains+"\n");
//				}
//				break;
//			} else if (pd.getDate().compareTo(initDate)>=0 && pd.getDate().compareTo(endDate)<0 &&
//					pd.getDate().getDate()==buyDay) {
//				//COMPRA
//				if (!hodl) {
//					hodl = true;
//					if (totalGains==0d)
//						totalGains = initInvest;
//					else {
//						totalGains = (addedInvest==0d?totalGains:totalGains+addedInvest);
//						addedAcum+=addedInvest;
//					}
//					prevGains = totalGains;
//					cryptoPrice = pd.getPrice();
//					accumHodl = false;
//					messages.append("COMPRA el "+(pd.getDate().getYear()+1900)+"-"+(pd.getDate().getMonth()+1)+"-"+pd.getDate().getDate()+" \\ Precio Crypto: "+pd.getPrice()+ " \\ Ganancia acum: "+totalGains+"\n");
//				} else {
//					totalGains = (prevGains*pd.getPrice())/cryptoPrice;
//					totalGains+=addedInvest;
//					accumHodl = true;
//					messages.append("NO COMPRA el "+(pd.getDate().getYear()+1900)+"-"+(pd.getDate().getMonth()+1)+"-"+pd.getDate().getDate()+" \\ Precio Crypto: "+pd.getPrice()+ " \\ Ganancia acum: "+totalGains+"\n");
//				}
////				System.out.println(cryptoPrice);
//			} else if (hodl && pd.getDate().after(initDate) &&
//					pd.getDate().getDate()==saleDay) {
//				//VENTA
//				hodl = false;
//				saleCount++;
////				System.out.println(prevGains+"*"+pd.getPrice()+"/"+cryptoPrice);
////				totalGains = (totalGains*pd.getPrice())/cryptoPrice;
//				totalGains = (prevGains*pd.getPrice())/cryptoPrice;
//				if (totalGains>=prevGains) {
//					profitCount++;
//					hodl = false;
//					accumHodl = false;
//					messages.append("VENTA el "+(pd.getDate().getYear()+1900)+"-"+(pd.getDate().getMonth()+1)+"-"+pd.getDate().getDate()+" \\ Precio Crypto: "+pd.getPrice()+ " \\ Ganancia acum: "+totalGains+"\n");
//				} else {
//					hodl = true;
//					accumHodl = true;
//					totalGains = (prevGains*pd.getPrice())/cryptoPrice;
//					messages.append("NO VENTA el "+(pd.getDate().getYear()+1900)+"-"+(pd.getDate().getMonth()+1)+"-"+pd.getDate().getDate()+" \\ Precio Crypto: "+pd.getPrice()+ " \\ Ganancia acum: "+totalGains+"\n");
//					
//				}
//			} else if (accumHodl) {
//				double tmpGain = (prevGains*pd.getPrice())/cryptoPrice;
//				if (tmpGain>=prevGains) {
//					accumHodl = false;
//					hodl = false;
//					totalGains = tmpGain;
//					messages.append("VENTA el "+(pd.getDate().getYear()+1900)+"-"+(pd.getDate().getMonth()+1)+"-"+pd.getDate().getDate()+" \\ Precio Crypto: "+pd.getPrice()+ " \\ Ganancia acum: "+totalGains+"\n");
//				}
//			}
//			
//		}
//		messages.append("Fecha inicio: "+initDate+"\n");
//		messages.append("Fecha Fin:    "+endDate+"\n");
//		messages.append("Probabilidad ganancias por ciclo: "+((profitCount/saleCount)*100d)+"%\n");
//		messages.append("Inversión inicial:   "+initInvest+"\n");
//		if (addedAcum>0d)
//			messages.append("Total inversion:      "+(addedAcum+initInvest)+"\n");
//		messages.append("Inversión al final:   "+totalGains+"\n");
//		return totalGains;
//	}
//	
//	private double idealGainAnalysis(List<PriceDate> pricesList, double initInvest, int buyDay, int saleDay, Date initDate, Date endDate) {
//		double totalGains = 0d;
//		double prevGains = 0d;
//		double cryptoPrice = 0d;
//		boolean hodl = false;
//		boolean accumHodl = false;
//		double addedAcum = 0d;
//		double saleCount = 0d;
//		double profitCount = 0d;
//		for (PriceDate pd:pricesList) {
//			if (pd.getDate().getYear()==endDate.getYear() && 
//					pd.getDate().getMonth()==endDate.getMonth() &&
//					pd.getDate().getDate()==endDate.getDate()) {
//				if (hodl) {
//					totalGains = (!accumHodl?(totalGains*pd.getPrice())/cryptoPrice:(prevGains*pd.getPrice())/cryptoPrice);
//					messages.append("VENTA el "+(pd.getDate().getYear()+1900)+"-"+(pd.getDate().getMonth()+1)+"-"+pd.getDate().getDate()+" \\ Precio Crypto: "+pd.getPrice()+ " \\ Ganancia acum: "+totalGains+"\n");
//				}
//				break;
//			} else if (pd.getDate().compareTo(initDate)>=0 && pd.getDate().compareTo(endDate)<0 &&
//					pd.getDate().getDate()==buyDay) {
//				//COMPRA
////				if (!hodl) {
//					hodl = true;
//					if (totalGains==0d)
//						totalGains = initInvest;
//					else {
//						totalGains = (addedInvest==0d?totalGains:totalGains+addedInvest);
//						addedAcum+=addedInvest;
//					}
//					prevGains = totalGains;
//					cryptoPrice = pd.getPrice();
//					accumHodl = true;
//					messages.append("COMPRA el "+(pd.getDate().getYear()+1900)+"-"+(pd.getDate().getMonth()+1)+"-"+pd.getDate().getDate()+" \\ Precio Crypto: "+pd.getPrice()+ " \\ Ganancia acum: "+totalGains+"\n");
//
//			} else if (hodl && pd.getDate().after(initDate) &&
//					pd.getDate().getDate()==saleDay) {
//				//VENTA
//				hodl = false;
//				saleCount++;
////				System.out.println(prevGains+"*"+pd.getPrice()+"/"+cryptoPrice);
////				totalGains = (totalGains*pd.getPrice())/cryptoPrice;
//				totalGains = (prevGains*pd.getPrice())/cryptoPrice;
////				if (totalGains>=prevGains) {
//					profitCount++;
//					hodl = false;
//					accumHodl = false;
//					messages.append("VENTA el "+(pd.getDate().getYear()+1900)+"-"+(pd.getDate().getMonth()+1)+"-"+pd.getDate().getDate()+" \\ Precio Crypto: "+pd.getPrice()+ " \\ Ganancia acum: "+totalGains+"\n");
////				} else {
////					hodl = true;
////					accumHodl = true;
////					totalGains = (prevGains*pd.getPrice())/cryptoPrice;
////					messages.append("NO VENTA el "+(pd.getDate().getYear()+1900)+"-"+(pd.getDate().getMonth()+1)+"-"+pd.getDate().getDate()+" \\ Precio Crypto: "+pd.getPrice()+ " \\ Ganancia acum: "+totalGains+"\n");
////					
////				}
//			} else if (accumHodl) {
//				double tmpGain = (prevGains*pd.getPrice())/cryptoPrice;
//				System.out.println("tmpGain: "+tmpGain+"  --  "+(((prevGains*ideal)/100)+prevGains));
//				if (tmpGain>=(((prevGains*ideal)/100)+prevGains)) {
//					accumHodl = false;
//					hodl = false;
//					totalGains = tmpGain;
//					messages.append("VENTA el "+(pd.getDate().getYear()+1900)+"-"+(pd.getDate().getMonth()+1)+"-"+pd.getDate().getDate()+" \\ Precio Crypto: "+pd.getPrice()+ " \\ Ganancia acum: "+totalGains+"\n");
//				}
//			}
//			
//		}
//		messages.append("Fecha inicio: "+initDate+"\n");
//		messages.append("Fecha Fin:    "+endDate+"\n");
//		messages.append("Probabilidad ganancias por ciclo: "+((profitCount/saleCount)*100d)+"%\n");
//		messages.append("Inversión inicial:   "+initInvest+"\n");
//		if (addedAcum>0d)
//			messages.append("Total inversion:      "+(addedAcum+initInvest)+"\n");
//		messages.append("Inversión al final:   "+totalGains+"\n");
//		return totalGains;
//	}
//	
//	private double idealGainAnalysisReinvert(List<PriceDate> pricesList, double initInvest, int buyDay, int saleDay, Date initDate, Date endDate) {
//		double totalGains = 0d;
//		double prevGains = 0d;
//		double cryptoPrice = 0d;
//		boolean hodl = false;
//		double addedAcum = 0d;
//		double saleCount = 0d;
//		double profitCount = 0d;
//		int notGains = 0;
//		boolean huboCompra = false;
//		boolean huboVenta = false;
//		for (PriceDate pd:pricesList) {
//			if (pd.getDate().getYear()==endDate.getYear() && 
//					pd.getDate().getMonth()==endDate.getMonth() &&
//					pd.getDate().getDate()==endDate.getDate()) {
//				if (hodl) {
//					totalGains = (totalGains*pd.getPrice())/cryptoPrice;
//					messages.append("VENTA FINAL el "+(pd.getDate().getYear()+1900)+"-"+(pd.getDate().getMonth()+1)+"-"+pd.getDate().getDate()+" \\ Precio Crypto: "+pd.getPrice()+ " \\ Ganancia acum: "+totalGains+"\n");
//				}
//				break;
//			} else if (pd.getDate().compareTo(initDate)>=0 && pd.getDate().compareTo(endDate)<0 &&
//					pd.getDate().getDate()==buyDay) {
//				//COMPRA
//				hodl = true;
//				if (totalGains==0d)
//					totalGains = initInvest;
//				else {
//					totalGains = (addedInvest==0d?totalGains:totalGains+addedInvest);
//					addedAcum+=addedInvest;
//				}
//				prevGains = totalGains;
//				cryptoPrice = pd.getPrice();
//				huboCompra = true;
//				messages.append("COMPRA el "+(pd.getDate().getYear()+1900)+"-"+(pd.getDate().getMonth()+1)+"-"+pd.getDate().getDate()+" \\ Precio Crypto: "+pd.getPrice()+ " \\ Ganancia acum: "+totalGains+"\n");
//			} else if (hodl && pd.getDate().after(initDate) &&
//					pd.getDate().getDate()==saleDay) {
//				//VENTA
//				hodl = false;
//				saleCount++;
////				System.out.println(totalGains+"*"+pd.getPrice()+" div "+cryptoPrice);
//				totalGains = (totalGains*pd.getPrice())/cryptoPrice;
//				if (totalGains>=prevGains)	profitCount++;
//				double tmpGains = totalGains;
////				if (totalGains>=initInvest) 
//					totalGains = totalGains-this.ideal;
////				else
////					notGains++;
//				messages.append("VENTA el "+(pd.getDate().getYear()+1900)+"-"+(pd.getDate().getMonth()+1)+"-"+pd.getDate().getDate()+" \\ Precio Crypto: "+pd.getPrice()+
//						" \\ Ganancia acum: "+tmpGains+"\n"+(tmpGains>=initInvest?"-- Recogiendo ganancias: "+this.ideal:"No Ganancias")+"\n");
////						" \\ Ganancia acum: "+tmpGains+"\n"+"-- Recogiendo ganancias: "+totalGains+"\n");
//				if (totalGains<0) {
//					messages.append("BANCARROTA\n");
//					break;
//				}
//			} 
//			
//		}
//		messages.append("Fecha inicio: "+initDate+"\n");
//		messages.append("Fecha Fin:    "+endDate+"\n");
//		messages.append("Meses sin ganancia: "+notGains+"\n");
//		messages.append("Inversión inicial:   "+initInvest+"\n");
//		if (addedAcum>0d)
//			messages.append("Total inversion:      "+(addedAcum+initInvest)+"\n");
//		messages.append("Inversión al final:   "+totalGains+"\n");
//		return totalGains;
//	}
//	
//	private double idealGainAnalysisPercent(List<PriceDate> pricesList, double initInvest, int buyDay, int saleDay, Date initDate, Date endDate) {
//		double totalGains = 0d;
//		double prevGains = 0d;
//		double cryptoPrice = 0d;
//		boolean hodl = false;
//		double addedAcum = 0d;
//		double saleCount = 0d;
//		double profitCount = 0d;
//		int notGains = 0;
//		boolean huboCompra = false;
//		boolean huboVenta = false;
//		double minGain = 999999999999d;
//		double maxGain = 0d;
//		double acumGain = 0d;
//		for (PriceDate pd:pricesList) {
//			if (pd.getDate().getYear()==endDate.getYear() && 
//					pd.getDate().getMonth()==endDate.getMonth() &&
//					pd.getDate().getDate()==endDate.getDate()) {
//				if (hodl) {
//					totalGains = (totalGains*pd.getPrice())/cryptoPrice;
//					messages.append("VENTA FINAL el "+(pd.getDate().getYear()+1900)+"-"+(pd.getDate().getMonth()+1)+"-"+pd.getDate().getDate()+" \\ Precio Crypto: "+pd.getPrice()+ " \\ Ganancia acum: "+totalGains+"\n");
//				}
//				break;
//			} else if (pd.getDate().compareTo(initDate)>=0 && pd.getDate().compareTo(endDate)<0 &&
//					pd.getDate().getDate()==buyDay) {
//				//COMPRA
//				hodl = true;
//				if (totalGains==0d)
//					totalGains = initInvest;
//				else {
//					totalGains = (addedInvest==0d?totalGains:totalGains+addedInvest);
//					addedAcum+=addedInvest;
//				}
//				prevGains = totalGains;
//				cryptoPrice = pd.getPrice();
//				huboCompra = true;
//				messages.append("COMPRA el "+(pd.getDate().getYear()+1900)+"-"+(pd.getDate().getMonth()+1)+"-"+pd.getDate().getDate()+" \\ Precio Crypto: "+pd.getPrice()+ " \\ Ganancia acum: "+totalGains+"\n");
//			} else if (hodl && pd.getDate().after(initDate) &&
//					pd.getDate().getDate()==saleDay) {
//				//VENTA
//				hodl = false;
//				saleCount++;
////				System.out.println(totalGains+"*"+pd.getPrice()+" div "+cryptoPrice);
//				totalGains = (totalGains*pd.getPrice())/cryptoPrice;
//				if (totalGains>=prevGains)	profitCount++;
//				double tmpGains = totalGains;
//				double personalGains = (tmpGains*this.ideal)/100d;	
//				if (personalGains<minGain)
//					minGain = personalGains;
//				if (personalGains>maxGain)
//					maxGain = personalGains;
//				acumGain+= personalGains;
//				totalGains = totalGains-personalGains;
//
//				messages.append("VENTA el "+(pd.getDate().getYear()+1900)+"-"+(pd.getDate().getMonth()+1)+"-"+pd.getDate().getDate()+" \\ Precio Crypto: "+pd.getPrice()+
//						" \\ Ganancia acum: "+tmpGains+"\n"+"-- Ganancia:"+personalGains+"\n");
////						" \\ Ganancia acum: "+tmpGains+"\n"+"-- Recogiendo ganancias: "+totalGains+"\n");
//				if ((totalGains/initInvest)<0.06) {
//					messages.append("BANCARROTA\n");
//					break;
//				}
//			} 
//			
//		}
//		messages.append("Fecha inicio: "+initDate+"\n");
//		messages.append("Fecha Fin:    "+endDate+"\n");
//		messages.append("Ganancia mínima: "+minGain+"  ||  Ganancia máxima: "+maxGain+"  ||  Ganancia total: "+acumGain+" \nPromedio: "+(acumGain/saleCount)+"\n");
//		messages.append("Inversión inicial:   "+initInvest+"\n");
//		if (addedAcum>0d)
//			messages.append("Total inversion:      "+(addedAcum+initInvest)+"\n");
//		messages.append("Inversión al final:   "+totalGains+"\n");
//		return totalGains;
//	}
//	
//	private void hodlResult(List<PriceDate> pricesList, double initInvest, int buyDay, int saleDay, Date initDate, Date endDate) {
//		double totalGains = 0d;
//		double cryptoPrice = 0d;
//		for (PriceDate pd:pricesList) {
//			if (pd.getDate().getYear()==endDate.getYear() && 
//					pd.getDate().getMonth()==endDate.getMonth() &&
//					pd.getDate().getDate()==endDate.getDate()) {
//				totalGains = (totalGains*pd.getPrice())/cryptoPrice;
//				messages.append("VENTA el "+(pd.getDate().getYear()+1900)+"-"+(pd.getDate().getMonth()+1)+"-"+pd.getDate().getDate()+" \\ Precio Crypto: "+pd.getPrice()+ " \\ Ganancia acum: "+totalGains+"\n");
//				break;
//			} else if (pd.getDate().compareTo(initDate)==0) {
//				//COMPRA
//				totalGains = initInvest;
//				cryptoPrice = pd.getPrice();
//				messages.append("COMPRA el "+(pd.getDate().getYear()+1900)+"-"+(pd.getDate().getMonth()+1)+"-"+pd.getDate().getDate()+" \\ Precio Crypto: "+pd.getPrice()+ " \\ Ganancia acum: "+totalGains+"\n");
//			}
//		}
//		messages.append("Fecha inicio: "+initDate+"\n");
//		messages.append("Fecha Fin:    "+endDate+"\n");
//		messages.append("Inversión inicial:   "+initInvest+"\n");
//		messages.append("Inversión al final:   "+totalGains+"\n");
//	}
//	
//	public double getAddedInvest() {
//		return addedInvest;
//	}
//
//	public void setAddedInvest(double addedInvest) {
//		this.addedInvest = addedInvest;
//	}
//
//	public double getInitInvest() {
//		return initInvest;
//	}
//
//	public void setInitInvest(double initInvest) {
//		this.initInvest = initInvest;
//	}
//
//	public int getBuyDay() {
//		return buyDay;
//	}
//
//	public void setBuyDay(int buyDay) {
//		this.buyDay = buyDay;
//	}
//
//	public int getSaleDay() {
//		return saleDay;
//	}
//
//	public void setSaleDay(int saleDay) {
//		this.saleDay = saleDay;
//	}
//
//	public String getInitDateStr() {
//		return initDateStr;
//	}
//
//	public void setInitDateStr(String initDateStr) {
//		this.initDateStr = initDateStr;
//	}
//
//	public String getEndDateStr() {
//		return endDateStr;
//	}
//
//	public void setEndDateStr(String endDateStr) {
//		this.endDateStr = endDateStr;
//	}
//
//	public Date getInitDate() {
//		return initDate;
//	}
//
//	public void setInitDate(Date initDate) {
//		this.initDate = initDate;
//	}
//
//	public Date getEndDate() {
//		return endDate;
//	}
//
//	public void setEndDate(Date endDate) {
//		this.endDate = endDate;
//	}
//
//	public StringBuilder getMessages() {
//		return messages;
//	}
//
//	public void setMessages(StringBuilder sb) {
//		this.messages = sb;
//	}
//
//	public int getOption() {
//		return option;
//	}
//
//	public void setOption(int option) {
//		this.option = option;
//	}
//
//	public String getToken() {
//		return token;
//	}
//
//	public void setToken(String token) {
//		this.token = token;
//	}
//
//	public double getIdeal() {
//		return ideal;
//	}
//
//	public void setIdeal(double ideal) {
//		this.ideal = ideal;
//	}
//
//
}
