package com.gmail.ramawthar.priyash.logic;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProcessedInstrumentData {
	private String date = "";
	private String instrument = "";
	private String open = "0";
	private String close = "0";
	private String high = "0";
	private String low = "0";
	private String vol = "0";
	private String sma20 = "0";
	private String obv = "0";
	
	private String sma5 = "0";
	private String rsi14 = "0";
	private String macd = "0";
	private String macdsig = "0";
	
	private String roc = "0";

	private String dip = "0";
	private String din = "0";
	private String dx = "0";
	private String adx = "0";
	
	public ProcessedInstrumentData() {
		super();
	}

	public ProcessedInstrumentData(String date, String instrument, String open, String close, 
								   String high, String low, String sma20, String vol, String obv,
								   String sma5, String rsi14, String macd, String macdsig, String roc) {
		super();
		this.date = date;
		this.instrument = instrument;
		this.open = open;
		this.close = close;
		this.high = high;
		this.low = low;
		this.sma20 = sma20;
		this.vol = vol;
		
		if (obv.equalsIgnoreCase(""))
			obv = "0";
		this.obv = obv;

		this.sma5 = sma5;
		this.rsi14 = rsi14;
		this.macd = macd;
		this.macdsig = macdsig;
		
		this.roc = roc;
	}
	
	public ProcessedInstrumentData(String date, String instrument, String open, String close, 
			   String high, String low, String sma20, String vol, String obv,
			   String sma5, String rsi14, String macd, String macdsig, String roc, 
			   String dip, String din, String dx, String adx) {
		super();
		this.date = date;
		this.instrument = instrument;
		this.open = open;
		this.close = close;
		this.high = high;
		this.low = low;
		this.sma20 = sma20;
		this.vol = vol;
		
		if (obv.equalsIgnoreCase(""))
		obv = "0";
		this.obv = obv;
		
		this.sma5 = sma5;
		this.rsi14 = rsi14;
		this.macd = macd;
		this.macdsig = macdsig;
		
		this.dip = dip;
		this.din = din;
		this.dx = dx;
		this.adx = adx;
		
		this.roc = roc;
		}	

	public String getInstrument() {
		return instrument;
	}

	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}

	public String getOpen() {
		return open;
	}

	public void setOpen(String open) {
		this.open = open;
	}

	public String getClose() {
		return close;
	}

	public void setClose(String close) {
		this.close = close;
	}

	public String getHigh() {
		return high;
	}

	public void setHigh(String high) {
		this.high = high;
	}

	public String getLow() {
		return low;
	}

	public void setLow(String low) {
		this.low = low;
	}
	
	public String getSma20() {
		return sma20;
	}

	public void setSma20(String sma20) {
		this.sma20 = sma20;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getVol() {
		return vol;
	}

	public void setVol(String vol) {
		this.vol = vol;
	}

	public String getObv() {
		return obv;
	}

	public void setObv(String obv) {
		this.obv = obv;
	}
	
	
	
	public String getSma5() {
		return sma5;
	}

	public void setSma5(String sma5) {
		this.sma5 = sma5;
	}

	public String getRsi14() {
		return rsi14;
	}

	public void setRsi14(String rsi14) {
		this.rsi14 = rsi14;
	}

	public String getMacd() {
		return macd;
	}

	public void setMacd(String macd) {
		this.macd = macd;
	}

	public String getMacdsig() {
		return macdsig;
	}

	public void setMacdsig(String macdsig) {
		this.macdsig = macdsig;
	}

	
	public String getRoc() {
		return roc;
	}

	public void setRoc(String roc) {
		this.roc = roc;
	}

	public String getDip() {
		return dip;
	}

	public void setDip(String dip) {
		this.dip = dip;
	}

	public String getDin() {
		return din;
	}

	public void setDin(String din) {
		this.din = din;
	}

	public String getDx() {
		return dx;
	}

	public void setDx(String dx) {
		this.dx = dx;
	}

	public String getAdx() {
		return adx;
	}

	public void setAdx(String adx) {
		this.adx = adx;
	}

	public String getJSONFormat(){
		
		//String output = "[{\"instrument\":\"ADI\",\"open\":\"105\",\"close\":\"105\",\"high\":\"105\",\"low\":\"105\",\"sma20\":\"105\"}]";
		String output = "{\"instrument\":\""+instrument+"\",\"date\":"+date+",\"open\":\""+open+"\",\"close\":\""+close+"\",\"high\":\""+high+"\",\"low\":\""+low+"\",\"sma20\":\""+sma20+"\"}";
		return output;
	}
	
	//Return converted values
	

	public Date getDateDate() {
		
		//20-Oct-17
		
		DateFormat formatter = new SimpleDateFormat("d-MMM-yy");
		Date newDate = null;
		try {
			newDate = formatter.parse(date);
			} catch (ParseException e) {
					e.printStackTrace();
			} 		
		return newDate;
	} 
	
	public Long getLongOpen() {
		return Long.parseLong(open);
	}

	public Long getLongClose() {
		return Long.parseLong(close);
	}

	public Long getLongHigh() {
		return Long.parseLong(high);
	}

	public Long getLongLow() {
		return Long.parseLong(low);
	}
	
	public BigDecimal getBigDecimalSma20() {
		BigDecimal _sma20; 
		try {
			_sma20 = new BigDecimal(sma20);
		}catch(Exception e){
			_sma20 = null;
		}
		return (_sma20);
	}

	public Long getLongVol() {
		return Long.parseLong(vol);
	}

	public BigDecimal getLongObv() {
		BigDecimal _obv; 
		try {
			
			//if (obv.length() > 8){
			//	_obv = new BigDecimal(0);
			//}
			_obv = new BigDecimal(obv);
			//_obv = new BigDecimal("0");
		}catch(Exception e){
			_obv = null;
		}
		return (_obv);
	}

	//---

	public BigDecimal getLongSma5() {
		BigDecimal _sma5; 
		try {
			_sma5 = new BigDecimal(sma5);
		}catch(Exception e){
			_sma5 = null;
		}
		return (_sma5);
	}

	public BigDecimal getLongRsi14() {
		BigDecimal _rsi14; 
		try {
			_rsi14 = new BigDecimal(rsi14);
		}catch(Exception e){
			_rsi14 = null;
		}
		return (_rsi14);
	}

	public BigDecimal getLongMacd() {
		BigDecimal _macd; 
		try {
			_macd = new BigDecimal(macd);
		}catch(Exception e){
			_macd = null;
		}
		return (_macd);
	}

	public BigDecimal getLongMacdsig() {
		BigDecimal _macdsig; 
		try {
			_macdsig = new BigDecimal(macdsig);
		}catch(Exception e){
			_macdsig = null;
		}
		return (_macdsig);
	}

	public BigDecimal getLongRoc() {
		BigDecimal _roc; 
		try {
			_roc = new BigDecimal(roc);
		}catch(Exception e){
			_roc = null;
		}
		return (_roc);
	}
	
	public BigDecimal getBigDip() {
		BigDecimal _dip; 
		try {
			_dip= new BigDecimal(dip);
		}catch(Exception e){
			_dip = null;
		}
		return (_dip);
	}

	public BigDecimal getBigDin() {
		BigDecimal _din; 
		try {
			_din= new BigDecimal(din);
		}catch(Exception e){
			_din = null;
		}
		return (_din);
	}
	
	public BigDecimal getBigDx() {
		BigDecimal _dx; 
		try {
			_dx= new BigDecimal(dx);
		}catch(Exception e){
			_dx = null;
		}
		return (_dx);
	}	
	
	public BigDecimal getBigAdx() {
		BigDecimal _adx; 
		try {
			_adx= new BigDecimal(adx);
		}catch(Exception e){
			_adx = null;
		}
		return (_adx);
	}		
}
