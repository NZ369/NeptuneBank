package com.neptunebank.app.repository;

import com.neptunebank.app.domain.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data repository for the Currency entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CurrencyRepository extends JpaRepository<Currency, String>, JpaSpecificationExecutor<Currency> {
    
    //TODO: Determine if repository methods are needed here. **JpaRepository Parameter type changed to String**
}
