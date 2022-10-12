package crypto.obj;

public class GainsInterval {
	
	int initDay;
	int endDay;
	double totalGains;
	
	public GainsInterval(int initDay, int endDay, double totalGains) {
		this.initDay = initDay;
		this.endDay = endDay;
		this.totalGains = totalGains;
	}

	public int getInitDay() {
		return initDay;
	}

	public void setInitDay(int initDay) {
		this.initDay = initDay;
	}

	public int getEndDay() {
		return endDay;
	}

	public void setEndDay(int endDay) {
		this.endDay = endDay;
	}

	public double getTotalGains() {
		return totalGains;
	}

	public void setTotalGains(double totalGains) {
		this.totalGains = totalGains;
	}
	
}
