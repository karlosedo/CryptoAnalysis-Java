package crypto.obj;

import java.util.Date;

public class PriceDate {
	
	Date date;
	double price;
	
	public PriceDate(Date date, double price) {
		this.date = date;
		this.price = price;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	
	
}
