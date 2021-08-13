package com.neptunebank.app.service.dto;

import com.neptunebank.app.domain.Currency;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/*
 * A DTO for the {@link Currency} entity.
 */
public class CurrencyDTO implements Serializable {

    private String currencyID;

    @NotNull
	private String currencyName;

    @NotNull
	private Double unitsPerCad;

    @NotNull
	private Double cadPerUnit;

	private Double transactionFee;

    public String getCurrencyID() {
		return currencyID;
	}

	public void setCurrencyID(String currencyID) {
		this.currencyID = currencyID;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	public Double getUnitsPerCad() {
		return unitsPerCad;
	}

	public void setUnitsPerCad(Double unitsPerCad) {
		this.unitsPerCad = unitsPerCad;
	}

    public Double getCadPerUnit() {
		return cadPerUnit;
	}

	public void setCadPerUnit(Double cadPerUnit) {
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
		return Objects.hashCode(getCurrencyID());
	}

    @Override
	public String toString() {
		return "CurrencyDTO{" +
			"currencyID=" + getCurrencyID() +
			", currencyName='" + getCurrencyName() + "'" +
			", unitsPerCad=" + getUnitsPerCad() +
			", cadPerUnit=" + getCadPerUnit() +
			"}";
	}
}