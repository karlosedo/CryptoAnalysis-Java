package crypto.persistence;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.litesoftwares.coingecko.CoinGeckoApiClient;
import com.litesoftwares.coingecko.domain.Coins.CoinList;
import com.litesoftwares.coingecko.domain.Coins.MarketChart;
import com.litesoftwares.coingecko.exception.CoinGeckoApiException;
import com.litesoftwares.coingecko.impl.CoinGeckoApiClientImpl;

import crypto.obj.PriceDate;

public class DBCrypto {
	
	private String url = "jdbc:sqlite:./crypto.db";
	
	public void consulta() {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet resul = null;
		try {
			connection = DriverManager.getConnection(url);
			System.out.println("Conexión establecida");
			ps = connection.prepareStatement("select * from charged_infos");
			resul = ps.executeQuery();
			System.out.println(resul.getString("keyname"));
			
			DatabaseMetaData dmd = connection.getMetaData();
			if (dmd.getTables(null, null, "charged_infos", null).next())
				System.out.println("YEAH!");
			else
				System.out.println("NOPE");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeConnection(resul, ps, connection);
		}
		
	}
	
	public List<CoinList> getCoinList() {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet resul = null;
		try {
			connection = DriverManager.getConnection(url);
			
			try {
				CoinGeckoApiClient coinGecko = new CoinGeckoApiClientImpl();
				List<CoinList> coinList = coinGecko.getCoinList();
				for (CoinList cl: coinList) {
					if (!cl.getId().startsWith("binance-peg")) {
						String sqlInsert = "insert into tokens (id,symbol,name) values('"+cl.getId()+"','"+cl.getSymbol()+"','"+cl.getName().replaceAll("'", "''")+"');";					
						System.out.println(sqlInsert+"  "+sqlInsert.contains("'")+"  "+sqlInsert.contains("\'"));
						ps = connection.prepareStatement(sqlInsert);
						System.out.println(ps.executeUpdate());
						
					}
				}
			} catch (CoinGeckoApiException e) {
				e.getStackTrace();
			}
//			ps = connection.prepareStatement("select * from charged_infos where keyname = 'tokens'");
//			resul = ps.executeQuery();
//			if (resul.isClosed()) {
//				
//			} else {
//				Date lastProc = new SimpleDateFormat("yyyy-MM-dd").parse(resul.getString("date_process"));
//				Date today = new Date();
//				if (lastProc.getYear()==today.getYear() && lastProc.getMonth()==today.getMonth() && lastProc.getDate()==today.getDate()) {
//					//Consulta tokens
//				} else {
//					
//				}
//			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		} finally {
			closeConnection(resul, ps, connection);
		}
		
		return null;
	}
	
	public String getTokenId(String token) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet resul = null;
		try {
			connection = DriverManager.getConnection(url);
			ps = connection.prepareStatement("select id from tokens where symbol='"+token+"' limit 1");
			resul = ps.executeQuery();
			if (!resul.isClosed())
				return resul.getString("id");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeConnection(resul, ps, connection);
		}
		return "ERROR: Token "+token+" desconocido";
	}

	public List<PriceDate> getPriceList(String token) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet resul = null;
//		CoinGeckoApiClient coinGecko;
		try {
			connection = DriverManager.getConnection(url);
			ps = connection.prepareStatement("select date_process from charged_infos where keyname='"+token+"' limit 1");
			resul = ps.executeQuery();
			if (!resul.isClosed()) {
				Date dateP =new SimpleDateFormat("yyyy-MM-dd").parse(resul.getString("date_process"));
				closeConnection(resul, ps, connection);
				Date today = new Date();
//				System.out.println(today.getYear() +" - "+ dateP.getYear() +" - "+ today.getMonth() +" - "+ dateP.getMonth() +" - "+ today.getDate() +" - "+ dateP.getDate());
				if (today.getYear() != dateP.getYear() || today.getMonth() != dateP.getMonth() || today.getDate() != dateP.getDate()) {
//					System.out.println("Va a traer nuevos datos");
					createTableExecute(token, getCoinMarketChart(getTokenId(token)), false);
				}
			} else {
				closeConnection(resul, ps, connection);
				createTableExecute(token, getCoinMarketChart(getTokenId(token)), true);
			}
			return getDatePrices(token);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeConnection(resul, ps, connection);
		}
		return null;
	}
	
	private List<PriceDate> getCoinMarketChart(String tokenId) {
		CoinGeckoApiClient coinGecko = new CoinGeckoApiClientImpl();
		MarketChart marketChart = coinGecko.getCoinMarketChartById(tokenId, "usd", 200000);
		List<PriceDate> pricesList = new ArrayList<PriceDate>();
		String checkedDate = "";
		for (List<String> infoList: marketChart.getPrices()) {
			PriceDate pd = new PriceDate(new Date(Long.parseLong(infoList.get(0))), Double.parseDouble(infoList.get(1)));
			if (!((pd.getDate().getYear()+1900)+"-"+(pd.getDate().getMonth()+1)+"-"+pd.getDate().getDate()).equals(checkedDate)) {
				pricesList.add(pd);
				checkedDate = (pd.getDate().getYear()+1900)+"-"+(pd.getDate().getMonth()+1)+"-"+pd.getDate().getDate();
			}
		}
		return pricesList;
	}
	
	private void createTableExecute(String token, List<PriceDate> pricesList, boolean firstTime) {
		Connection connection = null;
		PreparedStatement ps = null;
		String table = token+"_usd";
		try {
			connection = DriverManager.getConnection(url);
//			if (!firstTime) {
			try {
				ps = connection.prepareStatement("drop table "+table);
				ps.executeUpdate();
			} catch (SQLException e) {/*Tabla no existe, procede a crearla*/}
//			}
			ps = connection.prepareStatement("create table "+table+" (date text not null, price text)");
			ps.executeUpdate(); ps.close();
			String dateString = "";
			for (PriceDate pd: pricesList) {
				dateString = ((pd.getDate().getYear()+1900)+"-"+(pd.getDate().getMonth()+1)+"-"+pd.getDate().getDate());
				ps = connection.prepareStatement("insert into "+table+" values ('"+dateString+"','"+pd.getPrice()+"')");
				ps.executeUpdate();
			}
			ps.close();
			Date today = new Date();
			String todayString = (today.getYear()+1900)+"-"+(today.getMonth()+1)+"-"+today.getDate();
			if (firstTime)
				ps = connection.prepareStatement("insert into charged_infos values ('"+token+"','"+todayString+"')");
			else
				ps = connection.prepareStatement("update charged_infos set date_process='"+todayString+"'");
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeConnection(null, ps, connection);
		}
	}
	
	private List<PriceDate> getDatePrices(String token) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet resul = null;
		String table = token+"_usd";
		List<PriceDate> pdList = new ArrayList<>();
		try {
			connection = DriverManager.getConnection(url);
			ps = connection.prepareStatement("select * from "+table);
			resul = ps.executeQuery();
			while (resul.next()) {
//				System.out.println(resul.getString("date"));
				PriceDate pd = new PriceDate(new SimpleDateFormat("yyyy-MM-dd").parse(resul.getString("date")), Double.valueOf(resul.getString("price")));
				pdList.add(pd);
			}
			return pdList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeConnection(resul, ps, connection);
		}
		return pdList;
	}
	
	public String cleanChargedInfo() {
		String ok = "OK";
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = DriverManager.getConnection(url);
			ps = connection.prepareStatement("delete from charged_infos");
			ps.executeUpdate();
			return ok;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return e.getMessage();
		} finally {
			closeConnection(null, ps, connection);
		}
		
	}
	
	private void closeConnection(ResultSet rs, PreparedStatement ps, Connection conn) {
		 if (rs != null) {
		        try {
		            rs.close();
		        } catch (SQLException e) { /* Ignored */}
		    }
		    if (ps != null) {
		        try {
		            ps.close();
		        } catch (SQLException e) { /* Ignored */}
		    }
		    if (conn != null) {
		        try {
		            conn.close();
		        } catch (SQLException e) { /* Ignored */}
		    }
	}

}
