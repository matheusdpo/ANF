package br.com.ether.applications.anf.controller;

import br.com.ether.applications.anf.services.ANFService;
import br.com.ether.model.DatasDBModel;
import br.com.ether.repository.DataBase;
import br.com.ether.utilities.DateUtility;
import br.com.ether.utilities.FolderUtility;
import br.com.ether.utilities.LogUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Controller
@EnableScheduling
@RequiredArgsConstructor
public class ControllerANF {
    private final DataBase dataBase;
    private final LogUtility logger;
    private final DateUtility dateUtility;
    private final ANFService anfService;

    @Scheduled(cron = "0 30 13 15 * *")
    public void run() {
        logger.registerLog("=============================================");
        logger.registerLog("Starting ANF | Month: " + dateUtility.getToday("MM/yyyy"));

        Instant start = Instant.now();

        List<DatasDBModel> datasDBModelList = dataBase.getCases();

        if (!datasDBModelList.isEmpty()) datasDBModelList.forEach(d -> anfService.run(d));
        else logger.registerLog("There are no cases to be processed.");

        Instant finish = Instant.now();

        Duration timeElapsed = Duration.between(start, finish);
        String tempo = String.format("%02d:%02d:%02d.%03d", timeElapsed.toHours(), timeElapsed.toMinutesPart(), timeElapsed.toSecondsPart(), timeElapsed.toMillisPart());

        logger.registerLog("Finish of ANF | Month: " + dateUtility.getToday("MM/yyyy"));
        logger.registerLog("Finished in " + tempo);
    }
}