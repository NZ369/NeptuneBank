package com.neptunebank.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/*
 * A Currency class for the Foreign Exchange Calculator.
 * 
 *  - Serializing in Java allows us to convert an object to a byte stream, to be sent over a network & store into a DB.
 */
@Entity
@Table(name = "currency")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Currency implements Serializable {

    //version number used during deserialization to verify sender/receiver compatibility
    private static final long serialVersionUID = 1L;

    //ID field - A 3-letter code for the exchange currency
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "currency_id")
	private String id;

    @NotNull
	@Column(name = "currency_name", nullable = false)
	private String currencyName;

    @NotNull
	@Column(name = "units_per_CAD", nullable = false)
	private Double unitsPerCad;

    @NotNull
	@Column(name = "CAD_per_unit", nullable = false)
	private Double cadPerUnit;

	@Column(name = "transaction_fee")
	private Double transactionFee;



    
    public String getCurrencyID(){
		return id;
	}

    public void setCurrencyID(String id) {
		this.id = id;
	}

    public String getCurrencyName(){
		return currencyName;
	}

    public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

    public Double getUnitsPerCad(){
        return unitsPerCad;
    }

    public void setUnitsPerCad(Double unitsPerCad) {
		this.unitsPerCad = unitsPerCad;
	}

    public Double getCadPerUnit(){
        return cadPerUnit;
    }

    public void setCadPerUnit(Double cadPerUnit){
        this.cadPerUnit = cadPerUnit;
    }

	public Double getTransactionFee(){
        return transactionFee;
    }

    public void setTransactionFee(Double transactionFee){
        this.transactionFee = transactionFee;
    }

    @Override
	public int hashCode() {
		return 31;
	}

	@Override
	public String toString() {
		return "Currency{" +
			"currencyID=" + getCurrencyID() +
			", currencyName='" + getCurrencyName() + "'" +
			", unitsPerCad=" + getUnitsPerCad() +
			", cadPerUnit=" + getCadPerUnit() +
			"}";
	}
}