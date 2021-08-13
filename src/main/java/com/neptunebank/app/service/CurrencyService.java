package com.neptunebank.app.service;

import com.neptunebank.app.domain.Currency;
import com.neptunebank.app.service.dto.CurrencyDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Currency}.
 */
public interface CurrencyService {

    /**
	 * Get all the foreign currencies.
	 *
	 * @param pageable the pagination information.
	 * @return the list of entities.
	 */
	Page<CurrencyDTO> findAll(Pageable pageable);

    /**
	 * Get the "id" foreign currency.
	 *
	 * @param id the id of the entity.
	 * @return the entity.
	 */
	Optional<CurrencyDTO> findOne(String id);
}