package com.simpleproductapp.application.service;

import com.simpleproductapp.application.constants.AppConstants;
import com.simpleproductapp.application.entity.ExchangeRate;
import com.simpleproductapp.application.repository.ExchangeRateRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;

@Service
public class HnbSchedulerServiceImpl implements HnbSchedulerService{

    private final ExchangeRateRepository exchangeRateRepository;
    private final WebClient.Builder webClientBuilder;

    public HnbSchedulerServiceImpl(ExchangeRateRepository exchangeRateRepository, WebClient.Builder webClientBuilder) {
        this.exchangeRateRepository = exchangeRateRepository;
        this.webClientBuilder = webClientBuilder;
    }


    @Override
    public LocalDate getLastDate() {
        return exchangeRateRepository.getLastDate();
    }

    @Override
    public void fillDatabase() {
        LocalDate start = LocalDate.parse("2022-01-01");
        LocalDate today = LocalDate.now();
        boolean last = false;
        while (!last) {
            if (!start.isEqual(today.plusDays(1))) {
                ExchangeRate[] exchangeRates = webClientBuilder.build()
                        .get()
                        .uri(AppConstants.HNB_EXCHANGE_RATE_EUR_URL + start.toString())
                        .retrieve()
                        .bodyToMono(ExchangeRate[].class)
                        .block();

                for (int i = 0; i < exchangeRates.length; i++) {
                    ExchangeRate var = exchangeRates[i];
                    exchangeRateRepository.save(var);
                }
                start = start.plusDays(1);
            } else {
                last = true;
            }
        }
    }

    @Override
    public void updateDatabase(LocalDate lastDate) {
        LocalDate start = lastDate;
        LocalDate today = LocalDate.now();
        boolean last = false;
        while (!last) {
            if (!start.isEqual(today)) {
                ExchangeRate[] sviTecajevi = webClientBuilder.build()
                        .get()
                        .uri(AppConstants.HNB_EXCHANGE_RATE_EUR_URL + start.plusDays(1).toString())
                        .retrieve()
                        .bodyToMono(ExchangeRate[].class)
                        .block();

                for (int i = 0; i < sviTecajevi.length; i++) {
                    ExchangeRate var = sviTecajevi[i];
                    exchangeRateRepository.save(var);
                }
                start = start.plusDays(1);
            } else {
                last = true;
            }
        }

    }
}
