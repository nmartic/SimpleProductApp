package com.simpleproductapp.application.repository;

import com.simpleproductapp.application.entity.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {

    @Query(value = "select er.datum_primjene from exchange_rate er order by er.datum_primjene desc limit 1", nativeQuery = true)
    LocalDate getLastDate();

    @Query(value = "select er.srednji_tecaj from exchange_rate er order by er.datum_primjene desc limit 1", nativeQuery = true)
    String getLastExchangeRate();
}
