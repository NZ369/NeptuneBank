package com.neptunebank.app.web.rest;

import com.neptunebank.app.domain.Currency;
import com.neptunebank.app.service.CurrencyQueryService;
import com.neptunebank.app.service.CurrencyService;
import com.neptunebank.app.service.dto.CurrencyDTO;
import com.neptunebank.app.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link Currency}.
 */
@RestController
@RequestMapping("/api")
public class CurrencyResource {

    private final Logger log = LoggerFactory.getLogger(CurrencyResource.class);

	private static final String ENTITY_NAME = "currency";

    @Value("${jhipster.clientApp.name}")
	private String applicationName;

	private final CurrencyService currencyService;

	private final CurrencyQueryService currencyQueryService;

	public CurrencyResource(CurrencyService currencyService, CurrencyQueryService currencyQueryService) {
		this.currencyService = currencyService;
		this.currencyQueryService = currencyQueryService;
	}

    /**
	 * {@code GET  /currencies} : Get all the currencies.
	 *
	 * @param pageable the pagination information.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of currencies in body.
	 */
	@GetMapping("/currencies")
	public ResponseEntity<List<CurrencyDTO>> getAllCurrencies(Pageable pageable) {
		log.debug("REST request to get all Currencies");
		Page<CurrencyDTO> page = currencyService.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
		return ResponseEntity.ok().headers(headers).body(page.getContent());
	}

    /**
	 * {@code GET  /currencies/:id} : Get the "id" currency.
	 *
	 * @param id the id of the currencyDTO to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the currencyDTO, or with status {@code 404 (Not Found)}.
	 */
	@GetMapping("/currencies/{id}")
	public ResponseEntity<CurrencyDTO> getCurrency(@PathVariable String id) {
		log.debug("REST request to get Currency : {}", id);
		Optional<CurrencyDTO> currencyDTO = currencyService.findOne(id);
		return ResponseUtil.wrapOrNotFound(currencyDTO);
	}

	/**
	 * {@code GET  /currencies/buyRate:id} : get the buy rate (in CAD) of the "id" currency.
	 *
	 * @param id the id of the currencyDTO to retrieve the buy rate for.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the currencyDTO, or with status {@code 404 (Not Found)}.
	 */
	@GetMapping("/currencies/buyRate/{id}")
	public ResponseEntity<Double> getBuyRate(@PathVariable String id) {
		log.debug("REST request to get buy rate of Currency : {}", id);
		return ResponseEntity.ok().body(currencyQueryService.getBuyRate(id));
	}

	/**
	 * {@code GET  /currencies/sellRate:id} : get the sell rate (in CAD) of the "id" currency.
	 *
	 * @param id the id of the currencyDTO to retrieve the sell rate for.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the currencyDTO, or with status {@code 404 (Not Found)}.
	 */
	@GetMapping("/currencies/sellRate/{id}")
	public ResponseEntity<Double> getSellRate(@PathVariable String id) {
		log.debug("REST request to get sell rate of Currency : {}", id);
		return ResponseEntity.ok().body(currencyQueryService.getSellRate(id));
	}
}