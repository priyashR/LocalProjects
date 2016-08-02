package com.gmail.ramawthar.priyash.dataConsumer;

import java.sql.Date;

import java.text.SimpleDateFormat;

public class InstrumentRow {
    
    private String  instrument        ;
    private String  name              ;
    private Integer rulingPrice       ;
    private Integer bid               ;
    private Integer bidVol            ;
    private Integer offer             ;
    private Integer offerVol          ;
    private Integer deals             ;
    private Integer high              ;
    private Integer low               ;
    private Long tradedVol         ;
    private Long tradedVal         ;
    private Integer varianceFromPrev  ;
    private Double  varPerc           ;
    private Integer previousRuling    ;
    private Date    date              ;
    private Double  dY                ;
    private Double  eY                ;
    private Double  pE                ;    
    
    public InstrumentRow() {
        super();
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public String getInstrument() {
        return instrument;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setRulingPrice(String rulingPrice) {
        this.rulingPrice = Integer.parseInt(rulingPrice);
    }

    public Integer getRulingPrice() {
        return rulingPrice;
    }

    public void setBid(String bid) {
        this.bid = Integer.parseInt(bid);
    }

    public Integer getBid() {
        return bid;
    }

    public void setBidVol(String bidVol) {
        this.bidVol = Integer.parseInt(bidVol);
    }

    public Integer getBidVol() {
        return bidVol;
    }

    public void setOffer(String offer) {
        this.offer = Integer.parseInt(offer);
    }

    public Integer getOffer() {
        return offer;
    }

    public void setOfferVol(String offerVol) {
        this.offerVol = Integer.parseInt(offerVol);
    }

    public Integer getOfferVol() {
        return offerVol;
    }

    public void setDeals(String deals) {
        this.deals = Integer.parseInt(deals);
    }

    public Integer getDeals() {
        return deals;
    }

    public void setHigh(String high) {
        this.high = Integer.parseInt(high);
    }

    public Integer getHigh() {
        return high;
    }

    public void setLow(String low) {
        this.low = Integer.parseInt(low);
    }

    public Integer getLow() {
        return low;
    }

    public void setTradedVol(String tradedVol) {
        this.tradedVol = Long.parseLong(tradedVol);
    }

    public Long getTradedVol() {
        return tradedVol;
    }

    public void setTradedVal(String tradedVal) {
        this.tradedVal = Long.parseLong(tradedVal);
    }

    public Long getTradedVal() {
        return tradedVal;
    }

    public void setVarianceFromPrev(String varianceFromPrev) {
        this.varianceFromPrev = Integer.parseInt(varianceFromPrev);
    }

    public Integer getVarianceFromPrev() {
        return varianceFromPrev;
    }

    public void setVarPerc(String varPerc) {
        this.varPerc = Double.parseDouble(varPerc);
    }

    public Double getVarPerc() {
        return varPerc;
    }

    public void setPreviousRuling(String previousRuling) {
        this.previousRuling = Integer.parseInt(previousRuling);
    }

    public Integer getPreviousRuling() {
        return previousRuling;
    }

    public void setDate(String date) {
        try {
            this.date = new java.sql.Date((new SimpleDateFormat("yyyy-MM-dd").parse(date)).getTime());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public Date getDate() {
        return date;
    }

    public void setDY(String dY) {
        this.dY = Double.parseDouble(dY);
    }

    public Double getDY() {
        return dY;
    }

    public void setEY(String eY) {
        this.eY = Double.parseDouble(eY);
    }

    public Double getEY() {
        return eY;
    }

    public void setPE(String pE) {
        this.pE = Double.parseDouble(pE);
    }

    public Double getPE() {
        return pE;
    }
    public String toString(){
        return
        "instrument       :"+instrument      
        +" name             :"+name
        +" rulingPrice      :"+rulingPrice
        +" bid              :"+bid
        //+" bidVol           :"+bidVol
        +" offer            :"+offer
        //+" offerVol         :"+offerVol
        +" deals            :"+deals
        +" high             :"+high
        +" low              :"+low
       // +"      tradedVol        :"+tradedVol
       // +"      tradedVal        :"+tradedVal
       // +" varianceFromPrev :"+varianceFromPrev
      //  +" varPerc          :"+varPerc
        //+" previousRuling   :"+previousRuling
        +" date             :"+date;
       // +" dY               :"+dY
       // +" eY               :"+eY
        //+" pE               :"+pE;
    }
}
