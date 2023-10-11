package br.com.ether.applications.anf.controller;

import br.com.ether.applications.anf.services.ANFService;
import br.com.ether.model.CredenciaisModel;
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

    @Scheduled(cron = "0 30 13 15 * *")
    public void run() {
        logger.registraLog("=============================================");
        logger.registraLog("Iniciando ANF | Mês: " + dateUtility.getToday("MM/yyyy"));

        List<DadosDBModel> dadosDBModelList = dataBase.getCasos();

        if (!dadosDBModelList.isEmpty()) {
            List<CredenciaisModel> acessosModelList = dataBase.getAcessos();
            acessosModelList.forEach(e -> {
                if (e.getPlataforma().equalsIgnoreCase("NF"))
                    dadosDBModelList.forEach(d -> anfService.run(d, e));
            });
        } else {
            logger.registraLog("Não há casos para serem executados");
        }

        logger.registraLog("Finalizando ANF | Mês: " + dateUtility.getToday("MM/yyyy"));
    }
}