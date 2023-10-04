package br.com.ether.applications.anf.controller;

import br.com.ether.applications.anf.services.ANFService;
import br.com.ether.model.DadosDBModel;
import br.com.ether.repository.DataBase;
import br.com.ether.utilities.DateUtility;
import br.com.ether.utilities.LogUtility;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@EnableScheduling
@RequiredArgsConstructor
public class ControllerANF {

    private final DataBase dataBase;
    private final LogUtility logger;
    private final DateUtility dateUtility;
    private final ANFService anfService;

    @Scheduled(cron = "0 0 12 15 * *")
    public void run() {
        logger.registraLog("=============================================");
        logger.registraLog("Iniciando ANF | Mês: " + dateUtility.getToday("MM/yyyy"));

        List<DadosDBModel> dadosDBModelList = dataBase.getCasos();

        if (!dadosDBModelList.isEmpty()) {
            dadosDBModelList.forEach(e -> anfService.run(e));
        } else {
            logger.registraLog("Não há notas fiscais para serem enviadas");
        }

        logger.registraLog("Finalizando ANF | Mês: " + dateUtility.getToday("MM/yyyy"));
    }

    @PostConstruct
    public void init() {
        dataBase.connectDB();
    }
}
