package com.simpleproductapp.application.service;

import java.time.LocalDate;

public interface HnbSchedulerService {
    
    LocalDate getLastDate();

    void fillDatabase();

    void updateDatabase(LocalDate lastDate);
}
