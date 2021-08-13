package com.neptunebank.app.service;

import com.neptunebank.app.domain.Currency;
import com.neptunebank.app.repository.CurrencyRepository;
import com.neptunebank.app.service.dto.CurrencyDTO;
import com.neptunebank.app.service.mapper.CurrencyMapper;
import io.github.jhipster.service.QueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.JoinType;
import java.util.List;
import java.util.Optional;

/**
 * Service for executing complex queries for {@link Currency} entities in the database.
 */
@Service
@Transactional(readOnly = true)
public class CurrencyQueryService extends QueryService<Currency> {

	private final Logger log = LoggerFactory.getLogger(CurrencyQueryService.class);

	private final CurrencyRepository currencyRepository;

	private final CurrencyMapper currencyMapper;

	public CurrencyQueryService(CurrencyRepository currencyRepository, CurrencyMapper currencyMapper) {
		this.currencyRepository = currencyRepository;
		this.currencyMapper = currencyMapper;
	}

	/**
	 * Return the buy rate (from bank's perspective: BUYING $Foreign for $CAD - offer LESS $CAD to customer)
     * of a currency with respect to CAD.
	 *
	 * @param id The id (String) of the foreign currency being used for calculation.
	 * @return the buy rate value (Double).
	 */
	@Transactional(readOnly = true)
	public Double getBuyRate(String id) {

        Optional<CurrencyDTO> foreignCurrency = null;
        Double buyRate;
        Double transactionFee = 0.15;

		log.debug("get buy rate of currency : {}", id);
		
        //get currency by id
        foreignCurrency = currencyRepository.findById(id).map(currencyMapper::toDto);

        //get 'cadPerUnit' field of the foreign currency & calculate buy rate
        Double cadPerUnit = foreignCurrency.get().getCadPerUnit();
        buyRate = cadPerUnit * (1 - transactionFee);
		return buyRate;
	}

    /**
	 * Return the sell rate (from bank's perspective: SELLING $Foreign for $CAD - demand MORE $CAD from customer)
     * of a currency with respect to CAD.
	 *
	 * @param id The id (String) of the foreign currency being used for calculation.
	 * @return the buy rate value (Double).
	 */
	@Transactional(readOnly = true)
	public Double getSellRate(String id) {

        Optional<CurrencyDTO> foreignCurrency = null;
        Double sellRate;
        Double transactionFee = 0.15;

		log.debug("get sell rate of currency : {}", id);
		
        //get currency by id
        foreignCurrency = currencyRepository.findById(id).map(currencyMapper::toDto);

        //get 'cadPerUnit' field of the foreign currency & calculate buy rate
        Double cadPerUnit = foreignCurrency.get().getCadPerUnit();
        sellRate = cadPerUnit * (1 + transactionFee);
		return sellRate;
	}
}
