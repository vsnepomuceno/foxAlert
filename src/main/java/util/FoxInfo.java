package util;

public class FoxInfo {
	
	String pair = "";
	Double last = 0.0;
	Double high = 0.0;
	Double low = 0.0;
	Double buy = 0.0;
	Double sell = 0.0;
	
	public FoxInfo(String pair, Double last, 
					Double high, Double low, 
					Double buy, Double sell) {
		this.pair = pair;
		this.last = last;
		this.high = high;
		this.low = low;
		this.buy = buy;
		this.sell = sell;
	}
	
	
}
