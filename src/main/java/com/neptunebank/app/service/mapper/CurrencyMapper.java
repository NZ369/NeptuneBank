package com.neptunebank.app.service.mapper;

import com.neptunebank.app.domain.Currency;
import com.neptunebank.app.service.dto.CurrencyDTO;
import org.mapstruct.Mapper;

/*
 * Mapper for the entity {@link Currency} and its DTO called {@link CurrencyDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CurrencyMapper extends EntityMapper<CurrencyDTO, Currency> {

	//TODO: may need to add @Mapping targets here.
    //import org.mapstruct.Mapping;

	Currency toEntity(CurrencyDTO currencyDTO);

	default Currency fromId(String id) {
		if (id == null) {
			return null;
		}
		Currency currency = new Currency();
		currency.setCurrencyID(id);
		return currency;
	}
}