package com.simpleproductapp.application.scheduler;

import com.simpleproductapp.application.service.HnbSchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;

@Slf4j
@Configuration
@EnableScheduling
public class HnbScheduler {

    private final HnbSchedulerService hnbSchedulerService;

    public HnbScheduler(HnbSchedulerService hnbSchedulerService) {
        this.hnbSchedulerService = hnbSchedulerService;
    }


    @Scheduled(fixedRate = 3600000)
    public void scheduleFixedRateTask() {
        LocalDate localDate = LocalDate.now();
        LocalDate lastDate =  hnbSchedulerService.getLastDate();

        if(localDate.equals(lastDate)){
            log.debug("Check - everything is up to date (scheduler)");
        }
        else if(lastDate==null){
            hnbSchedulerService.fillDatabase();
            log.debug("Database filled (scheduler)");

        }
        else {
            hnbSchedulerService.updateDatabase(lastDate);
            log.debug("Database updated with missing data (scheduler)");
        }
    }
}
