package com.gmail.ramawthar.priyash.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Table(name="trade_price_data_stage")  
@Entity
@IdClass(trade_price_data_stage.class)
public class trade_price_data_stage  implements Serializable{
	
    @Column(name="TRADE_DATE")
    @Temporal(TemporalType.DATE)
    @NotNull
    @Id
	private Date trade_date;
    
    @Id
    @Column(name="INSTRUMENT")
    @NotEmpty
	private String instrument;
    
    @Column(name="OPEN")	
    @NotEmpty
	private Long open;
    
    @Column(name="CLOSE")	
    @NotEmpty
	private Long close;
    
    @Column(name="HIGH")	
    @NotEmpty
	private Long high;

    @Column(name="LOW")	
    @NotEmpty
	private Long low;

    @Column(name="SMA20")
	private BigDecimal sma20;
    
    @Column(name="PROCESSED_STATUS")
	private String processed_status;

	public Date getTrade_date() {
		return trade_date;
	}

	public void setTrade_date(Date trade_date) {
		this.trade_date = trade_date;
	}

	public String getInstrument() {
		return instrument;
	}

	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}

	public Long getOpen() {
		return open;
	}

	public void setOpen(Long open) {
		this.open = open;
	}

	public Long getClose() {
		return close;
	}

	public void setClose(Long close) {
		this.close = close;
	}

	public Long getHigh() {
		return high;
	}

	public void setHigh(Long high) {
		this.high = high;
	}

	public Long getLow() {
		return low;
	}

	public void setLow(Long low) {
		this.low = low;
	}

	public BigDecimal getSma20() {
		return sma20;
	}

	public void setSma20(BigDecimal sma20) {
		this.sma20 = sma20;
	}

	public String getProcessed_status() {
		return processed_status;
	}

	public void setProcessed_status(String processed_status) {
		this.processed_status = processed_status;
	}
   
    
    
    
}
