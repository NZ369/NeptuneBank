package com.neptunebank.app.service.impl;

import com.neptunebank.app.domain.Currency;
import com.neptunebank.app.repository.CurrencyRepository;
import com.neptunebank.app.service.CurrencyService;
import com.neptunebank.app.service.dto.CurrencyDTO;
import com.neptunebank.app.service.mapper.CurrencyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Currency}.
 */
@Service
@Transactional
public class CurrencyServiceImpl implements CurrencyService {

    private final Logger log = LoggerFactory.getLogger(CurrencyServiceImpl.class);

	private final CurrencyRepository currencyRepository;

	private final CurrencyMapper currencyMapper;

    public CurrencyServiceImpl(CurrencyRepository currencyRepository, CurrencyMapper currencyMapper) {
		this.currencyRepository = currencyRepository;
		this.currencyMapper = currencyMapper;
	}

    /**
	 * Get all the foreign currencies.
	 *
	 * @param pageable the pagination information.
	 * @return the list of entities.
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<CurrencyDTO> findAll(Pageable pageable) {
		log.debug("Request to get all Currencies");
		return currencyRepository.findAll(pageable)
			.map(currencyMapper::toDto);
	}

    /**
	 * Get one foreign currency by id.
	 *
	 * @param id the id of the entity.
	 * @return the entity.
	 */
	@Override
	@Transactional(readOnly = true)
	public Optional<CurrencyDTO> findOne(String id) {
		log.debug("Request to get Currency : {}", id);
		return currencyRepository.findById(id)
			.map(currencyMapper::toDto);
	}
}